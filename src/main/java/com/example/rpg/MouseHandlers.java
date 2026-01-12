package com.example.rpg;

import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

import java.util.List;

/**
 * Handles all mouse interaction on the game canvas.
 * @author Carsen Gafford
 * @version alpha v0.2.0
 */
public final class MouseHandlers {

    private static Tooltip currentTooltip;

    private MouseHandlers() {}

    public static void attach(
            Canvas canvas,
            int[][] map,
            EntityManager entityManager,
            InputHandler inputHandler,
            List<Explosive> activeExplosives
    ) {
        attachClickHandler(canvas, map, inputHandler, activeExplosives);
        attachHoverHandler(canvas, map, entityManager, inputHandler);
    }

    private static void attachClickHandler(Canvas canvas, int[][] map, InputHandler inputHandler, List<Explosive> activeExplosives) {
        canvas.setOnMouseClicked(event -> {

            int col = (int) (event.getX() / inputHandler.getTileSize()
                    + inputHandler.getCameraX());
            int row = (int) (event.getY() / inputHandler.getTileSize()
                    + inputHandler.getCameraY());

            if (row < 0 || col < 0 ||
                row >= map.length || col >= map[0].length) {
                return;
            }

            int selectedTile = inputHandler.getNumberKeypressed();

            if (selectedTile == 22) {
                map[row][col] = 22;
                activeExplosives.add(
                        new Explosive("Bomb", 100, 4, col, row)
                );
            } else {
                map[row][col] = selectedTile;
            }
        });
    }

    private static void attachHoverHandler(
            Canvas canvas,
            int[][] map,
            EntityManager entityManager,
            InputHandler inputHandler
    ) {
        canvas.setOnMouseMoved(event ->
                handleHover(canvas, event, map, entityManager, inputHandler)
        );
    }

    private static void handleHover(
            Canvas canvas,
            MouseEvent event,
            int[][] map,
            EntityManager entityManager,
            InputHandler inputHandler
    ) {
        int tileX = (int) (event.getX() / inputHandler.getTileSize()
                + inputHandler.getCameraX());
        int tileY = (int) (event.getY() / inputHandler.getTileSize()
                + inputHandler.getCameraY());

        if (tileX < 0 || tileY < 0 ||
            tileY >= map.length || tileX >= map[0].length) {
            hideTooltip(canvas);
            return;
        }

        for (Npc npc : entityManager.getNpcList()) {
            if (npc.getxPos() == tileX && npc.getyPos() == tileY) {
                showTooltip(canvas, npc.getName(), event);
                return;
            }
        }

        for (mob m : entityManager.getMobList()) {
            if (m.getxPos() == tileX && m.getyPos() == tileY) {
                showTooltip(canvas, m.getName(), event);
                return;
            }
        }

        hideTooltip(canvas);
    }

    private static void showTooltip(Canvas canvas, String text, MouseEvent event) {
        if (currentTooltip == null) {
            currentTooltip = new Tooltip();
            Tooltip.install(canvas, currentTooltip);
        }

        currentTooltip.setText(text);
        currentTooltip.show(canvas, event.getScreenX(), event.getScreenY());
        canvas.setCursor(Cursor.HAND);
    }

    private static void hideTooltip(Canvas canvas) {
        if (currentTooltip != null && currentTooltip.isShowing()) {
            currentTooltip.hide();
        }
        canvas.setCursor(Cursor.DEFAULT);
    }
}
