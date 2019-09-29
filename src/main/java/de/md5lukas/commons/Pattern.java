package de.md5lukas.commons;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Pattern<T> {

	private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_#".toCharArray();

	private T defaultValue;
	private Map<Character, T> mappings;
	private char[][] grid;
	private boolean wrapAround;

	public Pattern(String... lines) {
		checkNotNull(lines, "The lines for pattern creation cannot be null");
		checkNotNull(lines[0], "The lines cannot be null");
		checkArgument(lines[0].length() > 0, "The lines cannot be of length zero");
		int length = lines[0].length();
		for (int i = 1; i < lines.length; ++i) {
			checkNotNull(lines[i], "The lines cannot be null");
			checkArgument(length == lines[i].length(), "Each line must have the same length");
		}
		grid = new char[lines.length][];
		for (int i = 0; i < lines.length; ++i) {
			grid[i] = lines[i].toCharArray();
		}
		mappings = new HashMap<>();
		defaultValue = null;
		wrapAround = false;
	}

	public int rows() {
		return grid.length;
	}

	public int columns() {
		return grid[0].length;
	}

	public Pattern<T> setDefault(T defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	public Pattern<T> setMapping(char key, T value) {
		mappings.put(key, value);
		return this;
	}

	public boolean getWrapAround() {
		return wrapAround;
	}

	public Pattern<T> setWrapAround(boolean wrapAround) {
		this.wrapAround = wrapAround;
		return this;
	}

	public T get(int row, int column) {
		char key = getKey(row, column);
		if (mappings.containsKey(key))
			return mappings.get(key);
		return defaultValue;
	}

	public char getKey(int row, int column) {
		int[] fixed = wrapNumbers(row, column);
		return grid[fixed[0]][fixed[1]];
	}

	public Pattern<T> setKey(int row, int column, char key) {
		int[] fixed = wrapNumbers(row, column);
		grid[fixed[0]][fixed[1]] = key;
		return this;
	}

	public Pattern<T> setValue(int row, int column, T value) {
		int[] fixed = wrapNumbers(row, column);

		boolean keyFound = false;
		char key = (char) 0;

		for (Map.Entry<Character, T> entry : mappings.entrySet()) {
			if (entry.getValue().equals(value)) {
				key = entry.getKey();
				keyFound = true;
				break;
			}
		}

		if (!keyFound) {
			key = findFreeKey();
		}

		grid[fixed[0]][fixed[1]] = key;
		mappings.put(key, value);
		return this;
	}

	private int[] wrapNumbers(int row, int column) {
		if (wrapAround) {
			row %= rows();
			column %= columns();
		}
		return new int[] { row, column };
	}

	private char findFreeKey() {
		for (char c : ALPHABET) {
			if (!mappings.containsKey(c))
				return c;
		}
		return 0;
	}
}