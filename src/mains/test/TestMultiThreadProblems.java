/** @file TestMultiThreadProblems.java
 *
 *  Regression test: benchmark problems must return the same fitness when they are evaluated by several threads at the same time.
 *
 *  In MT experiments the Problem object is shared by all the worker threads. Some benchmark implementations keep scratch
 *  buffers in static singletons (e.g. the CEC2010 rotated functions use `m_tmp`), so concurrent calls to f() overwrite
 *  each other's intermediate results and return wrong values.
 *
 *  For each problem the test computes the fitness of a fixed set of points sequentially (reference), then evaluates the same
 *  points from several threads at once and counts the values that differ from the reference.
 *  Run it as a plain Java application: it prints PASS/FAIL per problem and exits with status 1 if any check fails.
 */
package mains.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

import benchmarks.CEC2010;
import interfaces.Problem;

public class TestMultiThreadProblems
{
	private static final int THREADS = 8;   // fixed, so the test does not depend on the number of cores
	private static final int POINTS = 12;
	private static final int ROUNDS = 6;
	private static int failures = 0;

	static void check(String name, Problem problem) throws Exception
	{
		int n = problem.getDimension();
		double[][] bounds = problem.getBounds();   // bounds[i] = {lower, upper} of variable i
		Random rnd = new Random(12345);
		final double[][] x = new double[POINTS][n];
		for(int k = 0; k < POINTS; k++)
			for(int i = 0; i < n; i++)
				x[k][i] = bounds[i][0] + rnd.nextDouble()*(bounds[i][1] - bounds[i][0]);

		final double[] expected = new double[POINTS];
		for(int k = 0; k < POINTS; k++)
			expected[k] = problem.f(x[k].clone());

		final AtomicLong wrong = new AtomicLong();
		final AtomicLong total = new AtomicLong();
		final AtomicLong thrown = new AtomicLong();
		final CyclicBarrier start = new CyclicBarrier(THREADS);
		final Problem p = problem;
		List<Thread> threads = new ArrayList<>();
		for(int t = 0; t < THREADS; t++)
		{
			final int offset = t;
			Thread th = new Thread(() ->
			{
				try
				{
					start.await();
					for(int r = 0; r < ROUNDS; r++)
						for(int j = 0; j < POINTS; j++)
						{
							int k = (j + offset) % POINTS;   // threads work on different points at the same time
							double v = p.f(x[k].clone());
							total.incrementAndGet();
							if(Double.doubleToLongBits(v) != Double.doubleToLongBits(expected[k]) && Math.abs(v - expected[k]) > 1e-9*Math.abs(expected[k])) wrong.incrementAndGet();
						}
				}
				catch(Throwable e){thrown.incrementAndGet();}
			});
			threads.add(th);
			th.start();
		}
		for(Thread th : threads) th.join();

		boolean ok = wrong.get() == 0 && thrown.get() == 0;
		System.out.println((ok ? "PASS  " : "FAIL  ") + name + (ok ? "" : "  -> " + wrong.get() + "/" + total.get() + " evaluations differ from the sequential value, " + thrown.get() + " threads threw"));
		if(!ok) failures++;
	}

	public static void main(String[] args) throws Exception
	{
		// CEC2010 functions that use the shared scratch vector m_tmp (rotated groups): F4, F5, F6, F9, F10, F11, F14, F15, F16
		int[] rotated = {4, 5, 6, 9, 10, 11, 14, 15, 16};
		for(int f : rotated)
			check("CEC2010 f" + f, new CEC2010(f));
		// plain (not rotated) functions, as a control
		check("CEC2010 f1 (control)", new CEC2010(1));

		System.out.println(failures == 0 ? "\nALL CHECKS PASSED" : "\n" + failures + " CHECK(S) FAILED");
		System.exit(failures == 0 ? 0 : 1);
	}
}
