package com.github.kilianB.pcg.fast;

import com.github.kilianB.pcg.IncompatibleGeneratorException;
import com.github.kilianB.pcg.sync.PcgRS;

/**
 * A 64 bit State PcgRNG with 32 bit output. PCG-XSH-RR
 * <p>
 * 
 * The pcg family combines a linear congruential generators with a permutation
 * output function resulting in high quality pseudo random numbers.
 * <p>
 * 
 * The original concept was introduced by Melissa O’Neill please refer to
 * <a href="http://www.pcg-random.org/">pcg-random</a> for more information.
 * <p>
 * Opposed to RR this version performs a random shift rather than a random
 * rotation.
 * 
 * The RS instance permutates the output using the following function:
 * 
 * <pre>
 * {@code
 * ((state >>> 22) ^ state) >>> ((state >>> 61) + 22)
 * }
 * </pre>
 * 
 * This implementation is <b>Not</b> thread safe, inlines most methods manually
 * and performs other optimizations to maximize the throughput. All methods are
 * made static to achieve an even higher performance but leaves this class at a
 * questionable state. For almost all cases {@link PcgRSFast} is a much more
 * suited implementation.
 * 
 * @author Kilian
 * @see <a href="http://www.pcg-random.org/">www.pcg-random.org</a>
 */
public class PcgRSUFast {

	/**
	 * Linear congruential constant. Same as MMIX by Donald Knuth and Newlib, Musl
	 */
	private static final long MULT_64 = 6364136223846793005L;

	// 64 version
	/** 64 bit internal state */
	private static long state;
	/** Stream number of the rng. */
	private static long inc;

	private static boolean gausAvailable;
	private static double nextGaus;

	// static final variables are inlined by default
	private static final double DOUBLE_MASK = 1L << 53;
	private static final float FLOAT_UNIT = (float) (1 << 24);
	private static final long INTEGER_MASK = 0xFFFFFFFFL;
	// private static final int INTEGER_MASK_SIGNED = 0xFFFFFFFF;

	// Randomly seed the rng. A streamNumber of 0 results in a broken instance
	static {
		long seed = getRandomSeed(System.nanoTime());
		long streamNumber = getRandomSeed(System.currentTimeMillis() ^ System.nanoTime());
		seed(seed, streamNumber);
	}

	/**
	 * Hide default constructor. No reason to every initialize this class
	 */
	private PcgRSUFast() {
	}

	/**
	 * Seed the rng with the given seed and stream number. The seed defines the
	 * current state in which the rng is in and corresponds to seeds usually found
	 * in other RNG implementations. RNGs with different seeds are able to catch up
	 * after they exhaust their period and produce the same numbers.
	 * <p>
	 * 
	 * Different stream numbers alter the increment of the rng and ensure distinct
	 * state sequences
	 * <p>
	 * 
	 * Only generators with the same seed AND stream numbers will produce identical
	 * values
	 * <p>
	 * 
	 * @param seed         used to compute the starting state of the RNG
	 * @param streamNumber used to compute the increment for the lcg.
	 */
	public static void seed(long seed, long streamNumber) {
		state = 0;
		inc = (streamNumber << 1) | 1; // 2* + 1
		state = (state * MULT_64) + inc;
		state += seed;
		// Due to access to static vars the fast implementation is one step ahead of the
		// ordinary rngs. To get same results we can skip this state update
		// state = (state * MULT_64) + inc;
	}

