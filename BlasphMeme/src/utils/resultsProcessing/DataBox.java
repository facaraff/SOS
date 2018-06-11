/**
Copyright (c) 2018, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the FreeBSD Project.
*/
package utils.resultsProcessing;

import java.util.Collections;
import java.util.Vector;

public class DataBox
{
	private String name = null;
	private String benchmark = null;
	private String citation = null;
	private int dimension = 0;
	private int problems = 0;
	private int functionsNumber = 0;
	private Vector<Double>[] finalValues = null;
	private double[] avg = null;
	private double[] median = null;
	private double[] std = null;
	private double[] max = null;
	private double[] min = null;
	private double[] minima = null;

	boolean sorted = false;
	boolean checked = false;

	public String getName() {return this.name;}
	public void setName(String name) { this.name = name;}

	public int getDimension() {return this.dimension;}
	public void setDimension(int dimension) { this.dimension = dimension;}

	public int getRuns(int function) {return this.finalValues[function].size();}

	public double[] getMinima() {return this.minima;}

	public String getBenchmark() {return this.benchmark;}
	
	public void setBenchmark(String benchmark) 
	{ 
		this.benchmark = benchmark;
		if (benchmark.equals("BBOB2010"))
		{
			this.functionsNumber = 24;
			this.citation = "\\cite{bib:BBOB2010funDef}";
			//double[] temp = {79.48000001, -209.87999999, -462.08999998999997, -462.08999998999997, -9.20999999, 35.90000001, 92.94000000999999, 149.15000001, 123.83000000999999, -54.93999999, 76.27000000999999, -621.10999999, 29.97000001, -52.34999999, 1000.00000001, 71.35000000999999, -16.93999999, -16.93999999, -102.54999999, -546.49999999, 40.78000001, -999.99999999, 6.87000001, 102.61000001};
			double[] temp = {79.48, -209.88, -462.09, -462.09, -9.21, 35.9, 92.94, 149.15, 123.83, -54.94, 76.27, -621.11, 29.97, -52.35, 1000.0, 71.35, -16.94, -16.94, -102.55, -546.5, 40.78, -1000.0, 6.87, 102.61};
			this.minima = temp;
		}
		else if (benchmark.equals("CEC2005"))
		{
			this.functionsNumber = 25;
			this.citation = "\\cite{bib:Suganthan2005}";
			double[] temp = {-450.0, -450.0, -450.0, -450.0, -310.0, 390.0, -180.0, -140.0, -330.0, -330.0, 90.0, -460.0, -130.0, -330.0, 120.0, 120.0, 120.0, 10.0, 10.0, 10.0, 360.0, 360.0, 360.0, 260.0, 260.0};
			this.minima = temp;
		}
		else if (benchmark.equals("CEC2008"))
		{
			this.functionsNumber = 7;
			this.citation = "\\cite{bib:Tang2007CEC08}";
			double[] temp = {-450.0, -450, 390, -330, -180, -140, Double.NaN};
			this.minima = temp;
		} 
		else if (benchmark.equals("CEC2010"))
		{
			this.functionsNumber = 20;
			this.citation = "\\cite{bib:CEC2010}";
			//double[] temp = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
			this.minima = new double[this.functionsNumber];
		}
		else if (benchmark.equals("CEC2011"))
		{
			this.functionsNumber = 13;
			this.citation = "\\cite{bib:cec2011}";
			this.minima = new double[this.functionsNumber];
		}
		else if (benchmark.equals("CEC2013"))
		{
			this.functionsNumber = 28;
			this.citation = "\\cite{bib:cec2013}";
			double[] temp = {-1400.0, -1300.0, -1200.0, -1100.0, -1000.0, -900.0, -800.0, -700.0, -600.0, -500.0, -400.0, -300.0, -200.0, -100.0, 100.0, 200.0, 300.0, 400.0, 500.0, 600.0, 700.0, 800.0, 900.0, 1000.0, 1100.0, 1200.0, 1300.0, 1400.0};
			this.minima = temp;
		}
		else if (benchmark.equals("CEC2014"))
		{
			this.functionsNumber = 30;
			this.citation = "\\cite{}";
			double[] temp = {100.0, 200.0, 300.0, 400.0, 500.0, 600.0, 700.0, 800.0, 900.0, 1000.0, 1100.0, 1200.0, 1300.0, 1400.0, 1500.0, 1600.0, 1700.0, 1800.0, 1900.0, 2000.0, 2100.0, 2200.0, 2300.0, 2400.0, 2500.0, 2600.0, 2700.0, 2800.0, 2900.0, 3000.0};
			this.minima = temp;
		}
		else if (benchmark.equals("CEC2014RotInvStudy"))
		{
			this.functionsNumber = 30;
			this.citation = "\\cite{}";
			double[] temp = {100.0, 200.0, 300.0, 400.0, 500.0, 600.0, 700.0, 800.0, 900.0, 1000.0, 1100.0, 1200.0, 1300.0, 1400.0, 1500.0, 1600.0, 1700.0, 1800.0, 1900.0, 2000.0, 2100.0, 2200.0, 2300.0, 2400.0, 2500.0, 2600.0, 2700.0, 2800.0, 2900.0, 3000.0};
			this.minima = temp;
		}
		else if (benchmark.equals("CEC2015"))
		{
			this.functionsNumber = 15;
			this.citation = "\\cite{AGGIORNAMI DIOCANE!!!!}";
			double[] temp = {100.0, 200.0, 300.0, 400.0, 500.0, 600.0, 700.0, 800.0, 900.0, 1000.0, 1100.0, 1200.0, 1300.0, 1400.0, 1500.0}; 
			this.minima = temp;
			//this.minima = {100.0, 200.0, 300.0, 400.0, 500.0, 600.0, 700.0, 800.0, 900.0, 1000.0, 1100.0, 1200.0, 1300.0, 1400.0, 1500.0}; 
		}
		else if (benchmark.equals("CEC2013_LSGO"))
		{
			this.functionsNumber = 15;
			this.citation = "\\cite{bib:cec2013lsgo}";
			//double[] temp = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
			this.minima =  new double[this.functionsNumber];
		}
		else if (benchmark.equals("SISC2010"))
		{
			this.functionsNumber = 19;
			this.citation = "\\cite{bib:sisc2010}";
			double[] temp = {-450.0, -450.0, 390.0, -330.0, -180.0, -140.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
			this.minima = temp;
		}
		else
		{
			this.functionsNumber = 1;
			this.citation = "\\cite{}";
			this.minima = new double[this.functionsNumber];
		}

		this.initFinalValues();
	}	

	public int getFunctionsNumber(){return this.functionsNumber;}
	public String getCitation(){return this.citation;}

	public Vector<Double>[] getFinalValues(){return this.finalValues;}
	public Vector<Double> getFinalValues(int fun){return this.finalValues[fun];}
	
	@SuppressWarnings("unchecked")
	public void initFinalValues()
	{
		this.finalValues = new Vector[functionsNumber]; 
		for (int i=0; i < functionsNumber; i++)
			this.finalValues[i] = new Vector<Double>();
	}
	
	public void insertValue(double value, int function)
	{
		System.out.println(function+" "+finalValues.length);
		this.finalValues[function].add(value);
	}
	
	public void sortFinalValues()
	{	
		this.check();
		if (!sorted)
			for (int i=0; i < this.functionsNumber; i++)
				if (finalValues[i] != null)
					Collections.sort(finalValues[i]);
		this.sorted = true;	
	}
	
	public void deleteFinalValues()
	{
		this.finalValues = null;
	}

	public void check()
	{
		if (!this.checked)
			for (int i=0; i < this.functionsNumber; i++)
				if (finalValues[i].size() == 0)
					this.finalValues[i] = null;
				else
					this.problems++;
		this.checked = true;
	}

	public int getProblems(){return this.problems;}

	public void computeAVG() 
	{
		this.check();
		if (this.avg == null)
		{
			this.avg = new double[this.functionsNumber];
			for (int i=0; i < this.functionsNumber; i++)
			{	
				double temp = 0; 
				if (finalValues[i] != null)
				{
					for (int n=0; n < this.finalValues[i].size(); n++)
						temp += (Double)(this.finalValues[i].get(n));
					this.avg[i] = temp/this.finalValues[i].size();
				}
				else
					this.avg[i] = Double.NaN;
			}
		}
	}
	
	public double[] getAVG(){return this.avg;} 

	public void computeMedian() 
	{ 
		this.sortFinalValues();
		if (this.median == null)
		{
			this.median = new double[functionsNumber];
			for (int i=0; i < this.functionsNumber; i++)
				if (this.finalValues[i] != null)
					this.median[i] = (Double)(this.finalValues[i].get(finalValues[i].size()/2)); 
				else
					this.median[i] = Double.NaN;
		}
	}

	public double[] getMedian(){return this.median;} 

	public void findMin() 
	{ 
		this.sortFinalValues();
		if (this.min == null)
		{
			this.min = new double[functionsNumber];
			for (int i=0; i < this.functionsNumber; i++)
				if (this.finalValues[i] != null)
					this.min[i] = (Double)(this.finalValues[i].get(0)); 
				else
					this.min[i] = Double.NaN;
		}
	}

	public double[] getMin(){return this.min;} 

	public void findMax() 
	{ 
		this.sortFinalValues();
		if (this.max == null)
		{
			this.max = new double[functionsNumber];
			for (int i=0; i < this.functionsNumber; i++)
				if (this.finalValues[i] != null)
					this.max[i] = (Double)(this.finalValues[i].get(finalValues[i].size()-1)); 
				else
					this.max[i] = Double.NaN;
		}
	}

	public double[] getMax(){return this.max;} 

	public void computeSTD() 
	{
		this.check();
		if (this.std == null)
		{
			this.std = new double[functionsNumber];
			for (int i=0; i < this.functionsNumber; i++)
			{
				double temp = 0; 
				if (this.finalValues[i] != null)
				{
					for (int n=0; n < this.finalValues[i].size(); n++)
						temp += Math.pow((Double)(this.finalValues[i].get(n)) - this.avg[i], 2); 
					this.std[i] = Math.sqrt(temp/this.finalValues[i].size());
				}
				else
					this.std[i] = Double.NaN;
			}
		}
	}
	
	public double[] getSTD(){return this.std;} 

	public void displayMedian()
	{
		for (int i=0; i < this.functionsNumber; i++)
			System.out.println("Median f" + i + " : " + this.median[i]);

	}

	public void displayAVG()
	{
		for (int i=0; i < this.functionsNumber; i++)
			System.out.println("AVG f" + i + " : " + this.avg[i]);

	}

	public void displaySTD()
	{
		for (int i=0; i < this.functionsNumber; i++)
			System.out.println("STD  f" + i + " : " + this.std[i]);

	}

	public void describe()
	{
		this.check();
		System.out.println(this.problems + " problems of "+this.benchmark+" in "+this.dimension+" dimensions");
	}
}
