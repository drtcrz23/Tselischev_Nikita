package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BubbleTest {

    @Test
    void main() {
        var sorter = new ArraysSorting(List.of(new BubbleSort(10)));
        var array = List.of(7, 10, 3, 9, 4, 6);
        var abc = List.of(3, 4, 6, 7, 9, 10);
        assertEquals(abc, sorter.sort(array, SortsType.Bubble));
    }
}