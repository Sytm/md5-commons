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

package de.md5lukas.commons.uuid;

import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Helper class that contains the settings for the cache used in {@link UUIDUtils}
 */
public final class UUIDCacheSettings {

    private long maxSize;
    private long expireAfterWrite;
    private TimeUnit expireAfterWriteTimeUnit;

    /**
     * Creates a new settings object with the following default values:
     * <ul>
     *     <li>maxSize = {@link Bukkit#getMaxPlayers()}</li>
     *     <li>expireAfterWrite = 2</li>
     *     <li>expireAfterWriteTimeUnit = {@link TimeUnit#HOURS}</li>
     * </ul>
     */
    public UUIDCacheSettings() {
        this.maxSize = Bukkit.getMaxPlayers();
        this.expireAfterWrite = 2;
        this.expireAfterWriteTimeUnit = TimeUnit.HOURS;
    }

    /**
     * @return The max cache size
     */
    public long getMaxSize() {
        return maxSize;
    }

    /**
     * Updates the max size to the new value
     *
     * @param maxSize The new max size
     * @return <code>this</code> for builder-like usage
     * @throws IllegalArgumentException If maxSize is negative
     */
    public UUIDCacheSettings setMaxSize(long maxSize) {
        checkArgument(maxSize >= 0, "The max size cannot be negative, but was %d", maxSize);
        this.maxSize = maxSize;
        return this;
    }

    /**
     * The amount of {@link TimeUnit TimeUnits} that make up the amount of time a entry is still valid after it has been added
     *
     * @return The amount that is set
     * @see com.google.common.cache.CacheBuilder#expireAfterWrite(long, TimeUnit)
     */
    public long getExpireAfterWrite() {
        return expireAfterWrite;
    }

    /**
     * Updates the expire after write amount to the new value
     *
     * @param expireAfterWrite The new expire after write amount
     * @return <code>this</code> for a builder-like usage
     * @throws IllegalArgumentException If expireAfterWrite is negative
     */
    public UUIDCacheSettings setExpireAfterWrite(long expireAfterWrite) {
        checkArgument(expireAfterWrite >= 0, "The expire after write amount cannot be negative, but was %d", expireAfterWrite);
        this.expireAfterWrite = expireAfterWrite;
        return this;
    }

    /**
     * The {@link TimeUnit} that should be used for the expire after write amount
     *
     * @return The TimeUnit for the expire after write time
     * @see com.google.common.cache.CacheBuilder#expireAfterWrite(long, TimeUnit)
     */
    public TimeUnit getExpireAfterWriteTimeUnit() {
        return expireAfterWriteTimeUnit;
    }

    /**
     * Updates the expire after write time unit to the new value
     *
     * @param expireAfterWriteTimeUnit The new expire after write time unit
     * @return <code>this</code> for a builder-like usage
     * @throws NullPointerException If expireAfterWriteTimeUnit is null
     */
    public UUIDCacheSettings setExpireAfterWriteTimeUnit(TimeUnit expireAfterWriteTimeUnit) {
        this.expireAfterWriteTimeUnit = checkNotNull(expireAfterWriteTimeUnit, "The expire after write time unit cannot be null");
        return this;
    }
}
