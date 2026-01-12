package com.example.rpg;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.net.URISyntaxException;
import java.util.*;

/**
 * PrimaryController
 * -----------------
 * Main JavaFX controller responsible for wiring together
 * rendering, input, entity management, and game systems.
 *
 * All game logic has been moved into dedicated systems.
 *
 * @author Carsen Gafford
 * @version alpha v0.2.0
 */
public class PrimaryController {

    @FXML private Canvas canvas;
    @FXML private AnchorPane tilePane;

    private TextureManager textureManager;
    private ParticleSystem particleSystem;
    private EntityManager entityManager;
    private GameRenderer gameRenderer;
    private InputHandler inputHandler;

    private FireSystem fireSystem;
    private ExplosiveSystem explosiveSystem;
    private GameLoop gameLoop;

    private int[][] map;

    private final int mapWidth = 500;
    private final int mapHeight = 500;

    private final List<String> names = new ArrayList<>();
    private final List<Explosive> activeExplosives = new ArrayList<>();

    private static final Random rand = new Random();

    @FXML
    public void initialize() throws ClassNotFoundException {

        clearConsole();

        textureManager = new TextureManager();
        particleSystem = new ParticleSystem();

        map = MapGenerator.generateMap(mapWidth, mapHeight);
        MapImageExporter.export(map, "map.png");

        entityManager = new EntityManager(map);
        inputHandler = new InputHandler(mapWidth, mapHeight);
        gameRenderer = new GameRenderer(canvas, map, textureManager, particleSystem);

        fireSystem = new FireSystem(map);
        explosiveSystem = new ExplosiveSystem(map, activeExplosives);
        gameLoop = new GameLoop(entityManager, gameRenderer, fireSystem, explosiveSystem);

        TileButtonController.setup(tilePane, textureManager, inputHandler);

        InputSetup.attach(canvas, inputHandler, gameRenderer);

        MouseHandlers.attach(
                canvas,
                map,
                entityManager,
                inputHandler,
                activeExplosives
        );

        loadNames();

        spawnInitialMobs();

        gameLoop.startNpcTimer();
        gameLoop.startRenderLoop();
        gameLoop.startCameraLoop(inputHandler, gameRenderer);

        canvas.setFocusTraversable(true);
        canvas.requestFocus();
    }

    private void loadNames() {
        try {
            File file = new File(
                    getClass()
                            .getResource("/com/example/rpg/names/names.txt")
                            .toURI()
            );
            names.addAll(NameLoader.load(file));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void spawnInitialMobs() {
        int mobCount = mapWidth * mapHeight / 50;
        int printInterval = Math.max(mobCount / 100, 1);

        for (int i = 0; i < mobCount; i++) {
            int x = rand.nextInt(mapWidth);
            int y = rand.nextInt(mapHeight);
            if (map[y][x] == 0) {
                entityManager.spawnMob(x, y);
            }

            if (i % printInterval == 0 || i == mobCount - 1) {
                int percent = (i * 100) / mobCount;
                System.out.print("\rSpawning Mobs... " + percent + "%");
            }
        }
        System.out.println("\rSpawning Mobs... 100%");
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
