package de.md5lukas.commons.collections;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class ReplaceableListTest {

    static ReplaceableList list;

    @BeforeAll
    static void initializeList() {
        list = new ReplaceableList("A B C D", "F G H I", "A B C D", "F G H I");
    }

    @Test
    void checkReplace() {
        assertIterableEquals(Arrays.asList("1 2-3 4", "F G 5 I", "1 2-3 4", "F G 5 I"),
                list.replace("A", "1", "B C", "2-3", "D", "4", "F G I", "Not here", "H", "5"));
    }
}
