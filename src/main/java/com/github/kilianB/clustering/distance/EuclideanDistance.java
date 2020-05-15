package com.github.kilianB.clustering.distance;

import java.util.DoubleSummaryStatistics;

/**
 * @author Kilian
 *
 */
public class EuclideanDistance implements DistanceFunction{

	public double distance(DoubleSummaryStatistics[] v0, double v1[]) {
		return Math.sqrt(distanceSquared(v0,v1));
	}
	
	public double distanceSquared(DoubleSummaryStatistics[] v0, double v1[]) {
		assert v0.length == v1.length;
		double distance = 0;		
		for(int i = 0; i < v0.length; i++){
			double temp = v0[i].getAverage() - v1[i];
			distance += temp * temp;
		}
		return distance;
	}
	
	public double distance(double[] v0, double v1[]) {
		return Math.sqrt(distanceSquared(v0,v1));
	}

	@Override
	public double distanceSquared(double[] v0, double[] v1) {
		assert v0.length == v1.length;
		double distance = 0;		
		for(int i = 0; i < v0.length; i++){
			double temp = v0[i] - v1[i];
			distance += temp * temp;
		}
		return distance;
	}
	
	
}
