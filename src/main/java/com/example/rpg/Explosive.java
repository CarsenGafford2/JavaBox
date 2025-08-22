package com.example.rpg;

public class Explosive {
    

    private String name;
    private int damage;
    private int radius;
    private int xpos;
    private int ypos;

    public Explosive(String name, int damage, int radius, int xpos, int ypos) {
        this.name = name;
        this.damage = damage;
        this.radius = radius;
        this.xpos = xpos;
        this.ypos = ypos;
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

    public int getXPos() {
        return xpos;
    }

    public int getYPos() {
        return ypos;
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
