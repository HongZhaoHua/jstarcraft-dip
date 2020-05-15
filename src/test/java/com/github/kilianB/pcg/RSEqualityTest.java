package com.github.kilianB.pcg;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.kilianB.pcg.cas.PcgRSCas;
import com.github.kilianB.pcg.fast.PcgRSFast;
import com.github.kilianB.pcg.fast.PcgRSUFast;
import com.github.kilianB.pcg.lock.PcgRSLocked;
import com.github.kilianB.pcg.sync.PcgRS;

/**
 * Ensure that all RS Random number generators, no matter if fast locked synched
 * or cased perform the same in single threaded context
 */
public class RSEqualityTest {

	private Pcg rsCAS;
	private Pcg rsSyn;
	private Pcg rsLock;
	private Pcg rsFast;

	// Repeat each test count times
	private int count = 1000;

	@BeforeEach
	public void reseed() {
		rsCAS = new PcgRSCas(0L, 0L);
		rsSyn = new PcgRS(0L, 0L);
		rsLock = new PcgRSLocked(0L, 0L);
		rsFast = new PcgRSFast(0L, 0L);
		PcgRSUFast.seed(0L, 0L);
	}

	@Test
	public void equalBoolean() {

		boolean[] cas = new boolean[count];
		boolean[] syn = new boolean[count];
		boolean[] lock = new boolean[count];
		boolean[] fast = new boolean[count];
		boolean[] uFast = new boolean[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextBoolean();
			syn[i] = rsSyn.nextBoolean();
			lock[i] = rsLock.nextBoolean();
			fast[i] = rsFast.nextBoolean();
			uFast[i] = PcgRSUFast.nextBoolean();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock),
				() -> assertArrayEquals(cas, fast), () -> assertArrayEquals(cas, uFast));
	}

	@Test
	public void equalByte() {

		byte[] cas = new byte[count];
		byte[] syn = new byte[count];
		byte[] lock = new byte[count];
		byte[] fast = new byte[count];
		byte[] uFast = new byte[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextByte();
			syn[i] = rsSyn.nextByte();
			lock[i] = rsLock.nextByte();
			fast[i] = rsFast.nextByte();
			uFast[i] = PcgRSUFast.nextByte();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock),
				() -> assertArrayEquals(cas, fast), () -> assertArrayEquals(cas, uFast));
	}

	@Test
	public void equalByteArray() {

		byte[] cas = new byte[count];
		byte[] syn = new byte[count];
		byte[] lock = new byte[count];
		byte[] fast = new byte[count];
		byte[] uFast = new byte[count];

		rsCAS.nextBytes(cas);
		rsSyn.nextBytes(syn);
		rsLock.nextBytes(lock);
		rsFast.nextBytes(fast);
		PcgRSUFast.nextBytes(uFast);

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock),
				() -> assertArrayEquals(cas, fast), () -> assertArrayEquals(cas, uFast));
	}

	@Test
	public void equalChar() {

		char[] cas = new char[count];
		char[] syn = new char[count];
		char[] lock = new char[count];
		char[] fast = new char[count];
		char[] uFast = new char[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextChar();
			syn[i] = rsSyn.nextChar();
			lock[i] = rsLock.nextChar();
			fast[i] = rsFast.nextChar();
			uFast[i] = PcgRSUFast.nextChar();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock),
				() -> assertArrayEquals(cas, fast), () -> assertArrayEquals(cas, uFast));
	}

	@Test
	public void equalShort() {

		short[] cas = new short[count];
		short[] syn = new short[count];
		short[] lock = new short[count];
		short[] fast = new short[count];
		short[] uFast = new short[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextShort();
			syn[i] = rsSyn.nextShort();
			lock[i] = rsLock.nextShort();
			fast[i] = rsFast.nextShort();
			uFast[i] = PcgRSUFast.nextShort();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock),
				() -> assertArrayEquals(cas, fast), () -> assertArrayEquals(cas, uFast));
	}

	@Test
	public void equalInt() {

		int[] cas = new int[count];
		int[] syn = new int[count];
		int[] lock = new int[count];
		int[] fast = new int[count];
		int[] uFast = new int[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextInt();
			syn[i] = rsSyn.nextInt();
			lock[i] = rsLock.nextInt();
			fast[i] = rsFast.nextInt();
			uFast[i] = PcgRSUFast.nextInt();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock),
				() -> assertArrayEquals(cas, fast), () -> assertArrayEquals(cas, uFast));
	}

	@Test
	public void equalLong() {

		long[] cas = new long[count];
		long[] syn = new long[count];
		long[] lock = new long[count];
		long[] fast = new long[count];
		long[] uFast = new long[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextLong();
			syn[i] = rsSyn.nextLong();
			lock[i] = rsLock.nextLong();
			fast[i] = rsFast.nextLong();
			uFast[i] = PcgRSUFast.nextLong();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock),
				() -> assertArrayEquals(cas, fast), () -> assertArrayEquals(cas, uFast));
	}

	@Test
	public void equalFloat() {

		float[] cas = new float[count];
		float[] syn = new float[count];
		float[] lock = new float[count];
		float[] fast = new float[count];
		float[] uFast = new float[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextFloat();
			syn[i] = rsSyn.nextFloat();
			lock[i] = rsLock.nextFloat();
			fast[i] = rsFast.nextFloat();
			uFast[i] = PcgRSUFast.nextFloat();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock),
				() -> assertArrayEquals(cas, fast), () -> assertArrayEquals(cas, uFast));
	}

	@Test
	public void equalFloatIncludeZero() {

		float[] cas = new float[count];
		float[] syn = new float[count];
		float[] lock = new float[count];
		float[] fast = new float[count];
		float[] uFast = new float[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextFloat(true, false);
			syn[i] = rsSyn.nextFloat(true, false);
			lock[i] = rsLock.nextFloat(true, false);
			fast[i] = rsFast.nextFloat(true, false);
			uFast[i] = PcgRSUFast.nextFloat(true, false);
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock),
				() -> assertArrayEquals(cas, fast), () -> assertArrayEquals(cas, uFast));
	}

	@Test
	public void equalFloatIncludeOne() {

		float[] cas = new float[count];
		float[] syn = new float[count];
		float[] lock = new float[count];
		float[] fast = new float[count];
		float[] uFast = new float[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextFloat(false, true);
			syn[i] = rsSyn.nextFloat(false, true);
			lock[i] = rsLock.nextFloat(false, true);
			fast[i] = rsFast.nextFloat(false, true);
			uFast[i] = PcgRSUFast.nextFloat(false, true);
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock),
				() -> assertArrayEquals(cas, fast), () -> assertArrayEquals(cas, uFast));

	}

	@Test
	public void equalDouble() {

		double[] cas = new double[count];
		double[] syn = new double[count];
		double[] lock = new double[count];
		double[] fast = new double[count];
		double[] uFast = new double[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextDouble();
			syn[i] = rsSyn.nextDouble();
			lock[i] = rsLock.nextDouble();
			fast[i] = rsFast.nextDouble();
			uFast[i] = PcgRSUFast.nextDouble();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock),
				() -> assertArrayEquals(cas, fast), () -> assertArrayEquals(cas, uFast));
	}

	@Test
	public void equalDoubleIncludeZero() {

		double[] cas = new double[count];
		double[] syn = new double[count];
		double[] lock = new double[count];
		double[] fast = new double[count];
		double[] uFast = new double[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextDouble(true, false);
			syn[i] = rsSyn.nextDouble(true, false);
			lock[i] = rsLock.nextDouble(true, false);
			fast[i] = rsFast.nextDouble(true, false);
			uFast[i] = PcgRSUFast.nextDouble(true, false);
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock),
				() -> assertArrayEquals(cas, fast), () -> assertArrayEquals(cas, uFast));
	}

	@Test
	public void equalDoubleIncludeOne() {

		double[] cas = new double[count];
		double[] syn = new double[count];
		double[] lock = new double[count];
		double[] fast = new double[count];
		double[] uFast = new double[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextDouble(false,true);
			syn[i] = rsSyn.nextDouble(false,true);
			lock[i] = rsLock.nextDouble(false,true);
			fast[i] = rsFast.nextDouble(false,true);
			uFast[i] = PcgRSUFast.nextDouble(false,true);
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock),
				() -> assertArrayEquals(cas, fast), () -> assertArrayEquals(cas, uFast));
	}

	@Test
	public void equalGaus() {

		double[] cas = new double[count];
		double[] syn = new double[count];
		double[] lock = new double[count];
		double[] fast = new double[count];
		double[] uFast = new double[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextGaussian();
			syn[i] = rsSyn.nextGaussian();
			lock[i] = rsLock.nextGaussian();
			fast[i] = rsFast.nextGaussian();
			uFast[i] = PcgRSUFast.nextGaussian();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock),
				() -> assertArrayEquals(cas, fast), () -> assertArrayEquals(cas, uFast));
	}

	@Test
	public void equalIntN() {

		int[] cas = new int[count];
		int[] syn = new int[count];
		int[] lock = new int[count];
		int[] fast = new int[count];
		int[] uFast = new int[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextInt(i+1);
			syn[i] = rsSyn.nextInt(i+1);
			lock[i] = rsLock.nextInt(i+1);
			fast[i] = rsFast.nextInt(i+1);
			uFast[i] = PcgRSUFast.nextInt(i+1);
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock),
				() -> assertArrayEquals(cas, fast), () -> assertArrayEquals(cas, uFast));
		
	}

	@Test
	public void equalLongN() {

		long[] cas = new long[count];
		long[] syn = new long[count];
		long[] lock = new long[count];
		long[] fast = new long[count];
		long[] uFast = new long[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextLong(i+1);
			syn[i] = rsSyn.nextLong(i+1);
			lock[i] = rsLock.nextLong(i+1);
			fast[i] = rsFast.nextLong(i+1);
			uFast[i] = PcgRSUFast.nextLong(i+1);
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock),
				() -> assertArrayEquals(cas, fast), () -> assertArrayEquals(cas, uFast));
	}
	
	@Test
	public void distanceUnsafe() {
		PcgRSFast fastRs = new PcgRSFast(5,5);
		PcgRS rs = new PcgRS(5,5);
		assertEquals(0l,fastRs.distanceUnsafe(rs));
	}
	
	@Test
	public void distanceSafe() {
		PcgRSFast fastRs = new PcgRSFast(5,5);
		PcgRS rs = new PcgRS(5,5);
		assertThrows(IncompatibleGeneratorException.class,()->{fastRs.distance(rs);});
		assertThrows(IncompatibleGeneratorException.class,()->{rs.distance(fastRs);});
	}
	
	@Test
	public void distanceUnsafeAfterStep() {
		PcgRSFast fastRs = new PcgRSFast(5,5);
		PcgRS rs = new PcgRS(5,5);
		rs.nextBoolean();
		fastRs.nextBoolean();
		assertEquals(0l,fastRs.distanceUnsafe(rs));
	}
	
}
