package com.example.rpg;

public class Explosive {
    

    private String name;
    private int damage;
    private int radius;

    public Explosive(String name, int damage, int radius) {
        this.name = name;
        this.damage = damage;
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Explosive{" +
                "name='" + name + '\'' +
                ", damage=" + damage +
                ", radius=" + radius +
                '}';
    }
}
