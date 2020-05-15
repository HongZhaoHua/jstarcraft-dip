package com.github.kilianB.pcg;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Base interface for all PCG random generators.
 * 
 * The PCG family uses a linear congruential generator as the state-transition
 * function—the “CG” of PCG stands for “congruential generator”. Linear
 * congruential generators are known to be statistically weak. <p>
 * 
 * PCG uses a new technique called permutation functions on tuples to produce
 * output that is much more random than the RNG's internal state. The output
 * function is defined by the extending classes. <p>
 * 
 * A paper highlighting the individual properties can be found here. <a
 * href="http://www.pcg-random.org/paper.html">http://www.pcg-random.org/paper.html</a>.
 * This class is an adaption to the original c <a
 * href="https://github.com/imneme/pcg-c">source code</a> provided by M.E.
 * O'Neill.
 * 
 * @author Kilian
 * @see <a href="http://www.pcg-random.org/">www.pcg-random.org</a>
 */
public interface Pcg {

	/** Ensure that a unique seed is used for randomly seeded instances */
	static final AtomicLong UNIQUE_SEED = new AtomicLong(System.nanoTime());

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
	 * loop and consume more than one step to generate a number. <p>
	 * 
	 * To advance n steps the function performs <code>Math.ceil( log2(n) )</code>
	 * iterations. So you may go ahead and skip as many steps as you like without
	 * any performance implications. <p>
	 * 
	 * Negative indices can be used to jump backwards in time going the long way
	 * around
	 * 
	 * 
	 * @param steps
	 *            the amount of steps to advance or in case of a negative number go
	 *            back in history
	 * 
	 */
	void advance(long steps);

	/**
	 * Splits the generator in a copy with the exact same state and stream number.
	 * The produced generators don't share any state variables enabling to generate
	 * random numbers without impacting the other generator. While the states are
	 * independent they are exact copies resulting in the generated numbers to
	 * follow the same sequence.<p>
	 * 
	 * On the other hand {@link #splitDistinct()} will return a generator who has a
	 * different state and stream number ensuring that the generated sequence is NOT
	 * the same as this generator.
	 * 
	 * @param <T>
	 *            Class of the constructed generator which is equals the class this
	 *            method was invoked on.
	 * 
	 * @return an identical generator with no shared references
	 * @throws ReflectiveOperationException
	 *             if the extending class does not implement the required
	 *             constructor
	 */
	<T> T split() throws ReflectiveOperationException;

	/**
	 * Splits the generator in a copy with distinct state and stream number. The
	 * produced generators don't share any state variables enabling to generate
	 * random numbers without impacting the other generator. The generators are
	 * guaranteed to produce different sequences of numbers and can't be used
	 * independently of each other.<p>
	 * 
	 * On the other hand {@link #split()} will return a generator who has an
	 * identical state and stream number ensuring that the generated sequence is the
	 * same as this generator.
	 * 
	 * @param <T>
	 *            Class of the constructed generator which is equals the class this
	 *            method was invoked on.
	 * 
	 * @return a distinct generator with no shared references
	 * @throws ReflectiveOperationException
	 *             if the extending class does not implement the required
	 *             constructor
	 */
	<T> T splitDistinct() throws ReflectiveOperationException;

	/**
	 * Returns an integer with the next <i>n</i> low bits randomly set and are used
	 * as a base to deviate smaller data types. The used bits are the high bits used
	 * from the underlying integer. An n of more 31 bits will result in no bits
	 * being set, thus returning 0.
	 * 
	 * @param n
	 *            the number of randomly set bits. Must be positive and does not
	 *            produce reasonable results for {@literal>} 31
	 * @return an integer
	 */
	int next(int n);

	/**
	 * Returns the next pseudorandom, uniformly distributed {@code boolean} value
	 * from this random number generator's sequence. The general contract of
	 * {@code nextBoolean} is that one {@code boolean} value is pseudorandomly
	 * generated and returned. All possible {@code boolean} values are produced with
	 * (approximately) equal probability.
	 *
	 * @return the next pseudorandom, uniformly distributed {@code boolean} value
	 *         from this random number generator's sequence
	 */
	boolean nextBoolean();

