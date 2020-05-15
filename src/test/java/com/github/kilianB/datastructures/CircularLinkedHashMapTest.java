package com.github.kilianB.datastructures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
class CircularLinkedHashMapTest {

	@Test
	void invalidConstructorZero() {
		assertThrows(IllegalArgumentException.class, () -> {
			new CircularLinkedHashMap<>(0);
		});
	}

	@Test
	void invalidConstructorNegative() {
		assertThrows(IllegalArgumentException.class, () -> {
			new CircularLinkedHashMap<>(-1);
		});
	}

	@Test
	void preservesOrder() {
		CircularLinkedHashMap<Integer, Integer> map = new CircularLinkedHashMap<>(4);

		for (int i = 0; i < 4; i++) {
			map.put(Integer.valueOf(i), Integer.valueOf(i));
		}

		Set<Entry<Integer, Integer>> entries = map.entrySet();

		int i = 0;
		for (Entry<Integer, Integer> entry : entries) {
			assertEquals(Integer.valueOf(i), entry.getKey(), "Key not as expected");
			assertEquals(Integer.valueOf(i), entry.getValue(), "Value not as expected");
			i++;
		}
	}

	@Test
	void maxSize() {
		CircularLinkedHashMap<Integer, Integer> map = new CircularLinkedHashMap<>(4);
		for (int i = 0; i < 5; i++) {
			map.put(Integer.valueOf(i), Integer.valueOf(i));
		}
		assertEquals(4, map.size());
	}

	@Test
	void removeOldestValues() {
		CircularLinkedHashMap<Integer, Integer> map = new CircularLinkedHashMap<>(4);

		for (int i = 0; i < 7; i++) {
			map.put(Integer.valueOf(i), Integer.valueOf(i));
		}
		Set<Entry<Integer, Integer>> entries = map.entrySet();
		int i = 3;
		for (Entry<Integer, Integer> entry : entries) {
			assertEquals(Integer.valueOf(i), entry.getKey(), "Key not as expected");
			assertEquals(Integer.valueOf(i), entry.getValue(), "Value not as expected");
			i++;
		}
	}

}
