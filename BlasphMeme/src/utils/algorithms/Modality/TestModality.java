package utils.algorithms.Modality;

import static utils.algorithms.Misc.generateRandomSolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


import static utils.algorithms.operators.MemesLibrary.ThreeSome_ShortDistance;
import static utils.algorithms.operators.MemesLibrary.Rosenbrock;

import static utils.MatLab.min;

import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.EuclideanDoublePoint;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;

import utils.MatLab;
import interfaces.Problem;

/* Analyses problem modality in a scale of 1 to 10 (basins of attraction)
 *  uses LocalSearcher and Kmeans clustering and avgSilhouette for analysis.
 *  
 */

public class TestModality {
	
	protected Basins b = new Basins();
	
	public int getModality() {return this.b.getModality();}
	
	public void basinEstimate(Problem prob, int repeats , int searchersNumber, int localBudget) throws Exception 
	{

		double[][] bounds = prob.getBounds();
		int problemDimension = prob.getDimension();
		double[] fitnesses = new double[repeats];
		double[][] population = new double[repeats][problemDimension];
		
		
		//if(searchersNumber!=1 && searchersNumber!=1) System.out.println("searchersNumber must either be 1 or 2");
	
		int i = 0;	
		
		for (int j = 0; j < repeats; j++)
		{
			population[j] = generateRandomSolution(bounds, problemDimension);
			fitnesses[j] = prob.f(population[j]);									
			i++;
		}
		
		
		
		double[] point = new double[problemDimension];
		int numberOfSearchers = searchersNumber; //how many LS: rosenbrock + S-3some
		int LSbudget = localBudget/repeats/numberOfSearchers;
		
		while (i < localBudget)
		{		
			for (int j = 0; j < repeats; j++) 
			{
				point = population[j].clone();
				
				if(searchersNumber == 1)
				{
					int bg = min(LSbudget, localBudget-i);
					
					double[] temp = ThreeSome_ShortDistance(point, fitnesses[j], 0.4, 10, prob, bg, 0);
					fitnesses[j] = temp[0];
					i += temp[1];
					
				}
				else if(searchersNumber == 2)
				{
					int bg = min(LSbudget, localBudget-i);
					
					double[] temp = ThreeSome_ShortDistance(point, fitnesses[j], 0.4, 10, prob, bg, 0);
					fitnesses[j] = temp[0];
					i += temp[1];
					
					 bg = min(LSbudget, localBudget-i);
					
					temp = Rosenbrock(point, fitnesses[j], 10e-5, 2, 0.5, prob, bg, 0);
					fitnesses[j] = temp[0];
					i += temp[1];
				}
				else
					System.out.println("searchersNumber must either be 1 or 2");
				
				for (int k = 0; k < problemDimension; k++) 
					population[j][k] = point[k];	
			}
		}
		
	
		int modality = -1;
		double bestAvgSilhouette = 1;

				
		if (MatLab.std(fitnesses) < 1e-01) 
		{ //better solution needed here! (its problem dependant quantity)
			modality = 1;
		}		
		else 
		{
			for (int k = 2; k < 10; k++) 
			{	//detect 2-10 basins (cluster centers in this case)
				for (int n = 0; n < 10; n++) 
				{ //iterations to mitigate random initialization of centroids
					b.setClusters(kmeans(population, k, 1000));
					double tmpSilhouette = AvgSilhouette(b.getClusters());
					b.setAvgSilhouette(tmpSilhouette); 
					if (tmpSilhouette < bestAvgSilhouette) 
					{
						modality = k;
						bestAvgSilhouette = tmpSilhouette;
					}
				}
			}
		}
		
		
		b.setModality(modality);
		
		
		List<Cluster<EuclideanDoublePoint>> bestClustering = kmeans(population, modality, 100);
		
		b.setClusters(bestClustering);
			
		//return b;		
	}
	
