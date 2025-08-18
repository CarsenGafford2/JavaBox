package com.example.rpg;

import java.util.*;
import java.util.Map.Entry;
import java.awt.Point;
import java.io.File;
import java.net.URISyntaxException;

import javafx.scene.image.Image;

/**
 * Represents a human NPC in the simulation.
 * The NPC can perform various tasks such as wandering, gathering resources, and building structures.
 * Needs a class system, complex interactions, jobs, so on and so forth.
 * 
 * @author Carsen Gafford
 * @version alpha v0.1.11
 */
public class Npc {

    private int xpos;
    private int ypos;
    private int maxWidth;
    private int maxHeight;
    private int[][] map;
    private String name;
    private Image img;
    private String task;
    private Image rightImage;
    private Image leftImage;
    private int varient = 0;

    // Stats
    private Byte strength = 0;
    private Byte intelligence = 0;
    private Byte luck = 0;

    private List<Map.Entry<Point, Integer>> blueprint;

    private int requiredWood = 14;
    private int requiredStone = 11;

    private int wood = 0;
    private int stone = 0;

    private int buildStep = 0;

    private int whichBuilding;

    private ArrayList<String> traits = new ArrayList<String>();

    List<Point> path = null;

    public Npc(int xpos, int ypos, String name, int[][] map) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.name = name;
        this.maxHeight = map.length;
        this.maxWidth = map[0].length;
        this.map = map;
        this.task = "build";

