package org.example;

import org.example.MethodsOfSort;
import org.example.SortsType;

import java.util.ArrayList;
import java.util.List;

public class ArraysSorting {
    private final List<MethodsOfSort> types;

    public ArraysSorting(List<MethodsOfSort> types) {
        this.types = types;
    }

    public List<Integer> sort(List<Integer> array, SortsType typesorts) {
        for (MethodsOfSort type : types) {
            if (type.methodsOfSorting() != typesorts) {
                continue;
            }
            try {
                type.checkLengthOfArray(array);
                var SortedArr = new ArrayList<>(array);
                type.sort(SortedArr);
                return SortedArr;
            } catch (RuntimeException e) {

            }
        }
        throw new RuntimeException("Алгоритм не реализован");
    }
}
