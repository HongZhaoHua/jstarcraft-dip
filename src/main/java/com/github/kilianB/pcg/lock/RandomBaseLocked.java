package com.github.kilianB.pcg.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import com.github.kilianB.pcg.RandomBase64;

/**
 * Extension of the 64 bit state 32 bit output PCG Base. This class synchronizes
 * critical components to ensure thread safety using locks. Lock based
 * implementations shine in high congestion settings but usually have a huge
 * penalty associated with acquiring and releasing the locks in uncontested
 * settings.
 * 
 * See {@link RandomBase64 } for more information regarding the internal working
 * of the pcg family
 * 
 * @author Kilian
 * 
 * @see <a href="http://www.pcg-random.org/">www.pcg-random.org</a>
 */
public abstract class RandomBaseLocked extends RandomBase64 {

	private static final long serialVersionUID = 6005012112047562156L;

	private ReentrantReadWriteLock lock;
	private ReadLock readLock;
	private WriteLock writeLock;

	/** 64 bit internal state */
	protected long state;

	/** Stream number of the rng. */
	protected long inc;

	public RandomBaseLocked() {
		super();
		initLocks();
	}

	/**
	 * Create a random number generator with the given seed and stream number. The
	 * seed defines the current state in which the rng is in and corresponds to
	 * seeds usually found in other RNG implementations. RNGs with different seeds
	 * are able to catch up after they exhaust their period and produce the same
	 * numbers.
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
	public RandomBaseLocked(long seed, long streamNumber) {
		super(seed, streamNumber);
		initLocks();
	}

	@Deprecated
	protected RandomBaseLocked(long initialState, long increment, boolean dummy) {
		super(initialState, increment, dummy);
		initLocks();
	}

	@Override
	protected long stepRight() {
		writeLock.lock();
		long oldState = this.state;
		state = state * MULT_64 + inc;
		writeLock.unlock();
		return oldState;
	}

	@Override
	public void advance(long steps) {

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
		writeLock.lock();
		this.state = (acc_mult * state) + acc_plus;
		writeLock.unlock();
	}

	@Override
	protected void setInc(long increment) {
		if (increment == 0 || increment % 2 == 0) {
			throw new IllegalArgumentException("Increment may not be 0 or even. Value: " + increment);
		}
		writeLock.lock();
		this.inc = increment;
		writeLock.unlock();
	}

	@Override
	protected void setState(long initialState) {

		/*
		 * The super class constructor will call setState as the first function in it's
		 * own constructor before we had time to initialize our low. Lets check if
		 * already exists. We only use set state sparingly, during constructor and copy
		 * calls so it does not alter much performance wise.
		 */
		initLocks();

		writeLock.lock();
		this.state = initialState;
		writeLock.unlock();
	}

	@Override
	public boolean isFast() {
		return false;
	}

	@Override
	public long getState() {
		// Does locking really achieve anything here?
		readLock.lock();
		long state = this.state;
		readLock.unlock();
		return state;
	}

	@Override
	public long getInc() {
		// Does locking really achieve anything here?
		readLock.lock();
		long inc = this.inc;
		readLock.unlock();
		return inc;
	}

	private void initLocks() {
		/*
		 * The super class constructor will call setState as the first function in it's
		 * own constructor before we had time to initialize our low. Lets check if
		 * already exists. We only use set state sparingly, during constructor and copy
		 * calls so it does not alter much performance wise.
		 */
		if (lock == null) {
			lock = new ReentrantReadWriteLock();
			readLock = lock.readLock();
			writeLock = lock.writeLock();
		}

	}

}
