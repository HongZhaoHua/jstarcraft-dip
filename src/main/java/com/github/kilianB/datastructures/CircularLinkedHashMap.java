package com.github.kilianB.datastructures;

import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Spliterator;
import java.util.TreeMap;

import com.github.kilianB.Require;

/**
 * <p>
 * A linked hashmap only keeping the newest n entries. Useful for caching.
 * 
 * <p>
 * Hash table and linked list implementation of the <code>Map</code> interface, with
 * predictable iteration order. This implementation differs from
 * <code>HashMap</code> in that it maintains a doubly-linked list running through
 * all of its entries. This linked list defines the iteration ordering, which is
 * normally the order in which keys were inserted into the map
 * (<i>insertion-order</i>). Note that insertion order is not affected if a key
 * is <i>re-inserted</i> into the map. (A key <code>k</code> is reinserted into a
 * map <code>m</code> if <code>m.put(k, v)</code> is invoked when
 * <code>m.containsKey(k)</code> would return <code>true</code> immediately prior to the
 * invocation.)
 *
 * <p>
 * This implementation spares its clients from the unspecified, generally
 * chaotic ordering provided by {@link HashMap} (and {@link Hashtable}), without
 * incurring the increased cost associated with {@link TreeMap}. It can be used
 * to produce a copy of a map that has the same order as the original,
 * regardless of the original map's implementation:
 * 
 * <pre>
 *     void foo(Map m) {
 *         Map copy = new LinkedHashMap(m);
 *         ...
 *     }
 * </pre>
 * 
 * This technique is particularly useful if a module takes a map on input,
 * copies it, and later returns results whose order is determined by that of the
 * copy. (Clients generally appreciate having things returned in the same order
 * they were presented.)
 *
 * <p>
 * This class provides all of the optional <code>Map</code> operations, and permits
 * null elements. Like <code>HashMap</code>, it provides constant-time performance
 * for the basic operations (<code>add</code>, <code>contains</code> and
 * <code>remove</code>), assuming the hash function disperses elements properly
 * among the buckets. Performance is likely to be just slightly below that of
 * <code>HashMap</code>, due to the added expense of maintaining the linked list,
 * with one exception: Iteration over the collection-views of a
 * <code>LinkedHashMap</code> requires time proportional to the <i>size</i> of the
 * map, regardless of its capacity. Iteration over a <code>HashMap</code> is likely
 * to be more expensive, requiring time proportional to its <i>capacity</i>.
 *
 * <p>
 * A linked hash map has two parameters that affect its performance: <i>initial
 * capacity</i> and <i>load factor</i>. They are defined precisely as for
 * <code>HashMap</code>. Note, however, that the penalty for choosing an excessively
 * high value for initial capacity is less severe for this class than for
 * <code>HashMap</code>, as iteration times for this class are unaffected by
 * capacity.
 *
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> If
 * multiple threads access a linked hash map concurrently, and at least one of
 * the threads modifies the map structurally, it <em>must</em> be synchronized
 * externally. This is typically accomplished by synchronizing on some object
 * that naturally encapsulates the map.
 *
 * If no such object exists, the map should be "wrapped" using the
 * {@link Collections#synchronizedMap Collections.synchronizedMap} method. This
 * is best done at creation time, to prevent accidental unsynchronized access to
 * the map:
 * 
 * <pre>
 *   Map m = Collections.synchronizedMap(new LinkedHashMap(...));
 * </pre>
 *
 * A structural modification is any operation that adds or deletes one or more
 * mappings or, in the case of access-ordered linked hash maps, affects
 * iteration order. In insertion-ordered linked hash maps, merely changing the
 * value associated with a key that is already contained in the map is not a
 * structural modification. <strong>In access-ordered linked hash maps, merely
 * querying the map with <code>get</code> is a structural modification. </strong>)
 *
 * <p>
 * The iterators returned by the <code>iterator</code> method of the collections
 * returned by all of this class's collection view methods are
 * <em>fail-fast</em>: if the map is structurally modified at any time after the
 * iterator is created, in any way except through the iterator's own
 * <code>remove</code> method, the iterator will throw a
 * {@link ConcurrentModificationException}. Thus, in the face of concurrent
 * modification, the iterator fails quickly and cleanly, rather than risking
 * arbitrary, non-deterministic behavior at an undetermined time in the future.
 *
 * <p>
 * Note that the fail-fast behavior of an iterator cannot be guaranteed as it
 * is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification. Fail-fast iterators throw
 * <code>ConcurrentModificationException</code> on a best-effort basis. Therefore,
 * it would be wrong to write a program that depended on this exception for its
 * correctness: <i>the fail-fast behavior of iterators should be used only to
 * detect bugs.</i>
 *
 * <p>
 * The spliterators returned by the spliterator method of the collections
 * returned by all of this class's collection view methods are
 * <em><a href="Spliterator.html#binding">late-binding</a></em>,
 * <em>fail-fast</em>, and additionally report {@link Spliterator#ORDERED}.
 *
 * <p>
 * This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html"> Java
 * Collections Framework</a>.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * 
 * @author Kilian
 * 
 * @since 1.0.0
 */
public class CircularLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = 1L;

	private int bufferSize = 1 << 4; // HashMap.DEFAULT_INITIAL_CAPACITY;

	/**
	 * Constructs an empty insertion-ordered <code>CircularLinkedHashMap</code>
	 *
	 * @param bufferSize the initial capacity. When exceeding the capacity the
	 *                   oldest element will be removed
	 * @throws IllegalArgumentException if the initial capacity is negative
	 */
	public CircularLinkedHashMap(int bufferSize) {
		this(bufferSize, false);
		validatePreCondition();
	}

	/**
	 * Constructs an empty <code>CircularLinkedHashMap</code> instance
	 *
	 * @param bufferSize  the initial capacity. When exceeding the capacity the
	 *                    oldest element will be removed
	 * @param accessOrder the ordering mode - <code>true</code> for access-order,
	 *                    <code>false</code> for insertion-order
	 * @throws IllegalArgumentException if the bufferSize is non positive
	 */
	public CircularLinkedHashMap(int bufferSize, boolean accessOrder) {
		super(bufferSize + 1, 1f, accessOrder);
		this.bufferSize = bufferSize;
		validatePreCondition();
	}

	/**
	 * <p>
	 * Constructs an insertion-ordered <code>CircularLinkedHashMap</code> instance with
	 * the same mappings as the specified map. The <code>CircularLinkedHashMap</code>
	 * instance is created with a default buffer size of 12.
	 * 
	 * <p>
	 * If a different buffer size is required use another constructor and invoke the
	 * putAll() method to batch insert all elements. Be aware that this method is
	 * only deterministic for maps with a specified order. Else random elements may
	 * be present in this hashmap after the insertion is finished depending on the
	 * order of the elements returned by the itterator.
	 *
	 *
	 * @param m the map whose mappings are to be placed in this map
	 * @throws NullPointerException if the specified map is null
	 */
	public CircularLinkedHashMap(Map<? extends K, ? extends V> m) {
		super(m);
		this.bufferSize = m.size();
		validatePreCondition();
	}

	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > bufferSize;
	}

	private void validatePreCondition() {
		Require.positiveValue(this.bufferSize, "Buffer size has to be positive");
	}
}