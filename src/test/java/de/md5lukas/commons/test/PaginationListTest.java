package de.md5lukas.commons.test;

import de.md5lukas.commons.collections.PaginationList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class PaginationListTest {

	PaginationList<Integer> list;

	@BeforeEach
	void createList() {
		list = new PaginationList<>(2);
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
	}

	@Test
	public void checkPageCalculation() {
		assertEquals(3, list.pages());
		list.add(7);
		assertEquals(4, list.pages());
		list.add(8);
		assertEquals(4, list.pages());
	}

	@Test
	public void checkNegativePage() {
		assertThrows(IllegalArgumentException.class, () -> {
			list.page(-1);
		});
	}

	@Test
	public void checkTooBigPage() {
		assertThrows(IllegalArgumentException.class, () -> {
			list.page(4);
		});
	}

	@Test
	public void checkPageContents() {
		assertIterableEquals(Arrays.asList(1, 2), list.page(0));
		assertIterableEquals(Arrays.asList(3, 4), list.page(1));
		assertIterableEquals(Arrays.asList(5, 6), list.page(2));
	}
}
