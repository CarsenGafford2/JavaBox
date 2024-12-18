package com.example.rpg;

import java.util.*;

import javafx.scene.image.Image;

public class Npc {

    private int xpos;
    private int ypos;
    private int maxWidth;
    private int maxHeight;
    private int[][] map;
    private String name;
    private Image img;
    private String task;

    public Npc(int xpos, int ypos, String name, int[][] map) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.name = name;
        this.maxHeight = map.length;
        this.maxWidth = map[0].length;
        this.map = map;
        this.task = "stone";

        Random rand = new Random();
        this.img = new Image(getClass().getResource("res/npc/guy" + rand.nextInt(21) + ".png").toString());
    }

    public void move() {
        if (this.task.equals("wander")) {
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
        } else if (this.task.equals("stone")) {
            int[] coords = findStone();
            if (coords != null) {
                System.out.println("Stone found at: " + coords[1] + ", " + coords[0]);
            }
        }
    }

    private int[] findStone() {
        for (int y = 0; y < maxHeight; y++) {
            for (int x = 0; x < maxWidth; x++) {
                if (map[y][x] == 1) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    public int getxPos() { return xpos; }
    public int getyPos() { return ypos; }
    public String getName() { return name; }
    public Image getImage() { return img; }
}

class Node {
    int x, y;
    int gCost;
    int hCost;
    int fCost;
    Node parent;

    public Node(int x, int y, int gCost, int hCost) {
        this.x = x;
        this.y = y;
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = gCost + hCost;
        this.parent = null;
    }

    public Node(int x, int y, int gCost, int hCost, Node parent) {
        this(x, y, gCost, hCost);
        this.parent = parent;
    }
}