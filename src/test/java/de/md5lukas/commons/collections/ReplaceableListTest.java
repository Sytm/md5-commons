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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class ReplaceableListTest {

    static ReplaceableList list;

    @BeforeAll
    static void initializeList() {
        list = new ReplaceableList("A B C D", "F G H I", "A B C D", "F G H I");
    }

    @Test
    public void checkReplace() {
        assertIterableEquals(Arrays.asList("1 2-3 4", "F G 5 I", "1 2-3 4", "F G 5 I"),
                list.replace("A", "1", "B C", "2-3", "D", "4", "F G I", "Not here", "H", "5"));
    }
}
