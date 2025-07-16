package com.example.rpg;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;


import java.awt.Color;
import java.awt.image.BufferedImage;

public class PrimaryController {

    private int TILE_SIZE = 50;
    private int TILES_TO_RENDER = 11;

    @FXML
    private Canvas canvas;

    private int[][] map;
    private int[][] cleanMap;
    private double cameraX = 0;
    private double cameraY = 0;
    private int percent = 0;
    private int numberKeypressed = 0;
    private double moveSpeed = 0.1;
    private boolean upPressed, downPressed, leftPressed, rightPressed;


    private String grass = checkForModTexture("mods/textures/grass.png", "res/textures/grass.png");
    private String rock = checkForModTexture("mods/textures/rock.png", "res/textures/rock.png");
    private String tree = checkForModTexture("mods/textures/tree.png", "res/textures/tree.png");
    private String water = checkForModTexture("mods/textures/water.png", "res/textures/water.png");
    private String sand = checkForModTexture("mods/textures/sand.png", "res/textures/sand.png");
    private String brick = checkForModTexture("mods/textures/brick.png", "res/textures/brick.png");
    private String wood = checkForModTexture("mods/textures/wood.png", "res/textures/wood.png");
    private String bedTop = checkForModTexture("mods/textures/bedTop.png", "res/textures/bedTop.png");
    private String bedBottom = checkForModTexture("mods/textures/bedBottom.png", "res/textures/bedBottom.png");
    private String nightstand = checkForModTexture("mods/textures/nightstand.png", "res/textures/nightstand.png");
    private String door = checkForModTexture("mods/textures/door.png", "res/textures/door.png");
    private String chair = checkForModTexture("mods/textures/chair.png", "res/textures/chair.png");
    private String tableBleft = checkForModTexture("mods/textures/tableBleft.png", "res/textures/tableBleft.png");
    private String tableBright = checkForModTexture("mods/textures/tableBright.png", "res/textures/tableBright.png");
    private String tableTright = checkForModTexture("mods/textures/tableTright.png", "res/textures/tableTright.png");
    private String tableTleft = checkForModTexture("mods/textures/tableTleft.png", "res/textures/tableTleft.png");

    private String checkForModTexture(String modPath, String defaultPath) {
        boolean mod = false;
        if (getClass().getResource(modPath) != null && mod) {
            return getClass().getResource(modPath).toString();
        } else {
            if (getClass().getResource(defaultPath) != null) {
                return getClass().getResource(defaultPath).toString();
            } else {
                throw new IllegalArgumentException("Resource not found: " + defaultPath);
            }
        }
    }


    private Image grassImg = new Image(grass);
    private Image rockImg = new Image(rock);
    private Image treeImage = new Image(tree);
    private Image waterImage = new Image(water);
    private Image sandImage = new Image(sand);
    private Image brickImage = new Image(brick);
    private Image woodImage = new Image(wood);
    private Image bedTopImage = new Image(bedTop);
    private Image bedBottomImage = new Image(bedBottom);
    private Image nightstandImage = new Image(nightstand);
    private Image doorImage = new Image(door);
    private Image chairImage = new Image(chair);
    private Image tableBleftImage = new Image(tableBleft);
    private Image tableBrightImage = new Image(tableBright);
    private Image tableTrightImage = new Image(tableTright);
    private Image tableTleftImage = new Image(tableTleft);

    private ArrayList<Npc> npcList = new ArrayList<>();
    private ArrayList<mob> mobList = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> cowNames = new ArrayList<>();
    private ArrayList<String> sheepNames = new ArrayList<>();
    private ArrayList<String> chickenNames = new ArrayList<>();

