package com.github.kilianB.clustering;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
//@TestInstance(Lifecycle.PER_CLASS)
class KMeansPlusPlusTest {

	private static double[][] clusterTestData;

	@BeforeAll
	private static void loadTestset() {

		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				KMeansPlusPlusTest.class.getClassLoader().getResourceAsStream("clusterTestData.txt")))) {
			String line;
			List<double[]> dataPoints = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split("\t");
				dataPoints.add(new double[] { Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]) });
			}
			clusterTestData = dataPoints.toArray(new double[dataPoints.size()][2]);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	void compareKMeans() {
		KMeans kmeans = new KMeans(10);
		KMeans kmeansPp = new KMeansPlusPlus(10);
		
		int kCount = 0;
		int kppCount = 0;
		for(int i = 0; i < 15; i++) {
			kmeans.cluster(clusterTestData);
			kmeansPp.cluster(clusterTestData);
			
			kCount += kmeans.iterations();
			kppCount += kmeansPp.iterations();
		}
		assertTrue(kCount > kppCount);
	}

}