        Random rand = new Random();
        this.whichBuilding = rand.nextInt(2);
        this.varient = rand.nextInt(21);
        this.rightImage = new Image(getClass().getResource("res/npc/guy" + this.varient + ".png").toString());
        this.leftImage = new Image(getClass().getResource("res/npc/guy" + this.varient + "r" + ".png").toString());
        this.img = rightImage;
    }

    /*
     * Move the NPC based on its current task.
     * The NPC can wander, gather stone, gather wood, build structures, hunt, or explore.
     * The movement is determined using A* pathfinding to navigate the map.
     */
    public void move() {
        if (this.task.equals("wander")) {
            if (path == null) {
                Random rand = new Random();
                Point target = null;

                for (int attempts = 0; attempts < 100; attempts++) {
                    int targetX = xpos + rand.nextInt(21) - 10;
                    int targetY = ypos + rand.nextInt(21) - 10;

                    if (targetX >= 0 && targetY >= 0 && targetX < maxWidth && targetY < maxHeight && map[targetY][targetX] == 0) {
                        target = new Point(targetX, targetY);
                        break;
                    }
                }

                if (target != null) {
                    path = aStarPathfinding(new Point(xpos, ypos), target, 0);
                } else {
                    System.out.println("No valid wander target found.");
                }
            }

            if (path != null && !path.isEmpty()) {
                Point nextStep = path.remove(0);
                moveToTarget(nextStep, 0);
            } else {
                this.task = "build";
            }
        } else if (this.task.equals("stone")) {
            if (stone >= requiredStone) {
                this.task = "wander";
                path = null;
            } else {
                if (this.traits.contains("recycler")) {
                    Point target = findNearestTile(7);
                    if (target != null) {
                        moveToTarget(target, 7);
                    } else {
                        this.task = "wander";
                        path = null;
                    }
                } else {
                    Point target = findNearestTile(1);
                    if (target != null) {
                        moveToTarget(target, 1);
                    } else {
                        this.task = "wander";
                        path = null;
                    }
                }
            }
        } else if (this.task.equals("wood")) {
            if (wood >= requiredWood) {
                this.task = "wander";
                path = null;
            } else {
                if (this.traits.contains("recycler")) {
                    Point target = findNearestTile(8);
                    if (target != null) {
                        moveToTarget(target, 8);
                    }
                } else {
                    Point target = findNearestTile(3);
                    if (target != null) {
                        moveToTarget(target, 3);
                    }
                }
            }
        } else if (this.task.equals("build")) {
            if (this.whichBuilding == 0) {
                this.requiredStone = 11;
                this.requiredWood = 14;
                this.blueprint = loadBlueprintFromCsv("buildings/basicBlueprint.csv");
            } else {
                this.requiredStone = 55;
                this.requiredWood = 76;
                this.blueprint = loadBlueprintFromCsv("buildings/mediumBlueprint.csv");
            }

            if (buildStep < this.blueprint.size()) {
                if (this.wood < requiredWood) {
                    this.task = "wood";
                } else if (this.stone < requiredStone) {
                    this.task = "stone";
                } else {
                    Map.Entry<Point, Integer> instruction = this.blueprint.get(buildStep);
                    Point target = instruction.getKey();
                    int tileType = instruction.getValue();

                    if (target.x >= 0 && target.y >= 0 && target.x < maxWidth && target.y < maxHeight) {
                        Point movePoint = new Point(target.x, target.y + 1);
                        map[target.y][target.x] = tileType;
                        moveToTarget(movePoint, 756);
                    }
                    buildStep++;
                }
            } else {
                this.task = "wander";
                path = null;
                this.stone -= requiredStone;
                this.wood -= requiredWood;
                this.buildStep = 0;
            }
        } else if (this.task.equals("hunt")) {
            Point target = findNearestTile(4);
            if (target != null) {
                moveToTarget(target, 4);
            } else {
                this.task = "wander";
                path = null;
            }
        } else if (this.task.equals("explore")) {
            if (path == null) {
                Random rand = new Random();
                Point target = null;

                for (int attempts = 0; attempts < 100; attempts++) {
                    int targetX = rand.nextInt(maxWidth);
                    int targetY = rand.nextInt(maxHeight);

                    if (targetX >= 0 && targetY >= 0 && targetX < maxWidth && targetY < maxHeight && map[targetY][targetX] == 0) {
                        target = new Point(targetX, targetY);
                        break;
                    }
                }

                if (target != null) {
                    path = aStarPathfinding(new Point(xpos, ypos), target, 0);
                } else {
                    System.out.println("No valid explore target found.");
                }
            }

            if (path != null && !path.isEmpty()) {
                Point nextStep = path.remove(0);
                moveToTarget(nextStep, 0);
            } else {
                this.task = "build";
            }
        }
    }
    /**
     * Load building blueprint from a CSV file.
     * @param filePath the path to the CSV file
     * @return a list of map entries containing points and tile types
     */
    private List<Entry<Point, Integer>> loadBlueprintFromCsv(String filePath) {
        List<Entry<Point, Integer>> blueprint = new ArrayList<>();
        File file = null;
        try {
            if (getClass().getResource(filePath) != null) {
                file = new File(getClass().getResource(filePath).toURI());
            } else {
                System.err.println("Resource not found: " + filePath);
                return blueprint;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    int x = Integer.parseInt(parts[0].trim());
                    int y = Integer.parseInt(parts[1].trim());
                    int tileType = Integer.parseInt(parts[2].trim());
                    blueprint.add(new AbstractMap.SimpleEntry<>(new Point(xpos + x, ypos + y), tileType));
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading blueprint from CSV: " + e.getMessage());
        }
        return blueprint;
    }

    /**
     * Find the nearest tile of a specific type using BFS.
     * @param targetType the type of tile to find
     * @return the point of the nearest tile, or null if not found
     */
    private Point findNearestTile(int targetType) {
        if (targetType == -1) {
            this.task = "wander";
            this.path = null;
            return null;
        }

        boolean[][] visited = new boolean[maxHeight][maxWidth];
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        queue.add(new Node(xpos, ypos, 0));
        visited[ypos][xpos] = true;

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            int cx = current.x;
            int cy = current.y;

            if (map[cy][cx] == targetType) {
                return new Point(cx, cy);
            }

            for (int[] dir : new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
                int nx = cx + dir[0];
                int ny = cy + dir[1];

                if (nx >= 0 && ny >= 0 && nx < maxWidth && ny < maxHeight && !visited[ny][nx] && (map[ny][nx] == 0 || map[ny][nx] == targetType)) {
                    queue.add(new Node(nx, ny, current.cost + 1));
                    visited[ny][nx] = true;
                }
            }
        }
        this.task = "wander";
        this.path = null;
        return null;
    }

    /**
     * Move the NPC towards a target point using A* pathfinding.
     * @param target the target point to move towards
     * @param specialWalkable a special tile type that the NPC can walk on
     */
    private void moveToTarget(Point target, int specialWalkable) {
        if (target == null) return;

        List<Point> path = aStarPathfinding(new Point(xpos, ypos), target, specialWalkable);
        if (path != null && !path.isEmpty()) {
            Point nextStep = path.get(0);
            if (nextStep.x > xpos) {
                if (map[ypos][xpos + 1] == 1 || map[ypos][xpos + 1] == 7) {
                    stone++;
                } else if (map[ypos][xpos + 1] == 3 || map[ypos][xpos + 1] == 8) {
                    wood++;
                }
                xpos++;
                img = rightImage;
            } else if (nextStep.x < xpos) {
                if (map[ypos][xpos - 1] == 1 || map[ypos][xpos - 1] == 7) {
                    stone++;
                } else if (map[ypos][xpos - 1] == 3 || map[ypos][xpos - 1] == 8) {
                    wood++;
                }
                xpos--;
                img = leftImage;
            } else if (nextStep.y > ypos) {
                if (map[ypos + 1][xpos] == 1 || map[ypos + 1][xpos] == 7) {
                    stone++;
                } else if (map[ypos + 1][xpos] == 3 || map[ypos + 1][xpos] == 8) {
                    wood++;
                }
                ypos++;
            } else if (nextStep.y < ypos) {
                if (map[ypos - 1][xpos] == 1 || map[ypos - 1][xpos] == 7) {
                    stone++;
                } else if (map[ypos - 1][xpos] == 3 || map[ypos - 1][xpos] == 8) {
                    wood++;
                }
                ypos--;
            }
        }
    }

    /**
     * A* pathfinding algorithm to find a path from start to goal.
     * @param start the starting point
     * @param goal the goal point
     * @param specialWalkable a special tile type that the NPC can walk on
     * @return a list of points representing the path, or null if no path found
     */
    private List<Point> aStarPathfinding(Point start, Point goal, int specialWalkable) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost + n.heuristic));
        Map<Point, Point> cameFrom = new HashMap<>();
        Map<Point, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);

        openSet.add(new Node(start.x, start.y, 0, heuristic(start, goal)));

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            Point currentPoint = new Point(current.x, current.y);

            if (currentPoint.equals(goal)) {
                return reconstructPath(cameFrom, currentPoint);
            }

            for (int[] dir : new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
                Point neighbor = new Point(current.x + dir[0], current.y + dir[1]);
                if (specialWalkable != 756 && (neighbor.x < 0 || neighbor.y < 0 || neighbor.x >= maxWidth || neighbor.y >= maxHeight || (map[neighbor.y][neighbor.x] != 0 && map[neighbor.y][neighbor.x] != specialWalkable))) {
                    continue;
                }

                int tentativeGScore = gScore.getOrDefault(currentPoint, Integer.MAX_VALUE) + 1;
                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbor, currentPoint);
                    gScore.put(neighbor, tentativeGScore);
                    openSet.add(new Node(neighbor.x, neighbor.y, tentativeGScore, heuristic(neighbor, goal)));
                }
            }
        }
        return null;
    }

    /**
     * Reconstruct the path from the cameFrom map.
     * @param cameFrom the map of points and their predecessors
     * @param current the current point
     * @return the reconstructed path as a list of points
     */
    private List<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
        List<Point> path = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            path.add(0, current);
            current = cameFrom.get(current);
        }
        return path;
    }

    /**
     * Heuristic function for A* pathfinding (Manhattan distance).
     * @param a the first point
     * @param b the second point
     * @return the heuristic cost between the two points
     */
    private int heuristic(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y); // Manhattan distance
    }

    /**
     * Node class for A* pathfinding.
     * Contains coordinates, cost from start, and heuristic cost to goal.
     */
    private static class Node {
        int x, y, cost, heuristic;

        Node(int x, int y, int cost) {
            this(x, y, cost, 0);
        }

        Node(int x, int y, int cost, int heuristic) {
            this.x = x;
            this.y = y;
            this.cost = cost;
            this.heuristic = heuristic;
        }
    }

    /**
     * Getters for NPC properties.
     * @return the respective property value
     */
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

    public int getWood() {
        return wood;
    }

    public int getStone() {
        return stone;
    }

    public String getTask() {
        return task;
    }

    public ArrayList<String> getTraits() {
        return traits;
    }
    
}