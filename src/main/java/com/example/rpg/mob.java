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
    private String right;
    private String left;
    private Image rightImage;
    private Image leftImage;
    private int varient = 0;

    public mob(int xpos, int ypos, String name, String type, int[][] map) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.name = name;
        this.maxHeight = map.length;
        this.maxWidth = map[0].length;
        this.map = map;
        this.type = type;

        Random rand = new Random();
        this.varient = rand.nextInt(2);
        this.right = checkAndLoadTexture("mods/mob/" + this.type + this.varient + ".png", "res/mob/" + this.type + this.varient + ".png");
        this.left = checkAndLoadTexture("mods/mob/" + this.type + this.varient + "r" + ".png", "res/mob/" + this.type + this.varient + "r" + ".png");
        this.rightImage = new Image(right);
        this.leftImage = new Image(left);
        this.img = rightImage;
    }

    private String checkAndLoadTexture(String modPath, String defaultPath) {
        if (getClass().getResource(modPath) != null) {
            return getClass().getResource(modPath).toString();
        } else {
            if (getClass().getResource(defaultPath) != null) {
                return getClass().getResource(defaultPath).toString();
            } else {
                throw new IllegalArgumentException("Resource not found: " + defaultPath);
            }
        }
    }

    public void move() {
        Random rand = new Random();
        int temp = rand.nextInt(4);
        if (temp == 0) {
            if (xpos + 1 < maxWidth && map[ypos][xpos + 1] == 0 || xpos + 1 < maxWidth && map[ypos][xpos + 1] == 6) {
                xpos++;
                img = rightImage;
            }
        } else if (temp == 1) {
            if (xpos - 1 >= 0 && map[ypos][xpos - 1] == 0 || xpos - 1 >= 0 && map[ypos][xpos - 1] == 6) {
                xpos--;
                img = leftImage;
            }
        } else if (temp == 2) {
            if (ypos + 1 < maxHeight && map[ypos + 1][xpos] == 0 || ypos + 1 < maxHeight && map[ypos + 1][xpos] == 6) ypos++;
        } else if (temp == 3) {
            if (ypos - 1 >= 0 && map[ypos - 1][xpos] == 0 || ypos - 1 >= 0 && map[ypos - 1][xpos] == 6) ypos--;
        }
    }


    public int getxPos() { return xpos; }
    public int getyPos() { return ypos; }
    public String getName() { return name; }
    public Image getImage() { return img; }
}