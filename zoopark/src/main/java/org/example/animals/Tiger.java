package org.example.animals;

import org.example.food.Beef;
import org.example.food.Food;

public class Tiger extends Animal {
    public Tiger(int age, String name, boolean isPredator) {
        super(age, name, isPredator);
    }

    @Override
    public void eat(Food food) {
        if (food instanceof Beef){
            System.out.println("Прекрасная еда для тигра");
        }else {
            System.out.println("Ужасная еда для тигра");
        }
    }

    @Override
    public void go() {
        System.out.println("Тигр идёт");
    }
}
