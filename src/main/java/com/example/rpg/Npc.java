package com.example.rpg;

import java.util.Random;

public class Npc {

    private int xpos;
    private int ypos;
    private int maxWidth;
    private int maxHeight;
    private String type;

    public Npc(int xpos, int ypos, String type, int maxWidth, int maxHeight) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.type = type;
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
    }

    public void move() {
        Random rand = new Random();
        int temp = rand.nextInt(3);

        if (temp == 0) {
            if (!(xpos+1 > maxWidth)) {
                xpos++;
            }
        } else if (temp == 1) {
            if (!(xpos-1 < 0)) {
                xpos--;
            }
        } else if (temp == 2) {
            if (!(ypos+1 > maxHeight)) {
                ypos++;
            }
        } else if (temp == 3) {
            if (!(ypos-1 < 0)) {
                ypos--;
            }
        }
    }

    public int getxPos() {
        return this.xpos;
    }
    public int getyPos() {
        return this.ypos;
    }
}