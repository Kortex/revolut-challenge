package com.ariskourt.revolut.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    private static final String LEFT = "Left";
    private static final String RIGHT = "Right";

    private Pair<String, String> pair;

    @BeforeEach
    void setUp() {
        pair = Pair.of(LEFT, RIGHT);
    }

    @Test
    public void of_WhenStaticCreatorIsCalled_PairObjectGetsCreated() {
        assertNotNull(pair);
        assertNotNull(pair.getLeft());
        assertEquals(LEFT, pair.getLeft());
        assertNotNull(pair.getRight());
        assertEquals(RIGHT, pair.getRight());
    }

}