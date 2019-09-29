package de.md5lukas.commons.collections;

import de.md5lukas.commons.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ReplaceableList extends ArrayList<String> {

	public ReplaceableList() {
	}

	public ReplaceableList(int initialCapacity) {
		super(initialCapacity);
	}
	public ReplaceableList(Collection<String> collection) {
		super(collection);
	}

	public List<String> replace(String target, String replacement, String... targetsAndReplacements) {
		if (targetsAndReplacements.length % 2 != 0)
			throw new IllegalArgumentException("The amount targets and replacements need to be even!");

		List<String> result = new ArrayList<>(size());
		for (String s : this) {
			result.add(StringHelper.multiReplace(s, target, replacement, targetsAndReplacements));
		}
		return result;
	}

	public List<String> copy() {
		return new ArrayList<>(this);
	}

	public static ReplaceableList ofStrings(String... strings) {
		ReplaceableList rl = new ReplaceableList(strings.length);
		rl.addAll(Arrays.asList(strings));
		return rl;
	}

	public static final ReplaceableList EMPTY = new ReplaceableList();
}
