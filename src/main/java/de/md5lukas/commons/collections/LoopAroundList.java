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

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * A simple class that will present a cutout based on the index and will, if required loop around
 * <br>
 * Example:
 * <pre>{@literal
 * |1 2 3 4 5 6 7 8|
 * |      <----->  |}
 * {@link #next()}{@literal
 * |1 2 3 4 5 6 7 8|
 * |        <----->|}
 * {@link #next()}{@literal
 * |1 2 3 4 5 6 7 8|
 * |>         <----|}
 * {@link #next()}{@literal
 * |1 2 3 4 5 6 7 8|
 * |-->         <--|}
 * </pre>
 *
 * @param <T> The type of the list
 */
public class LoopAroundList<T> extends ArrayList<T> {

    private final int cutOutSize;
    private int index;

    /**
     * Creates a new instance of this list with the specified cutout size
     *
     * @param cutOutSize The cutout size to use
     * @throws IllegalArgumentException If cutout size is equal or lower than zero
     */
    public LoopAroundList(int cutOutSize) {
        checkArgument(cutOutSize > 0, "The cutout size needs to be greater than zero, but was %d", cutOutSize);
        this.cutOutSize = cutOutSize;
        this.index = 0;
    }

    /**
     * Moves the cutout one forward
     *
     * @return The current index
     */
    public int next() {
        return move(1);
    }

    /**
     * Moves the cutout one backwards
     *
     * @return The current index
     */
    public int previous() {
        return move(-1);
    }

    /**
     * Moves the cutout the specified amount. The value may be negative
     *
     * @param steps The amount of steps to move
     * @return The current index
     */
    public int move(int steps) {
        return setIndex(index + steps);
    }

    /**
     * Sets the index of the cutout start to the given value
     *
     * @param index The new index
     * @return The current index corrected for wrap-around
     */
    public int setIndex(int index) {
        this.index = index;
        while (this.index > size()) {
            this.index -= size();
        }
        while (this.index < 0) {
            this.index += size();
        }
        return this.index;
    }

    /**
     * @return The values that are currently in the cutout
     */
    public List<T> getCutOut() {
        ArrayList<T> result = new ArrayList<>();
        for (int i = 0; i < cutOutSize; i++) {
            int actualIndex = i - index;
            actualIndex = actualIndex >= 0 ? actualIndex : actualIndex + size();
            result.add(get(actualIndex));
        }
        return result;
    }
}
