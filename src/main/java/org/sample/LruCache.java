/**
 * LruCache
 * 
 * A thread-safe key-value store with O(1) insert, retrieval, 
 * and removal of the least-recently-used item in the cache.
 * 
 * If a maximum size for the cache is defined at creation,
 * adding items beyond that maximum size will cause
 * the least-recently used items to be removed from the cache.
 */
package org.sample;

import java.util.HashMap;
import java.util.Map;

public class LruCache<K, T> {

	private Map<K, Node> map;
	private Node head;
	private Node tail;
	private int maxSize = -1;
	private int size = 0;
	private long hits = 0L;
	private long misses = 0L;

	/**
	 * Instantiates an LRU Cache with a maximum size of 2^32 - 1.
	 */
	public LruCache() {
		this.map = new HashMap<K, Node>();

		// dummy tail. This way we don't need to check for null every time
		this.tail = new Node(null, null);
		this.head = this.tail;
	}

	/**
	 * Instantiates an LRU Cache with the specified maximum size.
	 * @param maxSize - The number of elements beyond which, when a new element
	 * is added to the cache, the least-recently accessed element in the cache
	 * will be evicted.
	 */
	public LruCache(int maxSize) {
		if (maxSize < 1) {
			throw new IllegalArgumentException(
					"Max size must be a positive integer.");
		}

		this.map = new HashMap<K, Node>(maxSize);
		this.maxSize = maxSize;

		// dummy tail. This way we don't need to check for null every time
		this.tail = new Node(null, null);
		this.head = this.tail;
	}

	/**
	 * Place an element into the cache.
	 * If a maximum size has been defined and the cache is full, 
	 * the least-recently accessed element will be removed to make space.
	 * 
	 * If the given key already exists in the cache, the key will not be
	 * re-added, and this method will fail silently.
	 * 
	 * @param key - Identification key for the current element
	 * @param value - The element to place in the cache
	 */
	public void add(K key, T value) {

		final Node node = new Node(key, value);

		synchronized(this) {
			
			if (this.map.containsKey(key)) {
				
				return;
				
			} else {
				if (maxSize > 0 && this.size == maxSize) {
					this.evict();
				}

				map.put(key, node);
				node.setNext(this.head);
				this.head = node;
				this.size += 1;	
				
			}			
		}
	}

	/**
	 * Retrieve an element from the cache. If the item is not
	 * present in the cache, a value of null is returned.
	 * @param key - Identification key for the requested item.
	 * @return The cached element referenced by the given key, or null if the
	 * element is not present in the cache.
	 */
	public T get(K key) {
		T value = null;

		synchronized(this) {
			final Node node = this.map.get(key);
			
			if (node == null) {
				misses++;
				return null;
			}
			
			hits++;

			// remove node from its current place in the list
			final Node prev = node.getPrev();
			if (prev != null) {
				prev.setNext(node.getNext());
			}

			// place it at the front
			node.setNext(this.head);
			this.head = node;
			value = node.getValue();
		}

		return value;
	}

	/**
	 * Removes the least-recently used element from the cache.
	 */
	public void evict() {
		synchronized(this) {

			if (this.size <= 0) {
				return;
			}

			final Node node = this.tail.getPrev();
			if (node != null) {
				node.getPrev().setNext(this.tail);
			}
			this.map.remove(node.getKey());
			this.size -= 1;
		}
	}

	/**
	 * Getter for the current size of the cache.
	 * @return
	 */
	public int getSize() {
		synchronized(this) {
			return this.size;
		}
	}

	/**
	 * Getter for the cache's maximum size.
	 * @return
	 */
	public int getMaxSize() {
		return this.maxSize;
	}
	
	/**
	 * Counter for how many times a hot cache was hit.
	 * @return
	 */
	public long getHits() {
		return this.hits;
	}
	
	/**
	 * Counter for how many times a cold cache was hit.
	 * @return
	 */
	public long getMisses() {
		return this.misses;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("LruCache: ");
		
		synchronized(this) {
	
			if (this.size == 0) {
				sb.append("[Empty]");
			} else {
				Node node = this.head;
				while (node.getValue() != null) {
					sb.append(node.getKey().toString());
	
					node = node.getNext();
					if (node.getValue() != null) {
						sb.append(" > ");
					}
				}
			}
		}
		return sb.toString();
	}

	private class Node {
		private K key;
		private T val;
		private Node next;
		private Node prev;

		public Node(K key, T value) {
			this.key = key;
			this.val = value;
		}
		public T getValue() {
			return this.val;
		}
		public K getKey() {
			return this.key;
		}
		public Node getNext() {
			return this.next;
		}
		public void setNext(Node next) {
			this.next = next;
			if (next != null && next.getPrev() != this) {
				next.setPrev(this);
			}
		}
		public Node getPrev() {
			return this.prev;
		}
		public void setPrev(Node prev) {
			this.prev = prev;
			if (prev != null && prev.getNext() != this) {
				prev.setNext(this);
			}
		}
	}
}