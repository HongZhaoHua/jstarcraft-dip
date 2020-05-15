package com.github.kilianB.datastructures;

import java.io.Serializable;

/**
 * A convenience data structure encapsulating 3 values. This class can be used
 * to return multiple values from a method call and create compound keys/values
 * in other collections.
 * 
 * @author Kilian
 *
 * @param <S> type of the first value
 * @param <U> type of the second value
 * @param <V> type of the third value
 * @since 1.5.6
 */
public class Triple<S, U, V> implements Serializable {

	private static final long serialVersionUID = -6739318752459774954L;

	private final S first;

	private final U second;

	private final V third;

	/**
	 * @param first  element of the pair
	 * @param second element of the pair
	 * @param third  tlement of the pair
	 */
	public Triple(S first, U second, V third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	/**
	 * Copy constructor.
	 * <p>
	 * Creates a copy of the supplied entry. This is a soft clone referencing the
	 * same instances of the original pair.
	 * 
	 * @param original the original pair to take the entries from.
	 */
	public Triple(Triple<? extends S, ? extends U, ? extends V> original) {
		this.first = original.first;
		this.second = original.second;
		this.third = original.third;
	}

	/**
	 * @return the first object stored in this triplet
	 */
	public S getFirst() {
		return first;
	}

	/**
	 * @return the second object stored in this triplet
	 */
	public U getSecond() {
		return second;
	}

	/**
	 * @return the second object stored in this triplet
	 */
	public V getThird() {
		return third;
	}

	@Override
	public String toString() {
		return "Triple [first=" + first + ", second=" + second + ", third=" + third + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		result = prime * result + ((third == null) ? 0 : third.hashCode());
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
		if (!(obj instanceof Triple)) {
			return false;
		}
		Triple other = (Triple) obj;
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
		if (third == null) {
			if (other.third != null) {
				return false;
			}
		} else if (!third.equals(other.third)) {
			return false;
		}
		return true;
	}

}
