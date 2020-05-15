package com.github.kilianB.mutable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
class MutableIntegerTest {

	/**
	 * Test method for {@link com.github.kilianB.mutable.MutableInteger#intValue()}.
	 */
	@Test
	void testIntValue() {
		MutableInteger i = new MutableInteger(1);
		assertEquals(1, i.intValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableInteger#longValue()}.
	 */
	@Test
	void testLongValue() {
		MutableInteger i = new MutableInteger(1);
		assertEquals(1l, i.longValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableInteger#floatValue()}.
	 */
	@Test
	void testFloatValue() {
		MutableInteger i = new MutableInteger(1);
		assertEquals(1f, i.floatValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableInteger#doubleValue()}.
	 */
	@Test
	void testDoubleValue() {
		MutableInteger i = new MutableInteger(1);
		assertEquals(1d, i.doubleValue());
	}

	/**
	 * Test method for {@link com.github.kilianB.mutable.MutableInteger#getValue()}.
	 */
	@Test
	void testGetValue() {
		MutableInteger i = new MutableInteger(1);
		assertEquals(Integer.valueOf(1), i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableInteger#setValue(java.lang.Integer)}.
	 */
	@Test
	void testSetValue() {
		MutableInteger i = new MutableInteger(1);
		i.setValue(Integer.valueOf(3));
		assertEquals(Integer.valueOf(3),i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableInteger#setValue(java.lang.Integer)}.
	 */
	@Test
	void testSetValuePrimitive() {
		MutableInteger i = new MutableInteger(1);
		i.setValue(3);
		assertEquals(Integer.valueOf(3),i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableInteger#equals(java.lang.Object)}.
	 */
	@Test
	void testEqualsObject() {
		MutableInteger i = new MutableInteger(4);
		MutableInteger i2 = new MutableInteger(4);
		assertEquals(i,i2);
		
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableInteger#getAndIncrement()}.
	 */
	@Test
	void testGetAndIncrement() {
		MutableInteger i = new MutableInteger(4);
		assertEquals(Integer.valueOf(4),i.getAndIncrement());
		assertEquals(Integer.valueOf(5),i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableInteger#incrementAndGet()}.
	 */
	@Test
	void testIncrementAndGet() {
		MutableInteger i = new MutableInteger(4);
		assertEquals(Integer.valueOf(5),i.incrementAndGet());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableInteger#getAndDecrement()}.
	 */
	@Test
	void testGetAndDecrement() {
		MutableInteger i = new MutableInteger(4);
		assertEquals(Integer.valueOf(4),i.getAndDecrement());
		assertEquals(Integer.valueOf(3),i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableInteger#decrementAndGet()}.
	 */
	@Test
	void testDecrementAndGet() {
		MutableInteger i = new MutableInteger(4);
		assertEquals(Integer.valueOf(3),i.decrementAndGet());
	}

}