	/**
	 * Advance or set back the rngs state.
	 * 
	 * In other words fast skip the next n generated random numbers or set the PNG
	 * back so it will create the last n numbers in the same sequence again.
	 * 
	 * <pre>
	 * 	int x = nextInt();
	 * 	nextInt(); nextInt();
	 * 	step(-3);
	 *	int y = nextInt(); 
	 *	x == y TRUE
	 * </pre>
	 * 
	 * Be aware that this relationship is only true for deterministic generation
	 * calls. {@link #nextGaussian()} or any bound limited number generations might
	 * loop and consume more than one step to generate a number.
	 * <p>
	 * 
	 * To advance n steps the function performs <code>Math.ceil( log2(n) )</code>
	 * iterations. So you may go ahead and skip as many steps as you like without
	 * any performance implications.
	 * <p>
	 * 
	 * Negative indices can be used to jump backwards in time going the long way
	 * around
	 * 
	 * 
	 * @param steps the amount of steps to advance or in case of a negative number
	 *              go back in history
	 */
	public static void advance(long steps) {
		long acc_mult = 1;
		long acc_plus = 0;

		long cur_plus = inc;
		long cur_mult = MULT_64;

		while (Long.compareUnsigned(steps, 0) > 0) {
			if ((steps & 1) == 1) { // Last significant bit is 1
				acc_mult *= cur_mult;
				acc_plus = acc_plus * cur_mult + cur_plus;
			}
			cur_plus *= (cur_mult + 1);
			cur_mult *= cur_mult;
			steps = Long.divideUnsigned(steps, 2);
		}
		state = (acc_mult * state) + acc_plus;
	}

	public static byte nextByte() {
		state = (state * MULT_64) + inc;
		return (byte) ((((state >>> 22) ^ state) >>> ((state >>> 61) + 22)) >>> 24);
	}

	public static void nextBytes(byte[] b) {
		for (int i = 0; i < b.length; i++) {
			state = (state * MULT_64) + inc;
			b[i] = (byte) ((((state >>> 22) ^ state) >>> ((state >>> 61) + 22)) >>> 24);
		}
	}

	public static char nextChar() {
		state = (state * MULT_64) + inc;
		// Why should we cast it to an int first can't we mask it to a char directly?
		return (char) ((((state >>> 22) ^ state) >>> ((state >>> 61) + 22)) >>> 16);
	}

	public static short nextShort() {
		state = (state * MULT_64) + inc;
		return (short) ((((state >>> 22) ^ state) >>> ((state >>> 61) + 22)) >>> 16);
	}

	/**
	 * Returns the next pseudorandom, uniformly distributed {@code int} value from
	 * this random number generator's sequence. The general contract of
	 * {@code nextInt} is that one {@code int} value is pseudorandomly generated and
	 * returned. All 2<sup>32</sup> possible {@code int} values are produced with
	 * (approximately) equal probability.
	 * 
	 * @return the next pseudorandom, uniformly distributed {@code int} value from
	 *         this random number generator's sequence
	 */
	public static int nextInt() {
		// we miss a single state and keep an old value around. but this does not alter
		// The produced number but shifts them 1 back.
		state = (state * MULT_64) + inc;
		// long oldState = state;
		return (int) (((state >>> 22) ^ state) >>> ((state >>> 61) + 22));
	}

	/**
	 * Returns a pseudorandom, uniformly distributed {@code int} value between 0
	 * (inclusive) and the specified value (exclusive), drawn from this random
	 * number generator's sequence.
	 * 
	 * @param n the upper bound (exclusive). Must be positive.
	 * @return the next pseudorandom, uniformly distributed {@code int} value
	 *         between zero (inclusive) and {@code bound} (exclusive) from this
	 *         random number generator's sequence
	 */
	public static int nextInt(int n) {
		state = (state * MULT_64) + inc;
		int r = (int) (((state >>> 22) ^ state) >>> ((state >>> 61) + 22)) >>> 1; // Unsigned!
		int m = n - 1;
		if ((n & m) == 0) // i.e., bound is a power of 2
			r = (int) ((n * (long) r) >> 31);
		else {
			for (int u = r; u - (r = u % n) + m < 0;) {
				state = (state * MULT_64) + inc;
				u = (int) (((state >>> 22) ^ state) >>> ((state >>> 61) + 22)) >>> 1;
			}
		}
		return r;
	};

