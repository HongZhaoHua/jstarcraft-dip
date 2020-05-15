package com.github.kilianB.clustering;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.github.kilianB.ArrayUtil;
import com.github.kilianB.clustering.distance.DistanceFunction;
import com.github.kilianB.clustering.distance.EuclideanDistance;

/**
 * @author Kilian
 *
 */
public class DBScan implements ClusterAlgorithm {

	/**
	 * number of neighboors a points has to have in order to be considered a core
	 * point
	 */
	private int minPoints;

	/** max distance to look for neightboors */
	private double eps;

	private DistanceFunction dist;

	public DBScan(int minPoints, double eps) {
		this(minPoints, eps, new EuclideanDistance());
	}

	public DBScan(int minPoints, double eps, DistanceFunction dist) {

		this.minPoints = minPoints;
		this.eps = eps;
		this.dist = dist;
	}

	// Possible optimizations

	// 0.
	// Avoid duplicate neighbor computation; Space bs memory?
	// HashMap<Integer,List<Integer>> neightboorMap = new HashMap<>();

	// 1.
	// We also could create copies of the data array one dimension at a time and
	// sort it by distance. Cutoff if distance is alreay > than x

	// 2.
	// Sort the datapoints into buckets (evently?) spaced over the domain.
	// Now only the distance extremes have to be computed to the current point.
	// if it is already furhter away discard all points else do indepth calculation
	// for all points.

	@Override
	public ClusterResult cluster(double[][] data) {

		// corepoints if minPts are within distance eps
		// direct reachable
		// reachable
		// noise

		int clusters = -1;

		// -2 not worked on. -1 noise
		int cluster[] = new int[data.length];
		ArrayUtil.fillArray(cluster, () -> {
			return -2;
		});

		for (int i = 0; i < data.length; i++) {

			// Point already processed
			if (cluster[i] != -2) {
				continue;
			}

			List<Integer> neighboor = new ArrayList<>();

			for (int j = 0; j < data.length; j++) {
				if (i != j && dist.distance(data[i], data[j]) <= eps) {
					neighboor.add(j);
				}
			}

			if (neighboor.size() < minPoints) {
				// Noise
				cluster[i] = -1;
				continue;
			}

			clusters++;
			// Cluster
			cluster[i] = clusters;

			ListIterator<Integer> liter = neighboor.listIterator();

			while (liter.hasNext()) {

				int j = liter.next();

				// if point is noise add it to the cluster
				if (cluster[j] == -1) {
					cluster[j] = clusters;
					continue;
					// If it already belongs to a cluster skip it
				} else if (cluster[j] != -2) {
					continue;
				}
				cluster[j] = clusters;

				List<Integer> newNeighboots = new ArrayList<>();

				for (int m = 0; m < data.length; m++) {
					if (m != j) {
						if (dist.distance(data[m], data[j]) < eps) {
							newNeighboots.add(m);
						}
					}
				}

				if (newNeighboots.size() >= eps) {
					// liter.add
					for (int n : newNeighboots) {
						liter.add(n);
						// Reset cursor
						liter.previous();
					}
				}
			}
		}

		return new ClusterResult(cluster, data);
	}
}