	public static double[] getBestPointFromCluster (Cluster<EuclideanDoublePoint> cluster, Problem prob) throws Exception {
		int problemDimension = prob.getDimension();
		double[] point = new double[problemDimension + 1]; //extra slot for fitness
		double bestFit = Double.NaN;
		for (int i = 0; i < cluster.getPoints().size(); i++) {
			double fit = prob.f(cluster.getPoints().get(i).getPoint());
			if (i == 0 || fit < bestFit) {
				bestFit = fit;
				for (int j = 0; j < problemDimension; j++) {
					point[j] = cluster.getPoints().get(i).getPoint()[j];
				}
				point[problemDimension] = fit;
			}
		}
		return point;
	}
	
	
	public static double[] getClusterCentroid (Cluster<EuclideanDoublePoint> cluster) throws Exception {
		 
		int clusterSize = cluster.getPoints().size();
		int problemDimension = cluster.getPoints().get(0).getPoint().length;
		double[] centroid = new double[problemDimension]; 
		double[] temp = null;
		for (int i = 0; i < clusterSize; i++) 
		{
			temp = cluster.getPoints().get(i).getPoint();
			for(int n = 0; n<problemDimension; n++)
				centroid[n]+=temp[n];
			centroid = MatLab.multiply((1.0/clusterSize),centroid);
		}

		return centroid; 
	}
	
	
	public double[][] getClusterCentroids () throws Exception {
		
		List<Cluster<EuclideanDoublePoint>> clusters =  this.b.getClusters();
		
		int numberOfClusters = clusters.size();
		int probDimension = 0; 
		double[][] centroids = null; 
		if(numberOfClusters == 0)
			System.out.println("There are 0 clusters!");
		else
		{
			probDimension = clusters.get(0).getPoints().get(0).getPoint().length;
			centroids = new double[numberOfClusters][probDimension];
		}
		
		
		for (int j = 0; j < numberOfClusters;  j++) 
			centroids[j] =  getClusterCentroid(clusters.get(j));

		return centroids; 
	}

	
	protected  List<Cluster<EuclideanDoublePoint>> kmeans(double[][] population, int expectedClusters, int iterations) {
        int populationSize = population.length;
        KMeansPlusPlusClusterer<EuclideanDoublePoint> transformer =
            new KMeansPlusPlusClusterer<EuclideanDoublePoint>(new Random());
            
        EuclideanDoublePoint[] points = new EuclideanDoublePoint[populationSize];
       
        for (int i=0; i < populationSize; i++) {
            points[i] = new EuclideanDoublePoint(population[i]);
        }
           
        List<Cluster<EuclideanDoublePoint>> clusters = transformer.cluster(Arrays.asList(points), expectedClusters, iterations);
        return clusters;
	}
	
	
	
	
	/** Return AVG silhouette (ILPO version) **/
	protected  double AvgSilhouette(List<Cluster<EuclideanDoublePoint>> clusters) {
    	ArrayList<Double> silhouettes = new ArrayList<Double>();
    	ArrayList<Double> betaranges = new ArrayList<Double>();
    	double silhouetteAvg = -1;
    	double alpha = 0; //a(i)
    	double beta = 0;  //b(i)
    	double temp = 0;
    	
    	for (int i = 0; i < clusters.size(); i++) {
    		int clusterSize = clusters.get(i).getPoints().size();
    		for (int j = 0; j < clusterSize; j++) {
    			betaranges.clear();
    			alpha = 0;
    			beta = 0;
    			//calculate avgdistance within home cluster points
    			for (int k = 0; k < clusterSize; k++) {
    				if (k != j) {
    					alpha = alpha + clusters.get(i).getPoints().get(j).distanceFrom(clusters.get(i).getPoints().get(k));
    				}
    			}
    			if (alpha != 0) {
    				alpha = alpha/(clusterSize-1);
    			}
    			//calculate avgdistances to other clusters points 
    			for (int k = 0; k < clusters.size(); k++) {
    				if (k != i) {
    					for (int n = 0; n < clusters.get(k).getPoints().size(); n++) {
    						temp = temp + clusters.get(i).getPoints().get(j).distanceFrom(clusters.get(k).getPoints().get(n));
    					}
    					betaranges.add(temp/clusters.get(k).getPoints().size());
    					temp = 0;
    				}
    			}
    			//pick smallest
    			Collections.sort(betaranges);
    			beta = betaranges.get(0); 
    			//calculate silhouette value of j
    			silhouettes.add((beta - alpha)/Math.max(alpha, beta));
    		}
    		
    	}
    	temp = 0;
    	for (int i = 0; i < silhouettes.size(); i++) {
    		temp = temp + silhouettes.get(i);
    	}
    	silhouetteAvg = temp/silhouettes.size();
    	return silhouetteAvg;
    }
	

	
	
