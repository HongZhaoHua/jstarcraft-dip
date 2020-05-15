package com.github.kilianB.pcg.cas;

/**
 * A 64 bit State PcgRNG with 32 bit output. PCG-XSH-RR <p>
 * 
 * The pcg family combines a linear congruential generators with a permutation
 * output function resulting in high quality pseudo random numbers. The original
 * concept was introduced by Melissa O’Neill please refer to <a
 * href="http://www.pcg-random.org/">pcg-random.org</a> for more information.
 * <p>
 * 
 * The RR instance permutates the output using the following function:
 * 
 * <pre>
 * {@code
 * 	int shift = (int) (((state >>> 18) ^ state) >>> 27);
 * 	int rotation = (int) (state >>> 59);
 * 	return Integer.rotateRight(shift, rotation);
 * }
 * </pre>
 * 
 * "[...The] design goal is to be a good all-purpose random number generator.
 * The intent is to balance speed with statistical performance and reasonable
 * security, charting a middle-of-the-road path. (It’s the generator that I
 * recommend for most users.) The strategy is to perform an xorshift to improve
 * the high bits, then randomly rotate them so that all bits are full period.
 * Hence the mnemonic PCG-XSH-RR, “xorshift high (bits), random rotation”."<p>
 * 
 * This implementation is thread safe utilizing CAS instructions similar to the
 * {@link java.util.Random} class. Performance wise CAS never achieves the same
 * throughput as the {@link com.github.kilianB.pcg.sync.PcgRR synchronized} or the
 * {@link com.github.kilianB.pcg.lock.PcgRRLocked locked} version.
 * 
 * @author Kilian
 * @see <a href="http://www.pcg-random.org/">www.pcg-random.org</a>
 * @see com.github.kilianB.pcg.sync.PcgRR PcgRR
 */
public class PcgRRCas extends RandomBaseCAS {

	private static final long serialVersionUID = 1L;

	/**
	 * Create a cas synchronized PcgRR instance seeded with with 2 longs generated
	 * by xorshift*. The values chosen are very likely not used as seeds in any
	 * other non argument constructor of any of the classes provided in this
	 * library.
	 */
	public PcgRRCas() {
		super();
	}

	/**
	 * Create a random number generator with the given seed and stream number. The
	 * seed defines the current state in which the rng is in and corresponds to
	 * seeds usually found in other RNG implementations. RNGs with different seeds
	 * are able to catch up after they exhaust their period and produce the same
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
	public PcgRRCas(long seed, long streamNumber) {
		super(seed, streamNumber);
	}

	/**
	 * Copy constructor. <b>Has</b> to be implemented in all inheriting instances.
	 * This will be invoked through reflection! when calling {@link #split()} or
	 * {@link #splitDistinct()} If no special behavior is desired simply pass though
	 * the values.
	 * 
	 * This constructor should usually not be called manually as the seed and
	 * increment will just be set without performing any randomization.
	 * 
	 * @param seed
	 *            of the lcg. The value will be set and not altered.
	 * @param streamNumber
	 *            used in the lcg as increment constant.
	 * @param dummy
	 *            unused. Resolve signature disambiguate
	 */
	@Deprecated
	public PcgRRCas(long seed, long streamNumber, boolean dummy) {
		super(seed, streamNumber, true);
	}

	@Override
	protected int getInt(long state) {
		// Permuted output function
		int shift = (int) (((state >>> 18) ^ state) >>> 27);
		int rotation = (int) (state >>> 59);
		return Integer.rotateRight(shift, rotation);
	}

	// protected int rotateRightUnsigned(int value, int rot) {
	// return (value >>> rot) | (value << ((- rot) & 31));
	// }

}
