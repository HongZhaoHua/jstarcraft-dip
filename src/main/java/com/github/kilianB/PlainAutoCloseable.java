package com.github.kilianB;

/**
 * Auto {@link java.lang.AutoCloseable} without throwing an exception
 * <p>
 * An object that may hold resources (such as file or socket handles) until it
 * is closed. The {@link #close()} method of an {@code AutoCloseable} object is
 * called automatically when exiting a {@code
 * try}-with-resources block for which the object has been declared in the
 * resource specification header. This construction ensures prompt release,
 * avoiding resource exhaustion exceptions and errors that may otherwise occur.
 * 
 * @author Kilian
 *
 */
public interface PlainAutoCloseable extends AutoCloseable {

	void close();
}
