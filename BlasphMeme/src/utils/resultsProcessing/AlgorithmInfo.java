package utils.resultsProcessing;

public class AlgorithmInfo
{
	private String name;
	private DataBox[] data = null; 

	public AlgorithmInfo(String name, DataBox[] data)
	{
		this.name = name;
		this.data = data;
	}

	public int subExperimentsNumber()
	{
		return this.data.length;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	public DataBox[] getData(){return this.data;}

	/*
	 * Use this method if all the algorithms have been tested on the same number of tests functions!
	 */
	public int getProblems() 
	{
		int prob = 0;
		for(int i=0; i < data.length; i++)
			prob += data[i].getProblems();
		return prob;
	}

	public void describe()
	{
		System.out.println(this.name+":");
		for(int i=0; i < data.length; i++)
			data[i].describe();
	}
}