package com.github.kilianB.pcg;

/**
 * Exception thrown when random number generators interact with each other but
 * are not in the correct state to be compared.
 * 
 * @author Kilian
 *
 */
public class IncompatibleGeneratorException extends IllegalArgumentException {

	private static final long serialVersionUID = 6715594414038407215L;

	public IncompatibleGeneratorException(String message) {
		super(message);
	}

}
