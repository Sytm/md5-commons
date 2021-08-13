package de.md5lukas.commons.collections;

import java.util.HashMap;

/**
 * A simple class that is based on {@link HashMap} to create them in a one-liner fashion
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public class HashMapBuilder<K, V> extends HashMap<K, V> {

    /**
     * Creates a new builder instance and directly adds a key / value pair
     *
     * @param key   The first key to add
     * @param value The first value to add
     */
    public HashMapBuilder(K key, V value) {
        this.put(key, value);
    }

    /**
     * Adds an additional key / value pair
     *
     * @param key   The key to add
     * @param value The value to add
     * @return <code>this</code> for a builder-like usage
     */
    public HashMapBuilder<K, V> append(K key, V value) {
        this.put(key, value);
        return this;
    }
}
