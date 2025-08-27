package com.example.rpg;

/*
 * Import statements
 */
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import javax.imageio.ImageIO;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Primary Controller is the main class and handles everything from map generation to rendering to NPC and mob management.
 * It uses JavaFX canvas for rendering and OpenSimplex2S for procedural map generation.
 * The class also supports modding by allowing custom textures to be loaded from a "mods" folder.
 * 
 * @author Carsen Gafford
 * @version alpha v0.2.0
 */
public class PrimaryController {

    /*
     * This is just basic initialization for the size of tiles and the amount that are rendered at once.
     * You can change these values to adjust the zoom level and how much of the map is visible.
     * The player has the option to change the zoom level with the + and - keys or the scroll wheel.
     */
    private int TILE_SIZE = 50;
    private int TILES_TO_RENDER = 11;

    /*
     * FXML Canvas for rendering the game.
     */
    @FXML
    private Canvas canvas;

    /*
     * Arrays for holding both the map and the clean map (without NPCs or mobs).
     */
    private int[][] map;
    private int[][] cleanMap;

    /*
     * Stores camera positioning.
     */
    private double cameraX = 0;
    private double cameraY = 0;

    private long npcUpdateTime;

    private static final Random rand = new Random();

    /*
     * Variables for handling NPC and mob spawning and movement.
     */
    private int numberKeypressed = 0;
    private double moveSpeed = 0.1;
    private boolean upPressed, downPressed, leftPressed, rightPressed;

    /*
     * Texture paths, checking for modded textures first, then defaulting to built-in textures.
     * Needs to be probably re-worked and moved into its own class at some point.
     */
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
    private String shelterBleft = checkForModTexture("mods/textures/shelterBleft.png", "res/textures/shelterBleft.png");
    private String shelterBright = checkForModTexture("mods/textures/shelterBright.png", "res/textures/shelterBright.png");
    private String shelterTright = checkForModTexture("mods/textures/shelterTright.png", "res/textures/shelterTright.png");
    private String shelterTleft = checkForModTexture("mods/textures/shelterTleft.png", "res/textures/shelterTleft.png");
    private String explosive = checkForModTexture("mods/textures/explosive.png", "res/textures/explosive.png");
    private String flame = checkForModTexture("mods/textures/flame.png", "res/textures/flame.png");
    private String burntGrass = checkForModTexture("mods/textures/burntGrass.png", "res/textures/burntGrass.png");


    /**
     * Checks for a modded texture first, if not found, defaults to the built-in texture.
     * @param modPath The path to the modded texture. (needs to be re programmed for different named folders)
     * @param defaultPath The path to the default texture.
     * @return The path to the texture to be used.
     * @throws IllegalArgumentException if neither texture is found.
     */
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

    /*
     * Image objects for each texture, loaded from the paths defined above. (could also be re-worked to be more efficient)
     */
    private Image[] imageArray = new Image[23];
    int in = 0;
    private Image grassImg = new Image(grass);
    imageArray[in] = grassImg;
    in++;
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
    private Image shelterBleftImage = new Image(shelterBleft);
    private Image shelterBrightImage = new Image(shelterBright);
    private Image shelterTrightImage = new Image(shelterTright);
    private Image shelterTleftImage = new Image(shelterTleft);
    private Image explosiveImage = new Image(explosive);
    private Image flameImage = new Image(flame);
    private Image burntGrassImage = new Image(burntGrass);

    /*
     * Lists for managing NPCs, mobs, and names.
     * Will need to be re-worked for having additional names and lists in an efficient manner.
     */
    private ArrayList<Npc> npcList = new ArrayList<>();
    private ArrayList<mob> mobList = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Explosive> activeExplosives = new ArrayList<>();

    @FXML private Button button1;
    @FXML private Button button2;
    @FXML private Button button3;
    @FXML private Button button4;

