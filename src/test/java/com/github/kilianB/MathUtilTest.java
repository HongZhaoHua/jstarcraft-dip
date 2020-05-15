package com.github.kilianB;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
class MathUtilTest {

	@Nested
	class ClosestDivisibleInteger{
		@Test
		void findClosestDivisibleIntegerTest() {
			// Error
			assertThrows(java.lang.ArithmeticException.class, () -> {
				MathUtil.findClosestDivisibleInteger(0, 0);
			});
		}

		@Test
		void findClosestDivisibleIntegerIdentityTest() {
			assertEquals(1, MathUtil.findClosestDivisibleInteger(1, 1));
		}

		@Test
		void findClosestDivisibleIntegerPossibleDivisionTest() {
			assertEquals(4, MathUtil.findClosestDivisibleInteger(4, 2));
		}

		@Test
		void findClosestDivisibleIntegerUpperBoundTest() {
			assertEquals(9, MathUtil.findClosestDivisibleInteger(8, 3));
		}

		@Test
		void findClosestDivisibleIntegerLowerBoundTest() {
			assertEquals(8, MathUtil.findClosestDivisibleInteger(9, 4));
		}

		@Test
		void findClosestDivisibleIntegerEquidistantTest() {
			// 3 to each side 12 - 15 - 18
			assertEquals(18, MathUtil.findClosestDivisibleInteger(15, 6));
		}

		@Test
		void findClosestDivisibleIntegerIdentityNegativeDivisorTest() {
			assertEquals(1, MathUtil.findClosestDivisibleInteger(1, -1));
		}
		
		@Test
		void findClosestDivisibleIntegerIdentityNegativeDivisorTest2() {
			assertEquals(2, MathUtil.findClosestDivisibleInteger(2, -2));
		}

		@Test
		void findClosestDivisibleIntegerPossibleDivisionNegativeDivisorTest() {
			assertEquals(4, MathUtil.findClosestDivisibleInteger(4, -2));
		}

		@Test
		void findClosestDivisibleIntegerUpperBoundNegativeDivisorTest() {
			assertEquals(12, MathUtil.findClosestDivisibleInteger(11, -3));
		}

		@Test
		void findClosestDivisibleIntegerLowerBoundNegativeDivisorTest() {
			assertEquals(6, MathUtil.findClosestDivisibleInteger(7, -3));
		}

		@Test
		void findClosestDivisibleIntegerIdentityNegativeDividendTest() {
			assertEquals(-1, MathUtil.findClosestDivisibleInteger(-1, 1));
		}
		
		@Test
		void findClosestDivisibleIntegerIdentityNegativeDividendTest2() {
			assertEquals(-2, MathUtil.findClosestDivisibleInteger(-2, 2));
		}

		@Test
		void findClosestDivisibleIntegerPossibleDivisionNegativeDividendTest() {
			assertEquals(-4, MathUtil.findClosestDivisibleInteger(-4, 2));
		}

		@Test
		void findClosestDivisibleIntegerLowerBoundNegativeDividendTest() {
			assertEquals(-12, MathUtil.findClosestDivisibleInteger(-11, 3));
		}

		@Test
		void findClosestDivisibleIntegerUpperBoundNegativeDividendTest() {
			assertEquals(-6, MathUtil.findClosestDivisibleInteger(-7, 3));
		}
		
		@Test
		void findClosestDivisibleIntegerEquidistantNegativeDividendTest() {
			// 3 to each side 12 - 15 - 18
			assertEquals(18, MathUtil.findClosestDivisibleInteger(15, 6));
		}
	}
	
	@Nested
	class ClampNumber{
		@Test
		void clampNumberIdentity() {
			assertEquals(0,(int)MathUtil.clampNumber(0,0,0));
		}
		
		@Test
		void clampNumberExactUpper() {
			assertEquals(1,(int)MathUtil.clampNumber(1,0,1));
		}
		
		@Test
		void clampNumberExactLower() {
			assertEquals(0,(int)MathUtil.clampNumber(0,0,1));
		}
		
		@Test
		void clampNumberUpper() {
			assertEquals(1,(int)MathUtil.clampNumber(2,0,1));
		}
		
		@Test
		void clampNumberLower() {
			assertEquals(0,(int)MathUtil.clampNumber(-1,0,1));
		}
		
