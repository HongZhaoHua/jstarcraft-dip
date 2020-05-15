package com.github.kilianB;

import java.util.Collection;

/**
 * @author Kilian
 *
 */
public class Require {

	/**
	 * Checks if the supplied argument is the exact numeric value as the supplied
	 * argument and throws a IllegalArgumentException if it isn't
	 * 
	 * @param valueToCheck to be checked
	 * @param target       the value to check against
	 * @return The supplied value
	 * @param <T> the type of the value
	 * @since 1.5.8
	 */
	public static <T extends Number> T exact(T valueToCheck, T target) {
		return exact(valueToCheck, target, null);
	}

	/**
	 * Checks if the supplied argument is the exact numeric value as the supplied
	 * argument and throws a IllegalArgumentException if it isn't
	 * 
	 * @param valueToCheck to be checked
	 * @param target       the value to check against
	 * @param message      to be thrown in case of error
	 * @return The supplied value
	 * @param <T> the type of the value
	 * @since 1.5.8
	 */
	public static <T extends Number> T exact(T valueToCheck, T target, String message) {

		if (valueToCheck instanceof Double || valueToCheck instanceof Float) {
			if (valueToCheck.doubleValue() == target.doubleValue()) {
				return valueToCheck;
			}
		} else {
			if (valueToCheck.longValue() == target.longValue()) {
				return valueToCheck;
			}
		}

		if (message == null) {
			throw new IllegalArgumentException();
		} else {
			throw new IllegalArgumentException(message);
		}

	}

	/**
	 * Checks if the supplied argument is a positive non null numeric value and
	 * throws a IllegalArgumentException if it isn't
	 * 
	 * @param value to be checked
	 * @return The supplied value
	 * @param <T> the type of the value
	 * @since 1.0.0
	 */
	public static <T extends Number> T positiveValue(T value) {
		return positiveValue(value, null);
	}

	/**
	 * Checks if the supplied argument is a positive non null numeric value and
	 * throws a IllegalArgumentException if it isn't
	 * 
	 * @param value   to be checked
	 * @param message to be thrown in case of error
	 * @return The supplied value
	 * @param <T> the type of the value
	 * @since 1.0.0
	 */
	public static <T extends Number> T positiveValue(T value, String message) {
		if (value.doubleValue() <= 0) {
			if (message == null) {
				throw new IllegalArgumentException();
			} else {
				throw new IllegalArgumentException(message);
			}
		}
		return value;
	}

	/**
	 * Checks if the supplied argument is lays within the given bounds throws a
	 * IllegalArgumentException if it doesn't
	 * 
	 * @param value       to be checked
	 * @param lowerBound  inclusively
	 * @param higherBound inclusively
	 * @param message     to be thrown in case of error
	 * @param <T>         the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T extends Number> T inRange(T value, T lowerBound, T higherBound, String message) {

		if (value instanceof Double && ((Double) value).isNaN() || (lowerBound instanceof Double && ((Double) lowerBound).isNaN()) || (higherBound instanceof Double && ((Double) higherBound).isNaN())) {
			throw new IllegalArgumentException("Input may not be NaN");
		}

		if (value.doubleValue() < lowerBound.doubleValue() || value.doubleValue() > higherBound.doubleValue()) {
			if (message == null) {
				throw new IllegalArgumentException();
			} else {
				throw new IllegalArgumentException(message);
			}
		}
		return value;
	}

	/**
	 * Checks if the supplied argument is lays within the given bounds throws a
	 * IllegalArgumentException if it doesn't
	 * 
	 * @param value       to be checked
	 * @param lowerBound  inclusively
	 * @param higherBound inclusively
	 * @param message     to be thrown in case of error
	 * @param <T>         the type of the value
	 * @return The supplied value
	 * @since 1.1.0
	 */
	public static <T extends Number> Collection<T> inRange(Collection<T> value, T lowerBound, T higherBound, String message) {
		for (T t : value) {
			inRange(t, lowerBound, higherBound, message);
		}
		return value;
	}

