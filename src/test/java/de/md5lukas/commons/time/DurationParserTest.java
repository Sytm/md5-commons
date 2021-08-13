package de.md5lukas.commons.time;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DurationParserTest {

    @Test
    void testOnlyMillisecondsPositive() {
        long expected = TimeUnit.MILLISECONDS.toMillis(1);
        assertEquals(expected, DurationParser.parseDuration("1ms", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("1millis", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("1milliseconds", TimeUnit.MILLISECONDS));
    }

    @Test
    void testOnlyMillisecondsNegative() {
        long expected = TimeUnit.MILLISECONDS.toMillis(-1);
        assertEquals(expected, DurationParser.parseDuration("-1ms", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("-1millis", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("-1milliseconds", TimeUnit.MILLISECONDS));
    }

    @Test
    void testOnlySecondsPositive() {
        long expected = TimeUnit.SECONDS.toMillis(1);
        assertEquals(expected, DurationParser.parseDuration("1s", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("1sec", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("1secs", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("1second", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("1seconds", TimeUnit.MILLISECONDS));
    }

    @Test
    void testOnlySecondsNegative() {
        long expected = TimeUnit.SECONDS.toMillis(-1);
        assertEquals(expected, DurationParser.parseDuration("-1s", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("-1sec", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("-1secs", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("-1second", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("-1seconds", TimeUnit.MILLISECONDS));
    }

    @Test
    void testOnlyMinutesPositive() {
        long expected = TimeUnit.MINUTES.toMillis(1);
        assertEquals(expected, DurationParser.parseDuration("1m", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("1min", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("1mins", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("1minute", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("1minutes", TimeUnit.MILLISECONDS));
    }

    @Test
    void testOnlyMinutesNegative() {
        long expected = TimeUnit.MINUTES.toMillis(-1);
        assertEquals(expected, DurationParser.parseDuration("-1m", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("-1min", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("-1mins", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("-1minute", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("-1minutes", TimeUnit.MILLISECONDS));
    }

    @Test
    void testOnlyHoursPositive() {
        long expected = TimeUnit.HOURS.toMillis(1);
        assertEquals(expected, DurationParser.parseDuration("1h", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("1hour", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("1hours", TimeUnit.MILLISECONDS));
    }

    @Test
    void testOnlyHoursNegative() {
        long expected = TimeUnit.HOURS.toMillis(-1);
        assertEquals(expected, DurationParser.parseDuration("-1h", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("-1hour", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("-1hours", TimeUnit.MILLISECONDS));
    }

    @Test
    void testOnlyDaysPositive() {
        long expected = TimeUnit.DAYS.toMillis(1);
        assertEquals(expected, DurationParser.parseDuration("1d", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("1day", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("1days", TimeUnit.MILLISECONDS));
    }

    @Test
    void testOnlyDaysNegative() {
        long expected = TimeUnit.DAYS.toMillis(-1);
        assertEquals(expected, DurationParser.parseDuration("-1d", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("-1day", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("-1days", TimeUnit.MILLISECONDS));
    }

    @Test
    void testOnlyWeeksPositive() {
        long expected = TimeUnit.DAYS.toMillis(7);
        assertEquals(expected, DurationParser.parseDuration("1w", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("1week", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("1weeks", TimeUnit.MILLISECONDS));
    }

    @Test
    void testOnlyWeeksNegative() {
        long expected = TimeUnit.DAYS.toMillis(-7);
        assertEquals(expected, DurationParser.parseDuration("-1w", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("-1week", TimeUnit.MILLISECONDS));
        assertEquals(expected, DurationParser.parseDuration("-1weeks", TimeUnit.MILLISECONDS));
    }
}
