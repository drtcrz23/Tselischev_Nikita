package org.example.animals;

import org.example.food.Food;

public abstract class Animal {
    protected int age;
    protected String name;
    protected boolean isPredator;

    public Animal(int age, String name, boolean isPredator) {
        this.age = age;
        this.name = name;
        this.isPredator = isPredator;
    }
    abstract void eat(Food food);
    abstract void go();
}
