/** @file TestMultiThreadRace.java
 *
 *  Regression test for race conditions in multi-thread (MT) experiments.
 *
 *  In MT mode every repetition of an optimiser is executed by a worker thread. All the repetitions of
 *  the same optimiser used to share ONE Algorithm object, so any per-run state kept in instance fields
 *  (fitness trend, evaluation counter, best solution, ...) was written concurrently by several threads.
 *
 *  Run it as a plain Java application: it prints PASS/FAIL for each check and exits with status 1 if any
 *  check fails. No external test framework is required.
 */
package mains.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import algorithms.DE;
import algorithms.singleSolution.PMS;
import interfaces.Algorithm;
import interfaces.Problem;
import utils.RunAndStore;
import utils.RunAndStore.AlgorithmRepetitionThread;
import utils.RunAndStore.FTrend;

public class TestMultiThreadRace
{
	private static final int THREADS = 8;          // fixed, so the test does not depend on the number of cores
	private static final int RUNS = 64;
	private static final int OVERSHOOT = 5;        // some optimisers (e.g. PMS) may exceed the budget by a few evaluations
	private static final int TIMEOUT_SECONDS = 60; // with shared state runs can even fail to terminate
	private static int failures = 0;

	//-------------------------------------------------------------------------------------------------//
	/** Sphere function that counts the evaluations performed by the CALLING thread. */
	static class CountingSphere extends Problem
	{
		static final ThreadLocal<long[]> COUNT = ThreadLocal.withInitial(() -> new long[1]);
		CountingSphere(int dimension){super(dimension, new double[]{-100, 100});}
		@Override
		public double f(double[] x)
		{
			COUNT.get()[0]++;
			double s = 0;
			for(double v : x) s += v*v;
			return s;
		}
	}

	/** Problem whose fitness is simply x[0]. */
	static class Identity extends Problem
	{
		Identity(){super(1, new double[]{0, 1e9});}
		@Override
		public double f(double[] x){return x[0];}
	}

	/**
	 * Minimal optimiser that keeps its per-run state in instance fields, exactly like PMS,
	 * LineMinimization, Powell_correct, ... do. Run in isolation it ALWAYS evaluates x = budget, budget-1, ..., 1
	 * and therefore always ends with fBest = 1.0 and a trend with budget+1 entries.
	 */
	static class StatefulProbe extends Algorithm
	{
		int i;
		double fBest;
		FTrend FT = new FTrend();

		@Override
		public FTrend execute(Problem problem, int maxEvaluations) throws Exception
		{
			fBest = Double.MAX_VALUE;
			i = 0;
			while(i < maxEvaluations)
			{
				double f = problem.f(new double[]{maxEvaluations - i});
				i++;
				if(f < fBest){fBest = f; FT.add(i, fBest);}
				if((i & 31) == 0) Thread.yield();   // widen the window in which the race can happen
			}
			FT.add(i, fBest);
			finalBest = new double[]{fBest};
			return FT;
		}
	}

	//-------------------------------------------------------------------------------------------------//
	/** Outcome of `runs` repetitions executed concurrently. */
	static class MTOutcome
	{
		double[] fbest;
		long[] evaluations;   // evaluations performed by each single run (only meaningful for CountingSphere)
		int errors = 0;       // runs that threw an exception
		String firstError = "";
		boolean timedOut = false;
	}

	static void report(String name, boolean ok, String detail)
	{
		System.out.println((ok ? "PASS  " : "FAIL  ") + name + (detail.isEmpty() ? "" : "  -> " + detail));
		if(!ok) failures++;
	}

	/** Runs `runs` repetitions through the same classes used by Experiment.startMTExperiment(). */
	static MTOutcome runMT(final Algorithm algorithm, final Problem problem, int runs, final int budgetFactor) throws Exception
	{
		final MTOutcome out = new MTOutcome();
		out.fbest = new double[runs];
		out.evaluations = new long[runs];
		ExecutorService pool = Executors.newFixedThreadPool(THREADS);
		try
		{
			CompletionService<double[]> cs = new ExecutorCompletionService<>(pool);
			for(int r = 0; r < runs; r++)
			{
				final int run = r;
				cs.submit(new Callable<double[]>()
				{
					public double[] call() throws Exception
					{
						long before = CountingSphere.COUNT.get()[0];
						double f = new AlgorithmRepetitionThread(algorithm, problem, run, budgetFactor, false, ".").call().fbest;
						return new double[]{run, f, CountingSphere.COUNT.get()[0] - before};
					}
				});
			}
			for(int r = 0; r < runs; r++)
			{
				Future<double[]> future = cs.poll(TIMEOUT_SECONDS, TimeUnit.SECONDS);
				if(future == null){out.timedOut = true; break;}
				try
				{
					double[] res = future.get();
					out.fbest[(int) res[0]] = res[1];
					out.evaluations[(int) res[0]] = (long) res[2];
				}
				catch(ExecutionException e)
				{
					if(out.errors++ == 0) out.firstError = String.valueOf(e.getCause());
				}
			}
			return out;
		}
		finally{pool.shutdownNow();}
	}

