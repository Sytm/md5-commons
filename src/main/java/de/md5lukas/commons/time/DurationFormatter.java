package de.md5lukas.commons.time;

import com.google.common.base.Preconditions;
import de.md5lukas.commons.TriFunction;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

/**
 * This class contains methods that help one to format durations from millisecond values into a human readable format
 */
public final class DurationFormatter {

    private static TriFunction<Player, TimeUnit, Boolean, String> pluralizationHelper;

    static {
        pluralizationHelper = (player, timeUnit, isPlural) -> {
            switch (timeUnit) {
                case SECONDS:
                    return isPlural ? "seconds" : "second";
                case MINUTES:
                    return isPlural ? "minutes" : "minute";
                case HOURS:
                    return isPlural ? "hours" : "hour";
                case DAYS:
                    return isPlural ? "days" : "day";
                default:
                    throw new UnsupportedOperationException("The TimeUnit " + timeUnit + " is not supported by this method!");
            }
        };
    }

    /**
     * Formats the given duration into a human readable format
     *
     * @param player The (optional) player to use for localization of the time unit names
     * @param millis The duration to format in milliseconds
     * @return The formatted duration
     * @throws IllegalArgumentException If the provided millisecond value is negative
     */
    public static String formatDuration(Player player, long millis) {
        Preconditions.checkArgument(millis >= 0, "The duration to convert to a string must be positive");
        StringBuilder builder = new StringBuilder();
        long days = MILLISECONDS.toDays(millis);
        if (days > 0) {
            builder.append(days).append(' ').append(pluralizationHelper(player, days, DAYS));
            millis -= DAYS.toMillis(days);
        }
        long hours = MILLISECONDS.toHours(millis);
        if (hours > 0) {
            if (days > 0)
                builder.append(' ');
            builder.append(hours).append(' ').append(pluralizationHelper(player, hours, HOURS));
            millis -= HOURS.toMillis(hours);
        }
        long minutes = MILLISECONDS.toMinutes(millis);
        if (minutes > 0) {
            if (days > 0 || hours > 0)
                builder.append(' ');
            builder.append(minutes).append(' ').append(pluralizationHelper(player, minutes, MINUTES));
            millis -= MINUTES.toMillis(minutes);
        }
        long seconds = MILLISECONDS.toSeconds(millis);
        if (seconds > 0) {
            if (days > 0 || hours > 0 || minutes > 0)
                builder.append(' ');
            builder.append(seconds).append(' ').append(pluralizationHelper(player, seconds, SECONDS));
        } else if (days == 0 && hours == 0 && minutes == 0) {
            builder.append("1 ").append(pluralizationHelper(player, 1, SECONDS));
        }

        return builder.toString();
    }

    /**
     * Simple method that applies pluralization to the given value, which is in seconds
     *
     * @param player  The player to use for localization
     * @param seconds The amount of seconds
     * @return A string the number as a string and the pluralization for seconds fitting for the value
     */
    public static String pluralizeSeconds(Player player, long seconds) {
        return seconds + " " + pluralizationHelper(player, seconds, SECONDS);
    }

    /**
     * Updates the currently used pluralization helper with the new one.
     * <br><br>
     * The following values are passed to the TriFunction:<br><br>
     * <code>Player</code> - The player to use for localization (can be null)<br>
     * <code>TimeUnit</code> - The TimeUnit to get the string from of<br>
     * <code>Boolean</code> - Whether or not it should be plural or not<br>
     *
     * @param pluralizationHelper The new pluralizationHelper to use.
     * @throws NullPointerException If the pluralizationHelper is null
     */
    public static void setPluralizationHelper(TriFunction<Player, TimeUnit, Boolean, String> pluralizationHelper) {
        Preconditions.checkNotNull(pluralizationHelper, "The pluralizationHelper cannot be null");
        DurationFormatter.pluralizationHelper = pluralizationHelper;
    }

    private static String pluralizationHelper(Player player, long value, TimeUnit timeUnit) {
        boolean isPlural = value != 1;
        return pluralizationHelper.apply(player, timeUnit, isPlural);
    }
}
