package com.github.kilianB.mutable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
public class MutableByteTest {
	/**
	 * Test method for {@link com.github.kilianB.mutable.MutableByte#intValue()}.
	 */
	@Test
	void testIntValue() {
		MutableByte i = new MutableByte((byte)1);
		assertEquals(1, i.intValue());
	}

	/**
	 * Test method for {@link com.github.kilianB.mutable.MutableByte#longValue()}.
	 */
	@Test
	void testByteValue() {
		MutableByte i = new MutableByte((byte)1);
		assertEquals(1l, i.longValue());
	}

	/**
	 * Test method for {@link com.github.kilianB.mutable.MutableByte#floatValue()}.
	 */
	@Test
	void testFloatValue() {
		MutableByte i = new MutableByte((byte)1);
		assertEquals(1f, i.floatValue());
	}

	/**
	 * Test method for {@link com.github.kilianB.mutable.MutableByte#doubleValue()}.
	 */
	@Test
	void testDoubleValue() {
		MutableByte i = new MutableByte((byte)1);
		assertEquals(1d, i.doubleValue());
	}

	/**
	 * Test method for {@link com.github.kilianB.mutable.MutableByte#getValue()}.
	 */
	@Test
	void testGetValue() {
		MutableByte i = new MutableByte((byte)1);
		assertEquals(Byte.valueOf((byte)1), i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableByte#setValue(java.lang.Byte)}.
	 */
	@Test
	void testSetValue() {
		MutableByte i = new MutableByte((byte)1);
		i.setValue(Byte.valueOf((byte)3));
		assertEquals(Byte.valueOf((byte)3), i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableByte#setValue(java.lang.Byte)}.
	 */
	@Test
	void testSetValuePrimitive() {
		MutableByte i = new MutableByte((byte)1);
		i.setValue((byte)3);
		assertEquals(Byte.valueOf((byte)3), i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableByte#equals(java.lang.Object)}.
	 */
	@Test
	void testEqualsObject() {
		MutableByte i = new MutableByte((byte)4);
		MutableByte i2 = new MutableByte((byte)4);
		assertEquals(i, i2);

	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableByte#getAndIncrement()}.
	 */
	@Test
	void testGetAndIncrement() {
		MutableByte i = new MutableByte((byte)4);
		assertEquals(Byte.valueOf((byte)4), i.getAndIncrement());
		assertEquals(Byte.valueOf((byte)5), i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableByte#incrementAndGet()}.
	 */
	@Test
	void testIncrementAndGet() {
		MutableByte i = new MutableByte((byte)4);
		assertEquals(Byte.valueOf((byte)5), i.incrementAndGet());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableByte#getAndDecrement()}.
	 */
	@Test
	void testGetAndDecrement() {
		MutableByte i = new MutableByte((byte)4);
		assertEquals(Byte.valueOf((byte)4), i.getAndDecrement());
		assertEquals(Byte.valueOf((byte)3), i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableByte#decrementAndGet()}.
	 */
	@Test
	void testDecrementAndGet() {
		MutableByte i = new MutableByte((byte)4);
		assertEquals(Byte.valueOf((byte)3), i.decrementAndGet());
	}
}
