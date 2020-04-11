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
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A helper list that supports pagination based on page size and page number
 *
 * @param <T> The type that is contained in the list
 */
public class PaginationList<T> extends ArrayList<T> {

    private final int itemsPerPage;

    /**
     * Creates a new PaginationList with the specified amount of items per page
     *
     * @param itemsPerPage The amount of items that should be on one page
     * @throws IllegalArgumentException If <code>itemsPerPage</code> is zero or lower
     */
    public PaginationList(int itemsPerPage) {
        checkArgument(itemsPerPage > 0, "The amount of items per page must be greater than zero, but was %d", itemsPerPage);
        this.itemsPerPage = itemsPerPage;
    }

    /**
     * Calculates the index in the list where the given page starts
     *
     * @param page The page number
     * @return The index where the page starts
     * @throws IllegalArgumentException If the page is out of bounds
     */
    public int pageStart(int page) {
        checkArgument(page >= 0 && page < pages(), "The provided page number is out of bounds of the available pages");
        return page * itemsPerPage;
    }

    /**
     * Calculates the index in the list where the given page ends
     *
     * @param page The page number
     * @return The index where the page ends
     * @throws IllegalArgumentException If the page is out of bounds
     */
    public int pageEnd(int page) {
        checkArgument(page >= 0 && page < pages(), "The provided page number is out of bounds of the available pages");
        int end = (page * itemsPerPage) + itemsPerPage;
        return Math.min(size(), end);
    }

    /**
     * Calculates the amount of pages this list has
     *
     * @return The amount of pages
     */
    public int pages() {
        return (int) Math.ceil((double) size() / (double) itemsPerPage);
    }

    /**
     * Gets the content of a page based on the number
     *
     * @param page The page number
     * @return The contents of the page
     * @throws IllegalArgumentException If the page is out of bounds
     * @see List#subList(int, int) This method is used to create the returned list, so do not moidfy the list
     */
    public List<T> page(int page) {
        if (size() == 0)
            return Collections.emptyList();
        return subList(pageStart(page), pageEnd(page));
    }
}