package com.github.kilianB.pcg;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PcgTest {

	@Test
	public void seedNotZero() {
		assertTrue(Pcg.UNIQUE_SEED.get() != 0);
	}

}
