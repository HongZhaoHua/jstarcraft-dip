package com.github.kilianB.clustering;

import java.util.DoubleSummaryStatistics;
import java.util.Random;

import com.github.kilianB.ArrayUtil;
import com.github.kilianB.clustering.distance.DistanceFunction;
import com.github.kilianB.clustering.distance.EuclideanDistance;
import com.github.kilianB.pcg.fast.PcgRSFast;

/**
 * @author Kilian
 *
 */
public class KMeans implements ClusterAlgorithm {

	/**
	 * The number of cluster the data will be partitioned into
	 */
	protected int k;

	/**
	 * Function to calculate distance between individual data points
	 */
	protected DistanceFunction distanceFunction;

	protected int lastIterationCount;

	/**
	 * Create a KMeans clusterer with k clusters and EuclideanDistance.
	 * 
	 * @param clusters the number of cluster to partition the data into
	 */
	public KMeans(int clusters) {
		this(clusters, new EuclideanDistance());
	}

	/**
	 * Create a KMeans clusterer
	 * 
	 * @param clusters         the number of cluster to partition the data into
	 * @param distanceFunction the distanceFunction used to compute the distance
	 *                         between data points
	 */
	public KMeans(int clusters, DistanceFunction distanceFunction) {
		this.k = clusters;
		this.distanceFunction = distanceFunction;
	}

	@Override
	public ClusterResult cluster(double[][] data) {

		int[] cluster = new int[data.length];

		// If only one cluster is available return an array indicating all data
		// belonging to this one cluster
		if (k == 1) {
			ArrayUtil.fillArray(cluster, () -> {
				return 0;
			});
			return new ClusterResult(cluster, data);
		} else if (k >= data.length) {
			throw new IllegalArgumentException("Can't compute more clusters than datapoints are present");
		}
		// How many dimension does each datapoint have?
		int dataDimension = data[0].length;

		// 0 = choose random start clusters
		DoubleSummaryStatistics[][] clusterMeans = computeStartingClusters(data, k, dataDimension);

		// Iteratively improve clusters
		computeKMeans(clusterMeans, data, cluster, dataDimension);

		return new ClusterResult(cluster, data);
	}

	protected DoubleSummaryStatistics[][] computeStartingClusters(double[][] data, int k, int dataDimension) {

		// Fast high quality rng
		Random rng = new PcgRSFast();

		double[][] range = new double[data.length][2];
		DoubleSummaryStatistics[][] clusterMeans = new DoubleSummaryStatistics[k][dataDimension];

		for (double[] arr : range) {
			arr[0] = Double.MAX_VALUE;
			arr[1] = -Double.MAX_VALUE;
		}

		ArrayUtil.fillArrayMulti(clusterMeans, () -> {
			return new DoubleSummaryStatistics();
		});

		// 0.1 find a minimum and maximum of each variable domain
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < dataDimension; j++) {
				double value = data[i][j];
				// Minimum value
				if (value < range[i][0]) {
					range[i][0] = value;
				}
				// Maximum value
				if (value > range[i][1]) {
					range[i][1] = value;
				}
			}
		}

		// We don't choose a random location we choose a random point. To get a location
		// we need min and max by itterating over the entire set.
		for (int i = 0; i < k; i++) {
			for (int j = 0; j < dataDimension; j++) {
				// compute a random cluster point within the min and max of this variable
				clusterMeans[i][j].accept((rng.nextDouble() * (range[j][1] - range[j][0])) + range[j][0]);
			}
		}

		return clusterMeans;
	}

	protected void computeKMeans(DoubleSummaryStatistics[][] clusterMeans, double[][] data, int[] cluster,
			int dataDimension) {
		lastIterationCount = 0;
		boolean dirty = false;
		do {
			dirty = false;
			for (int dataIndex = 0; dataIndex < data.length; dataIndex++) {

				double minDistance = Double.MAX_VALUE;
				int bestCluster = -1;

				for (int clusterIndex = 0; clusterIndex < k; clusterIndex++) {

					double distToCluster = distanceFunction.distance(clusterMeans[clusterIndex], data[dataIndex]);
					if (distToCluster < minDistance) {
						bestCluster = clusterIndex;
						minDistance = distToCluster;
					}
				}

				if (cluster[dataIndex] != bestCluster) {
					cluster[dataIndex] = bestCluster;
					dirty = true;
				}
			}

			if (dirty) {
				// recompute cluster means

				// Reset
				ArrayUtil.fillArrayMulti(clusterMeans, () -> {
					return new DoubleSummaryStatistics();
				});
				for (int dataIndex = 0; dataIndex < data.length; dataIndex++) {
					double dat[] = data[dataIndex];
					DoubleSummaryStatistics[] clusterTemp = clusterMeans[cluster[dataIndex]];
					for (int i = 0; i < dataDimension; i++) {
						clusterTemp[i].accept(dat[i]);
					}
				}
			}
			lastIterationCount++;
		} while (dirty);
	}

	/**
	 * @return the number of iterations used to cluster the data
	 */
	public int iterations() {
		return lastIterationCount;
	}
}
