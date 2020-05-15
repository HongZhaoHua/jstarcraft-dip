package com.github.kilianB.clustering.distance;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.HashSet;

/**
 * @author Kilian
 *
 */
public class JaccardIndex implements DistanceFunction {

	
	//TODO jmh performance benchmarking! This can't be optimal
	
	
	@Override
	public double distance(double[] v0, double[] v1) {
		HashSet<Double> a = new HashSet(Arrays.asList(v0));
		HashSet<Double> b = new HashSet(Arrays.asList(v0));
		HashSet union = new HashSet<>(a);
		union.addAll(b);
		b.retainAll(a);
		return b.size() / (double)union.size();
	}
	
	

	@Override
	public double distance(DoubleSummaryStatistics[] v0, double[] v1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double distanceSquared(double[] v0, double[] v1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double distanceSquared(DoubleSummaryStatistics[] v0, double[] v1) {
		// TODO Auto-generated method stub
		return 0;
	}

}
