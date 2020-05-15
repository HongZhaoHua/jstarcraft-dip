package com.github.kilianB.datastructures;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 * A hash collection offering a O(1) performance on {@link #contains},
 * {@link #add} and {@link #remove} allowing to keep track of duplicate values.
 * <code>Null</code> values are permitted.
 * <p>
 * This collection specializes in remembering how often a equal value are added
 * and removed. It is backed by a {@link java.util.HashMap HashMap} whose key is
 * the added object and the value holds the count of the duplicate elements
 * present in this collection. A <code>add</code> call will increment the
 * counter while a call to <code>remove</code> will decrease it. If the count
 * reaches 0 (add and remove was called the same amount of time) the key is
 * fully removed.
 * <p>
 * 
 * Objects are considered duplicates if their equals method returns true.
 * <p>
 * 
 * Unlike regular a collection a call to <code>remove</code> will decrease the
 * count of elements by one and not fully remove the node if there are still
 * duplicates present in the collection. If desired call
 * {@link #removeFully(Object)} instead.
 * <p>
 * 
 * <b>NOTE: duplicates are detected based on equality. Due to the fact that the
 * count is mapped to the first object inserted any methods returning values i.e
 * {@link #toArray()} or iterators , will return objects pointing to the same
 * reference.</b>
 * 
 * Therefore the following scenario might happen:
 * 
 * <pre>
 *  {@code
 * Object o;
 * Object o1;
 * 
 * CountHashCollection h;
 * 
 * // Equality :  o.equals(o1) -> true
 * // Reference: (o == o1)     -> false
 * h.add(o);
 * h.add(o1);
 * 
 * h.size() = 2
 * h.sizeUnique() = 1
 * 
 * Iterator<Object> iter = h.iterator();
 * 
 * Object o2 = iter.next();
 * Object o3 = iter.next();
 *
 * o2.equals(o) -> True
 * o2.equals(o1) -> True
 * o2.equals(o3) -> True
 * o3.equals(o) -> True
 * o3.equals(o1) -> True
 * 
 * o == o2
 * o == o3
 * 
 * BUT!
 * o1 != o2
 * o1 != o3
 * }
 * </pre>
 * 
 * @author Kilian
 * @param <K> the type of elements in this collection
 * @since 1.1.0
 */
public class CountHashCollection<K> implements Collection<K> {

	// We could also back this array using an identity hashmap and a normal hashmap
	// this would allow us to keep the identitiy reference for duplicates. But
	// since this is not important at the moment go ahead and use this one.
	private final HashMap<K, Integer> hashMap;

	/** How many elements are present in this collection (including duplicates) */
	private int elementSize;

	// Allow fail fast iterator
	private int modCount = 0;

	public CountHashCollection() {
		this.hashMap = new HashMap<>();
	}

	public CountHashCollection(Collection<K> c) {
		this();
		this.addAll(c);
	}
	
	@Override
	public boolean add(K o) {
		if (hashMap.containsKey(o)) {
			hashMap.put(o, hashMap.get(o).intValue() + 1);
		} else {
			hashMap.put(o, 1);
		}
		elementSize++;
		modCount++;
		return true;
	}

	@Override
	public boolean contains(Object o) {
		return hashMap.containsKey(o);
	}

	public int containsCount(K key) {
		if (contains(key)) {
			return hashMap.get(key);
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {

		if (hashMap.containsKey(o)) {
			elementSize--;
			Integer count = hashMap.get(o);
			if (count.intValue() > 1) {
				hashMap.put((K) o, count.intValue() - 1);
				modCount++;
				return true;
			} else {
				hashMap.remove(o);
				modCount++;
				return true;
			}

		}
		return false;
	}

	public boolean removeFully(Object o) {
		if (hashMap.containsKey(o)) {
			Integer obsRemoved = hashMap.remove(o);
			elementSize -= obsRemoved.intValue();
			modCount++;
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * To retrieve the unique count of elements see {@link #sizeUnique()}
	 * </p>
	 * 
	 * @see #sizeUnique()
	 */
	@Override
	public int size() {
		return elementSize;
	}

	/**
	 * Returns the number of unique elements in this collection. If this collection
	 * contains more than Integer.MAX_VALUE elements, returns Integer.MAX_VALUE.
	 * 
	 * @return the number of unique elements
	 * @see #size()
	 * 
	 */
	public int sizeUnique() {
		return hashMap.size();
	}

	@Override
	public boolean isEmpty() {
		return hashMap.isEmpty();
	}

	// Todo issues with type erasure?
	@SuppressWarnings("unchecked")
	public K[] toArrayUnique() {
		Object[] values = hashMap.keySet().toArray();
		return (K[]) values;
	}
	
	
	public K[] toArrayUnique(K[] array) {
		return hashMap.keySet().toArray(array);
	}

	/**
	 * 
	 * Returns an array containing all of the elements in this collection. Duplicate
	 * values will be present the amount of times they were added. If you are only
	 * interested in unique elements see {@link #toArrayUnique}.
	 * <p>
	 * 
	 * <b>Returned duplicate objects all reference the first object added the the
	 * collection</b>
	 * <p>
	 * 
	 * No guarantee is given about the order of the elements, only that duplicate
	 * objects will be present next to each other.
	 * 
	 * The returned array will be "safe" in that no references to it are maintained
	 * by this collection. (In other words, this method must allocate a new array
	 * even if this collection is backed by an array).The caller is thus free to
	 * modify the returned array.
	 *
	 * @return an array, whose runtime component type is Object, containing all of
	 *         the elements in this collection
	 */
	@Override
	public Object[] toArray() {
		Object[] returnValues = new Object[elementSize];
		int curOffset = 0;
		for (Entry<K, Integer> entry : hashMap.entrySet()) {
			K object = entry.getKey();
			Integer count = entry.getValue();
			for (int i = 0; i < count.intValue(); i++) {
				returnValues[curOffset++] = object;
			}
		}
		return hashMap.entrySet().toArray();
	}

	/**
	 * Returns an array containing all of the elements in this collection. Duplicate
	 * values will be present the amount of times they were added. If you are only
	 * interested in unique elements see {@link #toArrayUnique}.
	 * <p>
	 * 
	 * <b>Returned duplicate objects all reference the first object added the the
	 * collection</b>
	 * <p>
	 * 
	 * No guarantee is given about the order of the elements, only that duplicate
	 * objects will be present next to each other.
	 * <p>
	 * 
	 * If this collection fits in the specified array with room to spare(i.e., the
	 * array has more elements than this collection), the element in the array
	 * immediately following the end of the collection is set to null.
	 * 
	 * The returned array will be "safe" in that no references to it are maintained
	 * by this collection. (In other words, this method must allocate a new array
	 * even if this collection is backed by an array).The caller is thus free to
	 * modify the returned array.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		T[] r = a.length >= elementSize ? a
				: (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), elementSize);

		int curOffset = 0;
		for (Entry<K, Integer> entry : hashMap.entrySet()) {
			K object = entry.getKey();
			Integer count = entry.getValue();
			for (int i = 0; i < count.intValue(); i++) {
				r[curOffset++] = (T) object;
			}
		}

		if (r.length > elementSize) {
			a[elementSize] = null;
		}
		return r;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!hashMap.containsKey(o))
				return false;
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends K> c) {
		boolean sucessfull = false;
		for (K key : c) {
			if (this.add(key)) {
				sucessfull = true;
			}
		}
		return sucessfull;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for (Object key : c) {
			if (this.remove(key)) {
				changed = true;
			}
		}
		return changed;
	}

	/**
	 * <b>Retain all fully removes keys which are present in the collection and not
	 * just one instance of the duplicate!</b>
	 * <p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		Iter iter = new Iter();
		boolean mofidied = false;
		while (iter.hasNext()) {
			K entry = iter.next();
			if (!c.contains(entry)) {
				iter.removeFully();
				mofidied = true;
			}
		}
		return mofidied;
	}

	@Override
	public Iterator<K> iterator() {
		return new Iter();
	}

	@Override
	public void clear() {
		hashMap.clear();
		elementSize = 0;
		modCount++;
	}

	@Override
	public String toString() {
		final int maxLen = 20;
		return "CountHashCollection " + (hashMap != null ? toString(hashMap.entrySet(), maxLen) : null);
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

	class Iter implements Iterator<K> {

		private int elementsPresent = 0;
		private int pointer = 0;
		private int mod = modCount;
		private int arrayPointer = -1;
		private Object[] entryArray = hashMap.entrySet().toArray(new Object[0]);

		// We can't use an iterator due to fail fast.
		// Iterator<Entry<K, Integer>> internalIter = hashMap.entrySet().iterator();

		@Override
		public boolean hasNext() {
			checkModcount();
			return pointer < elementsPresent || arrayPointer + 1 < entryArray.length;
		}

		@SuppressWarnings("unchecked")
		@Override
		public K next() {
			checkModcount();
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			// Init
			if (arrayPointer == -1) {
				arrayPointer = 0;
				pointer = 0;
				elementsPresent = ((Entry<K, Integer>) entryArray[0]).getValue();
			} else if (pointer >= elementsPresent) {
				arrayPointer++;
				pointer = 0;
				elementsPresent = ((Entry<K, Integer>) entryArray[arrayPointer]).getValue();
				// entryArray[arrayPointer].getValue();
			}
			pointer++;
			return ((Entry<K, Integer>) entryArray[arrayPointer]).getKey();
		}

		@SuppressWarnings("unchecked")
		public void remove() {
			checkModcount();
			CountHashCollection.this.remove(((Entry<K, Integer>) entryArray[arrayPointer]).getKey());
			this.mod = modCount;
		}

		public void removeFully() {
			checkModcount();
			CountHashCollection.this.removeFully(((Entry<K, Integer>) entryArray[arrayPointer]).getKey());
			// Ignore all future elements in the array
			elementsPresent = 0;
			this.mod = modCount;
		}

		private void checkModcount() {
			if (mod != modCount)
				throw new ConcurrentModificationException();
		}

	}

}
