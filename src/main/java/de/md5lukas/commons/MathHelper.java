package de.md5lukas.commons;

import org.bukkit.Location;

import java.text.DecimalFormat;
import java.util.Objects;

public class MathHelper {

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.##");


	public static double distance2DSquared(double x1, double y1, double x2, double y2) {
		return square(x1 - x2) + square(y1 - y2);
	}

	public static double distance2D(double x1, double y1, double x2, double y2) {
		return Math.sqrt(distance2DSquared(x1, y1, x2, y2));
	}

	public static double distance2DSquared(Location loc1, Location loc2) {
		if (!Objects.equals(loc1.getWorld(), loc2.getWorld())) {
			return -1;
		}
		return distance2DSquared(loc1.getX(), loc1.getZ(), loc2.getX(), loc2.getZ());
	}

	public static double distance2D(Location loc1, Location loc2) {
		return Math.sqrt(distance2DSquared(loc1, loc2));
	}

	public static double square(double x) {
		return x * x;
	}

	public static long square(long x) {
		return x * x;
	}

	public static String format(double val) {
		return DECIMAL_FORMAT.format(val);
	}

	public static int clamp(int min, int max, int value) {
		return Math.max(min, Math.min(max, value));
	}
}
