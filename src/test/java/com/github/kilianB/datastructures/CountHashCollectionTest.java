package com.github.kilianB.datastructures;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
class CountHashCollectionTest {

	// Two Integers with the same int values are equal! We can simply cache the
	// value instead of creating a new object
	private final static Integer ZERO = Integer.valueOf(0);
	private final static Integer ONE = Integer.valueOf(1);
	private final static Integer TWO = Integer.valueOf(2);

	CountHashCollection<Integer> c;

	@BeforeEach
	void setup() {
		c = new CountHashCollection<>();
	}

	@Test
	void sizeEmpty() {
		assertEquals(0, c.size());
	}

	@Test
	void sizeOne() {
		c.add(ZERO);
		assertEquals(1, c.size());
	}

	@Test
	void sizeTwoDistinct() {
		c.add(ZERO);
		c.add(ONE);
		assertEquals(2, c.size());
	}

	@Test
	void sizeTwoEquals() {
		c.add(ONE);
		c.add(ONE);
		assertEquals(2, c.size());
	}

	@Test
	void sizeRemoveEquals() {
		c.add(ONE);
		c.add(ONE);
		c.remove(ONE);
		assertEquals(1, c.size());
	}

	@Test
	void sizeUniqueEmpty() {
		assertEquals(0, c.sizeUnique());
	}

	@Test
	void sizeUniqueOne() {
		c.add(ZERO);
		assertEquals(1, c.sizeUnique());
	}

	@Test
	void sizeUniqueTwoDistinct() {
		c.add(ZERO);
		c.add(ONE);
		assertEquals(2, c.sizeUnique());
	}

	@Test
	void sizeUniqueTwoEquals() {
		c.add(ONE);
		c.add(ONE);
		assertEquals(1, c.sizeUnique());
	}

	@Test
	void sizeUniqueRemovequals() {
		c.add(ONE);
		c.add(ONE);
		c.remove(ONE);
		assertEquals(1, c.sizeUnique());
	}

	@Test
	void containsCountNotPresent() {
		assertEquals(0, c.containsCount(ZERO));
	}

	@Test
	void containsCount() {
		c.add(ONE);
		c.add(TWO);
		assertEquals(1, c.containsCount(ONE));
		assertEquals(1, c.containsCount(TWO));
	}

	@Test
	void containsCount2() {
		c.add(ONE);
		c.add(TWO);
		c.add(ONE);
		assertEquals(2, c.containsCount(ONE));
		assertEquals(1, c.containsCount(TWO));
	}
	
	@Test 
	void removeFullyNotPresent() {
		assertFalse(c.removeFully(ZERO));
	}
	
	@Test
	void removeFully() {
		c.add(ZERO);
		c.add(ZERO);
		
		assertAll(
				() -> assertEquals(2,c.size()),
				() -> assertTrue(c.removeFully(ZERO)),
				() -> assertEquals(0,c.size())
				);
	}

	// iterator

	@Test
	void iterElem() {
		for (int i = 0; i < 4; i++) {
			c.add(ZERO);
			c.add(ONE);
			c.add(TWO);
		}
		
		int count = 0;
		Iterator<Integer> iter = c.iterator();
		while (iter.hasNext()) {
			iter.next();
			count++;
		}
		assertEquals(12, count);
	}

	@Test
	void iterRemoveElem() {
		for (int i = 0; i < 4; i++) {
			c.add(ZERO);
			c.add(ONE);
			c.add(TWO);
		}
		
		Iterator<Integer> iter = c.iterator();
		while(iter.hasNext()) {
			iter.next();
			iter.remove();

		}
		
		assertAll(
			()-> assertEquals(0,c.size()),
			()-> assertEquals(0,c.sizeUnique()),
			() ->assertEquals(0,c.containsCount(ZERO))
			);	
	}

}
