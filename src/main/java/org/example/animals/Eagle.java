package org.example.animals;

import org.example.food.Food;
import org.example.food.Meat;

public class Eagle extends Animal {
    public Eagle(int age, String name, boolean isPredator) {
        super(age, name, isPredator);
    }

    @Override
    public void eat(Food food) {
        if (food instanceof Meat){
            System.out.println("Прекрасная еда для орла");
        }else {
            System.out.println("Ужасная еда для орла");
        }
    }

    @Override
    public void go() {
        System.out.println("Орёл летит");
    }
}
