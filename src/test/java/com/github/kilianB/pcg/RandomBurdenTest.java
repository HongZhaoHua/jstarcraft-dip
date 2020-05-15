package com.github.kilianB.pcg;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * Test cases our class inherited due to extending the Random class
 * 
 * @author Kilian
 *
 */
public abstract class RandomBurdenTest {

	@Test
	public void setSeed() {
		Random rng = getInstance();
		
		rng.setSeed(15);
		int[] randomInts = new int[10];
		for(int i = 0; i < randomInts.length; i++) {
			randomInts[i] = rng.nextInt();
		}
		
		rng.setSeed(15);
		int[] randomInts2 = new int[10];
		for(int i = 0; i < randomInts.length; i++) {
			randomInts2[i] = rng.nextInt();
		}
		assertArrayEquals(randomInts,randomInts2);
	}
	
	protected abstract Random getInstance();
	
	

}
