package com.github.kilianB.pcg;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.github.kilianB.pcg.sync.PcgRR;

/**
 * @author Kilian
 *
 */
public abstract class PcgBaseTest {

	@Nested
	class State {

		public void equalGenerationSeed() {
			Pcg rng0 = getInstance(0, 0);
			Pcg rng1 = getInstance(0, 0);

			byte[] values = new byte[50];
			byte[] values1 = new byte[50];

			rng0.nextBytes(values);
			rng1.nextBytes(values1);

			assertArrayEquals(values, values1);
		}

		@Test
		public void unequelGenerationSeed() {

			Pcg rng0 = getInstance();
			Pcg rng1 = getInstance();

			byte[] values = new byte[50];
			byte[] values1 = new byte[50];

			rng0.nextBytes(values);
			rng1.nextBytes(values1);

			// the generators produce distinct output
			assertFalse(Arrays.equals(values, values1));

		}

		@Test
		public void splitted() {
			try {

				Pcg rng = getInstance();
				Pcg clone = rng.split();

				// Make sure that they don't share the same state
				byte[] values = new byte[50];
				byte[] values1 = new byte[50];

				rng.nextBytes(values);
				clone.nextBytes(values1);

				assertArrayEquals(values, values1);

			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
		}

		@Test
		public void splittedDistinct() {
			try {
				Pcg rng = getInstance();
				Pcg clone = rng.splitDistinct();

				// Make sure that they don't share the same state
				byte[] values = new byte[50];
				byte[] values0 = new byte[50];
				byte[] values1 = new byte[50];

				rng.nextBytes(values);
				clone.nextBytes(values1);

				rng.advance(-50);
				rng.nextBytes(values0);

				// first condition the generators don't impact each other
				assertArrayEquals(values, values0);
				// second condition the generators produce distinct output
				assertFalse(Arrays.equals(values, values1));

			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
		}

		@Test
		@Disabled
		public void setIncEven() {
			assertThrows(IllegalArgumentException.class, () -> {
				// Both are private or protected
				// new PcgRSFast(4,4,false);
			});
		}
	}

	/**
	 * Rngs support to skip x numbers and fast skip or rewind it's state
	 * 
	 * @author Kilian
	 *
	 */
	@Nested
	class Step {

		@Test
		public void skip() {

			Pcg rng = getInstance(0, 0);

			for (int i = 0; i < 1000; i++) {
				rng.nextInt();
			}

			// Generate 1000 ints;
			int baseInt = rng.nextInt();

			// Re seed
			rng = getInstance(0, 0);
			// Fast skip 1000
			rng.advance(1000);

			int skipInt = rng.nextInt();

			assertEquals(baseInt, skipInt);
		}

		@Test
		public void rewind() {
			Pcg rng = getInstance(0, 0);

			int[] generatedValues = new int[10];
			int[] generatedValues1 = new int[10];
			for (int i = 0; i < 10; i++) {
				generatedValues[i] = rng.nextInt();
			}

			// Rewind
			rng.advance(-10);
			for (int i = 0; i < 10; i++) {
				generatedValues1[i] = rng.nextInt();
			}
			assertArrayEquals(generatedValues, generatedValues1);
		}

	}

	@Nested
	class Distance {

		Pcg rng;
		Pcg rng0;

		@BeforeEach
		public void reSeed() {

			// Seed
			long seed = System.nanoTime();
			long streamNumber = 0;

			// Seed both instances
			rng = getInstance(seed, streamNumber);
			rng0 = getInstance(seed, streamNumber);
		}

		@Test
		public void identity() {
			assertEquals(0, rng.distance(rng));
		}

		@Test
		public void positiveDistance() {
			rng0.advance(1000);
			assertEquals(1000, rng.distance(rng0));
		}

		@Test
		public void negativeDistance() {
			rng0.advance(-1000);
			assertEquals(-1000, rng.distance(rng0));
		}

		@Test
		public void equalDistance() {
			assertEquals(0, rng.distanceUnsafe(rng0));
		}

		@Test
		public void incompatibleGeneratosClass() {
			assertThrows(IncompatibleGeneratorException.class, () -> {
				rng.distance(getInstance());
			});
		}

		// TODO
		@SuppressWarnings("deprecation")
		@Test
		public void incompatibleGeneratosIncrement() {
			// Why TODO
			assertThrows(IllegalArgumentException.class, () -> {
				rng.distance(new PcgRR(rng.getState(), rng.getInc() + 1, false));
			});
		}

	}

	/**
	 * Test if the bounded function return the expected values. Rough tests
	 * 
	 * @author Kilian
	 *
	 */
	@Nested
	class Bounds {

		Pcg rng;

		@BeforeEach
		public void seed() {
			rng = getInstance();
		}

		@Test
		public void boolProbabilityAlwaysTrue() {
			for (int i = 0; i < 500; i++) {
				assertTrue(rng.nextBoolean(1d));
			}
		}

		@Test
		public void boolProbabilityAlwaysFalse() {
			for (int i = 0; i < 500; i++) {
				assertFalse(rng.nextBoolean(0));
			}
		}

		
		@Test
		public void boolProbabilityOutOfBoundsUpper() {
			assertThrows(IllegalArgumentException.class,()->{rng.nextBoolean(1.1);});
		}
		
		@Test
		public void boolProbabilityOutOfBoundsLower() {
			assertThrows(IllegalArgumentException.class,()->{rng.nextBoolean(-1);});
		}
		
		
		/*
		 * This is just a very very rough test.. Not sure if it even should be included
		 */
		@Test
		public void booleanProbability() {

			int trueC = 0;
			double probability = 0.3;

			double acceptedDelta = 0.01;

			int reps = 50000;

			for (int i = 0; i < reps; i++) {
				if (rng.nextBoolean(probability)) {
					trueC++;
				}
			}
			double expected = (reps * probability);
			assertEquals(expected, trueC, reps * acceptedDelta);
		}

		@Test
		public void intBoundPow2() {
			int upperBound = 4;
			for (int i = 0; i < 10000; i++) {
				int genInt = rng.nextInt(upperBound);
				if (genInt < 0 || genInt >= upperBound) {
					fail();
				}
			}
		}

		@Test
		public void intBound() {
			int upperBound = 141;
			for (int i = 0; i < 10000; i++) {
				int genInt = rng.nextInt(upperBound);
				if (genInt < 0 || genInt >= upperBound) {
					fail();
				}
			}
		}

		@Test
		public void longBound() {
			long upperBound = 4;
			for (int i = 0; i < 10000; i++) {
				long genLong = rng.nextLong(upperBound);
				if (genLong < 0 || genLong >= upperBound) {
					fail();
				}
			}
		}
		
		@Test
		public void longBoundInvalid() {
			assertThrows(IllegalArgumentException.class,()->{rng.nextLong(0);});
		}
	}

	@Test
	public void nonFast() {
		Pcg rng = getInstance();
		assertEquals(isFast(), rng.isFast());
	}

	public abstract Pcg getInstance();

	public abstract Pcg getInstance(long seed, long streamNumber);

	public abstract boolean isFast();

}
