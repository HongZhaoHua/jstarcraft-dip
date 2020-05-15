package com.github.kilianB.clustering;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import com.github.kilianB.ArrayUtil;
import com.github.kilianB.MathUtil;
import com.github.kilianB.StringUtil;
import com.github.kilianB.clustering.distance.DistanceFunction;
import com.github.kilianB.clustering.distance.EuclideanDistance;
import com.github.kilianB.graphics.ColorUtil;
import com.github.kilianB.mutable.MutableDouble;

import javafx.scene.paint.Color;

/**
 * @author Kilian
 *
 */
public class ClusterResult {

	protected int numberOfClusters;

	/** Keep track to which cluster a certain points belongs */
	protected int clusterIndex[];

	/** Compute the min max average and mean of each cluster */
	protected HashMap<Integer, DoubleSummaryStatistics[]> stats = new HashMap<>();

	// For each cluster we have a list with features in n dimension
	protected HashMap<Integer, List<double[]>> clusters = new HashMap<>();
	// clusters.get(clusterIndex).get(dataPoint)[dataDimension]

	// Cluster metrics
	private double sseSum;
	private HashMap<Integer, MutableDouble> sse = new HashMap<>();
	private HashMap<Integer, MutableDouble> silhouetteCoef = new HashMap<>();

	//Cohesion ...
	
	//Radius ... diameter
	//density volume/points
	
	
	public ClusterResult(int[] clusterIndex, double[][] data) {

		int dimensions = data[0].length;

		this.clusterIndex = clusterIndex;

		// How many clusters do we work with
		numberOfClusters = ArrayUtil.maximum(clusterIndex) + 1;

		// Prepare datastructures

		// -1 for noise
		for (int cluster = -1; cluster < numberOfClusters; cluster++) {
			clusters.put(cluster, new ArrayList<>());
			DoubleSummaryStatistics[] clusterStats = new DoubleSummaryStatistics[dimensions];
			ArrayUtil.fillArray(clusterStats, () -> {
				return new DoubleSummaryStatistics();
			});
			stats.put(cluster, clusterStats);
			sse.put(cluster, new MutableDouble(0));
			silhouetteCoef.put(cluster, new MutableDouble(0));
		}

		for (int i = 0; i < data.length; i++) {
			int cluster = clusterIndex[i];

			clusters.get(cluster).add(data[i]);
			DoubleSummaryStatistics[] clusterStats = stats.get(cluster);
			for (int dim = 0; dim < dimensions; dim++) {
				clusterStats[dim].accept(data[i][dim]);
			}
		}

		// Compute metrics

		DistanceFunction eucD = new EuclideanDistance();

		// For each datapoint
		for (int i = 0; i < data.length; i++) {

			int cluster = clusterIndex[i];

			if (cluster == -1) {
				continue;
			}

			// Summed Squared Error
			MutableDouble m = sse.get(cluster);
			double error = 0;
			for (int j = 0; j < data[i].length; j++) {
				error += Math.pow((data[i][j] - stats.get(cluster)[j].getAverage()), 2);
			}
			m.setValue(m.getValue() + error);

			// Silhouette Coefficient

			// 0. For each point calculate the distance to all other points in the same
			// cluster

			List<double[]> sameCluster = clusters.get(cluster);

			// -1 don't count itself
			int pointsInCluster = sameCluster.size() - 1;

			double avgDistSameCluster = 0;
			for (double[] p : sameCluster) {
				avgDistSameCluster += (eucD.distance(data[i], p) / pointsInCluster);
			}

			double minAvgDistanceOtherCluster = Double.MAX_VALUE;

			for (int j = 0; j < numberOfClusters; j++) {
				if (j != cluster) {
					double avgDistanceOtherCluster = 0;
					List<double[]> otherCluster = clusters.get(j);
					pointsInCluster = otherCluster.size();

					for (double[] p : otherCluster) {
						avgDistanceOtherCluster += (eucD.distance(data[i], p) / pointsInCluster);
					}
					if (avgDistanceOtherCluster < minAvgDistanceOtherCluster) {
						minAvgDistanceOtherCluster = avgDistanceOtherCluster;
					}
				}
			}

			double silhoutteCoefficient;

			if (avgDistSameCluster < minAvgDistanceOtherCluster) {
				silhoutteCoefficient = 1 - (avgDistSameCluster / minAvgDistanceOtherCluster);
			} else {
				silhoutteCoefficient = (minAvgDistanceOtherCluster / avgDistSameCluster) - 1;
			}

			// System.out.println(avgDistSameCluster<minAvgDistanceOtherCluster);

			MutableDouble sil = silhouetteCoef.get(cluster);
			sil.setValue(sil.getValue() + silhoutteCoefficient / sameCluster.size());

		}

		for (int i = 0; i < numberOfClusters; i++) {
			sseSum += sse.get(i).doubleValue();
		}

	}

