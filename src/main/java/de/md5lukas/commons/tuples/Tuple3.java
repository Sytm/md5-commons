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
 * Simple tuple to pass three values within one object
 *
 * @param <L> The type of the left value
 * @param <M> The type of the middle value
 * @param <R> The type of the right value
 */
public final class Tuple3<L, M, R> {

    private final L l;
    private final M m;
    private final R r;

    private Tuple3(L l, M m, R r) {
        this.l = l;
        this.m = m;
        this.r = r;
    }

    /**
     * @return The left value
     */
    public L getL() {
        return l;
    }

    /**
     * @return The middle value
     */
    public M getM() {
        return m;
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
     * @param m   The middle value
     * @param r   The right value
     * @param <L> The left type
     * @param <M> The middle type
     * @param <R> The right type
     * @return The created tuple object
     */
    public static <L, M, R> Tuple3<L, M, R> of(L l, M m, R r) {
        return new Tuple3<>(l, m, r);
    }
}