    public void initialize() {
        System.out.print("\033[H\033[2J");  
        int mapWidth = 500;
        int mapHeight = 500;

        setupCanvasMouseEvents();

        cleanMap = new int[mapHeight][mapWidth];

        map = generateMap(mapWidth, mapHeight);
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                cleanMap[i][j] = map[i][j];
            }
        }
        saveMapAsImage("map.png");


        File file = new File("");
        try {
            file = new File(getClass().getResource("names/names.txt").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                names.add(scan.nextLine());
            }
            scan.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        try {
            file = new File(getClass().getResource("names/cowNames.txt").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                cowNames.add(scan.nextLine());
            }
            scan.close();
        } catch (Exception e) {
            System.err.println(e);
        }

        try {
            file = new File(getClass().getResource("names/sheepNames.txt").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                sheepNames.add(scan.nextLine());
            }
            scan.close();
        } catch (Exception e) {
            System.err.println(e);
        }

        try {
            file = new File(getClass().getResource("names/chickenNames.txt").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                chickenNames.add(scan.nextLine());
            }
            scan.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        canvas.setFocusTraversable(true);
        canvas.requestFocus();

        Timer t = new Timer();


        Random r = new Random();
        System.out.print("\033[H\033[2J");
        int i = 1000;
        for (int index = 0; index < i; index++) {
            int x = r.nextInt(mapWidth);
            int y = r.nextInt(mapHeight);
            if (map[x][y] == 0) {
                spawnMob(x, y);
            }
            System.out.print("\rSpawning Mobs... " + percent + "%");
            percent = (index * 100) / i;
        }

        t.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Npc guy : new ArrayList<>(npcList)) {
                    map[guy.getyPos()][guy.getxPos()] = 0;
                    guy.move();
                    drawNpc(guy);
                }
                List<mob> mobListCopy = new ArrayList<>(mobList);
                for (mob guy : mobListCopy) {
                    int mobY = guy.getyPos();
                    int mobX = guy.getxPos();
                    if (mobY >= 0 && mobY < map.length && mobX >= 0 && mobX < map[0].length) {
                        map[mobY][mobX] = 0;
                        if (guy.isHunted()) {
                            mobList.remove(guy);
                        } else {
                            guy.move();
                            drawMob(guy);
                        }
                    } else {
                        System.err.println("Error: Mob position out of bounds: (" + mobY + ", " + mobX + ")");
                    }
                }
                renderMap();
            }
        }, 0, 500);

        canvas.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP) upPressed = true;
                    if (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN) downPressed = true;
                    if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) leftPressed = true;
                    if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) rightPressed = true;
                    if (event.getCode() == KeyCode.SHIFT) moveSpeed = 0.3;
                    if (event.getCode() == KeyCode.EQUALS) {
                        TILE_SIZE = Math.min(100, TILE_SIZE + 5);
                        TILES_TO_RENDER = 700 / TILE_SIZE;
                        renderMap();
                    }
                    if (event.getCode() == KeyCode.MINUS) {
                        TILE_SIZE = Math.max(5, TILE_SIZE - 5);
                        TILES_TO_RENDER = 700 / TILE_SIZE;
                    }
                });

                newScene.setOnKeyReleased(event -> {
                    if (event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP) upPressed = false;
                    if (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN) downPressed = false;
                    if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) leftPressed = false;
                    if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) rightPressed = false;
                    if (event.getCode() == KeyCode.SHIFT) moveSpeed = 0.1;
                });

            }
        });

        renderMap();
        }

        private void setupCanvasMouseEvents() {
            canvas.setOnMouseClicked(event -> {
                int col = (int)(event.getX() / TILE_SIZE);
                int row = (int)(event.getY() / TILE_SIZE);

                int mapRow = (int)(cameraY + row);
                int mapCol = (int)(cameraX + col);

                if (mapRow >= map.length || mapCol >= map[0].length) return;

                switch (numberKeypressed) {
                    case 0:
                        spawnNpc(mapRow, mapCol);
                        break;
                    case 1:
                        spawnMob(mapRow, mapCol);
                        break;
                    case 2:
                        map[mapRow][mapCol] = 1;
                        break;
                    case 3:
                        map[mapRow][mapCol] = 3;
                        break;
                    case 4:
                        map[mapRow][mapCol] = 5;
                        break;
                    case 5:
                        map[mapRow][mapCol] = 6;
                        break;
                    case 6:
                        map[mapRow][mapCol] = 7;
                        break;
                    case 7:
                        map[mapRow][mapCol] = 8;
                        break;
                    case 78:
                        map[mapRow][mapCol] = 0;
                        break;
                }

                renderMap();
            });
        }



        private int[][] generateMap(int width, int height) {
            int[][] generatedMap = new int[height][width];
            Random rand = new Random();
            long seed = 100 + (long) (rand.nextDouble() * (1000000 - 100));
        
            for (int y = 0; y < height; y++) {
                int percent = (y * 100) / height;
                System.out.print("\rGenerating map... " + percent + "%");
        
                for (int x = 0; x < width; x++) {
                    double value = OpenSimplex2S.noise2(seed, x * 0.05, y * 0.05);
                    
                    if (value < -0.6) {
                        generatedMap[y][x] = 5; // Water (5)
                    } else if (value < -0.5) {
                        generatedMap[y][x] = 6; // Sand (6)
                    } else if (value < 0.1) {
                            generatedMap[y][x] = 0; // Tree (3)
                    } else if (value < 0.2) {
                        generatedMap[y][x] = 1; // Stone (1)
                    } else {
                        generatedMap[y][x] = 3; // Grass (0)
                    }
                }
            }
        
            return generatedMap;
        }       
        
        public void saveMapAsImage(String fileName) {
            int cellSize = 1;
            int imageWidth = map[0].length * cellSize;
            int imageHeight = map.length * cellSize;

            BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

            for (int row = 0; row < map.length; row++) {
                for (int col = 0; col < map[0].length; col++) {
                    Color color;
                    switch (map[row][col]) {
                        case 0:
                            color = new Color(34, 139, 34); // Green
                            break;
                        case 1:
                            color = new Color(169, 169, 169); // Gray
                            break;
                        case 2:
                            color = new Color(255, 0, 0); // Red
                            break;
                        case 3:
                            color = new Color(0, 100, 0); // Dark Green
                            break;
                        case 4:
                            color = new Color(0, 0, 255); // Blue
                            break;
                        case 5:
                            color = new Color(0, 191, 255); // Light Blue
                            break;
                        case 6:
                            color = new Color(194, 178, 128); // Sand color
                            break;
                        default:
                            color = Color.BLACK; // Unknown
                    }

                    for (int y = 0; y < cellSize; y++) {
                        for (int x = 0; x < cellSize; x++) {
                            image.setRGB(col * cellSize + x, row * cellSize + y, color.getRGB());
                        }
                    }
                }
            }

            try {
                File outputFile = new File(fileName);
                ImageIO.write(image, "png", outputFile);
                System.out.println("Map saved as " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }

            AnimationTimer timer = new AnimationTimer() {
                private long lastUpdate = 0;

                @Override
                public void handle(long now) {
                    if (lastUpdate == 0) {
                        lastUpdate = now;
                        return;
                    }
                    double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                    lastUpdate = now;

                    double speed = moveSpeed * deltaTime * 60; // scale to 60 FPS

                    boolean moved = false;
                    if (upPressed) {
                        cameraY = Math.max(0, cameraY - speed);
                        moved = true;
                    }
                    if (downPressed) {
                        cameraY = Math.min(map.length - 10, cameraY + speed);
                        moved = true;
                    }
                    if (leftPressed) {
                        cameraX = Math.max(0, cameraX - speed);
                        moved = true;
                    }
                    if (rightPressed) {
                        cameraX = Math.min(map[0].length - 10, cameraX + speed);
                        moved = true;
                    }

                    if (moved) renderMap();
                }
            };
            timer.start();

        }

    private void spawnNpc(int x, int y) {
        Random rand = new Random();
        if (!names.isEmpty()) {
            npcList.add(new Npc(y, x, names.get(rand.nextInt(names.size())), map));
            if (npcList.get(npcList.size()-1).getName().equals("Bodger")) {
                System.out.println("Bodger Spawned");
            }
            drawNpc(npcList.get(npcList.size() - 1));
        } else {
            System.err.println("Error: Names list is empty. Cannot spawn NPC.");
        }
    }

    private void drawNpc(Npc guy) {
        map[guy.getyPos()][guy.getxPos()] = 2;
    }

    private void spawnMob(int x, int y) {
        Random rand = new Random();
        int temp = rand.nextInt(3);
        if (temp == 0) {
            if (!cowNames.isEmpty()) {
                mobList.add(new mob(y, x, cowNames.get(rand.nextInt(cowNames.size())), "cow", map));
                drawMob(mobList.get(mobList.size() - 1));
            } else {
                System.err.println("Error: Names list is empty. Cannot spawn mob.");
            }
        } else if (temp == 1) {
            if (!sheepNames.isEmpty()) {
                mobList.add(new mob(y, x, sheepNames.get(rand.nextInt(sheepNames.size())), "sheep", map));
                drawMob(mobList.get(mobList.size() - 1));
            } else {
                System.err.println("Error: Names list is empty. Cannot spawn mob.");
            }
        } else if (temp == 2) {
            if (!chickenNames.isEmpty()) {
                if (rand.nextInt(1000) == 0) {// 0.1% chance of BIG BIRD
                    mobList.add(new mob(y, x, "Big Bird", "jumbo", map));
                } else {
                    mobList.add(new mob(y, x, chickenNames.get(rand.nextInt(chickenNames.size())), "chicken", map));
                }
                drawMob(mobList.get(mobList.size() - 1));
            } else {
                System.err.println("Error: Names list is empty. Cannot spawn mob.");
            }
        }
    }

    private void drawMob(mob guy) {
        map[guy.getyPos()][guy.getxPos()] = 4;
    }

    private void renderMap() {
        Platform.runLater(() -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            for (int row = 0; row < TILES_TO_RENDER; row++) {
                for (int col = 0; col < TILES_TO_RENDER; col++) {
                    int mapRow = (int)(cameraY + row);
                    int mapCol = (int)(cameraX + col);

                    if (mapRow < 0 || mapCol < 0 || mapRow >= map.length || mapCol >= map[0].length) {
                        continue;
                    }

                    Image image;
                    switch (map[mapRow][mapCol]) {
                        case 0:
                            image = grassImg;
                            break;
                        case 1:
                            image = rockImg;
                            break;
                        case 3:
                            image = treeImage;
                            break;
                        case 5:
                            image = waterImage;
                            break;
                        case 6:
                            image = sandImage;
                            break;
                        case 7:
                            image = brickImage;
                            break;
                        case 8:
                            image = woodImage;
                            break;
                        case 9:
                            image = bedTopImage;
                            break;
                        case 10:
                            image = bedBottomImage;
                            break;
                        case 11:
                            image = nightstandImage;
                            break;
                        case 12:
                            image = doorImage;
                            break;
                        case 13:
                            image = chairImage;
                            break;
                        case 14:
                            image = tableBleftImage;
                            break;
                        case 15:
                            image = tableBrightImage;
                            break;
                        case 16:
                            image = tableTrightImage;
                            break;
                        case 17:
                            image = tableTleftImage;
                            break;
                        default:
                            image = grassImg;
                            break;
                    }

                    // NPC check
                    if (map[mapRow][mapCol] == 2) {
                        for (Npc guy : npcList) {
                            if (guy.getxPos() == mapCol && guy.getyPos() == mapRow) {
                                image = guy.getImage();
                                break;
                            }
                        }
                    }

                    // Mob check
                    if (map[mapRow][mapCol] == 4) {
                        for (mob guy : mobList) {
                            if (guy.getxPos() == mapCol && guy.getyPos() == mapRow) {
                                image = guy.getImage();
                                break;
                            }
                        }
                    }

                    double drawX = Math.round(col * TILE_SIZE - (cameraX % 1) * TILE_SIZE);
                    double drawY = Math.round(row * TILE_SIZE - (cameraY % 1) * TILE_SIZE);
                    gc.drawImage(image, drawX, drawY, TILE_SIZE, TILE_SIZE);

                }
            }
        });
    }
}