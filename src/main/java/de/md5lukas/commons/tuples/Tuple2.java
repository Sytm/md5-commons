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

package de.md5lukas.commons.tuples;

/**
 * Simple tuple to pass two values within one object
 *
 * @param <L> The type of the left value
 * @param <R> The type of the right value
 */
public final class Tuple2<L, R> {

    private final L l;
    private final R r;

    private Tuple2(L l, R r) {
        this.l = l;
        this.r = r;
    }

    /**
     * @return The left value
     */
    public L getL() {
        return l;
    }

    /**
     * @return The right value
     */
    public R getR() {
        return r;
    }

    /**
     * Creates a new tuple object with the two given values
     *
     * @param l   The left value
     * @param r   The right value
     * @param <L> The left type
     * @param <R> The right type
     * @return The created tuple object
     */
    public static <L, R> Tuple2<L, R> of(L l, R r) {
        return new Tuple2<>(l, r);
    }
}
