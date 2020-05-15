package com.github.kilianB.mutable;

import java.io.Serializable;

/**
 * Mutable class wrapper for boolean values. Mutable classes are useful
 * in lambda expressions or anonymous classes which want to alter the content of
 * a variable but are limited to final or effective final variables.
 * 
 * @author Kilian
 * @since 1.0.0
 */
public class MutableBoolean implements Mutable<Boolean>, Comparable<MutableBoolean>, Serializable {

	private static final long serialVersionUID = 126801622569995551L;

	private boolean field;

	/**
	 * Create a mutable Boolean with an initial value of False
	 */
	public MutableBoolean() {
	};

	/**
	 * Create a mutable Boolean.
	 * 
	 * @param initialValue the initial value of the integer
	 */
	public MutableBoolean(boolean initialValue) {
		this.field = initialValue;
	}

	@Override
	public Boolean getValue() {
		return field;
	}

	@Override
	public void setValue(Boolean newValue) {
		this.field = newValue;
	}
	
	/**
	 * Set the internal field to the new value
	 * @param newValue the new value
	 * @since 1.2.0
	 */
	public void setValue(boolean newValue) {
		this.field = newValue;
	}

	public boolean booleanValue() {
		return field;
	}
	
	/**
	 * Invert the value of the internal field
	 */
	public void invertValue() {
		field = !field;
	}

	@Override
	public int compareTo(MutableBoolean o) {
		return (this.field == o.field) ? 0 : (this.field ? 1 : -1);
	}

	@Override
	public int hashCode() {
		return (field ? 1231 : 1237);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MutableBoolean other = (MutableBoolean) obj;
		return field == other.field;
	}
}
