package com.github.kilianB;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
class StringUtilTest {


	@Test
	void charsNeededZero() {
		assertEquals(1,StringUtil.charsNeeded(Integer.valueOf(0)));
	}
	
	@Test
	void charsNeededOne() {
		assertEquals(1,StringUtil.charsNeeded(Integer.valueOf(1)));
	}
	
	@Test
	void charsNeededTen() {
		assertEquals(2,StringUtil.charsNeeded(Integer.valueOf(10)));
	}
	
	@Test
	void charsNeededNegative() {
		assertEquals(2,StringUtil.charsNeeded(Integer.valueOf(-1)));
	}
	
	@Test
	void charsNeededFraction() {
		assertEquals(2,StringUtil.charsNeeded(Double.valueOf(10.1234)));
	}
	
	@Test
	void charsNeededFractionNegative() {
		assertEquals(3,StringUtil.charsNeeded(Double.valueOf(-10.1234)));
	}
	
	@Test
	void multiplyTextEmpty() {
		assertEquals(0,StringUtil.multiplyChar("",10).length());
	}
	
	@Test
	void multiplyTextSimple() {
		assertEquals(10,StringUtil.multiplyChar(" ",10).length());
	}
	
	@Test
	void multiplyTextTwoSpaces() {
		assertEquals(20,StringUtil.multiplyChar("  ",10).length());
	}
	
	@Test
	void fillStringSimple() {
		String content = "Hello";
		String result = StringUtil.fillString("-",10,content);
		assertTrue(result.startsWith(content));
		assertTrue(result.endsWith("-----"));
	}
}
