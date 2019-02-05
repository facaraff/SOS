package utils.algorithms;

import static utils.algorithms.Misc.generateRandomSolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


import static utils.algorithms.operators.MemesLibrary.ThreeSome_ShortDistance;
import static utils.algorithms.operators.MemesLibrary.Rosenbrock;

import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.EuclideanDoublePoint;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;

import utils.MatLab;
import interfaces.Problem;

/* Analyses problem modality in a scale of 1 to 10 (basins of attraction)
 *  uses LocalSearcher and Kmeans clustering and avgSilhouette for analysis.
 *  
 *  work in progress..
 */

public class TestModality {
	
	@SuppressWarnings("unused")
	public static double[][] basinEstimate(Problem prob, int repeats, int repeatsSaved , int localBudget) throws Exception {

		double[][] bounds = prob.getBounds();
		int problemDimension = prob.getDimension();
		double[] fitnesses = new double[repeats];
		double[][] population = new double[repeats][problemDimension];
		//double[][] returnMatrix = new double[repeatsSaved + modality + 1][problemDimension + 1];
		
		/* returnMatrix
		 * 
		 *    repeatsSaved + 1
		 *     ____________________
		 * d  | x x x x x x m c c c| 
		 * i  | x x x x x x   c c c|
		 * m  | x x x x x x   c c c| 
		 * +  | x x x x x x   c c c|
		 * 1  | z z z z z z        |
		 * 
		 *
		 * x: x_i = solution
		 * z: z = fitness of solution
		 * m: m = estimated modality of landscape (1-10)
		 * c: c_i = centroid (from 2 to 10 centroids or 0 in case of unimodal) // this can also be best points of clusters etc.
		 */
		
	
		int i = 0;
		
		for (int j = 0; j < repeats; j++)
		{
			double[] tmp = generateRandomSolution(bounds, problemDimension);
			for (int n = 0; n < problemDimension; n++)
				population[j][n] = tmp[n];
			fitnesses[j] = prob.f(population[j]);									
			i++;
		}
		
		//double[][] proximityMatrixStart = ProximityMatrix(population);
		double[] point = new double[problemDimension];
		int numberOfSearchers = 2; //how many LS: rosenbrock + S-some
		int LSbudget = localBudget/repeats/numberOfSearchers;
		
		while (i < localBudget)
		{		
			for (int j = 0; j < repeats; j++) {
				point = population[j].clone();
				
				
				double[] temp = ThreeSome_ShortDistance(point, fitnesses[j], 0.4, 10, prob, LSbudget, 0);
				fitnesses[j] = temp[0];
				i += temp[1];
				
				temp = Rosenbrock(point, fitnesses[j], 10e-5, 2, 0.5, prob, LSbudget, 0);
				fitnesses[j] = temp[0];
				i += temp[1];
				for (int k = 0; k < problemDimension; k++) {
					population[j][k] = point[k];
				}
			}
		}
		
		//double[][] proximityMatrixEnd = ProximityMatrix(population);
		int modality = -1;
		double bestsilhouette = 1;
		double silhouette;
		double[][] bestCentroids = new double[10][problemDimension];
		//List<Cluster<EuclideanDoublePoint>> bestClustering = null;
				
		if (MatLab.std(fitnesses) < 1e-01) { //better solution needed here! (its problem dependant quantity)
			modality = 1;
		}		
		else {
			for (int k = 2; k < 10; k++) {	//detect 2-10 basins (cluster centers in this case)
				for (int n = 0; n < 10; n++) { //iterations to mitigate random initialization of centroids
					List<Cluster<EuclideanDoublePoint>> clusters = kmeans(population, k, 1000);
					silhouette = AvgSilhouette(clusters);
					if (silhouette < bestsilhouette) {
						//bestClustering = copyClusters(clusters);
						modality = k;
						bestsilhouette = silhouette;
					}
				}
			}
		}
		List<Cluster<EuclideanDoublePoint>> bestClustering = kmeans(population, modality, 100);
		
		//get bestpoints from each cluster
		double [][] clusterBests = new double[modality][problemDimension + 1]; //extra slot for fitness
		if (modality == 1) {
			for (int j = 0; j < problemDimension; j++) {
				clusterBests[0][j] = population[0][j];
			}
			clusterBests[0][problemDimension] = fitnesses[0];
		} else {
			for (int j = 0; j < bestClustering.size(); j++) {
				clusterBests[j] = getBestPointFromCluster(bestClustering.get(j), prob).clone();
			}
		}
		//k-th order statistics (brute force implementation for now)
		double[] tempFitness = new double[repeats];
		for (int j = 0; j < fitnesses.length; j++) {
			tempFitness[j] = fitnesses[j];
		}
		Arrays.sort(tempFitness);
		
		double[][] returnMatrix = new double[modality][problemDimension + 1];
		
		
		//prep returnMatrix with solutions, fitnesses, centroids and modality.
		//copy n-best solutions
		/*
		for (int k = 0; k < repeatsSaved; k++) { 
			for (int j = 0; j < fitnesses.length; j++) {
				if (tempFitness[k] == fitnesses[j]) {
					for (int n = 0; n < problemDimension; n++) {
						returnMatrix[k][n] = population[j][n];
					}
					returnMatrix[k][problemDimension] = tempFitness[k];
					break;
				}
			}
		}
		*/
		
		//copy centroids
		/*
		if (modality > 1) {
			for (int k = repeatsSaved + 1; k < repeatsSaved + modality + 1; k++) {
				for (int j = 0; j < problemDimension; j++) {
					returnMatrix[k][j] = bestCentroids[k - repeatsSaved - 1][j];
				}
			}
		}
		*/
		//copy best cluster points
//		if (modality > 1) {
			for (int k = 0; k < modality; k++) {
				for (int j = 0; j < problemDimension + 1; j++) {
					//returnMatrix[k][j] = bestCentroids[k - repeatsSaved - 1][j];
					returnMatrix[k][j] = clusterBests[k][j];
				}
			}
//		} else {
//			for (int j = 0; j < problemDimension + 1; j++) {
//				returnMatrix[0][j] = population[0][j];
//			}
//			returnMatrix[0][problemDimension + 1] = fitnesses[0];
//		}
		return returnMatrix;		
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
	
	public static List<Cluster<EuclideanDoublePoint>> copyClusters(List<Cluster<EuclideanDoublePoint>> clusterList) {
		List<Cluster<EuclideanDoublePoint>> temp = clusterList;
		for (int i = 0; i < clusterList.size(); i++) {
			System.out.println(clusterList.size());
			temp.add(clusterList.get(i));			
		}
		return temp;
	}

	
	public static List<Cluster<EuclideanDoublePoint>> kmeans(double[][] population, int expectedClusters, int iterations) {
        int populationSize = population.length;
        KMeansPlusPlusClusterer<EuclideanDoublePoint> transformer =
            new KMeansPlusPlusClusterer<EuclideanDoublePoint>(new Random());
            //new KMeansPlusPlusClusterer<EuclideanDoublePoint>(new Random(1746432956321l));
       
        EuclideanDoublePoint[] points = new EuclideanDoublePoint[populationSize];
       
        for (int i=0; i < populationSize; i++) {
            points[i] = new EuclideanDoublePoint(population[i]);
        }
           
        List<Cluster<EuclideanDoublePoint>> clusters = transformer.cluster(Arrays.asList(points), expectedClusters, iterations);
        return clusters;
	}
	
	public static double AvgSilhouette(List<Cluster<EuclideanDoublePoint>> clusters) {
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
	
	public static double[][] ProximityMatrix(double[][] population) {
		int populationSize = population.length;
		double[][] ProximityMatrix = new double[populationSize][populationSize];
		
		//initialize ProximityMatrix
		for (int i = 0; i < populationSize; i++) {
			for (int j = 0; j < populationSize; j++) {
				if (i != j) {
					ProximityMatrix[i][j] = MatLab.distance(population[i], population[j]);
				} else ProximityMatrix[i][j] = 0;
			}
		}
		return ProximityMatrix;
	}
}
