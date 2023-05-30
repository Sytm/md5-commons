package de.md5lukas.commons.time;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import static java.util.concurrent.TimeUnit.*;

/**
 * This class contains methods that help one to format durations from millisecond values into a human readable format
 */
public final class DurationFormatter {

    private final @NotNull BiFunction<@NotNull TimeUnit, @NotNull Boolean, @NotNull String> pluralizationHelper;

    public DurationFormatter(@NotNull BiFunction<@NotNull TimeUnit, @NotNull Boolean, @NotNull String> pluralizationHelper) {
        this.pluralizationHelper = Preconditions.checkNotNull(pluralizationHelper, "The pluralizationHelper cannot be null");
    }

    /**
     * Formats the given duration into a human readable format
     *
     * @param duration The duration to format
     * @return The formatted duration
     * @throws IllegalArgumentException If the provided millisecond value is negative
     */
    public @NotNull String formatDuration(@NotNull Duration duration) {
        Preconditions.checkArgument(!(duration.isNegative() || duration.isZero()), "The duration to convert to a string must be positive");
        final StringBuilder builder = new StringBuilder();
        final long days = duration.toDaysPart();
        if (days > 0) {
            builder.append(days).append(' ').append(pluralizationHelper(days, DAYS));
        }
        final int hours = duration.toHoursPart();
        if (hours > 0) {
            if (!builder.isEmpty())
                builder.append(' ');
            builder.append(hours).append(' ').append(pluralizationHelper(hours, HOURS));
        }
        final int minutes = duration.toMinutesPart();
        if (minutes > 0) {
            if (!builder.isEmpty())
                builder.append(' ');
            builder.append(minutes).append(' ').append(pluralizationHelper(minutes, MINUTES));
        }
        final int seconds = duration.toSecondsPart();
        if (seconds > 0) {
            if (!builder.isEmpty())
                builder.append(' ');
            builder.append(seconds).append(' ').append(pluralizationHelper(seconds, SECONDS));
        } else if (days == 0 && hours == 0 && minutes == 0) {
            builder.append("< 1 ").append(pluralizationHelper(1, SECONDS));
        }

        return builder.toString();
    }

    /**
     * Simple method that applies pluralization to the given value, which is in seconds
     *
     * @param seconds The amount of seconds
     * @return A string the number as a string and the pluralization for seconds fitting for the value
     */
    public @NotNull String pluralizeSeconds(long seconds) {
        return seconds + " " + pluralizationHelper(seconds, SECONDS);
    }

    private String pluralizationHelper(long value, @NotNull  TimeUnit timeUnit) {
        final boolean isPlural = value != 1;
        return pluralizationHelper.apply(timeUnit, isPlural);
    }
}
