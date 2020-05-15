package com.github.kilianB.pcg.fast;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.github.kilianB.pcg.sync.PcgRS;

/**
 * JUnit tests are only used to test methods like distance advance/skip/rewind
 * states and other ordinary functionality. It is not designed to test
 * distributions or statistical properties!
 * <p>
 * 
 * Statistical properties are tested with PractRand evaluation
 * <p>
 * Performance are checked by jmh
 * 
 * @author Kilian
 *
 */
class PcgRSUFastTest {

	// Compare to non fast version for consistency
	private static PcgRS rsRNG;

	/**
	 * Rngs support to skip x numbers and fast skip or rewind it's state
	 * 
	 * @author Kilian
	 *
	 */
	@Nested
	class Step {

		@Test
		void skip() {

			PcgRSUFast.seed(0, 0);

			for (int i = 0; i < 1000; i++) {
				PcgRSUFast.nextInt();
			}

			// Generate 1000 ints;
			int baseInt = PcgRSUFast.nextInt();

			// Re seed
			PcgRSUFast.seed(0, 0);
			// Fast skip 1000
			PcgRSUFast.advance(1000);

			int skipInt = PcgRSUFast.nextInt();

			assertEquals(baseInt, skipInt);
		}

		@Test
		void rewind() {
			int[] generatedValues = new int[10];
			int[] generatedValues1 = new int[10];
			for (int i = 0; i < 10; i++) {
				generatedValues[i] = PcgRSUFast.nextInt();
			}

			// Rewind
			PcgRSUFast.advance(-10);
			for (int i = 0; i < 10; i++) {
				generatedValues1[i] = PcgRSUFast.nextInt();
			}
			assertArrayEquals(generatedValues, generatedValues1);
		}

	}

	@Nested
	class Distance {

		@BeforeEach
		void reSeed() {

			// Seed
			long seed = System.nanoTime();
			long streamNumber = 0;

			// Seed both instances
			rsRNG = new PcgRS(seed, streamNumber);
			PcgRSUFast.seed(seed, streamNumber);
		}

		@Test
		void positiveDistance() {
			rsRNG.advance(1000);
			assertEquals(1000, PcgRSUFast.distance(rsRNG));
		}

		@Test
		void negativeDistance() {
			rsRNG.advance(-1000);
			assertEquals(-1000, PcgRSUFast.distance(rsRNG));
		}

		@Test
		void equalDistance() {
			assertEquals(0, PcgRSUFast.distance(rsRNG));
		}
	}

	/**
	 * These tests are just rough
	 * 
	 * @author Kilian
	 *
	 */
	@Nested
	class Bounds {

		@Test
		void boolProbabilityAlwaysTrue() {
			for (int i = 0; i < 500; i++) {
				assertTrue(PcgRSUFast.nextBoolean(1));
			}
		}

		@Test
		void boolProbabilityAlwaysFalse() {
			for (int i = 0; i < 500; i++) {
				assertFalse(PcgRSUFast.nextBoolean(0));
			}
		}

		/*
		 * This is just a very very rough test.. Not sure if it even should be included
		 */
		@Test
		void booleanProbability() {

			int trueC = 0;
			double probability = 0.3;

			double acceptedDelta = 0.01;

			int reps = 50000;

			for (int i = 0; i < reps; i++) {
				if (PcgRSUFast.nextBoolean(probability)) {
					trueC++;
				}
			}
			double expected = (reps * probability);
			assertEquals(expected, trueC, reps * acceptedDelta);
		}

		@Test
		void intBound() {
			int upperBound = 4;
			for (int i = 0; i < 10000; i++) {
				int genInt = PcgRSUFast.nextInt(upperBound);
				if (genInt < 0 || genInt >= upperBound) {
					fail();
				}
			}
		}

		@Test
		void longBound() {
			long upperBound = 4;
			for (int i = 0; i < 10000; i++) {
				long genLong = PcgRSUFast.nextLong(upperBound);
				if (genLong < 0 || genLong >= upperBound) {
					fail();
				}
			}
		}
	}

}
