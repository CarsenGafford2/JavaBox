package com.example.rpg;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Handles all game rendering logic.
 */
public class GameRenderer {
    
    private TextureManager textureManager;
    private ParticleSystem particleSystem;
    private int[][] map;
    private Canvas canvas;
    private double cameraX = 0;
    private double cameraY = 0;
    private int tileSize = 50;
    private int tilesToRender = 11;
    private long npcUpdateTime;

    /**
     * Constructs a GameRenderer with required dependencies.
     * @param canvas The JavaFX canvas to render to.
     * @param map The game map array.
     * @param textureManager The texture manager for loading images.
     * @param particleSystem The particle system for rendering effects.
     */
    public GameRenderer(Canvas canvas, int[][] map, TextureManager textureManager, ParticleSystem particleSystem) {
        this.canvas = canvas;
        this.map = map;
        this.textureManager = textureManager;
        this.particleSystem = particleSystem;
        this.npcUpdateTime = System.currentTimeMillis();
    }

    /**
     * Renders the visible portion of the map on the canvas based on the camera position.
     * NPCs and mobs are rendered as overlays on top of the base tile.
     * Includes particle effects like wind and falling leaves.
     * @param entityManager The entity manager containing NPCs and mobs.
     */
    public void renderMap(EntityManager entityManager) {
        Platform.runLater(() -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            // Render base tiles
            for (int row = 0; row < tilesToRender; row++) {
                for (int col = 0; col < tilesToRender; col++) {
                    int mapRow = (int)(cameraY + row);
                    int mapCol = (int)(cameraX + col);

                    if (mapRow < 0 || mapCol < 0 || mapRow >= map.length || mapCol >= map[0].length) {
                        continue;
                    }

                    Image image = getTileImage(map[mapRow][mapCol]);
                    double drawX = Math.round(col * tileSize - (cameraX % 1) * tileSize);
                    double drawY = Math.round(row * tileSize - (cameraY % 1) * tileSize);
                    
                    if (map[mapRow][mapCol] == 22) {
                        gc.drawImage(textureManager.getGrassImg(), drawX, drawY, tileSize, tileSize);
                        gc.drawImage(image, drawX, drawY, tileSize, tileSize);
                    } else {
                        gc.drawImage(image, drawX, drawY, tileSize, tileSize);
                    }
                }
            }

            // Render particle effects
            particleSystem.initialize(canvas.getWidth(), canvas.getHeight());
            particleSystem.renderParticles(gc, canvas.getWidth(), canvas.getHeight());

            // Render NPCs and mobs with interpolation
            long currentTime = System.currentTimeMillis();
            long timeSinceUpdate = currentTime - npcUpdateTime;
            double alpha = Math.min(1.0, (double) timeSinceUpdate / 500.0);
            
            for (Npc guy : entityManager.getNpcList()) {
                double lastX = guy.getLastXPos();
                double lastY = guy.getLastYPos();
                double newX = guy.getxPos();
                double newY = guy.getyPos();
            
                double interpX = lastX + (newX - lastX) * alpha;
                double interpY = lastY + (newY - lastY) * alpha;
            
                double screenX = (interpX - cameraX) * tileSize;
                double screenY = (interpY - cameraY) * tileSize;
            
                if (screenX >= -tileSize && screenX < tilesToRender * tileSize &&
                    screenY >= -tileSize && screenY < tilesToRender * tileSize) {
                    gc.drawImage(guy.getImage(), screenX, screenY, tileSize, tileSize);
                }
            }

            for (mob guy : entityManager.getMobList()) {
                double lastX = guy.getLastXPos();
                double lastY = guy.getLastYPos();
                double newX = guy.getxPos();
                double newY = guy.getyPos();

                double interpX = lastX + (newX - lastX) * alpha;
                double interpY = lastY + (newY - lastY) * alpha;

                double screenX = (interpX - cameraX) * tileSize;
                double screenY = (interpY - cameraY) * tileSize;

                if (screenX >= -tileSize && screenX < tilesToRender * tileSize &&
                    screenY >= -tileSize && screenY < tilesToRender * tileSize) {
                    gc.drawImage(guy.getImage(), screenX, screenY, tileSize, tileSize);
                }
            }
        });
    }

    /**
     * Gets the image for a specific tile type.
     * @param tileType The tile type ID.
     * @return The Image for the tile type.
     */
    private Image getTileImage(int tileType) {
        switch (tileType) {
            case 0: return textureManager.getGrassImg();
            case 1: return textureManager.getRockImg();
            case 3: return textureManager.getTreeImage();
            case 5: return textureManager.getWaterImage();
            case 6: return textureManager.getSandImage();
            case 7: return textureManager.getBrickImage();
            case 8: return textureManager.getWoodImage();
            case 9: return textureManager.getBedTopImage();
            case 10: return textureManager.getBedBottomImage();
            case 11: return textureManager.getNightstandImage();
            case 12: return textureManager.getDoorImage();
            case 13: return textureManager.getChairImage();
            case 14: return textureManager.getTableBleftImage();
            case 15: return textureManager.getTableBrightImage();
            case 16: return textureManager.getTableTrightImage();
            case 17: return textureManager.getTableTleftImage();
            case 18: return textureManager.getShelterBleftImage();
            case 19: return textureManager.getShelterBrightImage();
            case 20: return textureManager.getShelterTrightImage();
            case 21: return textureManager.getShelterTleftImage();
            case 22: return textureManager.getExplosiveImage();
            case 23: return textureManager.getFlameImage();
            case 24: return textureManager.getBurntGrassImage();
            case 25: return textureManager.getBurntTreeImage();
            default: return textureManager.getGrassImg();
        }
    }

    // Getters and setters
    public void setMap(int[][] map) { this.map = map; }
    public void setCameraX(double x) { this.cameraX = x; }
    public void setCameraY(double y) { this.cameraY = y; }
    public void setTileSize(int size) { this.tileSize = size; }
    public void setTilesToRender(int tiles) { this.tilesToRender = tiles; }
    public void updateNpcUpdateTime() { this.npcUpdateTime = System.currentTimeMillis(); }
    public double getCameraX() { return cameraX; }
    public double getCameraY() { return cameraY; }
    public int getTileSize() { return tileSize; }
    public int getTilesToRender() { return tilesToRender; }
}
