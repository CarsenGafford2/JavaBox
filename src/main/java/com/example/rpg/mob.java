package com.example.rpg;
import java.util.*;

import javafx.scene.image.Image;

public class mob {

    private int xpos;
    private int ypos;
    private int maxWidth;
    private int maxHeight;
    private int[][] map;
    private String name;
    private String type;
    private Image img;

    public mob(int xpos, int ypos, String name, String type, int[][] map) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.name = name;
        this.maxHeight = map.length;
        this.maxWidth = map[0].length;
        this.map = map;
        this.type = type;

        Random rand = new Random();
        this.img = new Image(getClass().getResource("res/mob/" + this.type + rand.nextInt(2) + ".png").toString());
    }

    public void move() {
        Random rand = new Random();
        int temp = rand.nextInt(4);
        if (temp == 0) {
            if (xpos + 1 < maxWidth && map[ypos][xpos + 1] == 0) xpos++;
        } else if (temp == 1) {
            if (xpos - 1 >= 0 && map[ypos][xpos - 1] == 0) xpos--;
        } else if (temp == 2) {
            if (ypos + 1 < maxHeight && map[ypos + 1][xpos] == 0) ypos++;
        } else if (temp == 3) {
            if (ypos - 1 >= 0 && map[ypos - 1][xpos] == 0) ypos--;
        }
    }


    public int getxPos() { return xpos; }
    public int getyPos() { return ypos; }
    public String getName() { return name; }
    public Image getImage() { return img; }
}