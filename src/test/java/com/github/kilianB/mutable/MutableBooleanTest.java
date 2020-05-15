package com.github.kilianB.mutable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
class MutableBooleanTest {

	/**
	 * Test method for {@link com.github.kilianB.mutable.MutableBoolean#getValue()}.
	 */
	@Test
	void testGetValue() {
		MutableBoolean i = new MutableBoolean(true);
		assertEquals(Boolean.TRUE, i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableBoolean#setValue(java.lang.Boolean)}.
	 */
	@Test
	void testSetValue() {
		MutableBoolean i = new MutableBoolean(true);
		i.setValue(Boolean.valueOf(Boolean.FALSE));
		assertEquals(Boolean.FALSE, i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableBoolean#setValue(java.lang.Boolean)}.
	 */
	@Test
	void testSetValuePrimitive() {
		MutableBoolean i = new MutableBoolean(false);
		i.setValue(true);
		assertEquals(Boolean.TRUE, i.getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableBoolean#equals(java.lang.Object)}.
	 */
	@Test
	void testEqualsObject() {
		MutableBoolean i = new MutableBoolean(false);
		MutableBoolean i2 = new MutableBoolean(false);
		assertEquals(i, i2);

	}

	/**
	 * Test method for
	 * {@link com.github.kilianB.mutable.MutableBoolean#getAndIncrement()}.
	 */
	@Test
	void testInvertValueToogle() {
		MutableBoolean i = new MutableBoolean(false);
		i.invertValue();
		assertTrue(i.getValue());
	}
}