		@Test 
		void clampNumberInBetween() {
			assertEquals(2,(int) MathUtil.clampNumber(0,2,4));
		}
	}
	
	@Nested 
	class FractionalPart{
		@Test
		void fractionalNothing() {
			assertEquals(0,MathUtil.getFractionalPart(4d));
		}
		
		@Test
		void fractionalDefined() {
			assertEquals(0.123,MathUtil.getFractionalPart(4.123d),1e-6);
		}
		
		@Test
		void fractionalDefinedNegative() {
			assertEquals(-0.123,MathUtil.getFractionalPart(-4.123d),1e-6);
		}
	}
	
	@Nested
	class DoubleEquals{
		@Test
		void isDoubleEqualsValid() {
			assertTrue(MathUtil.isDoubleEquals(1d,1d,0));
		}
		
		@Test
		void isDoubleEqualsInvalid() {
			assertFalse(MathUtil.isDoubleEquals(2d,1d,0));
		}
		
		@Test
		void isDoubleEqualsNegativeValid() {
			assertTrue(MathUtil.isDoubleEquals(-1d,-1d,0));
		}
		
		@Test
		void isDoubleEqualsNegativeInValid() {
			assertFalse(MathUtil.isDoubleEquals(-2d,1d,0));
		}
		
		@Test
		void isDoubleEqualsValidEpisolon() {
			assertTrue(MathUtil.isDoubleEquals(1.2d,1d,1));
		}
		
		@Test
		void isDoubleEqualsNegativeValidEpisolon() {
			assertTrue(MathUtil.isDoubleEquals(-1.2d,-1d,1));
		}
		
		@Test
		void isDoubleEqualsInvalidEpsilon() {
			assertFalse(MathUtil.isDoubleEquals(2.2d,2d,0.1d));
		}
	}
	
	@Nested
	class IsNumeric{
		
		@Test
		void intPrimitive() {
			assertTrue(MathUtil.isNumeric(1));
		}
		
		@Test
		void bytePrimitive() {
			assertTrue(MathUtil.isNumeric((byte)1));
		}
		
		@Test
		void booleanPrimitive() {
			assertFalse(MathUtil.isNumeric(false));
		}
		
		@Test
		void longPrimitive() {
			assertTrue(MathUtil.isNumeric(1l));
		}
		
		@Test
		void floatPrimitive() {
			assertTrue(MathUtil.isNumeric(1f));
		}
		
		@Test
		void doublePrimitive() {
			assertTrue(MathUtil.isNumeric(1d));
		}
		
		@Test
		void charPrimitive() {
			assertFalse(MathUtil.isNumeric('c'));
		}
		
		@Test
		void string() {
			assertFalse(MathUtil.isNumeric("HelloWorld"));
		}
		
		@Test
		void object() {
			assertFalse(MathUtil.isNumeric(new Object()));
		}
		
		//Numeric Objects
		@Test
		void Byte() {
			assertTrue(MathUtil.isNumeric(Byte.valueOf((byte)1)));
		}
		
		@Test
		void Boolean() {
			assertFalse(MathUtil.isNumeric(Boolean.TRUE));
		}
		
		@Test
		void Long() {
			assertTrue(MathUtil.isNumeric(Long.valueOf(0)));
		}
		
		@Test
		void Float() {
			assertTrue(MathUtil.isNumeric(Float.valueOf(0.2f)));
		}
		
		@Test
		void Double() {
			assertTrue(MathUtil.isNumeric(Double.valueOf(0.1d)));
		}
		
		@Test
		void Character() {
			assertFalse(MathUtil.isNumeric(Character.valueOf('c')));
		}
		
	}

	@Nested
	class Log{
		
		@Test
		void valid() {
			assertEquals(2,MathUtil.log(4,2));
		}
		
		@Test
		void validFractional() {
			assertEquals(2.321928094887362347870319429489390175864831393024580612054,MathUtil.log(5,2),1e-15);
		}
		
		@Test
		void logBase10() {
			assertEquals(1.176091259055681242081289008530622282431938982728587323519,MathUtil.log(15,10),1e-15);
		}
		