	//-------------------------------------------------------------------------------------------------//
	/** Check 1: deterministic probe. Every MT run must give exactly the same result as an isolated run. */
	static void checkProbe() throws Exception
	{
		StatefulProbe probe = new StatefulProbe();
		probe.setID("probe");
		MTOutcome out = runMT(probe, new Identity(), RUNS, 2000);

		int wrong = 0;
		for(double v : out.fbest) if(v != 1.0) wrong++;
		boolean ok = !out.timedOut && out.errors == 0 && wrong == 0;
		report("probe: every MT run returns the isolated-run result (1.0)", ok,
				wrong + "/" + RUNS + " wrong values, " + out.errors + " runs threw " + out.firstError + (out.timedOut ? ", TIMED OUT" : ""));
	}

	/** Check 2: real optimisers. Every run must consume its own budget, whatever the other threads are doing. */
	static void checkRealAlgorithm(String name, Algorithm algorithm, int dimension, int budgetFactor) throws Exception
	{
		algorithm.setID(name);
		int budget = budgetFactor*dimension;
		MTOutcome out = runMT(algorithm, new CountingSphere(dimension), RUNS, budgetFactor);

		int outOfRange = 0;
		long min = Long.MAX_VALUE, max = Long.MIN_VALUE;
		for(long e : out.evaluations)
		{
			if(e < budget || e > budget + OVERSHOOT) outOfRange++;
			min = Math.min(min, e); max = Math.max(max, e);
		}
		boolean ok = !out.timedOut && out.errors == 0 && outOfRange == 0;
		report(name + ": every MT run uses its own budget of " + budget + " evaluations", ok,
				outOfRange + "/" + RUNS + " runs out of range [" + budget + "," + (budget + OVERSHOOT) + "] (min " + min + ", max " + max + "), "
				+ out.errors + " runs threw " + out.firstError + (out.timedOut ? ", TIMED OUT after " + TIMEOUT_SECONDS + "s" : ""));
	}

	/**
	 * Check 3: RunAndStore.format() is called by all the worker threads when results are saved, and it relies on a
	 * DecimalFormat, which is documented as not thread-safe. NB: a failure of this check could not be provoked on a
	 * 2-core machine, so this is a guard for the hardening, not a proof of the problem.
	 */
	static void checkFormat() throws Exception
	{
		final double[] values = new double[1000];
		final String[] expected = new String[values.length];
		for(int k = 0; k < values.length; k++)
		{
			values[k] = (k + 1)*1.2345678e-3*Math.pow(10, (k % 40) - 20);
			expected[k] = RunAndStore.format(values[k]);
		}
		final AtomicLong mismatches = new AtomicLong();
		List<Thread> threads = new ArrayList<>();
		for(int t = 0; t < THREADS; t++)
		{
			Thread th = new Thread(() ->
			{
				for(int rep = 0; rep < 300; rep++)
					for(int k = 0; k < values.length; k++)
						if(!expected[k].equals(RunAndStore.format(values[k]))) mismatches.incrementAndGet();
			});
			threads.add(th);
			th.start();
		}
		for(Thread th : threads) th.join();
		report("RunAndStore.format() is thread-safe", mismatches.get() == 0, mismatches.get() + " corrupted strings");
	}

	//-------------------------------------------------------------------------------------------------//
	public static void main(String[] args) throws Exception
	{
		checkProbe();

		PMS pms = new PMS();
		pms.setParameter("p0", 0.95); pms.setParameter("p1", 150.0); pms.setParameter("p2", 0.4);
		pms.setParameter("p3", 1e-5); pms.setParameter("p4", 2.0);   pms.setParameter("p5", 0.5);
		checkRealAlgorithm("PMS", pms, 10, 500);

		DE de = new DE("ro", 'b');
		de.setParameter("p0", 30.0); de.setParameter("p1", 0.5); de.setParameter("p2", 0.9); de.setParameter("p3", 1.0);
		checkRealAlgorithm("DE", de, 10, 500);

		checkFormat();

		System.out.println(failures == 0 ? "\nALL CHECKS PASSED" : "\n" + failures + " CHECK(S) FAILED");
		System.exit(failures == 0 ? 0 : 1);   // worker threads of a hung run are not daemons, so exit explicitly
	}
}
