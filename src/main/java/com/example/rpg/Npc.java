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
    private boolean[][] tempMap;
    private boolean completed = true;
    private int stone;
    // private int wood;
    private Image rightImage;
    private Image leftImage;
    private int varient = 0;

    List<Point> path;
    public Npc(int xpos, int ypos, String name, int[][] map) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.name = name;
        this.maxHeight = map.length;
        this.maxWidth = map[0].length;
        this.map = map;
        this.task = "wander";
        this.tempMap = new boolean[maxHeight][maxWidth];

        Random rand = new Random();
        this.varient = rand.nextInt(21);
        this.rightImage = new Image(getClass().getResource("res/npc/guy" + this.varient + ".png").toString());
        this.leftImage = new Image(getClass().getResource("res/npc/guy" + this.varient + "r" + ".png").toString());
        this.img = rightImage;
    }

    public void move() {
        if (this.task.equals("wander")) {
            Random rand = new Random();
            int temp = rand.nextInt(4);
            if (temp == 0) {
                if (xpos + 1 < maxWidth && map[ypos][xpos + 1] == 0 || xpos + 1 < maxWidth && map[ypos][xpos + 1] == 6)
                    xpos++;
                    img = rightImage;
            } else if (temp == 1) {
                if (xpos - 1 >= 0 && map[ypos][xpos - 1] == 0 || xpos - 1 >= 0 && map[ypos][xpos - 1] == 6)
                    xpos--;
                    img = leftImage;
            } else if (temp == 2) {
                if (ypos + 1 < maxHeight && map[ypos + 1][xpos] == 0 || ypos + 1 < maxHeight && map[ypos + 1][xpos] == 6)
                    ypos++;
            } else if (temp == 3) {
                if (ypos - 1 >= 0 && map[ypos - 1][xpos] == 0 || ypos - 1 >= 0 && map[ypos - 1][xpos] == 6)
                    ypos--;
            }
            this.task = "stone";
        } else if (this.task.equals("stone")) {
            int[] coords = findStone();
            if (coords != null) {
                if (completed) {

                    for (int y = 0; y < maxHeight; y++)
                        for (int x = 0; x < maxWidth; x++) {
                            if (map[y][x] == 0) {
                                tempMap[y][x] = true;
                            } else {
                                tempMap[y][x] = false;
                            }
                        }
                    Grid grid = new Grid(maxHeight, maxWidth, tempMap);
                    Point start = new Point(xpos, ypos);
                    Point target = new Point(coords[0], coords[1]);
                    path = PathFinding.findPath(grid, start, target, false);
                    if (path != null && !path.isEmpty()) {
                        completed = false;
                    } else {
                        this.task = "wander";
                    }   
                } else {
                    if (path != null && !path.isEmpty()) {
                        this.xpos = path.get(0).x;
                        this.ypos = path.get(0).y;
                        path.remove(0);
                    } else {
                        completed = true;
                        this.path = null;
                        stone++;
                        System.out.println("Stone collected: " + stone);
                    }
                }
            }
        }
    }
        private int[] findStone() {
            int[] closestStone = null;
            double minDistance = Double.MAX_VALUE;

            for (int y = 0; y < maxHeight; y++) {
            for (int x = 0; x < maxWidth; x++) {
                if (map[y][x] == 1) {
                double distance = Math.sqrt(Math.pow(x - xpos, 2) + Math.pow(y - ypos, 2));
                if (distance < minDistance) {
                    minDistance = distance;
                    closestStone = new int[] { x, y };
                }
                }
            }
            }
            return closestStone;
        }

    public int getxPos() {
        return xpos;
    }

    public int getyPos() {
        return ypos;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return img;
    }
}