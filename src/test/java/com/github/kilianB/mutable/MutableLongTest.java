package com.github.kilianB.mutable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
class MutableLongTest {

	/**
	 * Test method for {@link com.github.kilianB.mutable.MutableLong#intValue()}.
	 */
	@Test
	void testIntValue() {
		MutableLong i = new MutableLong(1);
		assertEquals(1, i.intValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableLong#longValue()}.
	 */
	@Test
	void testLongValue() {
		MutableLong i = new MutableLong(1);
		assertEquals(1l, i.longValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableLong#floatValue()}.
	 */
	@Test
	void testFloatValue() {
		MutableLong i = new MutableLong(1);
		assertEquals(1f, i.floatValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableLong#doubleValue()}.
	 */
	@Test
	void testDoubleValue() {
		MutableLong i = new MutableLong(1);
		assertEquals(1d, i.doubleValue());
	}

	/**
	 * Test method for {@link com.github.kilianB.mutable.MutableLong#getValue()}.
	 */
	@Test
	void testGetValue() {
		MutableLong i = new MutableLong(1);
		assertEquals(Long.valueOf(1), i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableLong#setValue(java.lang.Long)}.
	 */
	@Test
	void testSetValue() {
		MutableLong i = new MutableLong(1);
		i.setValue(Long.valueOf(3));
		assertEquals(Long.valueOf(3),i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableLong#setValue(java.lang.Long)}.
	 */
	@Test
	void testSetValuePrimitive() {
		MutableLong i = new MutableLong(1);
		i.setValue(3l);
		assertEquals(Long.valueOf(3),i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableLong#equals(java.lang.Object)}.
	 */
	@Test
	void testEqualsObject() {
		MutableLong i = new MutableLong(4);
		MutableLong i2 = new MutableLong(4);
		assertEquals(i,i2);
		
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableLong#getAndIncrement()}.
	 */
	@Test
	void testGetAndIncrement() {
		MutableLong i = new MutableLong(4);
		assertEquals(Long.valueOf(4),i.getAndIncrement());
		assertEquals(Long.valueOf(5),i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableLong#incrementAndGet()}.
	 */
	@Test
	void testIncrementAndGet() {
		MutableLong i = new MutableLong(4);
		assertEquals(Long.valueOf(5),i.incrementAndGet());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableLong#getAndDecrement()}.
	 */
	@Test
	void testGetAndDecrement() {
		MutableLong i = new MutableLong(4);
		assertEquals(Long.valueOf(4),i.getAndDecrement());
		assertEquals(Long.valueOf(3),i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableLong#decrementAndGet()}.
	 */
	@Test
	void testDecrementAndGet() {
		MutableLong i = new MutableLong(4);
		assertEquals(Long.valueOf(3),i.decrementAndGet());
	}

}
