package org.sample;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LruCacheTest {

	@Test
	void testAdd() {
		final LruCache<String, String> cache = new LruCache<>(5);
		cache.add("foo", "bar");
		assertEquals(1, cache.getSize());
		assertEquals("bar", cache.get("foo"));
		cache.add("animal", "dog");
		cache.add("food", "pizza");
		cache.add("sport", "soccer");
		cache.add("instrument", "guitar");
		cache.add("color", "red");
		
		// don't go over max
		assertEquals(5, cache.getSize());
		// evict the oldest members as we go
		assertNull(cache.get("foo"));
	}

	@Test
	void testGet() {
		final LruCache<String, String> cache = new LruCache<>(5);
		cache.add("foo", "bar");
		final String foo = cache.get("foo");
		assertEquals("bar", foo);
	}

	@Test
	void testEvict1() {
		final LruCache<String, String> cache = new LruCache<>(5);
		cache.add("a", "a");
		cache.add("b", "b");
		cache.add("c", "c");
		cache.evict();
		assertNull(cache.get("a"));
		assertNotNull(cache.get("b"));
		assertNotNull(cache.get("c"));
	}
	
	@Test
	void testEvict2() {
		final LruCache<String, String> cache = new LruCache<>(5);
		cache.add("a", "a");
		cache.add("b", "b");
		cache.add("c", "c");
		cache.get("a");

		cache.evict();
		assertNull(cache.get("b"));
		assertNotNull(cache.get("a"));
		assertNotNull(cache.get("c"));
	}

	@Test
	void testGetSize_empty() {
		final LruCache<String, String> cache = new LruCache<>(10);
		assertEquals(0, cache.getSize());
	}

	@Test
	void testGetSize_2() {
		final LruCache<String, String> cache = new LruCache<>(10);
		cache.add("foo", "bar");
		assertEquals(1, cache.getSize());
		cache.add("baz", "bat");
		assertEquals(2, cache.getSize());
	}

	@Test
	void testGetMaxSize_unlimited() {
		final LruCache<String, String> cache = new LruCache<>();
		assertEquals(-1, cache.getMaxSize());
	}
	
	@Test
	void testGetMaxSize_100() {
		final LruCache<String, String> cache = new LruCache<>(100);
		assertEquals(100, cache.getMaxSize());
	}

	@Test
	void testToString_empty() {
		final LruCache<String, String> cache = new LruCache<>(4);
		assertEquals("LruCache: [Empty]", cache.toString());
	}

	@Test
	void testToString_single_item() {
		final LruCache<String, String> cache = new LruCache<>(4);
		cache.add("a", "a");
		assertEquals("LruCache: a", cache.toString());
	}
	
	@Test
	void testToString_many_items() {
		final LruCache<String, String> cache = new LruCache<>(4);
		cache.add("a", "a");
		cache.add("b", "b");
		cache.add("c", "c");
		cache.add("d", "d");
		assertEquals("LruCache: d > c > b > a", cache.toString());
	}

}