		@Test
		void validBase3() {
			assertEquals(1.630929753571457437099527114342760854299585640131880427870,MathUtil.log(6,3),1e-15);
		}
		@Test
		void zeroValue() {
			assertEquals(Double.NEGATIVE_INFINITY,MathUtil.log(0,1));
		}
		@Test
		void negativeValue() {
			assertEquals(Double.NaN,MathUtil.log(-1,1));
		}
		@Test
		void zeroBase() {
			assertEquals(Double.NaN,MathUtil.log(10,0));
		}
		
		@Test
		void nanValue() {
			assertEquals(Double.NaN,MathUtil.log(Double.NaN,2));
		}
		
		@Test
		void nanBase() {
			assertEquals(Double.NaN,MathUtil.log(3,Double.NaN));
		}
		
		
	}

	@Nested
	class TriangularNumber{
		
		@Test
		void one() {
			assertEquals(1,MathUtil.triangularNumber(1));
		}
		
		@Test
		void two() {
			assertEquals(3,MathUtil.triangularNumber(2));
		}
		
		@Test
		void three() {
			assertEquals(6,MathUtil.triangularNumber(3));
		}
		
		@Test
		void four() {
			assertEquals(10,MathUtil.triangularNumber(4));
		}
		
		@Test
		void five() {
			assertEquals(15,MathUtil.triangularNumber(5));
		}
		
		@Test
		void six() {
			assertEquals(21,MathUtil.triangularNumber(6));
		}
		
		
	}

	@Nested
	class FitGaussian {
		//TODO more test cases
		void zero() {
			assertEquals(0,MathUtil.fitGaussian(0,1,0));
		}
		
		void meanMove() {
			assertEquals(0,MathUtil.fitGaussian(1,1,1));
		}
	}

	@Nested
	class NormalizeValue{
		
		@Test
		void testLowerZeroIncrease() {
			assertEquals(0,MathUtil.normalizeValue(0,0,1,0,10));
		}
		
		@Test
		void testLowerZeroDecrease() {
			assertEquals(0,MathUtil.normalizeValue(0,0,10,0,1));
		}
		
		@Test
		void testLowerIncrease() {
			assertEquals(1,MathUtil.normalizeValue(1,1,2,1,10));
		}
		
		@Test
		void testLowerDecrease() {
			assertEquals(1,MathUtil.normalizeValue(1,1,10,1,2));
		}
		
		@Test
		void testHigherIncrease() {
			assertEquals(10,MathUtil.normalizeValue(1,0,1,0,10));
		}
		
		@Test
		void testHigherDecrease() {
			assertEquals(1,MathUtil.normalizeValue(5,0,5,0,1));
		}
		
		@Test
		void increase() {
			assertEquals(5,MathUtil.normalizeValue(1,0,2,0,10));
		}
		
		@Test
		void decrease() {
			assertEquals(1,MathUtil.normalizeValue(5,0,10,0,2));
		}
		
	}

	@Nested
	class LowerShiftBitMask{
		
		@Test 
		void zeroMask(){
			assertEquals(-1,MathUtil.getLowerShiftBitMask(0x0));
		}
		
		@Test 
		void zeroOffset(){
			assertEquals(0,MathUtil.getLowerShiftBitMask(0x1));
		}
		
		@Test 
		void zeroOffsetNegative(){
			assertEquals(0,MathUtil.getLowerShiftBitMask(-1));
		}
		
		@Test 
		void oneBitOffset(){
			assertEquals(1,MathUtil.getLowerShiftBitMask(0x2));
		}
		
		@Test 
		void oneBitOffsetNegative(){
			assertEquals(1,MathUtil.getLowerShiftBitMask(-2));
		}
		
		@Test 
		void twoBitOffset(){
			assertEquals(2,MathUtil.getLowerShiftBitMask(0x4));
		}
		
		//RGB
		
		@Test 
		void eightBitOffset(){
			assertEquals(8,MathUtil.getLowerShiftBitMask(0x0000ff00));
		}
		
		@Test 
		void sixteenBitOffset(){
			assertEquals(16,MathUtil.getLowerShiftBitMask(0x00ff0000));
		}
		
		@Test 
		void twentyFourBitOffset(){
			assertEquals(24,MathUtil.getLowerShiftBitMask(0xff000000));
		}
		
		
		@Test 
		void mixedBitOffset(){
			assertEquals(0,MathUtil.getLowerShiftBitMask(0xff0000ff));
		}
		
		
	}
}
