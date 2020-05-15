package com.github.kilianB.mutable;

import java.io.Serializable;

/**
 * Mutable class wrapper for double values. Mutable classes are useful
 * in lambda expressions or anonymous classes which want to alter the content of
 * a variable but are limited to final or effective final variables.
 * 
 * @author Kilian
 * @since 1.0.0
 */
public class MutableDouble extends Number implements Mutable<Double>, Comparable<MutableDouble>, Serializable{

	private static final long serialVersionUID = 6846548022746719522L;
	
	private double field;
	
	/**
	 * Create a mutable Double with an initial value of 0
	 */
	public MutableDouble() {};
	
	/**
	 * Create a mutable Double.
	 * @param initialValue the initial value of the float
	 */
	public MutableDouble(double initialValue) {
		this.field = initialValue;
	}
	
	@Override
	public int compareTo(MutableDouble o) {
		return Double.compare(field,o.field);
	}

	@Override
	public Double getValue() {
		return Double.valueOf(field);
	}

	@Override
	public void setValue(Double newValue) {
		field = newValue;
	}
	
	/**
	 * Set the internal field to the new value
	 * @param newValue the new value
	 * @since 1.2.0
	 */
	public void setValue(double newValue) {
		field = newValue;
	}
	
	/**
	 * @return the current value as double primitive
	 */
	public double doubleValue() {
		return field;
	}

	@Override
	public int hashCode() {
		long temp;
		temp = Double.doubleToLongBits(field);
		return (int) (temp ^ (temp >>> 32));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MutableDouble other = (MutableDouble) obj;
		if (Double.doubleToLongBits(field) != Double.doubleToLongBits(other.field))
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
	public float floatValue() {
		return (float) field;
	}

}
