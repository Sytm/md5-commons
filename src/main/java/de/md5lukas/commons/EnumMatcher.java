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

package de.md5lukas.commons;

import java.util.regex.Pattern;

/**
 * Get a value from a enum based on its name, disregarding the case. Useful when trying to get an enum value based on a config value
 */
public final class EnumMatcher {

    private static final Pattern STRIP_STRING = Pattern.compile("\\s|_");

    /**
     * Tries to match a value of the enum based on its name, ignoring the casing, spaces and underscores
     *
     * @param clazz The class of the enum
     * @param name  The name that should be matched
     * @param <E>   The enum type
     * @return The matched enum value or null if the name is null or the value could not be found
     */
    public static <E extends Enum<E>> E valueOf(Class<E> clazz, String name) {
        if (clazz == null || name == null)
            return null;
        name = STRIP_STRING.matcher(name).replaceAll("");
        for (E e : clazz.getEnumConstants()) {
            if (STRIP_STRING.matcher(e.name()).replaceAll("").equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
