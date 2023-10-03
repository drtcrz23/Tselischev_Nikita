package org.example.animals;

import org.example.food.Fish;
import org.example.food.Food;

public class Dolphin extends Animal {
    public Dolphin(int age, String name, boolean isPredator) {
        super(age, name, isPredator);
    }

    @Override
    public void eat(Food food) {
        if (food instanceof Fish){
            System.out.println("Прекрасная еда для дельфинчика");
        }else {
            System.out.println("Ужасная еда для дельфина");
        }
    }
    @Override
    public void go() {
        System.out.println("Дельфин плывёт");
    }
}
