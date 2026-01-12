package com.example.rpg;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Handles all keyboard and mouse input for the game.
 * @author Carsen Gafford
 * @version alpha v0.2.0
 */
public class InputHandler {
    
    private int numberKeypressed = 0;
    private double moveSpeed = 0.1;
    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private int tileSize = 50;
    private int tilesToRender = 11;
    private int mapWidth;
    private int mapHeight;
    private double cameraX = 0;
    private double cameraY = 0;

    /**
     * Constructs an InputHandler with map dimensions.
     * @param mapWidth The width of the map.
     * @param mapHeight The height of the map.
     */
    public InputHandler(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    /**
     * Handles key press events.
     * @param event The key event.
     */
    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP) upPressed = true;
        if (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN) downPressed = true;
        if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) leftPressed = true;
        if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) rightPressed = true;
        if (event.getCode() == KeyCode.SHIFT) moveSpeed = 0.3;
        if (event.getCode() == KeyCode.EQUALS) {
            tileSize = Math.min(100, tileSize + 5);
            tilesToRender = 700 / tileSize;
        }
        if (event.getCode() == KeyCode.MINUS) {
            tileSize = Math.max(5, tileSize - 5);
            tilesToRender = 700 / tileSize;
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
    }

    /**
     * Handles key release events.
     * @param event The key event.
     */
    public void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP) upPressed = false;
        if (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN) downPressed = false;
        if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) leftPressed = false;
        if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) rightPressed = false;
        if (event.getCode() == KeyCode.SHIFT) moveSpeed = 0.1;
    }

    /**
     * Handles scroll wheel events for zoom.
     * @param event The scroll event.
     */
    public void handleScroll(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            tileSize = Math.min(100, tileSize + 2);
        } else if (event.getDeltaY() < 0) {
            tileSize = Math.max(5, tileSize - 2);
        }
        tilesToRender = 700 / tileSize;
    }

    /**
     * Gets the currently pressed number key.
     * @return The number key pressed (0-9), or -1 if none.
     */
    public int getNumberKeypressed() {
        return numberKeypressed;
    }

    /**
     * Sets the currently pressed number key.
     * @param key The number key.
     */
    public void setNumberKeypressed(int key) {
        this.numberKeypressed = key;
    }

    /**
     * Updates camera position based on input.
     * @param deltaTime The time since the last update in seconds.
     */
    public void updateCamera(double deltaTime) {
        double speed = moveSpeed * deltaTime * 60;

        if (upPressed) {
            cameraY = Math.max(0, cameraY - speed);
        }
        if (downPressed) {
            cameraY = Math.min(mapHeight - 10, cameraY + speed);
        }
        if (leftPressed) {
            cameraX = Math.max(0, cameraX - speed);
        }
        if (rightPressed) {
            cameraX = Math.min(mapWidth - 10, cameraX + speed);
        }
    }

    public double getMoveSpeed() { return moveSpeed; }
    public boolean isUpPressed() { return upPressed; }
    public boolean isDownPressed() { return downPressed; }
    public boolean isLeftPressed() { return leftPressed; }
    public boolean isRightPressed() { return rightPressed; }
    public int getTileSize() { return tileSize; }
    public void setTileSize(int size) { this.tileSize = size; }
    public int getTilesToRender() { return tilesToRender; }
    public void setTilesToRender(int tiles) { this.tilesToRender = tiles; }
    public double getCameraX() { return cameraX; }
    public double getCameraY() { return cameraY; }
    public void setCameraX(double x) { this.cameraX = x; }
    public void setCameraY(double y) { this.cameraY = y; }
}
