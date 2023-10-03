package org.example.animals;

import org.example.food.Food;
import org.example.food.Grass;

public class Camel extends Animal {
    public Camel(int age, String name, boolean isPredator) {
        super(age, name, isPredator);
    }

    @Override
    public void eat(Food food) {
        if (food instanceof Grass){
            System.out.println("Прекрасная еда для верблюда");
        }else{
            System.out.println("Ужасная еда для верблюда");
        }
    }
    @Override
    public void go() {
        System.out.println("Верблюд идёт");
    }
}