	/**
	 * 
	 * Returns the next pseudorandom {@code boolean} value from this random number
	 * generator's sequence with the given probability of being true.<p>
	 * 
	 * A probability value of 0 will always return {@code false} and a value of 1
	 * will always return {@code true}.
	 * 
	 * 
	 * @param probability
	 *            the probability of the returned boolean to be true in range of
	 *            [0-1]
	 * @return the next pseudorandom boolean with given probability to tbe true
	 * @throws IllegalArgumentException
	 *             if probability is {@literal>} 1 or {@literal<} 0
	 */
	boolean nextBoolean(double probability);

	/**
	 * Generates random bytes and places them into a user-supplied byte array. The
	 * number of random bytes produced is equal to the length of the byte array.
	 *
	 * @param bytes
	 *            the byte array to fill with random bytes
	 * @throws NullPointerException
	 *             if the byte array is null
	 * @see #nextByte()
	 */
	void nextBytes(byte[] bytes);

	/**
	 * Returns the next pseudorandom, uniformly distributed {@code char} value from
	 * this random number generator's sequence. The general contract of
	 * {@code nextChar} is that one {@code char} value is pseudorandomly generated
	 * and returned. All possible {@code char} values are produced with
	 * (approximately) equal probability.
	 *
	 * @return the next pseudorandom, uniformly distributed {@code char} value from
	 *         this random number generator's sequence
	 */
	char nextChar();

	/**
	 * Returns the next pseudorandom, uniformly distributed {@code short} value from
	 * this random number generator's sequence. The general contract of
	 * {@code nextShort} is that one {@code short} value is pseudorandomly generated
	 * and returned. All possible {@code short} values are produced with
	 * (approximately) equal probability.
	 *
	 * @return the next pseudorandom, uniformly distributed {@code short} value from
	 *         this random number generator's sequence
	 */
	short nextShort();

	/**
	 * Returns the next pseudorandom, uniformly distributed {@code byte} value from
	 * this random number generator's sequence. The general contract of
	 * {@code nextByte} is that one {@code byte} value is pseudorandomly generated
	 * and returned. All possible {@code byte} values are produced with
	 * (approximately) equal probability.
	 *
	 * @return the next pseudorandom, uniformly distributed {@code byte} value from
	 *         this random number generator's sequence
	 */
	byte nextByte();

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
	int nextInt();

	/**
	 * Returns a pseudorandom, uniformly distributed {@code int} value between 0
	 * (inclusive) and the specified value (exclusive), drawn from this random
	 * number generator's sequence. The general contract of {@code nextInt} is that
	 * one {@code int} value in the specified range is pseudorandomly generated and
	 * returned. All {@code bound} possible {@code int} values are produced with
	 * (approximately) equal probability.
	 *
	 * @param n
	 *            the upper bound (exclusive). Must be positive.
	 * @return the next pseudorandom, uniformly distributed {@code int} value
	 *         between zero (inclusive) and {@code bound} (exclusive) from this
	 *         random number generator's sequence
	 * @throws IllegalArgumentException
	 *             if bound is not positive
	 * @see #nextInt()
	 */
	int nextInt(int n);

	/**
	 * Returns the next pseudorandom, uniformly distributed {@code long} value from
	 * this random number generator's sequence. The general contract of
	 * {@code nextLong} is that one {@code long} value is pseudorandomly generated
	 * and returned.
	 *
	 * @return the next pseudorandom, uniformly distributed {@code long} value from
	 *         this random number generator's sequence
	 */
	long nextLong();

	/**
	 * Returns the next pseudorandom, uniformly distributed {@code long} value from
	 * this random number generator's sequence. The general contract of
	 * {@code nextLong} is that one {@code long} value is pseudorandomly generated
	 * and returned.
	 *
	 * @param n
	 *            the upper bound (exclusive). Must be positive.
	 *
	 * @return the next pseudorandom, uniformly distributed {@code long} value from
	 *         this random number generator's sequence
	 */
	long nextLong(long n);