	/**
	 * Returns the next pseudorandom, uniformly distributed {@code boolean} value
	 * from this random number generator's sequence. The general contract of
	 * {@code nextBoolean} is that one {@code boolean} value is pseudorandomly
	 * generated and returned. The values {@code true} and {@code false} are
	 * produced with (approximately) equal probability.
	 * 
	 * @return the next pseudorandom, uniformly distributed {@code boolean} value
	 *         from this random number generator's sequence
	 */
	public static boolean nextBoolean() {
		// Two choices either take the low bit or get a range 2 int and make an if
		state = (state * MULT_64) + inc;
		return (((((state >>> 22) ^ state) >>> (state >>> 61) + 22) & INTEGER_MASK) >>> 31) != 0;
	}

	public static boolean nextBoolean(double probability) {
		// Borrowed from https://cs.gmu.edu/~sean/research/mersenne/MersenneTwister.java
		if (probability == 0.0)
			return false;
		if (probability == 1.0)
			return true;

		state = (state * MULT_64) + inc;
		long l = ((((state >>> 22) ^ state) >>> ((state >>> 61) + 22))) & INTEGER_MASK;

		state = (state * MULT_64) + inc;

		return (((l >>> 6) << 27) + (((((state >>> 22) ^ state) >>> ((state >>> 61) + 22)) & INTEGER_MASK) >>> 5)) / DOUBLE_MASK < probability;
	}

	public static long nextLong() {

		state = (state * MULT_64) + inc;
		// No need to mask if we shift by 32 bits
		long l = (((state >>> 22) ^ state) >>> ((state >>> 61) + 22));

		state = (state * MULT_64) + inc;
		long j = (((state >>> 22) ^ state) >>> ((state >>> 61) + 22));

		// Long keep consistent with the random definition of keeping the lower word
		// signed,
		// But should this really be the case? Why don't we mask the sign bit?
		return (l << 32) + (int) j;
	}

	public static long nextLong(long n) {
		long bits;
		long val;
		do {
			state = (state * MULT_64) + inc;
			// No need to mask if we shift by 32 bits
			long l = (((state >>> 22) ^ state) >>> ((state >>> 61) + 22));

			state = (state * MULT_64) + inc;
			long j = (((state >>> 22) ^ state) >>> ((state >>> 61) + 22));

			bits = ((l << 32) + (int) j >>> 1);
			val = bits % n;
		} while (bits - val + (n - 1) < 0);
		return val;
	}

	public static double nextDouble() {
		state = (state * MULT_64) + inc;
		long l = ((((state >>> 22) ^ state) >>> ((state >>> 61) + 22))) & INTEGER_MASK;
		state = (state * MULT_64) + inc;
		return (((l >>> 6) << 27) + (((((state >>> 22) ^ state) >>> ((state >>> 61) + 22)) & INTEGER_MASK) >>> 5)) / DOUBLE_MASK;
	}

	// TODO
	public static double nextDouble(boolean includeZero, boolean includeOne) {
		double d = 0.0;
		do {
			state = (state * MULT_64) + inc;
			long l = ((((state >>> 22) ^ state) >>> ((state >>> 61) + 22))) & INTEGER_MASK;
			state = (state * MULT_64) + inc;
			d = (((l >>> 6) << 27) + (((((state >>> 22) ^ state) >>> ((state >>> 61) + 22)) & INTEGER_MASK) >>> 5)) / DOUBLE_MASK;

			// grab a value, initially from half-open [0.0, 1.0)
			if (includeOne) {
				// Only generate the boolean if it really is the case or we scramble the state
				state = (state * MULT_64) + inc;
				if ((((((state >>> 22) ^ state) >>> (state >>> 61) + 22) & INTEGER_MASK) >>> 31) != 0) {
					d += 1.0;
				}

			}

		} while ((d > 1.0) || // everything above 1.0 is always invalid
		        (!includeZero && d == 0.0)); // if we're not including zero, 0.0 is invalid
		return d;
	}

