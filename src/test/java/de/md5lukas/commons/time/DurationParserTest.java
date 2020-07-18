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