	/**
	 * Returns the next pseudorandom, uniformly distributed {@code float} value
	 * between {@code 0.0} and {@code 1.0} from this random number generator's
	 * sequence.
	 *
	 * <p>The general contract of {@code nextFloat} is that one {@code float} value,
	 * chosen (approximately) uniformly from the range {@code 0.0f} (inclusive) to
	 * {@code 1.0f} (exclusive), is pseudorandomly generated and returned. All
	 * 2<sup>24</sup> possible {@code float} values of the form
	 * <i>m&nbsp;x&nbsp;</i>2<sup>-24</sup>, where <i>m</i> is a positive integer
	 * less than 2<sup>24</sup>, are produced with (approximately) equal
	 * probability.
	 *
	 * @return the next pseudorandom, uniformly distributed {@code float} value
	 *         between {@code 0.0} and {@code 1.0} from this random number
	 *         generator's sequence
	 */
	float nextFloat();

	/**
	 * Returns the next pseudorandom, uniformly distributed {@code float} valuet in
	 * the range from 0.0f to 1.0f, possibly inclusive of 0.0f and 1.0f themselves.
	 * Thus:
	 * 
	 * <table style="border:none;"> <tr><th>Expression</th><th>Interval</th></tr>
	 * <tr><td>nextFloat(false, false)</td><td>(0.0f, 1.0f)</td></tr>
	 * <tr><td>nextFloat(true, false)</td><td>[0.0f, 1.0f)</td></tr>
	 * <tr><td>nextFloat(false, true)</td><td>(0.0f, 1.0f]</td></tr>
	 * <tr><td>nextFloat(true, true)</td><td>[0.0f, 1.0f]</td></tr> <caption>Table
	 * of intervals</caption> </table>
	 * 
	 * <p>This version preserves all possible random values in the float range.
	 * 
	 * @param includeZero
	 *            if true may return 0f
	 * @param includeOne
	 *            if true may return 1f
	 * @return the next pseudorandom, uniformly distributed {@code float} value from
	 *         this random number generator's sequence
	 * @see #nextFloat()
	 */
	float nextFloat(boolean includeZero, boolean includeOne);

	/**
	 * Returns the next pseudorandom, uniformly distributed {@code double} value
	 * between {@code 0.0} and {@code 1.0} from this random number generator's
	 * sequence.
	 *
	 * <p>The general contract of {@code nextDouble} is that one {@code double}
	 * value, chosen (approximately) uniformly from the range {@code 0.0d}
	 * (inclusive) to {@code 1.0d} (exclusive), is pseudorandomly generated and
	 * returned.
	 *
	 * @return the next pseudorandom, uniformly distributed {@code double} value
	 *         between {@code 0.0} and {@code 1.0} from this random number
	 *         generator's sequence
	 */
	double nextDouble();

	/**
	 * Returns the next pseudorandom, uniformly distributed {@code double} value in
	 * the range from 0.0 to 1.0, possibly inclusive of 0.0 and 1.0 themselves.
	 * Thus:
	 * 
	 * <table style="border:none;"> <tr><th>Expression</th><th>Interval</th></tr>
	 * <tr><td>nextDouble(false, false)</td><td>(0.0, 1.0)</td></tr>
	 * <tr><td>nextDouble(true, false)</td><td>[0.0, 1.0)</td></tr>
	 * <tr><td>nextDouble(false, true)</td><td>(0.0, 1.0]</td></tr>
	 * <tr><td>nextDouble(true, true)</td><td>[0.0, 1.0]</td></tr> <caption>Table of
	 * intervals</caption> </table>
	 * 
	 * <p>This version preserves all possible random values in the double range.
	 * 
	 * @param includeZero
	 *            if true may return 0d
	 * 
	 * @param includeOne
	 *            if true may return 1d
	 * 
	 * @return the next pseudorandom, uniformly distributed {@code double} value
	 *         from this random number generator's sequence
	 * @see #nextDouble()
	 */
	double nextDouble(boolean includeZero, boolean includeOne);

