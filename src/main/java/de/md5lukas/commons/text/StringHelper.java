package de.md5lukas.commons.text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class StringHelper {

    private static final Pattern MC_USERNAME_PATTERN = Pattern.compile("^\\w{3,16}$");

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
        return string.substring(0, string.length() - suffix.length());
    }
}