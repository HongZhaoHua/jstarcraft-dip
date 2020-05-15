package com.github.kilianB.clustering.distance;

import java.util.DoubleSummaryStatistics;

/**
 * @author Kilian
 *
 */
public interface DistanceFunction {

	public double distance(double[] v0, double[] v1);
	public double distance(DoubleSummaryStatistics[] v0, double[] v1);
	
	public double distanceSquared(double[] v0, double[] v1);
	public double distanceSquared(DoubleSummaryStatistics[] v0, double[] v1);
	
}
