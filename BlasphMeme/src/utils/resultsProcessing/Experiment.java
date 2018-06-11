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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Experiment
{
	private AlgorithmInfo[] algorithms = null;

	private String directory;

	public AlgorithmInfo[] getAlgorithms(){return this.algorithms;}	

	private boolean trendsFlag = false;
	private boolean errorTrendsFlag = true;
	private boolean FVDistributionFlag = true;
	private int samples = 100; //fitness trend samples number
	private boolean trendsFolderFlag = true;
	private boolean FVDistrFolderFlag = true;
	

	//methods
	public void setTrendsFlag(boolean trends, boolean error, boolean finalValues){this.trendsFlag = trends; this.errorTrendsFlag = error; this.FVDistributionFlag = finalValues;}
	public void setTrendsFlag(boolean trends, boolean error){this.trendsFlag = trends; this.errorTrendsFlag = error;}
	public void setTrendsFlag(boolean trends){this.trendsFlag = trends;}
	public boolean getTrendsFlag(){return this.trendsFlag;}
	public boolean getErrorTrendsFlag(){return this.errorTrendsFlag;}

	public void setFinalValuesDistrFlag(boolean finalValues){this.FVDistributionFlag = finalValues;}
	public boolean getFinalValuesDistrFlag(){return this.FVDistributionFlag;}
	
	public void setSamples(int points){this.samples = points;}
	public int getSamples(){return this.samples;}

	public void setTrendsFlag(boolean trends, boolean error, boolean finalValues, int points)
	{
		this.setTrendsFlag(trends, error, finalValues);
		this.setSamples(points);
	}
	public void setTrendsFlag(boolean trends, int points)
	{
		this.setTrendsFlag(trends);
		this.setSamples(points);
	}

	public void setDirectory(String wdir)
	{
		this.directory = wdir;
	}

	public void importData() throws IOException
	{
		this.algorithNameCheck();

		File dir = new File(this.directory);
		File[] list = dir.listFiles();

		int nrFolders = 0;
		for (int i=0; i < list.length; i++)
		{
			if (list[i].isDirectory())
				nrFolders++;
		}
				
		this.algorithms = new AlgorithmInfo[nrFolders];
		System.out.println("Algorithms list:");
		
		nrFolders = 0;
		for (int i=0; i < list.length; i++) 
		{
			String name = list[i].getName();
			if (list[i].isDirectory())
			{
				if (this.trendsFlag)
					algorithms[nrFolders++] = new AlgorithmInfo(name, importDataTrends(name));
				else
				{
					System.out.println(name);
					algorithms[nrFolders++] = new AlgorithmInfo(name, importDataAlgorithm(name));						
				}
			}
		}
	}

	public DataBox[] importDataAlgorithm(String alg) throws IOException
	{
		int emptyRuns = 0;

		File dir = null;
		File dir2 = null;
		String input;
		String temp = null;
		Vector<String> v = new Vector<String>();

		dir = new File(this.directory + "/" + alg); 
		File[] list = dir.listFiles();

		String ben = null;
		String dim = null;
		String comp = null;
		String s =null;

		for (int i=0; i < list.length; i++) 
		{	
			s = list[i].getName();
			if (s.charAt(0) != '.') //hidden folders
			{
				ben = getBenchmark(s);
				dim = getDimension(s);
				comp = ben + "-" + dim;
				boolean b = false;
				for (int k=0; k < v.size() && !b; k++)
					if (comp.equals(v.get(k)))
						b = true;
				if (!b)
					v.add(comp);
			}	
		} 

		DataBox[] dataBoxes = new DataBox[v.size()];
		for (int k=0; k < v.size(); k++) 
		{	 
			dataBoxes[k] = new DataBox();
			dataBoxes[k].setName(v.get(k)); 
			dataBoxes[k].setBenchmark(v.get(k).substring(0, v.get(k).indexOf("-")));
			dataBoxes[k].setDimension(Integer.parseInt(v.get(k).substring(v.get(k).indexOf("-") + 1)));	
		}

		for (int i=0; i < list.length; i++) 
		{	
			s = list[i].getName();
			if (s.charAt(0) != '.')
			{
				ben = getBenchmark(s); 	dim = getDimension(s); comp = ben + "-" + dim;

				int index = getIndex(dataBoxes, comp);
				int f = getFunction(list[i].getName());

				dir2 = new File(list[i].getPath()); 
				File[] list2 = dir2.listFiles();

				for (int n=0; n< list2.length; n++) 
				{
					if (!list2[n].isDirectory() && 
						!list2[n].getName().equals("catalog") && 
						!list2[n].getName().contains("~"))
					{
						BufferedReader fileBufereReader = new BufferedReader(new FileReader(list2[n].getPath()));
						while ((input = fileBufereReader.readLine()) != null)
							temp = input;

						fileBufereReader.close();

						//CHECK
						if (temp.equals("#EvaluationCount	Fitness") || (temp == "" ))
						{
							emptyRuns++;
							System.out.println(list2[n].getPath());
						}
						else 
						{
							int firstIndex = temp.indexOf("N"); 
							int firstTab = temp.indexOf("\t");

							if (firstIndex == -1)
							{
								// this check is needed only with the results obtained with the extra handling option
								temp = temp.substring(temp.indexOf("\t") + 1);
								dataBoxes[index].insertValue(Double.parseDouble(temp), f-1);
							}							
							else
							{
								//NaN
								if (firstIndex - firstTab != 1)
								{
									temp = temp.substring(temp.indexOf("\t") + 1, firstIndex-1); 
									dataBoxes[index].insertValue(Double.parseDouble(temp), f-1);
								}
								else
								{
									System.out.println(list2[n].getPath()+ " WARNING! (NaN) non numeric value was found in this file! ");
									emptyRuns++;
								}
							}
						}
					} 
				}
			}		
		}

		if (emptyRuns != 0)
			System.out.println("\n WARNING: " + emptyRuns + " missing runs for this algorithm \n");
		return dataBoxes;
	}

	public DataBox[] importDataTrends(String alg) throws IOException
	{
		int emptyRuns = 0;

		File dir = null;
		File dir2 = null;
		String input;
		String temp = null;
		Vector<String> v = new Vector<String>();

		dir = new File(this.directory + "/" + alg); 
		File[] list = dir.listFiles();

		String ben = null;
		String dim = null;
		String comp = null;
		String s = null;

		for (int i=0; i < list.length; i++) 
		{	
			s = list[i].getName();
			if (s.charAt(0) != '.') //hidden folders
			{
				ben = getBenchmark(s);
				dim = getDimension(s);
				comp = ben + "-" + dim;
				boolean b = false;
				for (int k=0; k < v.size() && !b; k++)
					if (comp.equals(v.get(k)))
						b = true;
				if (!b)
					v.add(comp);
			}	
		} 

		DataBox[] dataBoxes = new DataBox[v.size()];
		for (int k=0; k < v.size(); k++) 
		{	 
			dataBoxes[k] = new DataBox();
			dataBoxes[k].setName(v.get(k)); 
			dataBoxes[k].setBenchmark(v.get(k).substring(0, v.get(k).indexOf("-")));
			dataBoxes[k].setDimension(Integer.parseInt(v.get(k).substring(v.get(k).indexOf("-") + 1)));

		} 
//		String DirRes = "./results/";
		String DirRes = "./tables/";
		String DirFV = DirRes+"FinalValues";;
		boolean success = (new File(DirRes)).mkdir();
//		DirRes = "./results/trends";
		DirRes = "./tables/trends";
		if(this.trendsFolderFlag && this.FVDistrFolderFlag)
		{
			boolean successFV;
			success = (new File(DirRes)).mkdir();
			successFV = (new File(DirFV)).mkdir();
			for (int k=0; k < dataBoxes.length; k++) 
			{
				String name = dataBoxes[k].getName();
				success = (new File(DirRes+"/"+name)).mkdir();
				successFV = (new File(DirFV+"/"+name)).mkdir();
				if (success)
					System.out.println("Path: " + DirRes+"/"+name+" has been created");
				if (successFV)
					System.out.println("Path: " + DirFV+"/"+name+" has been created");
			}
			this.FVDistrFolderFlag = false;	
		}
		else if (this.trendsFolderFlag)
		{
			success = (new File(DirRes)).mkdir();
			for (int k=0; k < dataBoxes.length; k++) 
			{
				String name = dataBoxes[k].getName();
				success = (new File(DirRes+"/"+name)).mkdir();
				if (success)
					System.out.println("Path: " + DirRes+"/"+name+" has been created");
			}
			this.trendsFolderFlag = false;
		}

		for (int i=0; i < list.length; i++)
		{	
			s = list[i].getName();
			if (s.charAt(0) != '.')
			{
				ben = getBenchmark(s); 	dim = getDimension(s); comp = ben + "-" + dim;

				int index = getIndex(dataBoxes, comp);
				int f = getFunction(list[i].getName());

				dir2 = new File(list[i].getPath()); 
				File[] list2 = dir2.listFiles();	

				//boolean checkOnRun = false; 
				double[] avg_trend = null;

				String Siter = null;
				String Ssample = null;
				int delta = 0;
				boolean check = true; int lines = -1;
				for (int n=0; n< list2.length; n++) 
				{
					if (!list2[n].getName().equals("catalog") && !list2[n].getName().contains("~"))
					{	
						boolean checkOnRun = false;
						BufferedReader fileBufferedReader = new BufferedReader(new FileReader(list2[n].getPath()));
						while ((input = fileBufferedReader.readLine()) != null)
							temp = input;
						fileBufferedReader.close();

						//CHECK
						if (temp.equals("#EvaluationCount	Fitness") || (temp == "" ))
						{
							emptyRuns++;
							System.out.println(list2[n].getPath());
						}
						else 
						{
							int firstIndex = temp.indexOf("N"); 
							int firstTab = temp.indexOf("\t");

							if (firstIndex == -1)
							{
								// this check is needed only with the results obtained with the extra handling option
								Siter = temp.substring(0,temp.indexOf("\t"));
								Ssample = temp.substring(temp.indexOf("\t")+1,temp.length());

								temp = temp.substring(temp.indexOf("\t") + 1); //old part for tabs
								dataBoxes[index].insertValue(Double.parseDouble(temp), f-1);//old part for tabs

								if (check)
								{
									checkOnRun = true;
									delta++;
									check = false;
									lines = Integer.parseInt(Siter);
								}
								else
								{
									if (lines == Integer.parseInt(Siter))
									{
										checkOnRun = true;
										delta++;
									}
								}
							}							
							else
							{
								if (firstIndex - firstTab != 1)
								{
									Siter = temp.substring(0,temp.indexOf("\t"));
									Ssample = temp.substring(temp.indexOf("\t")+1,temp.length());

									temp = temp.substring(temp.indexOf("\t") + 1, firstIndex-1); 
									dataBoxes[index].insertValue(Double.parseDouble(temp), f-1);

									if (check)
									{
										checkOnRun = true;
										delta++;
										check = false;
										lines = Integer.parseInt(Siter);
									}
									else
									{
										if (lines == Integer.parseInt(Siter))
										{
											checkOnRun = true;
											delta++;
										}
									}
								}
								else
								{
									System.out.println(list2[n].getPath()+ " WARNING!  (NaN) non numeric value was found in this file! ");
									emptyRuns++;
								}
							}
						}

						if (checkOnRun)
						{
							int iter = Integer.parseInt(Siter);
							double sample = Double.parseDouble(Ssample);
							if (avg_trend == null)
								avg_trend = new double[iter+1];
							double[] avg_trend_temp = new double[iter+1];
							for (int a=0; a<avg_trend.length;a++)
								avg_trend_temp[a] = Double.NaN;
							fileBufferedReader = new BufferedReader(new FileReader(list2[n].getPath()));
							fileBufferedReader.readLine(); 

							while ((input = fileBufferedReader.readLine()) != null)
							{
								temp = input;
								Siter = temp.substring(0,temp.indexOf("\t"));
								Ssample = temp.substring(temp.indexOf("\t")+1,temp.length());
								iter = Integer.parseInt(Siter);
								sample = Double.parseDouble(Ssample);
								if (Double.isNaN(avg_trend_temp[iter]))
									avg_trend_temp[iter] = 0;
								avg_trend_temp[iter] = sample;
							}
							fileBufferedReader.close();
							this.interpolation(avg_trend_temp);
							for (int a=0; a<avg_trend.length;a++)
								avg_trend[a] += avg_trend_temp[a];
						}
					} 				
				}
				
				if(avg_trend == null)
					System.out.println("The avg_trend variable is null here!");
				else 
				{
					if (this.errorTrendsFlag)
					{
						double[] minima = dataBoxes[index].getMinima();
						
						for (int a=0; a<avg_trend.length; a++)
							if(Double.isNaN(avg_trend[a]))
								System.out.println("NaN value found at position="+a+" in algorithm: "+alg);
							else
							{
								avg_trend[a] /= delta;
								avg_trend[a] = avg_trend[a] - minima[f-1];
							}
					}
					else
					{
							for (int a=0; a<avg_trend.length;a++) 
								if(Double.isNaN(avg_trend[a]))
									System.out.println("NaN value found at position="+" in algorithm: "+alg);
								else
									avg_trend[a] /= delta;
						
					}

					String folder = dataBoxes[index].getName();
					String fileName = alg +"_"+f+".txt";
					File fileTrend = new File(DirRes + "/"+folder+"/"+fileName);
					// if file doesn't exists, then create it
					if (!fileTrend.exists()) 
						fileTrend.createNewFile();
					FileWriter fw = new FileWriter(fileTrend.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					int rate = avg_trend.length/this.samples;
					for (int a=0; a < avg_trend.length; a++)
					{
						if (a==0 || a==avg_trend.length-1 || a%rate==0)
						{
							bw.write(a+"\t"+avg_trend[a]);
							bw.newLine();
						}
					}
					bw.close();
					
					if(FVDistributionFlag)
					{
						Vector<Double>[] V = dataBoxes[index].getFinalValues();
						
						File fileFVDistribution = new File(DirFV + "/"+folder+"/"+fileName);
						// if file doesn't exists, then create it
						if (!fileFVDistribution.exists()) 
							fileFVDistribution.createNewFile();
						FileWriter fwFV = new FileWriter(fileFVDistribution.getAbsoluteFile());
						BufferedWriter bwFV = new BufferedWriter(fwFV);
						for (int a=0; a < V[f-1].size(); a++)
							bwFV.write(V[f-1].get(a)+"\n");
						//fw.close();
						bwFV.close();
					}
					//fw.close();
					//bw.close();
				}

//				if (this.errorTrendsFlag)
//				{
//					double[] minima = dataBoxes[index].getMinima();
//					
//					for (int a=0; a<avg_trend.length; a++)
//						if(Double.isNaN(avg_trend[a]))
//							System.out.println("null value found at a="+a);
//						else
//						{
//							avg_trend[a] /= delta;
//							avg_trend[a] = avg_trend[a] - minima[f-1];
//						}
//				}
//				else
//				{
//						for (int a=0; a<avg_trend.length;a++) 
//							if(Double.isNaN(avg_trend[a]))
//								System.out.println("null value found at a="+a);
//							else
//								avg_trend[a] /= delta;
//					
//				}
//
//				String folder = dataBoxes[index].getName();
//				String fileName = alg +"_"+f+".txt";
//				File fileTrend = new File(DirRes + "/"+folder+"/"+fileName);
//				// if file doesn't exists, then create it
//				if (!fileTrend.exists()) 
//					fileTrend.createNewFile();
//				FileWriter fw = new FileWriter(fileTrend.getAbsoluteFile());
//				BufferedWriter bw = new BufferedWriter(fw);
//				int rate = avg_trend.length/this.samples;
//				for (int a=0; a < avg_trend.length; a++)
//				{
//					if (a==0 || a==avg_trend.length-1 || a%rate==0)
//					{
//						bw.write(a+"\t"+avg_trend[a]);
//						bw.newLine();
//					}
//				}
//				bw.close();
				
//				if(FVDistributionFlag)
//				{
//					Vector<Double>[] V = dataBoxes[index].getFinalValues();
//					
//					File fileFVDistribution = new File(DirFV + "/"+folder+"/"+fileName);
//					// if file doesn't exists, then create it
//					if (!fileFVDistribution.exists()) 
//						fileFVDistribution.createNewFile();
//					FileWriter fwFV = new FileWriter(fileFVDistribution.getAbsoluteFile());
//					BufferedWriter bwFV = new BufferedWriter(fwFV);
//					for (int a=0; a < V[f-1].size(); a++)
//						bwFV.write(V[f-1].get(a)+"\n");
//					//fw.close();
//					bwFV.close();
//				}
//				//fw.close();
//				//bw.close();
			}
		}

		if (emptyRuns != 0)
			System.out.println("\n WARNING: " + emptyRuns + " missing runs for this algorithm \n");
		return dataBoxes;
	}

	public void computeAVG()
	{
		for (int i=0; i < this.algorithms.length; i++)
			for (int n=0; n < algorithms[i].getData().length; n++)
				algorithms[i].getData()[n].computeAVG();
	}

	public void computeSTD()
	{
		for (int i=0; i < this.algorithms.length; i++)
			for (int n=0; n < algorithms[i].getData().length; n++)
				algorithms[i].getData()[n].computeSTD();
	}

	public void computeMedian()
	{
		for (int i=0; i < this.algorithms.length; i++)
			for (int n=0; n < algorithms[i].getData().length; n++)
				algorithms[i].getData()[n].computeMedian();
	}

	public void getMin()
	{
		for (int i=0; i < this.algorithms.length; i++)
			for (int n=0; n < algorithms[i].getData().length; n++)
				algorithms[i].getData()[n].findMin();
	}

	public void getMax()
	{
		for (int i=0; i < this.algorithms.length; i++)
			for (int n=0; n < algorithms[i].getData().length; n++)
				algorithms[i].getData()[n].findMax();
	}

	public void deleteFinalValues()
	{
		for (int i=0; i < this.algorithms.length; i++)
			for (int n=0; n < algorithms[i].getData().length; n++)
				algorithms[i].getData()[n].deleteFinalValues();
	}

	public String getBenchmark(String str)
	{                                  
		int end = str.lastIndexOf("."); 
		//int start = str.indexOf(".") + 10;
		int start = str.indexOf(".")+1;
		return str.substring(start,end);
	}

	public String getDimension(String str)
	{
		int start = str.lastIndexOf("-") + 1;
		return str.substring(start);
	}

	public int getFunction(String str)
	{
		return Integer.parseInt(str.substring(str.lastIndexOf(".") + 2, str.indexOf("-")));
	}

	public int getIndex(DataBox[] db, String str)
	{
		for (int i=0; i < db.length; i++)
			if (db[i].getName().equals(str))
				return i;
		return -1;
	}

	public void describeExperiment()
	{
		String s = new String("Algorithms: ");	
		for (int i=0; i < algorithms.length; i++)
			s += algorithms[i].getName()+", ";
		System.out.println(s);
		for (int i=0; i < algorithms.length; i++)
			algorithms[i].describe();	
	}

	public void algorithNameCheck()
	{
		File dir = new File(this.directory);
		File[] list = dir.listFiles();
		System.out.println(dir);

		for (int i=0; i < list.length; i++)
		{
			if (list[i].isDirectory())
			{
				if (list[i].getName().contains("_"))
				{	
					String newName = list[i].getName().replace('_','-');
					System.out.println("Folder \""+list[i].getName()+"\" has been renamed \""+newName+"\"");
					File newDir = new File(this.directory+"/"+newName);
					list[i].renameTo(newDir);   
				}			
			}
		}
	}
	
	public void interpolation(double[] array)
	{
		for (int i=array.length-2; i>0; i--)
		{
			if (Double.isNaN(array[i]))
				array[i] = array[i+1];
		}
	}
}