	/** Return Silhouettes **/
	protected static ArrayList<Double> Silhouettes(List<Cluster<EuclideanDoublePoint>> clusters) {
    	ArrayList<Double> silhouettes = new ArrayList<Double>();
    	ArrayList<Double> betaranges = new ArrayList<Double>();

    	double alpha = 0; //a(i)
    	double beta = 0;  //b(i)
    	double temp = 0;
    	
    	for (int i = 0; i < clusters.size(); i++) 
    	{
    		int clusterSize = clusters.get(i).getPoints().size();
    		for (int j = 0; j < clusterSize; j++) 
    		{
    			betaranges.clear();
    			alpha = 0;
    			beta = 0;
    			//calculate avgdistance within home cluster points
    			for (int k = 0; k < clusterSize; k++) 
    				if (k != j) 
    					alpha = alpha + clusters.get(i).getPoints().get(j).distanceFrom(clusters.get(i).getPoints().get(k));
    				
    			if (alpha != 0) 
    				alpha = alpha/(clusterSize-1);
    		
    			//calculate avgdistances to other clusters points 
    			for (int k = 0; k < clusters.size(); k++) 
    			{
    				if (k != i) 
    				{
    					for (int n = 0; n < clusters.get(k).getPoints().size(); n++) 
    						temp = temp + clusters.get(i).getPoints().get(j).distanceFrom(clusters.get(k).getPoints().get(n));
    					
    					betaranges.add(temp/clusters.get(k).getPoints().size());
    					temp = 0;
    				}
    			}
    			//pick smallest
    			Collections.sort(betaranges);
    			beta = betaranges.get(0); 
    			//calculate silhouette value of j
    			silhouettes.add((beta - alpha)/Math.max(alpha, beta));
    		}
    		
    	}

    	return silhouettes;
    }
	
	
	
	
	
	
	
	
	
	
	public class Basins
	{
		private double[][] basinsMatrix = null; //Never used (yet)
		private double[] basinsFitnesses = null; //Never used (yet)
		private List<Cluster<EuclideanDoublePoint>> clusters = null;
		private double AvgSilhouette = Double.NaN; //Never used (yet)
		private int modality = -1;
		
		public void setBasinsMatrix(double[][] BM) {this.basinsMatrix=BM;}
		public void setBasinFitnnesses(double[] BF) {this.basinsFitnesses=BF;}
		public void setClusters(List<Cluster<EuclideanDoublePoint>> clusters) {this.clusters=clusters;}
		public void setAvgSilhouette(double avgs) {this.AvgSilhouette=avgs;}
		public void setModality(int M) {this.modality=M;}
		
		public double[][] getBasinsMatrix() {printNull(); return this.basinsMatrix;}
		public double[] getBasinFitnnesses() {printNull(); return this.basinsFitnesses;}
		public List<Cluster<EuclideanDoublePoint>> getClusters() {printNull(); return this.clusters;}
		public double getAvgSilhouette() {printNull(); return this.AvgSilhouette;}
		public int getModality() {return this.modality;}
		
		public void printNull() 
		{
			if(this.basinsMatrix == null)
				System.out.println("basinsMatrix is null");
			
			if(this.basinsFitnesses == null)
				System.out.println("basinsFitnesses is null");
			
			if(this.clusters == null)
				System.out.println("clusters is null");
			
			if(this.AvgSilhouette == Double.NaN)
				System.out.println("AvgSilhouette is NaN");
		}
	}
	
	
}
