package org.example;

import org.example.animals.*;
import org.example.food.Beef;
import org.example.food.Fish;
import org.example.food.Grass;

public class Main {
    public static void main(String[] args) {
        Tiger tiger = new Tiger(17, "Alesha", true);
        Horse horse = new Horse(7,"Mikhail",false);
        Camel camel = new Camel(5,"Petr",false);
        Dolphin dolphin = new Dolphin(14,"Bogdan" , true);
        Eagle eagle = new Eagle(3,"Alex", true);
        Beef beef = new Beef();
        Fish fish = new Fish();
        Grass grass = new Grass();
        tiger.eat(grass);
        horse.eat(grass);
        eagle.eat(beef);
        camel.eat(beef);
        tiger.eat(fish);
        dolphin.eat(fish);
        dolphin.eat((grass));
        dolphin.go();
        tiger.go();
        camel.go();
        eagle.go();
        horse.go();
    }
}