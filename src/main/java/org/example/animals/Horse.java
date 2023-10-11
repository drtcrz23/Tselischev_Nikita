package org.example.animals;

import org.example.food.Food;
import org.example.food.Grass;

public class Horse extends Animal {
    public Horse(int age, String name, boolean isPredator) {
        super(age, name, isPredator);
    }

    @Override
    public void eat(Food food) {
        if (food instanceof Grass){
            System.out.println("Прекрасная еда для лошади");
        }else {
            System.out.println("Ужасная еда для лошади");
        }
    }

    @Override
    public void go() {
        System.out.println("Лошать скачет");
    }
}
