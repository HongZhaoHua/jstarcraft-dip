package com.github.kilianB;

/**
 * @author Kilian
 *
 */
public class MathUtil {

	/**
	 * Get the right shift offset until the first masked bit
	 * 
	 * @param mask bit mask to compute the offset for
	 * @return the number off bits a number needs to be shifted to be caught by the
	 *         mask or -1 if the mask == 0
	 */
	public static int getLowerShiftBitMask(int mask) {
		int offset = 0;
		if (mask != 0) {
			while ((mask & 0x1) == 0) {
				mask >>= 1;
				offset++;
			}
		} else {
			return -1;
		}
		return offset;
	}

	/**
	 * Clamp a number between its lower and upper bound. If x {@literal>} upper
	 * bound return upper bound. If x {@literal<} lower bound return lower bound
	 * 
	 * @param value      the input value
	 * @param lowerBound the lower bound
	 * @param upperBound the upper bound
	 * @param <T>        The type of the input and return value
	 * @return the original value if it's between bounds or the bound
	 * @since 1.0.0
	 */
	public static <T extends Number & Comparable<T>> T clampNumber(T value, T lowerBound, T upperBound) {
		if (value.compareTo(lowerBound) <= 0) {
			return lowerBound;
		} else if (value.compareTo(upperBound) >= 0) {
			return upperBound;
		}
		return value;
	}

	/**
	 * Find the closest value to a number which can be divided by the divisor.
	 * 
	 * <pre>
	 * if  dividend%divisor == 0 return dividend
	 * if  dividend%divisor != 0 return the value closest
	 *     to dividend which fulfills the condition
	 * </pre>
	 * 
	 * If two numbers are exactly the same distance away one of them will returned.
	 * 
	 * @param dividend the dividend
	 * @param divisor  the divisor
	 * @return the nearest number to dividend which can be divided by divisor
	 * @throws java.lang.ArithmeticException if divisor is zero
	 * 
	 * @since 1.0.0
	 */
	public static long findClosestDivisibleInteger(int dividend, int divisor) {
		int quot = dividend / divisor;

		int n1 = divisor * quot;

		int n2 = (dividend * divisor) > 0 ? (divisor * (quot + 1)) : (divisor * (quot - 1));

		if (Math.abs(dividend - n1) < Math.abs(dividend - n2))
			return n1;

		return n2;
	}

	/**
	 * Compare two doubles. Necessary due to non exact floating point arithmetics
	 * 
	 * @param needle  the needle
	 * @param target  the target
	 * @param epsilon the amount the values may differ to still consider a match
	 * @return true if numbers are considered equal, false otherwise
	 * @since 1.0.0
	 */
	public static boolean isDoubleEquals(double needle, double target, double epsilon) {
		// We could use machine precision e.g. Math.ulp(d)
		return Math.abs(needle - target) <= epsilon;
	}

	/**
	 * Return the fractional part of the number
	 * 
	 * @param d a double
	 * @return the fractional part of the number. If the base number is negative the
	 *         returned fraction will also be negative.
	 * @since 1.0.0
	 */
	public static double getFractionalPart(double d) {
		return d - (int) d;
	}

	/**
	 * 
	 * @param gaussian A gaussian with std 1 and mean 0
	 * @param newStd   new standard deviation
	 * @param newMean  new mean
	 * @return a value fitted to the new mean and std
	 * @since 1.0.0
	 */
	public static double fitGaussian(double gaussian, double newStd, double newMean) {
		return gaussian * newStd + newMean;
	}

	/**
	 * Linearly fit/transform a value from a given to a new range.
	 * 
	 * <pre>
	 * {@code
	 * [observedMin <= value <= observedMax] 
	 * 				---> 
	 * [newMin <= transformed <= newMax]
	 * }
	 * </pre>
	 * 
	 * @param value       The value to fit
	 * @param observedMin the minimum value of the current dataset
	 * @param observedMax the maximum value of the current dataset
	 * @param newMin      the lower end of the newly fitted range
	 * @param newMax      the upper end of the newly fitted range
	 * @return the transformed value
	 */
	public static double normalizeValue(double value, double observedMin, double observedMax, double newMin, double newMax) {
		return normalizeValue(value, observedMax - observedMin, observedMax, newMax - newMin, newMax, false);
	}

	/**
	 * Linearly fit/transform a value from a given to a new range.
	 * 
	 * <pre>
	 * {@code
	 * [observedMin <= value <= observedMax] 
	 * 				---> 
	 * [newMin <= transformed <= newMax]
	 * }
	 * </pre>
	 * 
	 * This method uses a the pre computed range instead of the single range bounds
	 * to minimize repetitive calculations in case this method gets called multiple
	 * times. For more convenient arguments take a look at
	 * {@link #normalizeValue(double, double, double, double, double)};
	 * 
	 * @param value         The value to fit
	 * @param observedRange the observedMax - observedMin
	 * @param observedMax   the maximum value of the current dataset
	 * @param newRange      the newMax - newMin
	 * @param newMax        the upper end of the newly fitted range
	 * @param dummy         dummy variables used to prevent ambiguous method
	 *                      signatures
	 * @return the transformed value
	 */
	public static double normalizeValue(double value, double observedRange, double observedMax, double newRange, double newMax, boolean dummy) {
		return newRange / observedRange * (value - observedMax) + newMax;
	}

	/**
	 * Check if the supplied variable represents a numeric value
	 * 
	 * @param var the variable to check
	 * @return true if the variable is a number, false otherwise
	 * @since 1.2.0
	 */
	public static boolean isNumeric(Object var) {
		return var instanceof Number;
	}

	/**
	 * 
	 * Returns the logarithm of an arbitrary base of a double value.
	 * 
	 * Special cases:
	 * <ul>
	 * <li>If the argument is NaN or less than zero, then the result is NaN.
	 * <li>If the argument is positive infinity, then the result is positive
	 * infinity.
	 * <li>If the argument is positive zero or negative zero, then the result is
	 * negative infinity.
	 * <li>If the base is zero or NaN the result is NaN</li>
	 * </ul>
	 * 
	 * @param value the value of the logarithm
	 * @param base  the base of the logarithm
	 * @return the log_base(value)
	 * @since 1.3.2
	 */
	public static double log(double value, double base) {
		if (base == 0) {
			return Double.NaN;
		}
		return StrictMath.log(value) / StrictMath.log(base);
	}

	/**
	 * Calculate the triangular number. The triangular sum is the sum of all
	 * positive natural number up to n. The triangular number (n-1) represents the
	 * number of distinct 2 element sets that can be constructed given n
	 * individuals.
	 * 
	 * <code> 1 + 2 + 3 + 4 + ... + n</code>
	 * 
	 * The method is undefined for n {@literal<} 1
	 * 
	 * @param n the number up to which the numbers are summed up
	 * @return the triangular number
	 * @since 1.4.1
	 * @throws ArithmeticException if n is 0
	 */
	public static int triangularNumber(int n) {
		return (n * (n + 1)) / 2;
	}
}