	double nextGaussian();

	/**
	 * Return the distance between the two generators. The distance is the number of
	 * steps this generator is ahead or behind the other generator. After advancing
	 * or rewinding the amount of steps returned by this function both instances
	 * have caught up and will return the same value for primitive nextX() function
	 * calls. <p>
	 * 
	 * Be aware that this guarantee only holds true for single threaded instances.
	 * An exception will be thrown if the comparing objects are not from the same
	 * class. If you regardlessly want to compare two generators for which
	 * <code>getClass() != other.getClass()</code> is true but may still reach the
	 * same state (e.g. a non fast and fast or locked and cas) implementation have a
	 * look at {@link #distanceUnsafe(Pcg)} which does not check for class safety.
	 * 
	 * 
	 * @param other
	 *            the generator to compare this instance to
	 * @return the distance between the two generators
	 * @throws IncompatibleGeneratorException
	 *             if the other generator isn't of the same class as this generator
	 *             or the increment of the generators is distinct resulting the
	 *             generators to never be able to reach the same state.
	 * @see #distanceUnsafe(Pcg)
	 */
	default long distance(Pcg other) {
		if (!other.getClass().equals(getClass())) {
			throw new IncompatibleGeneratorException(
					"Generator most likely should belong to the same class to be comparable.");
		}
		return distanceUnsafe(other);
	}

	/**
	 * Return the distance between the two generators. The distance is the number of
	 * steps this generator is ahead or behind the other generator. After advancing
	 * or rewinding the amount of steps returned by this function both instances
	 * have caught up and will return the same value for primitive nextX() function
	 * calls. <p>
	 * 
	 * Be aware that this guarantee only holds true for single threaded instances.
	 * This method does <b>Not</b> check if the 2 instances of the generators are of
	 * the same class, employ the same algorithm. This may result in undetermined
	 * results being returned if the algorithm terminates at all.
	 * 
	 * For a save version take a look at {@link #distance(Pcg)}
	 * 
	 * @param other
	 *            the generator to compare this instance to
	 * @return the distance between the two generators
	 * @throws IncompatibleGeneratorException
	 *             if the increment of the generators is distinct resulting the
	 *             generators to never be able to reach the same state.
	 * @see #distance(Pcg)
	 */
	default long distanceUnsafe(Pcg other) {
		// Check if they are the same stream...
		if (this.getInc() != other.getInc()) {
			throw new IncompatibleGeneratorException(
					"Generators with distinct incremeants are not able" + " to reach the same state");
		}

		long curState = getState();
		long newState = other.getState();

		long curPlus = getInc();
		long curMult = getMult();

		long bit = 1;	// Fix bit was overflowing as an int was used!
		long distance = 0;

		// TODO why should we mask here? This does exactly nothing!
		// long mask = ~0;

		while ((curState /*& mask*/) != (newState /*& mask*/)) {
			if ((curState & bit) != (newState & bit)) {
				curState = curState * curMult + curPlus;
				distance |= bit;
			}

			bit = bit << 1;
			curPlus = (curMult + 1) * curPlus;
			curMult *= curMult;
		}

		if (isFast() != other.isFast()) {
			return distance - 1;
		} else {
			return distance;
		}
	}

	/**
	 * Return true if this rng is a fast instance. This check is mostly used int he
	 * distance calculation due to the fact that the state of fast RNGs is shifted
	 * by one. They first calculate a new value and directly use it instead of using
	 * the old state and calculating a new one
	 * 
	 * @return true if the subclass uses the newly generated state directly
	 */
	boolean isFast();

	/**
	 * Returns the internal state of the congruential generator used by this pcg
	 * 
	 * @return the internal state
	 */
	long getState();

	/**
	 * Returns the internal increment of the congurential generator used by this pcg
	 * 
	 * @return the increment
	 */
	long getInc();

	/**
	 * Returns the internal multiplication of the congurential generator used by
	 * this pcg
	 * 
	 * @return the multiplication factor
	 */
	long getMult();

}