package com.github.kilianB.mutable;

import java.io.Serializable;

/**
 * Mutable class wrapper for long values. Mutable classes are useful in lambda
 * expressions or anonymous classes which want to alter the content of a
 * variable but are limited to final or effective final variables.
 * 
 * @author Kilian
 * @since 1.0.0
 */
public class MutableLong extends Number implements Mutable<Long>, Comparable<MutableLong>, Serializable {

	private static final long serialVersionUID = 6846548022746719522L;

	private long field;

	/**
	 * Create a mutable Long with an initial value of 0L
	 */
	public MutableLong() {
	};

	/**
	 * Create a mutable Long.
	 * 
	 * @param initialValue the initial value of the long
	 */
	public MutableLong(long initialValue) {
		this.field = initialValue;
	}

	@Override
	public int compareTo(MutableLong o) {
		return Long.compare(field, o.field);
	}

	@Override
	public Long getValue() {
		return Long.valueOf(field);
	}

	@Override
	public void setValue(Long newValue) {
		field = newValue;
	}

	/**
	 * Set the internal field to the new value
	 * 
	 * @param newValue the new value
	 * @since 1.2.0
	 */
	public void setValue(long newValue) {
		field = newValue;
	}

	@Override
	public long longValue() {
		return field;
	}

	@Override
	public int hashCode() {
		return (int) (field ^ (field >>> 32));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MutableLong other = (MutableLong) obj;
		if (field != other.field)
			return false;
		return true;
	}

	@Override
	public int intValue() {
		return (int) field;
	}

	@Override
	public float floatValue() {
		return field;
	}

	@Override
	public double doubleValue() {
		return field;
	}

	/**
	 * Return the internal value and increment it afterwards.
	 * 
	 * @return the value of the internal field before performing the increment
	 *         operation.
	 * @since 1.2.0
	 */
	public Long getAndIncrement() {
		return Long.valueOf(field++);
	}

	/**
	 * Increment the internal value and return the result.
	 * 
	 * @return the new value after after performing the increment operation.
	 * @since 1.2.0
	 */
	public Long incrementAndGet() {
		return Long.valueOf(++field);
	}

	/**
	 * Return the internal value and decrement it afterwards.
	 * 
	 * @return the value of the internal field before performing the decrement
	 *         operation.
	 * @since 1.2.0
	 */
	public Long getAndDecrement() {
		return Long.valueOf(field--);
	}

	/**
	 * Decrement the internal value and return the result.
	 * 
	 * @return the new value after after performing the decrement operation.
	 * @since 1.2.0
	 */
	public Long decrementAndGet() {
		return Long.valueOf(--field);
	}

}
