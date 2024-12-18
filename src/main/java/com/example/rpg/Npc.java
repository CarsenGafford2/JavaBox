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
                List<Node> path = pathFind(coords);
                System.out.println(path);
                if (path != null && path.size() > 1) {
                    // Get the next step in the path (path.get(1) because path.get(0) is the current position)
                    Node nextStep = path.get(1);
    
                    // Move the NPC step by step using xpos++/ypos++ style updates
                    if (nextStep.x > xpos) xpos++;
                    else if (nextStep.x < xpos) xpos--;
    
                    if (nextStep.y > ypos) ypos++;
                    else if (nextStep.y < ypos) ypos--;
    
                    System.out.println("Moving to: (" + xpos + ", " + ypos + ")");
                }
            }
        }
    }    

    private List<Node> pathFind(int[] destination) {
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.fCost));
        boolean[][] closedList = new boolean[maxHeight][maxWidth];
    
        Node start = new Node(xpos, ypos, 0, heuristic(xpos, ypos, destination));
        openList.add(start);
    
        Node[][] nodes = new Node[maxHeight][maxWidth];
        nodes[ypos][xpos] = start;
    
        int[][] directions = {
            {0, 1}, {1, 0}, {0, -1}, {-1, 0}
        };
    
        while (!openList.isEmpty()) {
            Node current = openList.poll();
    
            System.out.println("Visiting node: (" + current.x + ", " + current.y + ")");
    
            if (current.x == destination[0] && current.y == destination[1]) {
                System.out.println("Destination reached!");
                return reconstructPath(current);
            }
    
            closedList[current.y][current.x] = true;
    
            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];
    
                // Check boundaries and obstacles
                if (newX < 0 || newX >= maxWidth || newY < 0 || newY >= maxHeight) {
                    continue;
                }
    
                if (map[newY][newX] != 0) {
                    System.out.println("Skipping obstacle at: (" + newX + ", " + newY + ")");
                    continue;
                }
    
                if (closedList[newY][newX]) continue;
    
                int gCost = current.gCost + 1;
                int hCost = heuristic(newX, newY, destination);
                Node neighbor = nodes[newY][newX];
    
                if (neighbor == null) {
                    neighbor = new Node(newX, newY, gCost, hCost, current);
                    nodes[newY][newX] = neighbor;
                    openList.add(neighbor);
                    System.out.println("Adding neighbor to open list: (" + newX + ", " + newY + ")");
                } else if (gCost < neighbor.gCost) {
                    neighbor.gCost = gCost;
                    neighbor.fCost = gCost + hCost;
                    neighbor.parent = current;
                    openList.add(neighbor);
                    System.out.println("Updating neighbor with better G cost: (" + newX + ", " + newY + ")");
                }
            }
        }
    
        System.out.println("No valid path found.");
        return null;
    }
    
    private int heuristic(int x, int y, int[] destination) {
        return Math.abs(x - destination[0]) + Math.abs(y - destination[1]);
    }

    private List<Node> reconstructPath(Node endNode) {
        List<Node> path = new ArrayList<>();
        Node current = endNode;
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
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