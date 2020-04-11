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

package de.md5lukas.commons.test;

import de.md5lukas.commons.collections.LoopAroundList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

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
