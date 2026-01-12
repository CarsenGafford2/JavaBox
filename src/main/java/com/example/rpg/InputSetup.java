package com.example.rpg;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

/**
 * Wires keyboard and mouse input to the InputHandler and GameRenderer.
 * @author Carsen Gafford
 * @version alpha v0.2.0
 */
public final class InputSetup {

    private InputSetup() {}

    /**
     * Attaches keyboard and scroll handlers to the canvas scene.
     *
     * @param canvas Canvas receiving input
     * @param inputHandler Input handler
     * @param renderer Game renderer (for zoom updates)
     */
    public static void attach(
            Canvas canvas,
            InputHandler inputHandler,
            GameRenderer renderer
    ) {
        canvas.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) return;

            attachKeyHandlers(newScene, inputHandler, renderer);
            attachScrollHandler(canvas, inputHandler, renderer);
        });
    }

    private static void attachKeyHandlers(
            Scene scene,
            InputHandler inputHandler,
            GameRenderer renderer
    ) {
        scene.setOnKeyPressed(event -> {
            inputHandler.handleKeyPressed(event);

            renderer.setTileSize(inputHandler.getTileSize());
            renderer.setTilesToRender(inputHandler.getTilesToRender());
        });

        scene.setOnKeyReleased(event ->
            inputHandler.handleKeyReleased(event)
        );
    }

    private static void attachScrollHandler(
            Canvas canvas,
            InputHandler inputHandler,
            GameRenderer renderer
    ) {
        canvas.setOnScroll(event -> {
            inputHandler.handleScroll(event);
            renderer.setTileSize(inputHandler.getTileSize());
            renderer.setTilesToRender(inputHandler.getTilesToRender());
        });
    }
}
