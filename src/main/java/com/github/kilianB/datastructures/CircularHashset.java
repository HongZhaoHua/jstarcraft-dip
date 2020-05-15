package com.github.kilianB.datastructures;

import java.util.Collections;
import java.util.Set;

/**
 * 
 * @author Kilian
 * @since 1.1.0
 */
public class CircularHashset {

	private CircularHashset() {};
	
	public static <K> Set<K> create(int capacity){
		//return  Collections.newSetFromMap(new Base<K,Boolean>(capacity));
		return Collections.newSetFromMap(new CircularLinkedHashMap<K,Boolean>(capacity));
	}
	
//	private static class Base<K,V> extends LinkedHashMap<K,V>{
//		
//		private static final long serialVersionUID = 4853774520170918913L;
//		private int capacity = 0;
//		
//		public Base(int capacity) {
//			this.capacity = capacity;
//		}
//		
//		@Override
//		protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
//			return this.size() >= capacity;
//		}
//	}
}
