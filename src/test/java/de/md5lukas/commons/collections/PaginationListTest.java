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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class PaginationListTest {

    PaginationList<Integer> list;

    @BeforeEach
    void createList() {
        list = new PaginationList<>(2);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
    }

    @Test
    public void checkPageCalculation() {
        assertEquals(3, list.pages());
        list.add(7);
        assertEquals(4, list.pages());
        list.add(8);
        assertEquals(4, list.pages());
    }

    @Test
    public void checkNegativePage() {
        assertThrows(IllegalArgumentException.class, () -> {
            list.page(-1);
        });
    }

    @Test
    public void checkTooBigPage() {
        assertThrows(IllegalArgumentException.class, () -> {
            list.page(4);
        });
    }

    @Test
    public void checkPageContents() {
        assertIterableEquals(Arrays.asList(1, 2), list.page(0));
        assertIterableEquals(Arrays.asList(3, 4), list.page(1));
        assertIterableEquals(Arrays.asList(5, 6), list.page(2));
    }
}
