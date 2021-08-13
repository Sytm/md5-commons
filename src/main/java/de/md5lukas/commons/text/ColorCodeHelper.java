package de.md5lukas.commons.text;

import com.google.common.base.Preconditions;
import net.md_5.bungee.api.ChatColor;

import java.util.HashSet;
import java.util.Set;

/**
 * A class containing helper functions regarding color codes in strings
 */
public final class ColorCodeHelper {

    /**
     * Extracts the last color and effects applied to the provided string
     *
     * @param input The string that should be processed
     * @return A string containing a combination of color and effect codes to apply them else where
     * @throws NullPointerException If the input string is null
     */
    public static String getLastChatColor(String input) {
        Preconditions.checkNotNull(input, "The input string cannot be null");
        Set<Character> lastEffects = new HashSet<>(5);
        char lastColor = Character.MIN_VALUE;

        boolean isCode = false;
        for (char c : input.toCharArray()) {
            if (ChatColor.COLOR_CHAR == c) {
                isCode = true;
                continue;
            }
            if (isCode) {
                if (isEffect(c)) {
                    lastEffects.add(c);
                } else if (isColor(c)) {
                    lastColor = c;
                    lastEffects.clear();
                } else if (isReset(c)) {
                    lastEffects.clear();
                    lastColor = Character.MIN_VALUE;
                }
                isCode = false;
            }
        }
        StringBuilder sb = new StringBuilder(((lastColor == Character.MIN_VALUE ? 0 : 1) + lastEffects.size()) * 2);
        if (lastColor != Character.MIN_VALUE)
            sb.append(ChatColor.COLOR_CHAR).append(lastColor);
        for (char c : lastEffects)
            sb.append(ChatColor.COLOR_CHAR).append(c);
        return sb.toString();
    }

    private static boolean isColor(char color) {
        switch (ChatColor.getByChar(color)) {
            case BOLD:
            case ITALIC:
            case MAGIC:
            case RESET:
            case STRIKETHROUGH:
            case UNDERLINE:
                return false;
            default:
                return true;
        }
    }

    private static boolean isEffect(char color) {
        switch (ChatColor.getByChar(color)) {
            case BOLD:
            case ITALIC:
            case MAGIC:
            case RESET:
            case UNDERLINE:
                return true;
            default:
                return false;
        }
    }

    private static boolean isReset(char color) {
        return ChatColor.RESET.toString().charAt(1) == color;
    }
}
