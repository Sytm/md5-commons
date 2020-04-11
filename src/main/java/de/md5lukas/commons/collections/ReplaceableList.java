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

import de.md5lukas.commons.text.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A list that allows batch replacement using {@link StringHelper#multiReplace(String, String, String, String...)} on every item,
 * creating a new list in the process
 */
public class ReplaceableList extends ArrayList<String> {

    /**
     * Creates a new replaceable list and puts all the strings into the list
     *
     * @param strings The strings to initialize this list with
     */
    public ReplaceableList(String... strings) {
        this.addAll(Arrays.asList(strings));
    }

    /**
     * Creates a new replaceable list and puts all the strings into the list
     *
     * @param strings The strings to initialize this list with
     */
    public ReplaceableList(Collection<String> strings) {
        super(strings);
    }

    /**
     * Creates a new list and the runs {@link StringHelper#multiReplace(String, String, String, String...)} on every list entry.
     *
     * @param target                 The first target
     * @param replacement            The first replacement
     * @param targetsAndReplacements All additional targets and replacements in alternating order
     * @return A list containing all entries with the replacements applied
     */
    public List<String> replace(String target, String replacement, String... targetsAndReplacements) {
        List<String> result = new ArrayList<>(size());
        for (String s : this) {
            result.add(StringHelper.multiReplace(s, target, replacement, targetsAndReplacements));
        }
        return result;
    }
}
