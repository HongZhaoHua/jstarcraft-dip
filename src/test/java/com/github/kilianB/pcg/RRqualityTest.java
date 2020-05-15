package com.github.kilianB.pcg;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.kilianB.pcg.cas.PcgRRCas;
import com.github.kilianB.pcg.lock.PcgRRLocked;
import com.github.kilianB.pcg.sync.PcgRR;


/**
 * Ensure that all RS Random number generators, no matter if fast locked synched
 * or cased perform the same in single threaded context
 */
public class RRqualityTest {

	private Pcg rsCAS;
	private Pcg rsSyn;
	private Pcg rsLock;

	// Repeat each test count times
	private int count = 1000;

	@BeforeEach
	public void reseed() {
		rsCAS = new PcgRRCas(0L, 0L);
		rsSyn = new PcgRR(0L, 0L);
		rsLock = new PcgRRLocked(0L, 0L);
	}

	@Test
	public void equalBoolean() {

		boolean[] cas = new boolean[count];
		boolean[] syn = new boolean[count];
		boolean[] lock = new boolean[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextBoolean();
			syn[i] = rsSyn.nextBoolean();
			lock[i] = rsLock.nextBoolean();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock));
	}

	@Test
	public void equalByte() {

		byte[] cas = new byte[count];
		byte[] syn = new byte[count];
		byte[] lock = new byte[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextByte();
			syn[i] = rsSyn.nextByte();
			lock[i] = rsLock.nextByte();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock));
	}

	@Test
	public void equalByteArray() {

		byte[] cas = new byte[count];
		byte[] syn = new byte[count];
		byte[] lock = new byte[count];

		rsCAS.nextBytes(cas);
		rsSyn.nextBytes(syn);
		rsLock.nextBytes(lock);

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock));
	}

	@Test
	public void equalChar() {

		char[] cas = new char[count];
		char[] syn = new char[count];
		char[] lock = new char[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextChar();
			syn[i] = rsSyn.nextChar();
			lock[i] = rsLock.nextChar();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock));
	}

	@Test
	public void equalShort() {

		short[] cas = new short[count];
		short[] syn = new short[count];
		short[] lock = new short[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextShort();
			syn[i] = rsSyn.nextShort();
			lock[i] = rsLock.nextShort();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock));
	}

	@Test
	public void equalInt() {

		int[] cas = new int[count];
		int[] syn = new int[count];
		int[] lock = new int[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextInt();
			syn[i] = rsSyn.nextInt();
			lock[i] = rsLock.nextInt();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock));
	}

	@Test
	public void equalLong() {

		long[] cas = new long[count];
		long[] syn = new long[count];
		long[] lock = new long[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextLong();
			syn[i] = rsSyn.nextLong();
			lock[i] = rsLock.nextLong();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock));
	}

	@Test
	public void equalFloat() {

		float[] cas = new float[count];
		float[] syn = new float[count];
		float[] lock = new float[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextFloat();
			syn[i] = rsSyn.nextFloat();
			lock[i] = rsLock.nextFloat();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock));
	}

	@Test
	public void equalFloatIncludeZero() {

		float[] cas = new float[count];
		float[] syn = new float[count];
		float[] lock = new float[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextFloat(true, false);
			syn[i] = rsSyn.nextFloat(true, false);
			lock[i] = rsLock.nextFloat(true, false);
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock));
	}

	@Test
	public void equalFloatIncludeOne() {

		float[] cas = new float[count];
		float[] syn = new float[count];
		float[] lock = new float[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextFloat(false, true);
			syn[i] = rsSyn.nextFloat(false, true);
			lock[i] = rsLock.nextFloat(false, true);
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock));

	}

	@Test
	public void equalDouble() {

		double[] cas = new double[count];
		double[] syn = new double[count];
		double[] lock = new double[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextDouble();
			syn[i] = rsSyn.nextDouble();
			lock[i] = rsLock.nextDouble();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock));
	}

	@Test
	public void equalDoubleIncludeZero() {

		double[] cas = new double[count];
		double[] syn = new double[count];
		double[] lock = new double[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextDouble(true, false);
			syn[i] = rsSyn.nextDouble(true, false);
			lock[i] = rsLock.nextDouble(true, false);
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock));
	}

	@Test
	public void equalDoubleIncludeOne() {

		double[] cas = new double[count];
		double[] syn = new double[count];
		double[] lock = new double[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextDouble(false, true);
			syn[i] = rsSyn.nextDouble(false, true);
			lock[i] = rsLock.nextDouble(false, true);
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock));
	}

	@Test
	public void equalGaus() {

		double[] cas = new double[count];
		double[] syn = new double[count];
		double[] lock = new double[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextGaussian();
			syn[i] = rsSyn.nextGaussian();
			lock[i] = rsLock.nextGaussian();
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock));
	}

	@Test
	public void equalIntN() {

		int[] cas = new int[count];
		int[] syn = new int[count];
		int[] lock = new int[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextInt(i + 1);
			syn[i] = rsSyn.nextInt(i + 1);
			lock[i] = rsLock.nextInt(i + 1);
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock));

	}

	@Test
	public void equalLongN() {

		long[] cas = new long[count];
		long[] syn = new long[count];
		long[] lock = new long[count];

		for (int i = 0; i < count; i++) {
			cas[i] = rsCAS.nextLong(i + 1);
			syn[i] = rsSyn.nextLong(i + 1);
			lock[i] = rsLock.nextLong(i + 1);
		}

		assertAll(() -> assertArrayEquals(cas, syn), () -> assertArrayEquals(cas, lock));
	}
	
}
