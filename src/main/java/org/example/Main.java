package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        var sorter = new ArraysSorting(List.of(
                new BubbleSort(5),
                new MergeSort(100)
        ));

        Scanner scanner = new Scanner(System.in);

        ArrayList<Integer> array = new ArrayList<Integer>();

        System.out.println("Введите значения (вводите по одному числу в каждой строке, для завершения ввода введите '0'):");
        while (true) {
            int value = scanner.nextInt();
            if (value == 0) {
                break;
            }
            array.add(value);
        }

        List<Integer> result = sorter.sort(array, SortsType.Bubble);

        System.out.println(Arrays.toString(result.toArray()));
    }
}
