/*
 *     A collection of classes and methods designed for use in spigot plugins
 *     Copyright (C) 2020 Lukas Planz
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
