package de.md5lukas.commons.time

import com.google.common.base.Preconditions
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * This class contains methods that help one to format durations from millisecond values into a
 * human readable format
 */
class DurationFormatter(
    private val pluralizationHelper: (unit: TimeUnit, isPlural: Boolean) -> String
) {

  /**
   * Formats the given duration into a human readable format
   *
   * @param duration The duration to format
   * @return The formatted duration
   * @throws IllegalArgumentException If the provided millisecond value is negative
   */
  fun formatDuration(duration: Duration): String {
    Preconditions.checkArgument(
        !(duration.isNegative || duration.isZero),
        "The duration to convert to a string must be positive")

    val builder = StringBuilder()
    val days = duration.toDaysPart()
    if (days > 0) {
      builder.append(days).append(' ').append(pluralizationHelper(days, TimeUnit.DAYS))
    }
    val hours = duration.toHoursPart()
    if (hours > 0) {
      if (builder.isNotEmpty()) builder.append(' ')
      builder.append(hours).append(' ').append(pluralizationHelper(hours.toLong(), TimeUnit.HOURS))
    }
    val minutes = duration.toMinutesPart()
    if (minutes > 0) {
      if (builder.isNotEmpty()) builder.append(' ')
      builder
          .append(minutes)
          .append(' ')
          .append(pluralizationHelper(minutes.toLong(), TimeUnit.MINUTES))
    }
    val seconds = duration.toSecondsPart()
    if (seconds > 0) {
      if (builder.isNotEmpty()) builder.append(' ')
      builder
          .append(seconds)
          .append(' ')
          .append(pluralizationHelper(seconds.toLong(), TimeUnit.SECONDS))
    } else if (builder.isEmpty()) {
      builder.append("< 1 ").append(pluralizationHelper(1, TimeUnit.SECONDS))
    }
    return builder.toString()
  }

  /**
   * Simple method that applies pluralization to the given value, which is in seconds
   *
   * @param seconds The amount of seconds
   * @return A string the number as a string and the pluralization for seconds fitting for the value
   */
  fun pluralizeSeconds(seconds: Long): String {
    return seconds.toString() + " " + pluralizationHelper(seconds, TimeUnit.SECONDS)
  }

  private fun pluralizationHelper(value: Long, timeUnit: TimeUnit): String {
    val isPlural = value != 1L
    return pluralizationHelper(timeUnit, isPlural)
  }
}
