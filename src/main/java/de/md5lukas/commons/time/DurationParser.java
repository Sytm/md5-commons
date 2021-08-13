package de.md5lukas.commons.time;

import de.md5lukas.commons.text.StringHelper;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Class helping with parsing durations
 */
public final class DurationParser {

    private static final List<String> timeUnitMappingsKeys;
    private static final Map<String, DurationMapper> timeUnitMappings = new HashMap<>();

    static {
        final DurationMapper millisecondMapper = (value, unit) -> unit.convert(value, TimeUnit.MILLISECONDS);
        final DurationMapper secondMapper = (value, unit) -> unit.convert(value, TimeUnit.SECONDS);
        final DurationMapper minuteMapper = (value, unit) -> unit.convert(value, TimeUnit.MINUTES);
        final DurationMapper hourMapper = (value, unit) -> unit.convert(value, TimeUnit.HOURS);
        final DurationMapper dayMapper = (value, unit) -> unit.convert(value, TimeUnit.DAYS);
        final DurationMapper weekMapper = (value, unit) -> unit.convert(value * 7, TimeUnit.DAYS);

        timeUnitMappings.put("ms", millisecondMapper);
        timeUnitMappings.put("millis", millisecondMapper);
        timeUnitMappings.put("milliseconds", millisecondMapper);

        timeUnitMappings.put("s", secondMapper);
        timeUnitMappings.put("sec", secondMapper);
        timeUnitMappings.put("secs", secondMapper);
        timeUnitMappings.put("second", secondMapper);
        timeUnitMappings.put("seconds", secondMapper);

        timeUnitMappings.put("m", minuteMapper);
        timeUnitMappings.put("min", minuteMapper);
        timeUnitMappings.put("mins", minuteMapper);
        timeUnitMappings.put("minute", minuteMapper);
        timeUnitMappings.put("minutes", minuteMapper);

        timeUnitMappings.put("h", hourMapper);
        timeUnitMappings.put("hour", hourMapper);
        timeUnitMappings.put("hours", hourMapper);

        timeUnitMappings.put("d", dayMapper);
        timeUnitMappings.put("day", dayMapper);
        timeUnitMappings.put("days", dayMapper);

        timeUnitMappings.put("w", weekMapper);
        timeUnitMappings.put("week", weekMapper);
        timeUnitMappings.put("weeks", weekMapper);

        // We need to create an extra array for this, because the keys have to be sorted so the "s" for seconds matches thins like "hours" "weeks" and others ending with an "s"
        timeUnitMappingsKeys = new ArrayList<>(timeUnitMappings.keySet());
        timeUnitMappingsKeys.sort(Comparator.comparingInt(String::length).reversed());
    }

    public static long parseDuration(String durationString, TimeUnit outputUnit) {
        long result = 0;
        String[] parts = durationString.toLowerCase().split("\\s+");

        for (String part : parts) {
            for (String suffix : timeUnitMappingsKeys) {
                if (part.endsWith(suffix)) {
                    DurationMapper mapper = timeUnitMappings.get(suffix);
                    // Strip the suffix
                    long value = Long.parseLong(StringHelper.removeSuffix(part, suffix));
                    result += mapper.map(value, outputUnit);
                    break;
                }
            }
        }

        return result;
    }


    @FunctionalInterface
    public interface DurationMapper {
        long map(long value, TimeUnit unit);
    }
}
