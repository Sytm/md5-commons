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
