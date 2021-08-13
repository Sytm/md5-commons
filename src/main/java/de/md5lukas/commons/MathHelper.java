package de.md5lukas.commons;

import org.bukkit.Location;

import java.text.DecimalFormat;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class MathHelper {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.##");

    /**
     * Applies a simple format to the double value, only showing two numbers after the floating point
     *
     * @param value The double value to format
     * @return A formatted version of the double value
     */
    public static String format(double value) {
        return DECIMAL_FORMAT.format(value);
    }

    /**
     * Calculates the distance between two points in 2D space. The result is squared
     *
     * @param x1 The x coordinate of the first point
     * @param y1 The y coordinate of the first point
     * @param x2 The x coordinate of the second point
     * @param y2 The y coordinate of the second point
     * @return The distance between those two points squared
     */
    public static double distance2DSquared(double x1, double y1, double x2, double y2) {
        return square(x1 - x2) + square(y1 - y2);
    }

    /**
     * Calculates the distance between two points in 2D space
     *
     * @param x1 The x coordinate of the first point
     * @param y1 The y coordinate of the first point
     * @param x2 The x coordinate of the second point
     * @param y2 The y coordinate of the second point
     * @return The distance between those two points
     * @see #distance2DSquared(double, double, double, double) If you just want to compare two distances. This method does not have to calcluate the square
     * root and is more performant
     */
    public static double distance2D(double x1, double y1, double x2, double y2) {
        return Math.sqrt(distance2DSquared(x1, y1, x2, y2));
    }

    /**
     * Calculates the distance between the two locations, disregarding the Y axis. The result is squared
     *
     * @param location1 The first location
     * @param location2 The second location
     * @return The distance between those two locations squared
     * @throws NullPointerException     If either location1 or location 2 are null
     * @throws IllegalArgumentException If both locations are not in the same world
     */
    public static double distance2DSquared(Location location1, Location location2) {
        checkNotNull(location1, "The first location cannot be null");
        checkNotNull(location2, "The second location cannot be null");
        checkArgument(Objects.equals(location1.getWorld(), location2.getWorld()));
        return distance2DSquared(location1.getX(), location1.getZ(), location2.getX(), location2.getZ());
    }

    /**
     * Calculates the distance between the two locations, disregarding the Y axis.
     *
     * @param location1 The first location
     * @param location2 The second location
     * @return The distance between those two locations
     * @throws NullPointerException     If either location1 or location 2 are null
     * @throws IllegalArgumentException If both locations are not in the same world
     * @see #distance2DSquared(Location, Location) If you just want to compare two distances. This method does not have to calcluate the square
     * root and is more performant
     */
    public static double distance2D(Location location1, Location location2) {
        return Math.sqrt(distance2DSquared(location1, location2));
    }

    /**
     * Squares the provided value
     *
     * @param x The value to square
     * @return The result
     */
    public static double square(double x) {
        return x * x;
    }

    /**
     * Squares the provided value
     *
     * @param x The value to square
     * @return The result
     */
    public static long square(long x) {
        return x * x;
    }

    /**
     * Clamps the provided value between the minimum and maximum
     *
     * @param min   The minimum value
     * @param max   The maximum value
     * @param value The actual value
     * @return The value with the limits applied
     * @throws IllegalArgumentException If the minimum value is bigger than the maximum value
     */
    public static int clamp(int min, int max, int value) {
        checkArgument(min <= max, "The minimum cannot be bigger than the maximum value");
        return Math.max(min, Math.min(max, value));
    }
}
