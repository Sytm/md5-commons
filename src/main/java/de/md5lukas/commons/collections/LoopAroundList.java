package de.md5lukas.commons.collections;

import java.util.ArrayList;
import java.util.List;

public class LoopAroundList<T> extends ArrayList<T> {

	private int index;
	private int cutOutSize;

	public LoopAroundList(int cutOutSize) {
		this.cutOutSize = cutOutSize;
	}

	public int next() {
		return move(1);
	}

	public int previous() {
		return move(-1);
	}

	public int move(int steps) {
		return setIndex(index + steps);
	}

	public int setIndex(int index) {
		this.index = index;
		while (this.index > size()) {
			this.index -= size();
		}
		while (this.index < 0) {
			this.index += size();
		}
		return this.index;
	}

	public List<T> getCutOut() {
		ArrayList<T> result = new ArrayList<>();
		for (int i = 0; i < cutOutSize; i++) {
			int actualIndex = i - index;
			actualIndex = actualIndex >= 0 ? actualIndex : actualIndex + size();
			result.add(get(actualIndex));
		}
		return result;
	}
}
