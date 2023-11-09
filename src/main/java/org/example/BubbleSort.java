package org.example;

import java.util.ArrayList;
import java.util.List;

public class BubbleSort extends Sorts {

    protected BubbleSort(int maxLength) {
        super(maxLength);
    }

    @Override
    public void sort(ArrayList<Integer> array) {
        for (int i = 0; i < array.size(); i++) {
            for (int j = i + 1; j < array.size(); j++) {
                if (array.get(i) > array.get(j)) {
                    int temp = array.get(i);
                    array.set(i, array.get(j));
                    array.set(j, temp);
                }
            }
        }
    }

    @Override
    public SortsType methodsOfSorting() {
        return SortsType.Bubble;
    }
}