	/**
	 * Checks if the supplied argument is lays within the given bounds throws a
	 * IllegalArgumentException if it doesn't
	 * 
	 * @param value       to be checked
	 * @param lowerBound  inclusively
	 * @param higherBound inclusively
	 * @param <T>         the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T extends Number> T inRange(T value, T lowerBound, T higherBound) {
		return inRange(value, lowerBound, higherBound, null);
	}

	/**
	 * Checks if the supplied array does not contain null values and throws a
	 * IllegalArgumentException if it does.
	 * 
	 * @param array to be checked
	 * @param <T>   the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T> T[] nonNull(T[] array) {
		return nonNull(array, null);
	}

	/**
	 * Checks if the supplied array does not contain null values and throws a
	 * IllegalArgumentException if it does.
	 * 
	 * @param array   to be checked
	 * @param message message to be thrown in case of failure
	 * @param <T>     the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T> T[] nonNull(T[] array, String message) {
		if (ArrayUtil.allNotNull(array)) {
			return array;
		} else {
			if (message == null) {
				throw new IllegalArgumentException();
			} else {
				throw new IllegalArgumentException(message);
			}
		}
	}

	/**
	 * Deeply checks if the supplied array does not contain null values and throws a
	 * IllegalArgumentException if it does.
	 * <p>
	 * 
	 * This method supports nested arrays.
	 * 
	 * @param array to be checked
	 * @param <T>   the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T> T[] deepNonNull(T[] array) {
		return deepNonNull(array, null);
	}

	/**
	 * Deeply checks if the supplied array does not contain null values and throws a
	 * IllegalArgumentException if it does.
	 * <p>
	 * 
	 * This method supports nested arrays.
	 * 
	 * @param array   to be checked
	 * @param message message to be thrown in case of failure
	 * @param <T>     the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T> T[] deepNonNull(T[] array, String message) {
		if (ArrayUtil.deepAllNotNull(array)) {
			return array;
		} else {
			if (message == null) {
				throw new IllegalArgumentException();
			} else {
				throw new IllegalArgumentException(message);
			}
		}
	}

	/**
	 * Checks if the supplied array does only contain null values and throws a
	 * IllegalArgumentException if it does not..
	 * 
	 * @param array to be checked
	 * @param <T>   the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T> T[] allNull(T[] array) {
		return allNull(array, null);
	}

	/**
	 * Checks if the supplied array does only contain null values and throws a
	 * IllegalArgumentException if it does not..
	 * 
	 * @param array   to be checked
	 * @param message message to be thrown in case of failure
	 * @param <T>     the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T> T[] allNull(T[] array, String message) {
		if (ArrayUtil.allNull(array)) {
			return array;
		} else {
			if (message == null) {
				throw new IllegalArgumentException();
			} else {
				throw new IllegalArgumentException(message);
			}
		}
	}

	/**
	 * Checks if the supplied Collection does not contain null values and throws a
	 * IllegalArgumentException if it does.
	 * 
	 * @param list to be checked
	 * @param <T>  the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T> Collection<T> nonNull(Collection<T> list) {
		nonNull(list.toArray(), null);
		return list;
	}

	/**
	 * Checks if the supplied Collection does not contain null values and throws a
	 * IllegalArgumentException if it does.
	 * 
	 * @param list    to be checked
	 * @param message message to be thrown in case of failure
	 * @param <T>     the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T> Collection<T> nonNull(Collection<T> list, String message) {
		nonNull(list.toArray(), message);
		return list;
	}

	/**
	 * Checks if the supplied argument is an odd non null numeric value and throws a
	 * IllegalArgumentException if it isn't
	 * 
	 * @param value to be checked
	 * @return The supplied value
	 * @param <T> the type of the value
	 * @since 1.5.2
	 */
	public static <T extends Number> T oddValue(T value) {
		return oddValue(value, null);
	}

	/**
	 * Checks if the supplied argument is an odd non null numeric value and throws a
	 * IllegalArgumentException if it isn't
	 * 
	 * @param value   to be checked
	 * @param message to be thrown in case of error
	 * @return The supplied value
	 * @param <T> the type of the value
	 * @since 1.5.2
	 */
	public static <T extends Number> T oddValue(T value, String message) {

		if (value instanceof Double && ((Double) value).isNaN()) {
			throw new IllegalArgumentException("NaN is neither even or odd");
		}

		if (value.longValue() % 2 == 0) {
			if (message == null) {
				throw new IllegalArgumentException();
			} else {
				throw new IllegalArgumentException(message);
			}
		}
		return value;
	}

