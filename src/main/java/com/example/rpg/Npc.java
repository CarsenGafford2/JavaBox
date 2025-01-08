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

    List<Point> path;
    public Npc(int xpos, int ypos, String name, int[][] map) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.name = name;
        this.maxHeight = map.length;
        this.maxWidth = map[0].length;
        this.map = map;
        this.task = "stone";
        this.tempMap = new boolean[maxHeight][maxWidth];

        Random rand = new Random();
        this.img = new Image(getClass().getResource("res/npc/guy" + rand.nextInt(21) + ".png").toString());
    }

    public void move() {
        if (this.task.equals("wander")) {
            Random rand = new Random();
            int temp = rand.nextInt(4);
            if (temp == 0) {
                if (xpos + 1 < maxWidth && map[ypos][xpos + 1] == 0)
                    xpos++;
            } else if (temp == 1) {
                if (xpos - 1 >= 0 && map[ypos][xpos - 1] == 0)
                    xpos--;
            } else if (temp == 2) {
                if (ypos + 1 < maxHeight && map[ypos + 1][xpos] == 0)
                    ypos++;
            } else if (temp == 3) {
                if (ypos - 1 >= 0 && map[ypos - 1][xpos] == 0)
                    ypos--;
            }
            this.task = "stone";
        } else if (this.task.equals("stone")) {
            int[] coords = findStone();
            if (coords != null) {
                if (completed) {
                    System.out.println("Stone found at: " + coords[1] + ", " + coords[0]);

                    for (int y = 0; y < maxHeight; y++)
                        for (int x = 0; x < maxWidth; x++) {
                            if (map[x][y] == 0) {
                                tempMap[x][y] = true;
                            } else {
                                tempMap[x][y] = false;
                            }
                        }
                    Grid grid = new Grid(maxHeight, maxWidth, tempMap);
                    Point start = new Point(xpos, ypos);
                    Point target = new Point(coords[0], coords[1]);
                    path = PathFinding.findPath(grid, start, target, false);
                    if (path != null && !path.isEmpty()) {
                        completed = false;
                    } else {
                        System.out.println("No path found");
                        this.task = "wander";
                    }   
                } else {
                    if (!path.isEmpty()) {
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

class Cell {
    int parent_i, parent_j;
    double f, g, h;

    Cell() {
        this.parent_i = 0;
        this.parent_j = 0;
        this.f = 0;
        this.g = 0;
        this.h = 0;
    }
}