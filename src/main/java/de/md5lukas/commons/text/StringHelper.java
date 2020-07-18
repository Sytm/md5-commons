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

package de.md5lukas.commons.text;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.*;

public final class StringHelper {

    private static final Pattern MC_USERNAME_PATTERN = Pattern.compile("^\\w{3,16}$");

    /**
     * Repeats the provided string n times
     *
     * @param string The string that should be repeated
     * @param n      The amount of times the string should be repeated
     * @return The final string
     * @throws NullPointerException     If the provided string is null
     * @throws IllegalArgumentException If n is negative
     */
    public static String repeatString(String string, int n) {
        checkNotNull(string, "The string to repeat cannot be null");
        checkArgument(n >= 0, "n cannot be negative");
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < n; ++j)
            sb.append(string);
        return sb.toString();
    }

    /**
     * A simple method to replace chained {@link String#replace(CharSequence, CharSequence)} calls into a single method.
     * <br><br>
     * After the string parameter, the parameters are alternating between target and replacement and can continue indefinitely
     *
     * @param string                 The string where the replacement should be applied to
     * @param target                 The first target
     * @param replacement            The first replacement
     * @param targetsAndReplacements All additional targets and replacements in alternating order
     * @return The string where all the replacements have been applied to
     * @throws NullPointerException     If the string, or any of the targets or replacements are null
     * @throws IllegalArgumentException If the size of the targetsAndReplacements is not even
     */
    public static String multiReplace(String string, String target, String replacement, String... targetsAndReplacements) {
        checkArgument(targetsAndReplacements.length % 2 == 0, "The amount targets and replacements need to be even");

        string = string.replace(checkNotNull(target, "The first target cannot be null"), checkNotNull(replacement, "The first replacement cannot be null"));
        for (int i = 0; i < targetsAndReplacements.length; i += 2) {
            string = string.replace(
                    checkNotNull(targetsAndReplacements[i], "The target at index %d cannot be null", i),
                    checkNotNull(targetsAndReplacements[i + 1], "The replacement at index %d cannot be null", i + 1));
        }
        return string;
    }

    /**
     * Joins the string array with spaces starting at the specified offset
     *
     * @param array  The array to join
     * @param offset Where to start joining the string
     * @return The string that has been joined from the array
     * @throws NullPointerException      If the array is null
     * @throws IndexOutOfBoundsException If the offset is negative or not less than the size of the array
     */
    public static String buildStringFromArray(String[] array, int offset) {
        checkNotNull(array, "The array to build a string from cannot be null");
        checkElementIndex(offset, array.length, "The index where the building should start is out of bounds");
        StringJoiner sj = new StringJoiner(" ");
        for (int i = offset; i < array.length; i++) {
            sj.add(array[i]);
        }
        return sj.toString();
    }

    /**
     * Checks if a string is a valid Minecraft username
     *
     * @param string The string to check
     * @return <code>true</code> if the name is a valid username
     * @throws NullPointerException If the provided string is null
     */
    public static boolean isPlayerName(String string) {
        return MC_USERNAME_PATTERN.matcher(checkNotNull(string, "The string to check cannot be null")).matches();
    }

    /**
     * Limits the line length of the input string by splitting it up between words
     *
     * @param input         The input string that should be split up
     * @param maxLineLength The maximum line length in characters
     * @return A list that contains strings separated by line
     * @throws NullPointerException     If the input string is null
     * @throws IllegalArgumentException If the maxLineLength is negative
     */
    public static List<String> wordWrap(String input, int maxLineLength) {
        checkNotNull(input, "The input string cannot be null");
        checkArgument(maxLineLength >= 0, "The max line length cannot be negative");

        String[] words = input.trim().split("\\s+");
        StringBuilder currentLine = new StringBuilder();
        List<String> result = new ArrayList<>();

        for (String word : words) {
            if (currentLine.length() + word.length() > maxLineLength) {
                result.add(currentLine.toString());
                currentLine.setLength(0);
            }
            if (currentLine.length() > 0)
                currentLine.append(" ");
            currentLine.append(word);
        }

        if (currentLine.length() > 0) {
            result.add(currentLine.toString());
        }

        return result;
    }

    /**
     * Removes the suffix from the provided string based on the length of the suffix
     *
     * @param string The string where the suffix should be removed from
     * @param suffix The suffix string to remove
     * @return The string with the suffix removed
     * @throws NullPointerException     If the string or suffix is null
     * @throws IllegalArgumentException If the suffix is longer than the string
     */
    public static String removeSuffix(String string, String suffix) {
        checkNotNull(string, "The string cannot be null");
        checkNotNull(suffix, "The suffix cannot be null");
        checkArgument(string.length() >= suffix.length(), "The suffix is longer than the string where it should be removed");
        String result = string.substring(0, string.length() - suffix.length());
        return result;
    }

    /**
     * Removes the prefix from the provided string based on the length of the prefix
     *
     * @param string The string where the suffix should be removed from
     * @param prefix The prefix string to remove
     * @return The string with the suffix removed
     * @throws NullPointerException     If the string or prefix is null
     * @throws IllegalArgumentException If the prefix is longer than the string
     */
    public static String removePrefix(String string, String prefix) {
        checkNotNull(string, "The string cannot be null");
        checkNotNull(prefix, "The prefix cannot be null");
        checkArgument(string.length() >= prefix.length(), "The prefix is longer than the string where it should be removed");
        return string.substring(prefix.length());
    }
}