	// Cohesian /Area of the cluster.

	public void printInformation() {

		StringBuilder sb = new StringBuilder();
		sb.append("Observations: ").append(clusterIndex.length).append("\n").append("Number of Clusters: ")
				.append(numberOfClusters).append("\n");

		int clusterLength = StringUtil.charsNeeded(numberOfClusters);
		int obsLength = StringUtil.charsNeeded(clusterIndex.length);

		String format = "%-" + clusterLength + "d (Obs:%" + obsLength + "d) |";
		int hLength = Math.max("Clusters: |".length(), clusterLength + 1 + 5 + obsLength + 2);

		// Header
		sb.append(String.format("%-" + hLength + "s", "Clusters: ")).append("| Centeroids:\n");

		double silouetteCoeffificient = 0;

		// String formatCenteroid = "%.3f";
		DecimalFormat df = new DecimalFormat(".000");
		DecimalFormat sseDf = new DecimalFormat("0.00E0");

		for (int i = 0; i < numberOfClusters; i++) {
			sb.append(String.format(format, i, clusters.get(i).size()));
			// Cluster stats;
			DoubleSummaryStatistics[] cStats = stats.get(i);
			sb.append(" [ ");
			for (int j = 0; j < cStats.length; j++) {
				sb.append(df.format(cStats[j].getAverage())).append(" ");
			}
			silouetteCoeffificient += silhouetteCoef.get(i).getValue();
			sb.append("] Silhouette Coef: ").append(df.format(silhouetteCoef.get(i).getValue())).append(" SSE:")
					.append(sseDf.format(sse.get(i).doubleValue())).append("\n");
		}

		sb.append("SSE: " + df.format(sseSum)).append("\n");
		sb.append("Silhouette Coef/#clusters: " + df.format(silouetteCoeffificient / numberOfClusters)).append("\n");

		System.out.println(sb.toString());
	}

	public void toImage(File outputFile) {
		BufferedImage bi = new BufferedImage(700, 700, 0x1);
		Graphics g = bi.getGraphics();

		// Find the range of the data

		double minVal = Double.MAX_VALUE;
		double maxVal = -Double.MAX_VALUE;

		for (int cluster = 0; cluster < numberOfClusters; cluster++) {
			DoubleSummaryStatistics[] clusterStats = stats.get(cluster);
			for (int dim = 0; dim < clusterStats.length; dim++) {

				if (clusterStats[dim].getMax() > maxVal) {
					maxVal = clusterStats[dim].getMax();
				}
				if (clusterStats[dim].getMin() < minVal) {
					minVal = clusterStats[dim].getMin();
				}
			}
		}

		javafx.scene.paint.Color[] c = ColorUtil.ColorPalette.getPaletteHue(numberOfClusters, Color.BLUE, Color.RED);

		// Scale data
		g.fillRect(0, 0, 700, 700);

		double newMin = 0;
		double newMax = 700;
		double observedRange = maxVal - minVal;
		double newRange = newMax - newMin;

		for (int i = -1; i < numberOfClusters; i++) {
			if (i == -1) {
				g.setColor(ColorUtil.fxToAwtColor(Color.GRAY));
			} else {
				g.setColor(ColorUtil.fxToAwtColor(c[i]));
			}
			List<double[]> points = clusters.get(i);
			for (double[] point : points) {
				int x = (int) MathUtil.normalizeValue(point[0], observedRange, maxVal, newRange, newMax, true);
				int y = (int) MathUtil.normalizeValue(point[1], observedRange, maxVal, newRange, newMax, true);
				g.fillOval(x, y, 10, 10);
			}
		}
		g.dispose();
		try {
			ImageIO.write(bi, "png", outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<Integer, List<double[]>> getClusters() {
		return clusters;
	}

	public List<double[]> getCluster(int cluster) {
		return clusters.get(cluster);
	}
	
	public DoubleSummaryStatistics[] getStats(int cluster) {
		return stats.get(cluster);
	}
	

	public int[] getClusterData() {
		return clusterIndex;
	}

	// Metrics

	public double getSumSquaredError() {
		return sseSum;
	}

}
