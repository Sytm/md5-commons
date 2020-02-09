package de.md5lukas.commons.test;

import de.md5lukas.commons.collections.ReplaceableList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ReplaceableListTest {

	static ReplaceableList list;

	@BeforeAll
	static void initializeList() {
		list = ReplaceableList.ofStrings("A B C D", "F G H I", "A B C D", "F G H I");
	}

	@Test
	public void checkEmptyList() {
		assertEquals(0, ReplaceableList.EMPTY.size());
	}

	@Test
	public void checkReplace() {
		assertIterableEquals(Arrays.asList("1 2-3 4", "F G 5 I", "1 2-3 4", "F G 5 I"), list.replace("A", "1", "B C", "2-3", "D", "4", "F G I", "Not here", "H", "5"));
	}
}
