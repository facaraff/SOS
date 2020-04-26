/**
Copyright (c) 2020, Fabio Caraffini (fabio.caraffini@gmail.com, fabio.caraffini@dmu.ac.uk)
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
package utils.algorithms.operators;


//import static utils.MatLab.indexMin;
//import static utils.MatLab.subtract;
import static utils.MatLab.sum;
import static utils.MatLab.multiply;
//import utils.MatLab;

import static utils.algorithms.Misc.generateRandomSolution;
import interfaces.Problem;

import utils.random.RandUtils;

/** 
 *@author Fabio Caraffini (fabio.caraffini@gmail.com)
 *{@link www.tinyurl.com/FabioCaraffini}
 *
 *
 * @package utils.algorithms.operators
 * *{@link www.doi.org.com/10.0.81.208/preprints202003.0381.v1}
 * 
 *  
 * @file PSOOp.java
 * 
 * This class contains the implementation of Particle Swarm optimisation operators.
*/

public class PSOOp
{	
	/**
	* This method is used to initialise the velocity vector
	* 
	* @param s The initialisation method identifier.
	* @param p The problem to be optimised.
	* 
	* @return v  The generated velocity vector.
	*/
	public static double[] initVelocityVector(String s, Problem p) 
	{
		int n = p.getDimension();
		double[] v = new double[n];
		
		switch (s)
		{
			case "Kononova2015": //https://doi.org/10.1016/j.ins.2014.11.035
				for(int i=0; i<n; i++)
					v[i] = RandUtils.uniform(0, 0.1);
				break;
			default:
				v = generateRandomSolution(p);
				break;
		}
		
		return v;
	}
	
	/**
	* This is the classic PSO velocity update method
	* 
	* @param v The current velocity vector associated to a particle P
	* @param x The current position of the particle P
	* @param pBest The personal best position occupied by the particle P.
	* @param gBest The global best particle.
	* @param phi1 The inertia weight 
	* @param phi2 The first acceleration coefficient
	* @param phi3 The second acceleration coefficient
	* 
	* @return newV The updated velocity vector.
	*/
	public static double[] classicVelocityUpdate(double[] v, double[] x, double[] pBest, double[] gBest, double phi1, double phi2, double phi3) 
	{
		int n = v.length;
		double[] newV = new double[n];
		for(int i =0; i<n; i++)
			newV[i] = phi1*v[i]+phi2*RandUtils.random()*(pBest[i]-x[i])+phi3*RandUtils.random()*(gBest[i]-x[i]);
	 	return newV;
	}
	
	
	/**
	* This is the classic PSO perturbation operator
	* 
	* @param x The position of the current particle P (to be perturbed).
	* @param v The newly generated Velocity vector for the particle P.
	* 
	* @param gamma1 A multiplicative factor for x
	* @param gamma2 A multiplicative factor for v
	* 
	* @return a new particle.
	*/
	
	public static double[] moveParticle(double[] x, double[] v) {return moveParticle(x,v,1,1);}
	
	public static double[] moveParticle(double[] x, double[] v, double gamma1, double gamma2) {return sum(multiply(gamma1,x), multiply(gamma2,v));}
}