	public static float nextFloat() {
		state = (state * MULT_64) + inc;
		return (((((state >>> 22) ^ state) >>> ((state >>> 61) + 22)) & INTEGER_MASK) >>> 8) / FLOAT_UNIT;

	}

	public static float nextFloat(boolean includeZero, boolean includeOne) {
		float d = 0.0f;
		do {
			state = (state * MULT_64) + inc;
			d = (((((state >>> 22) ^ state) >>> ((state >>> 61) + 22)) & INTEGER_MASK) >>> 8) / FLOAT_UNIT; // grab a
			                                                                                                // value,
			                                                                                                // initially
			                                                                                                // from
			                                                                                                // half-open
			                                                                                                // [0.0f,
			                                                                                                // 1.0f)
			if (includeOne) {
				// Only generate the boolean if it really is the case or we scramble the state
				state = (state * MULT_64) + inc;
				if ((((((state >>> 22) ^ state) >>> (state >>> 61) + 22) & INTEGER_MASK) >>> 31) != 0) {
					d += 1.0f;
				}
			}
		} while ((d > 1.0f) || // everything above 1.0f is always invalid
		        (!includeZero && d == 0.0f)); // if we're not including zero, 0.0f is invalid
		return d;
	}

	// TODO
	public static double nextGaussian() {
		// Borrowed from https://cs.gmu.edu/~sean/research/mersenne/MersenneTwister.java

		// Shall we go atomic? the issue is after setting and returning a 2nd thread
		// could create
		// a new gaus making the following call return the same value. But for now we
		// don't care
		// about thread safety anyways
		if (gausAvailable) {
			gausAvailable = false;
			return nextGaus;
		} else {
			double v1;
			double v2;
			double s;
			do {
				v1 = 2 * nextDouble() - 1; // between -1.0 and 1.0
				v2 = 2 * nextDouble() - 1; // between -1.0 and 1.0
				s = v1 * v1 + v2 * v2;
			} while (s >= 1 || s == 0);
			double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
			nextGaus = v2 * multiplier;
			gausAvailable = true;
			return v1 * multiplier;
		}
	}

	// TODO
	@Deprecated
	public static void setStreamConstant(long constant) {
		inc = constant;
	}

	/**
	 * Calculate the distance of this generator to another RS instance. The distance
	 * is defined in the numbers of steps one generator has to perform to catch up
	 * and produce the same results as the other generator.
	 * 
	 * @param other the generator to compare this state to
	 * @return the distance between the two generators
	 */
	public static long distance(PcgRS other) {

		// Check if they are the same stream...
		if (inc != other.getInc()) {
			throw new IncompatibleGeneratorException("Can not compare generators with different" + " streams. Those generators will never converge");
		}

		long curState = state;
		long newState = other.getState();

		long curPlus = inc;
		long curMult = MULT_64;

		long bit = 1; // Fix bit was overflowing as an int was used!
		long distance = 0;

		// why should we mask here? This does exactly nothing!
		// long mask = ~0;

		while ((curState /* & mask */) != (newState /* & mask */)) {
			if ((curState & bit) != (newState & bit)) {
				curState = curState * curMult + curPlus;
				distance |= bit;
			}

			bit = bit << 1;
			curPlus = (curMult + 1) * curPlus;
			curMult *= curMult;
		}
		// Static instance is 1 step ahead by default
		return distance - 1;
	}

	/*
	 * It doesn't make sense for a static class to return a new copy of itself
	 * 
	 * @Deprecated public SpecificStreamRR split() { throw new
	 * NoSuchAlgorithmException(""); }
	 */

	private static long getRandomSeed(long input) {
		// xorshift64*
		input ^= input >> 12;
		input ^= input << 25; // b
		input ^= input >> 27; // c
		return input *= 0x2545F4914F6CDD1DL;
	}
}
