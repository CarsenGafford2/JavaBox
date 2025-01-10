package com.example.rpg;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

public class PrimaryController {
    @FXML
    private GridPane gridPane;

    private int[][] map;
    private int x = 0;
    private int y = 0;
    private int percent = 0;

    private String grass = getClass().getResource("res/grass.png").toString();
    private String rock = getClass().getResource("res/rock.png").toString();
    private String tree = getClass().getResource("res/tree.png").toString();

    private Image grassImg = new Image(grass);
    private Image rockImg = new Image(rock);
    private Image treeImage = new Image(tree);

    private ArrayList<Npc> npcList = new ArrayList<>();
    private ArrayList<mob> mobList = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> cowNames = new ArrayList<>();
    private ArrayList<String> sheepNames = new ArrayList<>();

    public void initialize() {
        System.out.print("\033[H\033[2J");  
        int mapWidth = 500;
        int mapHeight = 500;

        map = generateMap(mapWidth, mapHeight);


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
                    map[guy.getyPos()][guy.getxPos()] = 0;
                    guy.move();
                    drawNpc(guy);
                }
                for (mob guy : new ArrayList<>(mobList)) {
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
                    if (event.getCode() == KeyCode.UP) {
                        y = Math.max(0, y - 1);
                    } else if (event.getCode() == KeyCode.DOWN) {
                        y = Math.min(map.length - 1, y + 1);
                    } else if (event.getCode() == KeyCode.LEFT) {
                        x = Math.max(0, x - 1);
                    } else if (event.getCode() == KeyCode.RIGHT) {
                        x = Math.min(map[0].length - 1, x + 1);
                    }
                    renderMap();
                });
            }
        });

        renderMap();
        }

        private int[][] generateMap(int width, int height) {
        int[][] generatedMap = new int[height][width];
        Random random = new Random();
        
        for (int y = 0; y < height; y++) {
            System.out.print("\rGenerating map... " + percent + "%");
            percent = (y * 100) / height;
            for (int x = 0; x < width; x++) {
            long seed = 781029377;
            double value = OpenSimplex2S.noise2(seed, x * 0.1, y * 0.1); // Adjust scale for larger groups
        
            if (value < -0.2) {
                generatedMap[y][x] = 3; // Tree (3)
            } else if (value < 0.2) {
                if (random.nextDouble() < 0.7) {
                generatedMap[y][x] = 1; // Stone (1)
                } else {
                generatedMap[y][x] = 0; // Grass (0)
                }
            } else {
                generatedMap[y][x] = 0; // Grass (0)
            }
            }
        }
        return generatedMap;
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
        int temp = rand.nextInt(2);
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
                    if (map[mapRow][mapCol] == 0) {
                        image = grassImg;
                    } else if (map[mapRow][mapCol] == 1) {
                        image = rockImg;
                    } else if (map[mapRow][mapCol] == 2) {
                        for (Npc guy : npcList) {
                            if (guy.getxPos() == mapCol && guy.getyPos() == mapRow) {
                                image = guy.getImage();
                            }
                        }
                    } else if (map[mapRow][mapCol] == 3) {
                        image = treeImage;
                    } else if (map[mapRow][mapCol] == 4) {
                        for (mob guy : mobList) {
                            if (guy.getxPos() == mapCol && guy.getyPos() == mapRow) {
                                image = guy.getImage();
                            }
                        }
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
                        if (map[clickedRow][clickedCol] == 0) {
                            spawnNpc(clickedRow, clickedCol);
                            renderMap();
                        }
                    });

                    gridPane.add(imageView, col, row);
                }
            }
        });
    }
}