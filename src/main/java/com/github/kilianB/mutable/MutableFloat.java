package com.github.kilianB.mutable;

import java.io.Serializable;

/**
 * Mutable class wrapper for float values. Mutable classes are useful
 * in lambda expressions or anonymous classes which want to alter the content of
 * a variable but are limited to final or effective final variables.
 * 
 * @author Kilian
 * @since 1.0.0
 */
public class MutableFloat extends Number implements Mutable<Float>, Comparable<MutableFloat>, Serializable{

	private static final long serialVersionUID = 6846548022746719522L;
	
	private float field;
	
	/**
	 * Create a mutable Float with an initial value of 0
	 */
	public MutableFloat() {};
	
	/**
	 * Create a mutable Float.
	 * @param initialValue the initial value of the float
	 */
	public MutableFloat(float initialValue) {
		this.field = initialValue;
	}
	
	@Override
	public int compareTo(MutableFloat o) {
		return Float.compare(field,o.field);
	}

	@Override
	public Float getValue() {
		return Float.valueOf(field);
	}

	@Override
	public void setValue(Float newValue) {
		field = newValue;
	}
	
	/**
	 * Set the internal field to the new value
	 * @param newValue the new value
	 * @since 1.2.0
	 */
	public void setValue(float newValue) {
		field = newValue;
	}
	
	/**
	 * @return the current value as float primitive
	 */
	public float floatValue() {
		return field;
	}

	@Override
	public int hashCode() {
		return Float.floatToIntBits(field);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MutableFloat other = (MutableFloat) obj;
		if (Float.floatToIntBits(field) != Float.floatToIntBits(other.field))
			return false;
		return true;
	}

	@Override
	public int intValue() {
		return (int) field;
	}

	@Override
	public long longValue() {
		return (long) field;
	}

	@Override
	public double doubleValue() {
		return field;
	}

}
