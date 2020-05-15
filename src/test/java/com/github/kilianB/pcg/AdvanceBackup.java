package com.github.kilianB.pcg;

/**
 * Test the step back and advance functionality for uniformity. Due to unsigned
 * long math first this method wasn't working as expected and the output was
 * compared to the original c++ source whose code can be found at
 * src/test/c++/UnsignedMath.cpp
 * 
 * @author Kilian
 *
 */
public class AdvanceBackup {

	private static long state = 5;
	private static long curStep = 0;

	public static void main(String[] args) {

		System.out.println("Java\n");

		// Single step forwards
		performAdvance(1, 10);

		// Single step backwards
		performAdvance(-1, 10);

		performAdvance(3, 10);
		performAdvance(-3, 10);

	}

	public static void performAdvance(long delta, int iterations) {
		// Reset state
		long steps = delta;
		long curMult = 6364136223846793005L;
		long curPlus = 16;

		for (int i = 0; i < iterations; i++) {
			state = stepRight(state, steps, curMult, curPlus);
			curStep += delta;
			System.out.println(curStep + " " + Long.toUnsignedString(state));
		}

	}

	public static long stepRight(long state, long steps, long mult, long inc) {

		long acc_mult = 1;
		long acc_plus = 0;

		long cur_plus = inc;
		long cur_mult = mult;

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
		return state;
	}
}
