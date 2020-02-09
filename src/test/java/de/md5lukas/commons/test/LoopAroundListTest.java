package de.md5lukas.commons.test;

import de.md5lukas.commons.collections.LoopAroundList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class LoopAroundListTest {

	LoopAroundList<Integer> list;

	@BeforeEach
	void initializeList() {
		list = new LoopAroundList<>(3);
		list.add(1);
		list.add(2);
		list.add(3);
	}

	@Test
	public void testForwardMovement() {
		assertIterableEquals(Arrays.asList(1, 2, 3), list.getCutOut());
		list.next();
		assertIterableEquals(Arrays.asList(3, 1, 2), list.getCutOut());
		list.next();
		assertIterableEquals(Arrays.asList(2, 3, 1), list.getCutOut());
		list.next();
		assertIterableEquals(Arrays.asList(1, 2, 3), list.getCutOut());
	}

	@Test
	public void testBackwardsMovement() {
		assertIterableEquals(Arrays.asList(1, 2, 3), list.getCutOut());
		list.previous();
		assertIterableEquals(Arrays.asList(2, 3, 1), list.getCutOut());
		list.previous();
		assertIterableEquals(Arrays.asList(3, 1, 2), list.getCutOut());
		list.previous();
		assertIterableEquals(Arrays.asList(1, 2, 3), list.getCutOut());
	}
}
