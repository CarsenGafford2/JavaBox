package com.example.rpg;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class PrimaryController {
    @FXML
    private GridPane gridPane;

    @FXML
    private TextFlow textFlow;

    private int[][] map;
    private int[][] cleanMap;
    private int x = 0;
    private int y = 0;
    private int percent = 0;
    private int numberKeypressed = 0;

    private String grass = getClass().getResource("res/textures/grass.png").toString();
    private String rock = getClass().getResource("res/textures/rock.png").toString();
    private String tree = getClass().getResource("res/textures/tree.png").toString();
    private String water = getClass().getResource("res/textures/water.png").toString();
    private String sand = getClass().getResource("res/textures/sand.png").toString();
    private String brick = getClass().getResource("res/textures/brick.png").toString();


    private Image grassImg = new Image(grass);
    private Image rockImg = new Image(rock);
    private Image treeImage = new Image(tree);
    private Image waterImage = new Image(water);
    private Image sandImage = new Image(sand);
    private Image brickImage = new Image(brick);

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
        gridPane.setFocusTraversable(true);
        gridPane.requestFocus();

        Timer t = new Timer();


        Random r = new Random();
        System.out.print("\033[H\033[2J");
        int i = 10000;
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
                    // map[guy.getyPos()][guy.getxPos()] = cleanMap[guy.getyPos()][guy.getxPos()];
                    map[guy.getyPos()][guy.getxPos()] = 0;
                    guy.move();
                    drawNpc(guy);
                }
                for (mob guy : new ArrayList<>(mobList)) {
                    // map[guy.getyPos()][guy.getxPos()] = cleanMap[guy.getyPos()][guy.getxPos()];
                    map[guy.getyPos()][guy.getxPos()] = 0;
                    guy.move();
                    drawMob(guy);
                }
                renderMap();
            }
        }, 0, 500);

        gridPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
                        y = Math.max(0, y - 1);
                    } else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
                        y = Math.min(map.length - 10, y + 1);
                    } else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                        x = Math.max(0, x - 1);
                    } else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                        x = Math.min(map[0].length - 10, x + 1);
                    } else if (event.getCode() == KeyCode.DIGIT1) {
                        numberKeypressed = 0;
                    } else if (event.getCode() == KeyCode.DIGIT2) {
                        numberKeypressed = 1;
                    } else if (event.getCode() == KeyCode.DIGIT3) {
                        numberKeypressed = 2;
                    } else if (event.getCode() == KeyCode.DIGIT4) {
                        numberKeypressed = 3;
                    } else if (event.getCode() == KeyCode.DIGIT5) {
                        numberKeypressed = 4;
                    } else if (event.getCode() == KeyCode.DIGIT6) {
                        numberKeypressed = 5;
                    } else if (event.getCode() == KeyCode.DIGIT7) {
                        numberKeypressed = 6;
                    }
                    renderMap();
                });
            }
        });

        renderMap();
        log("Test");
        }

        private void log(String message) {
            Platform.runLater(() -> {
                textFlow.getChildren().add(new Text(message + "\n"));
            });
        }
        
        private int[][] generateMap(int width, int height) {
            int[][] generatedMap = new int[height][width];
            long seed = 781029377;
        
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
            gridPane.getChildren().clear();

            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    int mapRow = y + row;
                    int mapCol = x + col;

                    if (mapRow >= map.length || mapCol >= map[0].length) {
                        continue;
                    }

                    Image image = grassImg;
                    if (map[mapRow][mapCol] == 0) { // Grass
                        image = grassImg;
                    } else if (map[mapRow][mapCol] == 1) { // Stone
                        image = rockImg;
                    } else if (map[mapRow][mapCol] == 2) { // Npc
                        for (Npc guy : npcList) {
                            if (guy.getxPos() == mapCol && guy.getyPos() == mapRow) {
                                image = guy.getImage();
                            }
                        }
                    } else if (map[mapRow][mapCol] == 3) { // Tree
                        image = treeImage;
                    } else if (map[mapRow][mapCol] == 4) { // Mob
                        for (mob guy : mobList) {
                            if (guy.getxPos() == mapCol && guy.getyPos() == mapRow) {
                                image = guy.getImage();
                            }
                        }
                    } else if (map[mapRow][mapCol] == 5) { // Water
                        image = waterImage;
                    } else if (map[mapRow][mapCol] == 6) { // Sand
                        image = sandImage;
                    } else if (map[mapRow][mapCol] == 7) { // Brick
                        image = brickImage;
                    }
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);
                    imageView.setPreserveRatio(true);

                    if (map[mapRow][mapCol] == 2) {
                        Tooltip t = new Tooltip("Error");
                        for (Npc guy : npcList) {
                            if (guy.getxPos() == mapCol && guy.getyPos() == mapRow) {
                                t = new Tooltip(guy.getName());
                            }
                        }
                        t.setShowDelay(javafx.util.Duration.ZERO);
                        Tooltip.install(imageView, t);
                    }
                    if (map[mapRow][mapCol] == 4) {
                        Tooltip t = new Tooltip("Error");
                        for (mob guy : mobList) {
                            if (guy.getxPos() == mapCol && guy.getyPos() == mapRow) {
                                t = new Tooltip(guy.getName());
                            }
                        }
                        t.setShowDelay(javafx.util.Duration.ZERO);
                        Tooltip.install(imageView, t);
                    }

                    int clickedRow = mapRow;
                    int clickedCol = mapCol;
                    imageView.setOnMouseClicked(event -> {
                        if (numberKeypressed == 0) {
                            spawnNpc(clickedRow, clickedCol);
                            renderMap();
                        } else if (numberKeypressed == 1) {
                            spawnMob(clickedRow, clickedCol);
                            renderMap();
                        } else if (numberKeypressed == 2) {
                            map[clickedRow][clickedCol] = 1;
                            renderMap();
                        } else if (numberKeypressed == 3) {
                            map[clickedRow][clickedCol] = 3;
                            renderMap();
                        } else if (numberKeypressed == 4) {
                            map[clickedRow][clickedCol] = 5;
                            renderMap();
                        } else if (numberKeypressed == 5) {
                            map[clickedRow][clickedCol] = 6;
                            renderMap();
                        } else if (numberKeypressed == 6) {
                            map[clickedRow][clickedCol] = 7;
                            renderMap();
                        }
                    });

                    gridPane.add(imageView, col, row);
                }
            }
        });
    }
}