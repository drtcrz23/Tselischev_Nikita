package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MergeTest {

    @Test
    void sort() {
        var sorter = new ArraysSorting(List.of(new MergeSort(100)));
        var array = List.of(57, 29, 8, 39, 22, 47, 32, 71, 91, 76,
                17, 23, 100, 5, 17, 2, 22, 69, 27, 50, 63, 51, 92, 46);
        var abc = List.of(2, 5, 8, 17, 17, 22, 22, 23, 27, 29, 32,
                39, 46, 47, 50, 51, 57, 63, 69, 71, 76, 91, 92, 100);
        assertEquals(abc, sorter.sort(array, SortsType.Merge));
    }
}