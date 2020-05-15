package com.github.kilianB.mutable;

import java.io.Serializable;

/**
 * Mutable class wrapper for byte values. Mutable classes are useful in lambda
 * expressions or anonymous classes which want to alter the content of a
 * variable but are limited to final or effective final variables.
 * 
 * @author Kilian
 * @since 1.0.0
 */
public class MutableByte extends Number implements Mutable<Byte>, Comparable<MutableByte>, Serializable {

	private static final long serialVersionUID = -1973847474259823325L;

	private byte field;

	/**
	 * Create a mutable Boolean with an initial value of 0
	 */
	public MutableByte() {
	};

	/**
	 * Create a mutable Boolean
	 * 
	 * @param initialValue the intial value of the byte
	 */
	public MutableByte(byte initialValue) {
		this.field = initialValue;
	}

	@Override
	public int compareTo(MutableByte o) {
		return Byte.compare(field, o.field);
	}

	@Override
	public Byte getValue() {
		return Byte.valueOf(field);
	}

	@Override
	public void setValue(Byte newValue) {
		this.field = newValue;
	}

	/**
	 * Set the internal field to the new value
	 * 
	 * @param newValue the new value
	 * @since 1.2.0
	 */
	public void setValue(byte newValue) {
		this.field = newValue;
	}

	/**
	 * @return the current value as byte primitive
	 */
	public byte byteValue() {
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
		MutableByte other = (MutableByte) obj;
		if (field != other.field)
			return false;
		return true;
	}

	@Override
	public int intValue() {
		return field;
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
	public Byte getAndIncrement() {
		return Byte.valueOf(field++);
	}

	/**
	 * Increment the internal value and return the result.
	 * 
	 * @return the new value after after performing the increment operation.
	 * @since 1.2.0
	 */
	public Byte incrementAndGet() {
		return Byte.valueOf(++field);
	}

	/**
	 * Return the internal value and decrement it afterwards.
	 * 
	 * @return the value of the internal field before performing the decrement
	 *         operation.
	 * @since 1.2.0
	 */
	public Byte getAndDecrement() {
		return Byte.valueOf(field--);
	}

	/**
	 * Decrement the internal value and return the result.
	 * 
	 * @return the new value after after performing the decrement operation.
	 * @since 1.2.0
	 */
	public Byte decrementAndGet() {
		return Byte.valueOf(--field);
	}

}
