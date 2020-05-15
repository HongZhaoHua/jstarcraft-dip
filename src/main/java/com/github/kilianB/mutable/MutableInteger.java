package com.github.kilianB.mutable;

import java.io.Serializable;

/**
 * Mutable class wrapper for integer values. Mutable classes are useful
 * in lambda expressions or anonymous classes which want to alter the content of
 * a variable but are limited to final or effective final variables.
 * 
 * @author Kilian
 * @since 1.0.0
 */
public class MutableInteger extends Number implements Mutable<Integer>, Comparable<MutableInteger>, Serializable{

	private static final long serialVersionUID = 6846548022746719522L;
	
	private int field;
	
	/**
	 * Create a mutable Integer with an initial value of 0
	 */
	public MutableInteger() {};
	
	/**
	 * Create a mutable Integer.
	 * @param initialValue the initial value of the integer
	 */
	public MutableInteger(int initialValue) {
		this.field = initialValue;
	}
	
	@Override
	public int compareTo(MutableInteger o) {
		return Integer.compare(field,o.field);
	}

	@Override
	public Integer getValue() {
		return Integer.valueOf(field);
	}
	
	

	@Override
	public void setValue(Integer newValue) {
		field = newValue;
	}
	
	/**
	 * Set the internal field to the new value
	 * @param newValue the new value
	 * @since 1.2.0
	 */
	public void setValue(int newValue) {
		field = newValue;
	}
	
	@Override
	public int intValue() {
		return field;
	}

	@Override
	public int hashCode() {
		return field;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MutableInteger other = (MutableInteger) obj;
		if (field != other.field)
			return false;
		return true;
	}

	@Override
	public long longValue() {
		return field;
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
	public Integer getAndIncrement() {
		return Integer.valueOf(field++);
	}
	
	/**
	 * Increment the internal value and return the result.
	 * 
	 * @return the new value after after performing the increment operation.
	 * @since 1.2.0
	 */
	public Integer incrementAndGet() {
		return Integer.valueOf(++field);
	}
	
	/**
	 * Return the internal value and decrement it afterwards.
	 * 
	 * @return the value of the internal field before performing the decrement
	 *         operation.
	 * @since 1.2.0
	 */
	public Integer getAndDecrement() {
		return Integer.valueOf(field--);
	}
	
	/**
	 * Decrement the internal value and return the result.
	 * 
	 * @return the new value after after performing the decrement operation.
	 * @since 1.2.0
	 */
	public Integer decrementAndGet() {
		return Integer.valueOf(--field);
	}
}
