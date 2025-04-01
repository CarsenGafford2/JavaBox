package com.example.rpg;

import java.util.*;
import java.awt.Point;

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
    // private int wood;
    private Image rightImage;
    private Image leftImage;
    private int varient = 0;

    private int wood = 0;
    private int stone = 0;

    private int buildStep = 0;

    List<Point> path;
    public Npc(int xpos, int ypos, String name, int[][] map) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.name = name;
        this.maxHeight = map.length;
        this.maxWidth = map[0].length;
        this.map = map;
        this.task = "build";

        Random rand = new Random();
        this.varient = rand.nextInt(21);
        this.rightImage = new Image(getClass().getResource("res/npc/guy" + this.varient + ".png").toString());
        this.leftImage = new Image(getClass().getResource("res/npc/guy" + this.varient + "r" + ".png").toString());
        this.img = rightImage;
    }

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
            if (stone >= 11) {
                this.task = "wander";
                path = null;
            } else {
                Point target = findNearestResource("stone");
                if (target != null) {
                    moveToTarget(target, 1);
                }
            }
        } else if (this.task.equals("wood")) {
            if (wood >= 14) {
                this.task = "wander";
                path = null;
            } else {
                Point target = findNearestResource("wood");
                if (target != null) {
                    moveToTarget(target, 3);
                }
            }
        } else if (this.task.equals("build")) {
                List<Map.Entry<Point, Integer>> mediumBlueprint = Arrays.asList(
                new AbstractMap.SimpleEntry<>(new Point(xpos, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos, ypos), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 9),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 11),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos, ypos), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 10),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos, ypos), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos, ypos), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos, ypos), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos, ypos), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos, ypos), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos, ypos), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 7)
                );
                List<Map.Entry<Point, Integer>> basicBlueprint = Arrays.asList(
                new AbstractMap.SimpleEntry<>(new Point(xpos, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos, ypos), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 9),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 11),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos, ypos), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 10),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos, ypos), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 8),
                new AbstractMap.SimpleEntry<>(new Point(xpos - 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos, ypos), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 12),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 7),
                new AbstractMap.SimpleEntry<>(new Point(xpos + 1, ypos - 1), 7)
                );

                if (buildStep < basicBlueprint.size()) {
                if (this.wood < 14) {
                    this.task = "wood";
                } else if (this.stone < 11) {
                    this.task = "stone";
                } else {
                    Map.Entry<Point, Integer> instruction = basicBlueprint.get(buildStep);
                    Point target = instruction.getKey();
                    int tileType = instruction.getValue();

                    // Ensure the target is within map bounds
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
                this.stone -= 11;
                this.wood -= 14;
                this.buildStep = 0;
                }
        }
    }
        private Point findNearestResource(String resource) {
            int targetType;
            if (resource.equals("stone")) {
                targetType = 1;
            } else if (resource.equals("wood")) {
                targetType = 3;
            } else {
                targetType = -1;
            }
            if (targetType == -1) return null;

            boolean[][] visited = new boolean[maxHeight][maxWidth];
            PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
            queue.add(new Node(xpos, ypos, 0));
            visited[ypos][xpos] = true;

            while (!queue.isEmpty()) {
                Node current = queue.poll();
                int cx = current.x;
                int cy = current.y;

                if (map[cy][cx] == targetType) {
                    return new Point(cx, cy); // Found the nearest resource
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
            return null;
        }

        private void moveToTarget(Point target, int specialWalkable) {
                if (target == null) return;

                List<Point> path = aStarPathfinding(new Point(xpos, ypos), target, specialWalkable);
                if (path != null && !path.isEmpty()) {
                    Point nextStep = path.get(0);
                    if (nextStep.x > xpos) {
                        if (map[ypos][xpos + 1] == 1) {
                            stone++;
                        } else if (map[ypos][xpos + 1] == 3) {
                            wood++;
                        }
                        xpos++;
                        img = rightImage;
                    } else if (nextStep.x < xpos) {
                        if (map[ypos][xpos - 1] == 1) {
                            stone++;
                        } else if (map[ypos][xpos - 1] == 3) {
                            wood++;
                        }
                        xpos--;
                        img = leftImage;
                    } else if (nextStep.y > ypos) {
                        if (map[ypos + 1][xpos] == 1) {
                            stone++;
                        } else if (map[ypos + 1][xpos] == 3) {
                            wood++;
                        }
                        ypos++;
                    } else if (nextStep.y < ypos) {
                        if (map[ypos - 1][xpos] == 1) {
                            stone++;
                        } else if (map[ypos - 1][xpos] == 3) {
                            wood++;
                        }
                        ypos--;
                    }
                }
            }

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

            private List<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
                List<Point> path = new ArrayList<>();
                while (cameFrom.containsKey(current)) {
                    path.add(0, current);
                    current = cameFrom.get(current);
                }
                return path;
            }

            private int heuristic(Point a, Point b) {
                return Math.abs(a.x - b.x) + Math.abs(a.y - b.y); // Manhattan distance
            }

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
    
}