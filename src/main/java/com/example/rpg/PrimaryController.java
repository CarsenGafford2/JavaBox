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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Primary Controller is the main class and handles game orchestration.
 * It coordinates map generation, entity management, input handling, and rendering.
 * The class uses JavaFX canvas for rendering and OpenSimplex2S for procedural map generation.
 * 
 * @author Carsen Gafford
 * @version alpha v0.2.0
 */
public class PrimaryController {

    /*
     * Manager and handler instances
     */
    private TextureManager textureManager;
    private ParticleSystem particleSystem;
    private EntityManager entityManager;
    private GameRenderer gameRenderer;
    private InputHandler inputHandler;

    /*
     * This is just basic initialization for the size of tiles and the amount that are rendered at once.
     * You can change these values to adjust the zoom level and how much of the map is visible.
     * The player has the option to change the zoom level with the + and - keys or the scroll wheel.
     */
    private int mapWidth = 500;
    private int mapHeight = 500;

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

    private long npcUpdateTime;

    private static final Random rand = new Random();

    /*
     * Lists for managing names and explosive entities.
     */
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Explosive> activeExplosives = new ArrayList<>();

    @FXML private AnchorPane tilePane;

    /**
     * Initializes the game, generates the map, loads names, spawns NPCs and mobs, and sets up rendering and input handling.
     * This method is called automatically after the FXML elements have been loaded.
     * @throws ClassNotFoundException
     */
    public void initialize() throws ClassNotFoundException {
        // Initialize managers
        textureManager = new TextureManager();
        particleSystem = new ParticleSystem();
        
        // Clear console
        System.out.print("\033[H\033[2J");

        // Generate map
        map = MapGenerator.generateMap(mapWidth, mapHeight);
        cleanMap = MapGenerator.createCleanMapCopy(map);
        
        // Save map as image for debugging
        saveMapAsImage("map.png");

        // Initialize entity manager
        entityManager = new EntityManager(map);
        
        // Initialize input handler
        inputHandler = new InputHandler(mapWidth, mapHeight, map);
        
        // Initialize game renderer
        gameRenderer = new GameRenderer(canvas, map, textureManager, particleSystem);

        // Set up UI buttons for tile selection
        setupTileButtons();

        // Load mob scripts
        Class.forName("com.example.rpg.mobScripts.chicken");
        Class.forName("com.example.rpg.mobScripts.cow");
        Class.forName("com.example.rpg.mobScripts.sheep");
        Class.forName("com.example.rpg.mobScripts.pig");

        // Set up mouse events for the canvas
        setupCanvasMouseEvents();
        addMouseHoverHandler();

        // Load names from resources
        loadNames();

        canvas.setFocusTraversable(true);
        canvas.requestFocus();

        // Spawn Mobs in random locations on the map
        System.out.print("\033[H\033[2J");
        int mobCount = mapWidth * mapHeight / 50;
        int printInterval = Math.max(mobCount / 100, 1);
        for (int index = 0; index < mobCount; index++) {
            int x = rand.nextInt(mapWidth);
            int y = rand.nextInt(mapHeight);
            if (map[x][y] == 0) {
                entityManager.spawnMob(x, y);
            }

            if (index % printInterval == 0 || index == mobCount - 1) {
                int percent = (index * 100) / mobCount;
                System.out.print("\rSpawning Mobs... " + percent + "%");
            }
        }
        System.out.println("\rSpawning Mobs... 100%");

        // Timer for moving NPCs and mobs every 500 milliseconds
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                npcUpdateTime = System.currentTimeMillis();
                gameRenderer.updateNpcUpdateTime();

                for (Npc guy : new ArrayList<>(entityManager.getNpcList())) {
                    guy.setLastXPos(guy.getxPos());
                    guy.setLastYPos(guy.getyPos());
                    guy.move();
                    entityManager.drawNpc(guy);
                }

                // Handle fire spreading
                handleFireSpread();

                // Handle explosives
                handleExplosives();

                // Handle mobs
                List<mob> mobListCopy = new ArrayList<>(entityManager.getMobList());
                for (mob guy : mobListCopy) {
                    int mobY = guy.getyPos();
                    int mobX = guy.getxPos();
                    guy.setLastXPos(mobX);
                    guy.setLastYPos(mobY);
                    if (guy.isHunted()) {
                        entityManager.removeMob(guy);
                    } else {
                        guy.move();
                        entityManager.drawMob(guy);
                    }
                }
            }
        }, 0, 500);

        // Main render loop
        Timer renderTimer = new Timer();
        renderTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                gameRenderer.renderMap(entityManager);
            }
        }, 0, 8);

        // Handle key and mouse input
        setupInputHandling();

        // Camera movement animation timer
        AnimationTimer cameraTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                inputHandler.updateCamera(deltaTime);
                gameRenderer.setCameraX(inputHandler.getCameraX());
                gameRenderer.setCameraY(inputHandler.getCameraY());
            }
        };
        cameraTimer.start();
    }

    /**
     * Sets up UI buttons for tile selection.
     */
    private void setupTileButtons() {
        int buttonSize = 50;
        int padding = 0;
        int columns = 4;

        for (int i = 0; i < textureManager.imageArray.length; i++) {
            ImageView imageView = new ImageView(textureManager.imageArray[i]);
            imageView.setFitWidth(buttonSize);
            imageView.setFitHeight(buttonSize);
            imageView.setPreserveRatio(true);

            Button btn = new Button();
            btn.setGraphic(imageView);
            btn.setPrefSize(buttonSize, buttonSize);
            btn.setMinSize(buttonSize, buttonSize);
            btn.setMaxSize(buttonSize, buttonSize);

            int row = i / columns;
            int col = i % columns;

            btn.setLayoutX(col * (buttonSize + padding));
            btn.setLayoutY(row * (buttonSize + padding));

            final int index = i + 2;
            btn.setOnAction(event -> {
                inputHandler.setNumberKeypressed(index);
            });

            tilePane.getChildren().add(btn);
        }
    }

    /**
     * Loads NPC names from resource files.
     */
    private void loadNames() {
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
    }

    /**
     * Handles fire spreading on the map.
     */
    private void handleFireSpread() {
        List<int[]> newFlames = new ArrayList<>();
        double spreadChance = 0.6;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 23) {
                    map[i][j] = 25;
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
    }

    /**
     * Handles explosive detonation and effects.
     */
    private void handleExplosives() {
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
            activeExplosives.remove(explosive);
        }
    }

    /**
     * Sets up all input handling for keyboard and mouse.
     */
    private void setupInputHandling() {
        canvas.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    inputHandler.handleKeyPressed(event);
                    if (event.getCode().isDigitKey() || event.getCode().isArrowKey() || 
                        event.getCode().isModifierKey() || event.getCode().equals(javafx.scene.input.KeyCode.EQUALS) ||
                        event.getCode().equals(javafx.scene.input.KeyCode.MINUS)) {
                        gameRenderer.setTileSize(inputHandler.getTileSize());
                        gameRenderer.setTilesToRender(inputHandler.getTilesToRender());
                    }
                });

                newScene.setOnKeyReleased(event -> {
                    inputHandler.handleKeyReleased(event);
                });

                canvas.setOnScroll(event -> {
                    inputHandler.handleScroll(event);
                    gameRenderer.setTileSize(inputHandler.getTileSize());
                    gameRenderer.setTilesToRender(inputHandler.getTilesToRender());
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

            int tileX = (int) (mouseX / inputHandler.getTileSize() + inputHandler.getCameraX());
            int tileY = (int) (mouseY / inputHandler.getTileSize() + inputHandler.getCameraY());

            if (tileX >= 0 && tileX < map[0].length && tileY >= 0 && tileY < map.length) {
                for (Npc npc : entityManager.getNpcList()) {
                    if (npc.getxPos() == tileX && npc.getyPos() == tileY) {
                        showNpcTooltip(npc, event);
                        return;
                    }
                }
                for (mob m : entityManager.getMobList()) {
                    if (m.getxPos() == tileX && m.getyPos() == tileY) {
                        showMobTooltip(m, event);
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

    private void showMobTooltip(mob m, MouseEvent event) {
        if (currentTooltip == null) {
            currentTooltip = new Tooltip();
            Tooltip.install(canvas, currentTooltip);
        }
        currentTooltip.setText(m.getName());

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
     * Sets up mouse click events on the canvas for spawning NPCs, mobs, and changing terrain.
     */
    private void setupCanvasMouseEvents() {
        canvas.setOnMouseClicked(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();
            int col = (int) (mouseX / inputHandler.getTileSize() + inputHandler.getCameraX());
            int row = (int) (mouseY / inputHandler.getTileSize() + inputHandler.getCameraY());

            if (row < 0 || col < 0 || row >= map.length || col >= map[0].length) return;

            int selectedTile = inputHandler.getNumberKeypressed();
            if (selectedTile == 22) {
                map[row][col] = 22;
                Explosive bomb = new Explosive("Bomb", 100, 4, col, row);
                activeExplosives.add(bomb);
            } else {
                map[row][col] = selectedTile;
            }
        });
    }

    /**
     * Generates a procedural map using OpenSimplex2S noise.
     * @param width The width of the map.
     * @param height The height of the map.
     * @return A 2D array representing the generated map.
     */
    private int[][] generateMap(int width, int height) {
        return MapGenerator.generateMap(width, height);
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
    }

    /**
     * Spawns an NPC at the specified coordinates if the names list is not empty.
     * @param x xpos to spawn at
     * @param y ypos to spawn at
     */
    private void spawnNpc(int x, int y) {
        entityManager.spawnNpc(x, y, names);
    }

    /**
     * Draws an NPC on the map at its current position.
     * @param guy The NPC to be drawn.
     */
    private void drawNpc(Npc guy) {
        entityManager.drawNpc(guy);
    }

    /**
     * Spawns a mob at the specified coordinates with a random type and name.
     * @param x x position to spawn at
     * @param y y position to spawn at
     */
    private void spawnMob(int x, int y) {
        entityManager.spawnMob(x, y);
    }

    /**
     * Draws a mob on the map at its current position.
     * @param guy The mob to be drawn.
     */
    private void drawMob(mob guy) {
        entityManager.drawMob(guy);
    }
}