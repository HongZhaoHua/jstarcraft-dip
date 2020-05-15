package com.github.kilianB.clustering.distance;

import java.util.DoubleSummaryStatistics;

/**
 * @author Kilian
 *
 */
public class ManhattanDistance implements DistanceFunction{


	@Override
	public double distance(double[] v0, double[] v1) {
		double dist = 0;
		for(int i = 0; i < v0.length; i++) {
			dist += Math.abs(v0[i] - v1[i]);
		}
		return dist;
	}

	@Override
	public double distance(DoubleSummaryStatistics[] v0, double[] v1) {
		double dist = 0;
		for(int i = 0; i < v0.length; i++) {
			dist += Math.abs(v0[i].getAverage() - v1[i]);
		}
		return dist;
	}

	@Override
	public double distanceSquared(double[] v0, double[] v1) {
		return Math.pow(distance(v0,v1),2);
	}

	@Override
	public double distanceSquared(DoubleSummaryStatistics[] v0, double[] v1) {
		return Math.pow(distance(v0,v1),2);
	}

}