	/**
	 * Checks if the supplied argument is an even non null numeric value and throws
	 * a IllegalArgumentException if it isn't
	 * 
	 * @param value to be checked
	 * @return The supplied value
	 * @param <T> the type of the value
	 * @since 1.5.2
	 */
	public static <T extends Number> T evenValue(T value) {
		return evenValue(value, null);
	}

	/**
	 * Checks if the supplied argument is an even non null numeric value and throws
	 * a IllegalArgumentException if it isn't
	 * 
	 * @param value   to be checked
	 * @param message to be thrown in case of error
	 * @return The supplied value
	 * @param <T> the type of the value
	 * @since 1.5.2
	 */
	public static <T extends Number> T evenValue(T value, String message) {

		if (value instanceof Double && ((Double) value).isNaN()) {
			throw new IllegalArgumentException("NaN is neither even or odd");
		}

		if (value.longValue() % 2 != 0) {
			if (message == null) {
				throw new IllegalArgumentException();
			} else {
				throw new IllegalArgumentException(message);
			}
		}
		return value;
	}

	/**
	 * Checks if the supplied argument is a negative non null numeric value and
	 * throws a IllegalArgumentException if it isn't
	 * 
	 * @param value to be checked
	 * @return The supplied value
	 * @param <T> the type of the value
	 * @since 1.5.2
	 */
	public static <T extends Number> T negativeValue(T value) {
		return negativeValue(value, null);
	}

	/**
	 * Checks if the supplied argument is a negative non null numeric value and
	 * throws a IllegalArgumentException if it isn't
	 * 
	 * @param value   to be checked
	 * @param message to be thrown in case of error
	 * @return The supplied value
	 * @param <T> the type of the value
	 * @since 1.5.2
	 */
	public static <T extends Number> T negativeValue(T value, String message) {
		if (value.doubleValue() >= 0) {
			if (message == null) {
				throw new IllegalArgumentException();
			} else {
				throw new IllegalArgumentException(message);
			}
		}
		return value;
	}

	/**
	 * Checks if the supplied argument is strictly less than the upper bound value
	 * and throws a IllegalArgumentException if it isn't
	 * 
	 * @param value      to be checked
	 * @param upperBound the higher bound to check against
	 * @return The supplied value
	 * @param <T> the type of the value
	 * @since 1.5.2
	 */
	public static <T extends Number> T lessThan(T value, T upperBound) {
		return lessThan(value, upperBound, null);
	}

	/**
	 * Checks if the supplied argument is strictly less than the upper bound value
	 * and throws a IllegalArgumentException if it isn't
	 * 
	 * @param value      to be checked
	 * @param upperBound the higher bound to check against
	 * @param message    to be thrown in case of error
	 * @return The supplied value
	 * @param <T> the type of the value
	 * @since 1.5.2
	 */
	public static <T extends Number> T lessThan(T value, T upperBound, String message) {
		if (value.doubleValue() >= upperBound.doubleValue()) {
			if (message == null) {
				throw new IllegalArgumentException();
			} else {
				throw new IllegalArgumentException(message);
			}
		}
		return value;
	}

	/**
	 * Checks if the supplied argument is strictly greater than the lower bound
	 * value and throws a IllegalArgumentException if it isn't
	 * 
	 * @param value      to be checked
	 * @param lowerBound the lower bound to check against
	 * @return The supplied value
	 * @param <T> the type of the value
	 * @since 1.5.2
	 */
	public static <T extends Number> T greaterThan(T value, T lowerBound) {
		return greaterThan(value, lowerBound, null);
	}

	/**
	 * Checks if the supplied argument is strictly greater than the lower bound
	 * value and throws a IllegalArgumentException if it isn't
	 * 
	 * @param value      to be checked
	 * @param lowerBound the lower bound to check against
	 * @param message    to be thrown in case of error
	 * @return The supplied value
	 * @param <T> the type of the value
	 * @since 1.5.2
	 */
	public static <T extends Number> T greaterThan(T value, T lowerBound, String message) {
		if (value.doubleValue() <= lowerBound.doubleValue()) {
			if (message == null) {
				throw new IllegalArgumentException();
			} else {
				throw new IllegalArgumentException(message);
			}
		}
		return value;
	}
}
