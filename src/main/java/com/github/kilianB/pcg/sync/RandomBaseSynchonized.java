package com.github.kilianB.pcg.sync;

import com.github.kilianB.pcg.Pcg;
import com.github.kilianB.pcg.RandomBase64;

/**
 * Extension of the 64 bit state 32 bit output PCG Base. This class synchronizes
 * critical components to ensure thread safety using synchronized monitors. This
 * implementations shines in single threaded and normal congestion situation due
 * to good support by the compiler and should generally be the preferred class
 * to extend from when implementing new rngs. <p>
 * 
 * See {@link RandomBase64 } for more information regarding the internal working
 * of the pcg family
 * 
 * @author Kilian
 *
 * @see <a href="http://www.pcg-random.org/">www.pcg-random.org</a>
 */
public abstract class RandomBaseSynchonized extends RandomBase64 {

	private static final long serialVersionUID = -7066211507986864885L;

	/** 64 bit internal state */
	protected long state;

	/** Stream number of the rng. */
	protected long inc;

	public RandomBaseSynchonized() {
		super();
	}

	/**
	 * Create a random number generator with the given seed and stream number. The
	 * seed defines the current state in which the rng is in and corresponds to
	 * seeds usually found in other RNG implementations. RNGs with different seeds are
	 * able to catch up after they exhaust their period and produce the same
	 * numbers. <p>
	 * 
	 * Different stream numbers alter the increment of the rng and ensure distinct
	 * state sequences <p>
	 * 
	 * Only generators with the same seed AND stream numbers will produce identical
	 * values <p>
	 * 
	 * @param seed
	 *            used to compute the starting state of the RNG
	 * @param streamNumber
	 *            used to compute the increment for the lcg.
	 */
	public RandomBaseSynchonized(long seed, long streamNumber) {
		super(seed, streamNumber);
	}

	@Deprecated
	protected RandomBaseSynchonized(long initialState, long increment, boolean dummy) {
		super(initialState, increment, dummy);
	}

	@Override
	protected synchronized long stepRight() {
		long oldState = this.state;
		state = state * MULT_64 + inc;
		return oldState;
	}

	@Override
	public synchronized void advance(long steps) {

		long acc_mult = 1;
		long acc_plus = 0;

		long cur_plus = inc;
		long cur_mult = MULT_64;

		while (Long.compareUnsigned(steps, 0) > 0) {
			if ((steps & 1) == 1) { 	// Last significant bit is 1
				acc_mult *= cur_mult;
				acc_plus = acc_plus * cur_mult + cur_plus;
			}
			cur_plus *= (cur_mult + 1);
			cur_mult *= cur_mult;
			steps = Long.divideUnsigned(steps, 2);
		}
		this.state = (acc_mult * state) + acc_plus;
	}

	@Override
	public synchronized <T> T split() throws ReflectiveOperationException {
		return super.split();
	}

	@Override
	public synchronized <T> T splitDistinct() throws ReflectiveOperationException {
		return super.splitDistinct();
	}

	@Override
	public synchronized long distanceUnsafe(Pcg other) {
		return super.distanceUnsafe(other);
	}

	@Override
	public synchronized long getState() {
		return state;
	}

	@Override
	public synchronized long getInc() {
		return inc;
	}

	@Override
	protected synchronized void setInc(long increment) {
		this.inc = increment;
	}

	@Override
	protected synchronized void setState(long initialState) {
		this.state = initialState;
	}

	@Override
	public boolean isFast() {
		return false;
	}
}
