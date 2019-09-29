package de.md5lukas.commons.collections;

import java.util.HashMap;

public class HashMapBuilder<K, V> extends HashMap<K, V> {

	public HashMapBuilder(K key, V value) {
		this.put(key, value);
	}

	public HashMapBuilder<K, V> append(K key, V value) {
		this.put(key, value);
		return this;
	}
}
