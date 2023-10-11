package org.example;

import java.util.ArrayList;
import java.util.Collections;

public class MergeSort extends Sorts {

    protected MergeSort(int maxLength) {
        super(maxLength);
    }

    @Override
    public void sort(ArrayList<Integer> array) {
        Collections.sort(array);
    }

    @Override
    public SortsType methodsOfSorting() {
        return SortsType.Merge;
    }
}
