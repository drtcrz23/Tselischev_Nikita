package org.example;

import java.util.List;

public abstract class Sorts implements MethodsOfSort {
    private final int maxLength;

    protected Sorts(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void checkLengthOfArray(List<Integer> array) {
        if (array.size() > maxLength){
            throw new RuntimeException();
        }
    }
}
