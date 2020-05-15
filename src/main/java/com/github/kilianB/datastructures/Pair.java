package com.github.kilianB.datastructures;

import java.io.Serializable;

/**
 * A convenience data structure encapsulating 2 values. This class can be used
 * to return two values from a method call and create compound keys/values in
 * other collections.
 * 
 * @author Kilian
 *
 * @param <S> type of the first value
 * @param <U> type of the second value
 * @since 1.5.4
 * @since 1.5.6 implements serializable
 */
public class Pair<S, U> implements Serializable {

	private static final long serialVersionUID = 8525518254221383644L;

	private final S first;

	private final U second;

	/**
	 * @param first  element of the pair
	 * @param second element of the pair
	 */
	public Pair(S first, U second) {
		super();
		this.first = first;
		this.second = second;
	}

	/**
	 * Copy constructor.
	 * <p>
	 * Creates a copy of the supplied entry. This is a soft clone referencing the
	 * same instances of the original pair.
	 * 
	 * @param original the original pair to take the entries from.
	 */
	public Pair(Pair<? extends S, ? extends U> original) {
		this.first = original.first;
		this.second = original.second;
	}

	/**
	 * @return the first object stored in this pair
	 */
	public S getFirst() {
		return first;
	}

	/**
	 * @return the second object stored in this pair
	 */
	public U getSecond() {
		return second;
	}

	@Override
	public String toString() {
		return "Pair [first=" + first + ", second=" + second + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Pair)) {
			return false;
		}
		Pair other = (Pair) obj;
		if (first == null) {
			if (other.first != null) {
				return false;
			}
		} else if (!first.equals(other.first)) {
			return false;
		}
		if (second == null) {
			if (other.second != null) {
				return false;
			}
		} else if (!second.equals(other.second)) {
			return false;
		}
		return true;
	}
}