    /**
     * Initializes the game, generates the map, loads names, spawns NPCs and mobs, and sets up rendering and input handling.
     * This method is called automatically after the FXML elements have been loaded.
     * @throws ClassNotFoundException
     */
    public void initialize() throws ClassNotFoundException {

        Class.forName("com.example.rpg.mobScripts.chicken");
        Class.forName("com.example.rpg.mobScripts.cow");
        Class.forName("com.example.rpg.mobScripts.sheep");
        Class.forName("com.example.rpg.mobScripts.pig");

        button1.setGraphic(new ImageView(new Image(getClass().getResource("/com/example/rpg/res/textures/grass.png").toExternalForm())) {{
            setFitWidth(50);
            setFitHeight(50);
        }});
        button1.setOnMouseClicked((MouseEvent event) -> {
            numberKeypressed = 0;
        });
        button2.setGraphic(new ImageView(new Image(getClass().getResource("/com/example/rpg/res/textures/sand.png").toExternalForm())) {{
            setFitWidth(50);
            setFitHeight(50);
        }});
        button2.setOnMouseClicked((MouseEvent event) -> {
            numberKeypressed = 6;
        });
        button3.setGraphic(new ImageView(new Image(getClass().getResource("/com/example/rpg/res/textures/rock.png").toExternalForm())) {{
            setFitWidth(50);
            setFitHeight(50);
        }});
        button3.setOnMouseClicked((MouseEvent event) -> {
            numberKeypressed = 1;
        });
        button4.setGraphic(new ImageView(new Image(getClass().getResource("/com/example/rpg/res/textures/tree.png").toExternalForm())) {{
            setFitWidth(50);
            setFitHeight(50);
        }});
        button4.setOnMouseClicked((MouseEvent event) -> {
            numberKeypressed = 3;
        });


        // Clear console
        System.out.print("\033[H\033[2J");
        // Default map size, will be changed later to be dynamic.
        int mapWidth = 500;
        int mapHeight = 500;

        // Set up mouse events for the canvas
        setupCanvasMouseEvents();
        addMouseHoverHandler();

        cleanMap = new int[mapHeight][mapWidth];

        map = generateMap(mapWidth, mapHeight);
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                cleanMap[i][j] = map[i][j];
            }
        }
        //saves the map in an image, for debugging and also for fun :D
        saveMapAsImage("map.png");


        /*
         * Load names from resources (names.txt, cowNames.txt, sheepNames.txt, chickenNames.txt).
         * Needs to be its own method, this is really messy and inefficient.
         */
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

        canvas.setFocusTraversable(true);
        canvas.requestFocus();

        Timer t = new Timer();

        Timer q = new Timer();


        // Spawn Mobs in random locations on the map
        System.out.print("\033[H\033[2J");
        int i = mapWidth * mapHeight / 50;
        int printInterval = Math.max(i / 100, 1);
        for (int index = 0; index < i; index++) {
            int x = rand.nextInt(mapWidth);
            int y = rand.nextInt(mapHeight);
            if (map[x][y] == 0) {
                spawnMob(x, y);
            }

            // Update the progress only at intervals
            if (index % printInterval == 0 || index == i - 1) {
                int percent = (index * 100) / i;
                System.out.print("\rSpawning Mobs... " + percent + "%");
            }
        }
        System.out.println("\rSpawning Mobs... 100%");


        /*
         * Timer task for moving NPCs and mobs every 500 milliseconds.
         * needs to be adapted to allow user control of time flow.
         */
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                npcUpdateTime = System.currentTimeMillis();

                for (Npc guy : new ArrayList<>(npcList)) {
                    guy.setLastXPos(guy.getxPos());
                    guy.setLastYPos(guy.getyPos());
                    guy.move();
                    drawNpc(guy);
                }
                List<int[]> newFlames = new ArrayList<>();
                double spreadChance = 0.6;
                for (int i = 0; i < map.length; i++) {
                    for (int j = 0; j < map[i].length; j++) {
                        if (map[i][j] == 23) {
                            map[i][j] = 24;
                            if (i + 1 < map.length && map[i + 1][j] == 3 && rand.nextDouble() < spreadChance) {
                                newFlames.add(new int[]{i + 1, j});
                            }
                            if (i - 1 >= 0 && map[i - 1][j] == 3 && rand.nextDouble() < spreadChance) {
                                newFlames.add(new int[]{i - 1, j});
                            }
                            if (j + 1 < map[i].length && map[i][j + 1] == 3 && rand.nextDouble() < spreadChance) {
                                newFlames.add(new int[]{i, j + 1});
                            }
                            if (j - 1 >= 0 && map[i][j - 1] == 3 && rand.nextDouble() < spreadChance) {
                                newFlames.add(new int[]{i, j - 1});
                            }
                        }
                    }
                }
                for (int[] pos : newFlames) {
                    map[pos[0]][pos[1]] = 23;
                }
                for (Explosive explosive : new ArrayList<>(activeExplosives)) {
                    int radius = explosive.getRadius();
                    int xpos = explosive.getXPos();
                    int ypos = explosive.getYPos();
                    for (int y = ypos - radius; y <= ypos + radius; y++) {
                        for (int x = xpos - radius; x <= xpos + radius; x++) {
                            if (y >= 0 && y < map.length && x >= 0 && x < map[0].length) {
                                if (Math.sqrt(Math.pow(x - xpos, 2) + Math.pow(y - ypos, 2)) <= radius) {
                                    if (map[y][x] != 5 && map[y][x] != 6 && map[y][x] != 7) {
                                        map[y][x] = 23;
                                    }
                                }
                            }
                        }
                    }
                    activeExplosives.remove(explosive); // Remove the explosive after detonation
                }
                List<mob> mobListCopy = new ArrayList<>(mobList);
                for (mob guy : mobListCopy) {
                    int mobY = guy.getyPos();
                    int mobX = guy.getxPos();
                    guy.setLastXPos(mobX);
                    guy.setLastYPos(mobY);
                        if (guy.isHunted()) {
                            mobList.remove(guy);
                        } else {
                            guy.move();
                            drawMob(guy);
                        }
                }
            }
        }, 0, 500);

        // Main render loop
        q.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                renderMap();
            }
        }, 0, 8);

        // Handle key presses for movement and other actions.
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
                    }
                    if (event.getCode() == KeyCode.MINUS) {
                        TILE_SIZE = Math.max(5, TILE_SIZE - 5);
                        TILES_TO_RENDER = 700 / TILE_SIZE;
                    }
                    if (event.getCode() == KeyCode.DIGIT1) numberKeypressed = 0;
                    if (event.getCode() == KeyCode.DIGIT2) numberKeypressed = 1;
                    if (event.getCode() == KeyCode.DIGIT3) numberKeypressed = 2;
                    if (event.getCode() == KeyCode.DIGIT4) numberKeypressed = 3;
                    if (event.getCode() == KeyCode.DIGIT5) numberKeypressed = 4;
                    if (event.getCode() == KeyCode.DIGIT6) numberKeypressed = 5;
                    if (event.getCode() == KeyCode.DIGIT7) numberKeypressed = 6;
                    if (event.getCode() == KeyCode.DIGIT8) numberKeypressed = 7;
                    if (event.getCode() == KeyCode.DIGIT9) numberKeypressed = 8;
                    if (event.getCode() == KeyCode.DIGIT0) numberKeypressed = 9;

                    canvas.setOnScroll(scrollEvent -> {
                        if (scrollEvent.getDeltaY() > 0) {
                            TILE_SIZE = Math.min(100, TILE_SIZE + 2);
                        } else if (scrollEvent.getDeltaY() < 0) {
                            TILE_SIZE = Math.max(5, TILE_SIZE - 2);
                        }
                        TILES_TO_RENDER = 700 / TILE_SIZE;
                    });
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

        }

        /**
         * Sets up mouse hover events on the canvas to display NPC names and info when hovered over.
         */
        private void addMouseHoverHandler() {
            canvas.setOnMouseMoved((MouseEvent event) -> {
                double mouseX = event.getX();
                double mouseY = event.getY();

                int tileX = (int) (mouseX / TILE_SIZE + cameraX);
                int tileY = (int) (mouseY / TILE_SIZE + cameraY);

                if (tileX >= 0 && tileX < map[0].length && tileY >= 0 && tileY < map.length) {
                    for (Npc npc : npcList) {
                        if (npc.getxPos() == tileX && npc.getyPos() == tileY) {
                            showNpcTooltip(npc, event);
                            return;
                        }
                    }
                    for (mob mob : mobList) {
                        if (mob.getxPos() == tileX && mob.getyPos() == tileY) {
                            showMobTooltip(mob, event);
                            return;
                        }
                    }
                }
                
                hideTooltip();
            });
        }

        private Tooltip currentTooltip = null;

        /**
         * Displays a tooltip with the NPC's name when hovered over.
         * @param npc The NPC being hovered over.
         * @param event The mouse event triggering the tooltip.
         */
        private void showNpcTooltip(Npc npc, MouseEvent event) {
            if (currentTooltip == null) {
                currentTooltip = new Tooltip();
                Tooltip.install(canvas, currentTooltip);
            }
            currentTooltip.setText(npc.getName());

            currentTooltip.show(canvas, event.getScreenX(), event.getScreenY());
            canvas.setCursor(Cursor.HAND);
        }

        private void showMobTooltip(mob mob, MouseEvent event) {
            if (currentTooltip == null) {
                currentTooltip = new Tooltip();
                Tooltip.install(canvas, currentTooltip);
            }
            currentTooltip.setText(mob.getName());

            currentTooltip.show(canvas, event.getScreenX(), event.getScreenY());
            canvas.setCursor(Cursor.HAND);
        }

        /**
         * Hides the currently displayed tooltip, if any.
         */
        private void hideTooltip() {
            if (currentTooltip != null && currentTooltip.isShowing()) {
                currentTooltip.hide();
            }
            canvas.setCursor(Cursor.DEFAULT);
        }

        /**
         * Sets up mouse click events on the canvas for spawning NPCs, mobs, and changing terrain based on the number key pressed.
         */
        private void setupCanvasMouseEvents() {
            canvas.setOnMouseClicked(event -> {
                // Adjust for camera offset and zoom
                double mouseX = event.getX();
                double mouseY = event.getY();
                int col = (int) (mouseX / TILE_SIZE + cameraX);
                int row = (int) (mouseY / TILE_SIZE + cameraY);

                if (row < 0 || col < 0 || row >= map.length || col >= map[0].length) return;

                map[row][col] = numberKeypressed;
            });
        }

        /**
         * Generates a procedural map using OpenSimplex2S noise.
         * @param width The width of the map.
         * @param height The height of the map.
         * @return A 2D array representing the generated map.
         */
        private int[][] generateMap(int width, int height) {
            int[][] generatedMap = new int[height][width];
            long seed = 100 + (long) (rand.nextDouble() * (1000000 - 100));
        
            for (int y = 0; y < height; y++) {
                int percent = (y * 100) / height;
                System.out.print("\rGenerating map... " + percent + "%");
        
                for (int x = 0; x < width; x++) {
                    double value = OpenSimplex2S.noise2(seed, x * 0.05, y * 0.05);
                    
                    //reference codex doc for better labeled values
                    if (value < -0.6) {
                        generatedMap[y][x] = 5; // Water (5)
                    } else if (value < -0.5) {
                        generatedMap[y][x] = 6; // Sand (6)
                    } else if (value < 0.1) {
                            generatedMap[y][x] = 0; // Tree (3)
                    } else {
                        generatedMap[y][x] = 3; // Grass (0)
                    }
                }
                for (int i = 0; i < 35; i++) {
                    int x = rand.nextInt(width);
                    int b = rand.nextInt(height);
                    if (generatedMap[b][x] == 0) {
                        generatedMap[b][x] = 1; // Rock (1)
                    }
                }
            }
        
            return generatedMap;
        }       
        
        /**
         * Saves the current map as a PNG image for debugging and visualization purposes.
         * @param fileName The name of the file to save the image as.
         */
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

            // Animation timer for smooth camera movement
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

                    if (upPressed) {
                        cameraY = Math.max(0, cameraY - speed);
                    }
                    if (downPressed) {
                        cameraY = Math.min(map.length - 10, cameraY + speed);
                    }
                    if (leftPressed) {
                        cameraX = Math.max(0, cameraX - speed);
                    }
                    if (rightPressed) {
                        cameraX = Math.min(map[0].length - 10, cameraX + speed);
                    }

                }
            };
            timer.start();

        }

    /**
     * Spawns an NPC at the specified coordinates if the names list is not empty.
     * @param x xpos to spawn at
     * @param y ypos to spawn at
     */
    private void spawnNpc(int x, int y) {
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

    /**
     * Draws an NPC on the map at its current position.
     * @param guy The NPC to be drawn.
     */
    private void drawNpc(Npc guy) {
        map[guy.getyPos()][guy.getxPos()] = 2;
    }

    /**
     * Spawns a mob at the specified coordinates with a random type and name.
     * @param x x position to spawn at
     * @param y y position to spawn at
     */
    private void spawnMob(int x, int y) {
        mob randomMob = mob.createRandomMob();
        
        if (randomMob != null) {
            randomMob.setPosition(y, x);
            randomMob.setMap(map);
    
            mobList.add(randomMob);
            drawMob(randomMob);
        }
    }    

    /**
     * Draws a mob on the map at its current position.
     * @param guy The mob to be drawn.
     */
    private void drawMob(mob guy) {
        map[guy.getyPos()][guy.getxPos()] = 4;
    }
    /**
     * Renders the visible portion of the map on the canvas based on the camera position.
     * This method is called whenever the map needs to be redrawn, such as after movement or spawning entities.
     * NPCs and mobs are rendered as overlays on top of the base tile.
     * Adds swirling wind and falling leaves overlay effect.
     */
    private static final int WIND_PARTICLE_COUNT = 20;
    private static final int LEAF_PARTICLE_COUNT = 30;
    private final List<WindParticle> windParticles = new ArrayList<>();
    private final List<LeafParticle> leafParticles = new ArrayList<>();
    private boolean overlayInitialized = false;
    private static class LeafParticle {
        double x, y, vy, vx, angle, angularVelocity, width, height;
        javafx.scene.paint.Color color;
        Random r = new Random();

        LeafParticle(double canvasWidth, double canvasHeight) {
            x = r.nextDouble() * canvasWidth;
            y = r.nextDouble() * canvasHeight;
            vx = 0.5 + r.nextDouble() * 3;
            vy = 0.2 + r.nextDouble() * 0.4;
            angle = r.nextDouble() * 360;
            angularVelocity = -1.0 + r.nextDouble() * 2;
            width = 8 + r.nextDouble() * 4;
            height = 3 + r.nextDouble() * 2;
            javafx.scene.paint.Color[] colors = {
                javafx.scene.paint.Color.web("#2e8b57"),
                javafx.scene.paint.Color.web("#228b22"),
                javafx.scene.paint.Color.web("#006400"),
                javafx.scene.paint.Color.web("#3cb371"),
                javafx.scene.paint.Color.web("#66cdaa")
            };
            color = colors[r.nextInt(colors.length)];
        }

        void update(double canvasWidth, double canvasHeight) {
            x += vx + Math.sin(angle * Math.PI / 180) * 0.05;
            y += vy;
            angle += angularVelocity;
            if (y > canvasHeight) {
                y = 0;
                x = r.nextDouble() * canvasWidth;
            }
            if (x > canvasWidth) x = 0;
            if (x < 0) x = canvasWidth;
        }
    }

    private static class WindParticle {
        double x, y, vx, vy, phase;

        WindParticle(double width, double height) {
            Random r = new Random();
            x = r.nextDouble() * width;
            y = r.nextDouble() * height;
            vx = 0.5 + r.nextDouble() * 1.5;
            vy = -0.5 + r.nextDouble();
            phase = r.nextDouble() * Math.PI * 2;
        }

        void update(double width, double height) {
            x += vx + Math.sin(phase) * 0.5;
            y += vy + Math.cos(phase) * 0.5;
            phase += 0.05;
            if (x > width) x = 0;
            if (y > height) y = 0;
            if (x < 0) x = width;
            if (y < 0) y = height;
        }
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
                        case 18:
                            image = shelterBleftImage;
                            break;
                        case 19:
                            image = shelterBrightImage;
                            break;
                        case 20:
                            image = shelterTrightImage;
                            break;
                        case 21:
                            image = shelterTleftImage;
                            break;
                        case 22:
                            image = explosiveImage;
                            break;
                        case 23:
                            image = flameImage;
                            break;
                        case 24:
                            image = burntGrassImage;
                            break;
                        default:
                            image = grassImg;
                            break;
                    }

                    double drawX = Math.round(col * TILE_SIZE - (cameraX % 1) * TILE_SIZE);
                    double drawY = Math.round(row * TILE_SIZE - (cameraY % 1) * TILE_SIZE);
                    if (map[mapRow][mapCol] == 22) {
                        gc.drawImage(grassImg, drawX, drawY, TILE_SIZE, TILE_SIZE);
                        gc.drawImage(image, drawX, drawY, TILE_SIZE, TILE_SIZE);
                    } else {
                        gc.drawImage(image, drawX, drawY, TILE_SIZE, TILE_SIZE);
                    }
                }
            }

            // --- Swirling wind and falling leaves overlay effect ---
            if (!overlayInitialized) {
                windParticles.clear();
                for (int i = 0; i < WIND_PARTICLE_COUNT; i++) {
                    windParticles.add(new WindParticle(canvas.getWidth(), canvas.getHeight()));
                }
                leafParticles.clear();
                for (int i = 0; i < LEAF_PARTICLE_COUNT; i++) {
                    leafParticles.add(new LeafParticle(canvas.getWidth(), canvas.getHeight()));
                }
                overlayInitialized = true;
            }

            // Animate and draw wind particles (white swirling lines)
            gc.save();
            gc.setGlobalAlpha(0.25);
            gc.setStroke(javafx.scene.paint.Color.WHITE);
            gc.setLineWidth(2.0);
            for (WindParticle p : windParticles) {
                p.update(canvas.getWidth(), canvas.getHeight());
                gc.strokeOval(p.x, p.y, 18 + 8 * Math.sin(p.phase), 8 + 4 * Math.cos(p.phase));
            }
            gc.restore();

            // Animate and draw falling leaves (brown/orange/yellow ellipses)
            for (LeafParticle leaf : leafParticles) {
                leaf.update(canvas.getWidth(), canvas.getHeight());
                gc.save();
                gc.setGlobalAlpha(0.7);
                gc.setFill(leaf.color);
                gc.translate(leaf.x, leaf.y);
                gc.rotate(leaf.angle);
                gc.fillOval(-leaf.width/2, -leaf.height/2, leaf.width, leaf.height);
                gc.restore();
            }

            long currentTime = System.currentTimeMillis();
            long timeSinceUpdate = currentTime - npcUpdateTime;
            double alpha = Math.min(1.0, (double) timeSinceUpdate / 500.0);
            
            for (Npc guy : npcList) {
                double lastX = guy.getLastXPos();
                double lastY = guy.getLastYPos();
                double newX = guy.getxPos();
                double newY = guy.getyPos();
            
                double interpX = lastX + (newX - lastX) * alpha;
                double interpY = lastY + (newY - lastY) * alpha;
            
                double screenX = (interpX - cameraX) * TILE_SIZE;
                double screenY = (interpY - cameraY) * TILE_SIZE;
            
                if (screenX >= -TILE_SIZE && screenX < TILES_TO_RENDER * TILE_SIZE &&
                    screenY >= -TILE_SIZE && screenY < TILES_TO_RENDER * TILE_SIZE) {
                    gc.drawImage(guy.getImage(), screenX, screenY, TILE_SIZE, TILE_SIZE);
                }
            }
            

            for (mob guy : mobList) {
                double lastX = guy.getLastXPos();
                double lastY = guy.getLastYPos();
                double newX = guy.getxPos();
                double newY = guy.getyPos();

                double interpX = lastX + (newX - lastX) * alpha;
                double interpY = lastY + (newY - lastY) * alpha;

                double screenX = (interpX - cameraX) * TILE_SIZE;
                double screenY = (interpY - cameraY) * TILE_SIZE;

                if (screenX >= -TILE_SIZE && screenX < TILES_TO_RENDER * TILE_SIZE &&
                    screenY >= -TILE_SIZE && screenY < TILES_TO_RENDER * TILE_SIZE) {
                    gc.drawImage(guy.getImage(), screenX, screenY, TILE_SIZE, TILE_SIZE);
                }
            }
        });
    }
}