package com.example.rpg;
import java.util.*;
import java.util.function.Supplier;

import javafx.scene.image.Image;

/**
 * Class representing a mobile entity (mob) in the RPG game.
 * Handles movement, image loading, and interaction with the game map.
 * Similar to NPC but with random movement and simpler logic.
 * 
 * @author Carsen Gafford
 * @version alpha v0.2.2
 */
public abstract class mob {

    protected int xpos;
    protected int ypos;
    protected int maxWidth;
    protected int maxHeight;
    protected int[][] map;
    protected String name;
    protected Image img;
    protected String right;
    protected String left;
    protected int varient = 0;
    protected int lastxpos = 0;
    protected int lastypos = 0;

    private static final List<Supplier<? extends mob>> registry = new ArrayList<>();

    public mob(int xpos, int ypos, int[][] map, Image img) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.maxHeight = map.length;
        this.maxWidth = map[0].length;
        this.map = map;

        Random rand = new Random();
        this.varient = rand.nextInt(2);
        this.img = img;
    }

    /**
     * Check for a texture in the mod directory first, then load from default if not found.
     * @param modPath the path to the mod texture
     * @param defaultPath the path to the default texture
     * @return the URL of the loaded texture
     * @throws IllegalArgumentException if neither texture is found
     */
    protected static String checkAndLoadTexture(String modPath, String defaultPath) {
        boolean mod = false;
        if (mob.class.getResource(modPath) != null && mod) {
            return mob.class.getResource(modPath).toString();
        } else {
            if (mob.class.getResource(defaultPath) != null) {
                return mob.class.getResource(defaultPath).toString();
            } else {
                throw new IllegalArgumentException("Resource not found: " + defaultPath);
            }
        }
    }

    public void setPosition(int x, int y) {
        this.xpos = x;
        this.ypos = y;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public static mob createRandomMob() {
        if (registry.isEmpty()) return null;
        int index = new Random().nextInt(registry.size());
        return registry.get(index).get();
    }

    public static void register(Supplier<? extends mob> supplier) {
        registry.add(supplier);
    }

    public static List<Supplier<? extends mob>> getRegistry() {
        return registry;
    }

    /**
     * Move the mob randomly on the map, avoiding obstacles.
     * The mob can move up, down, left, or right if the target cell is walkable.
     */
    public void move() {
        Random rand = new Random();
        int temp = rand.nextInt(4);
        if (temp == 0) {
            if (xpos + 1 < maxWidth && map[ypos][xpos + 1] == 0) {
                xpos++;
            }
        } else if (temp == 1) {
            if (xpos - 1 >= 0 && map[ypos][xpos - 1] == 0) {
                xpos--;
            }
        } else if (temp == 2) {
            if (ypos + 1 < maxHeight && map[ypos + 1][xpos] == 0) ypos++;
        } else if (temp == 3) {
            if (ypos - 1 >= 0 && map[ypos - 1][xpos] == 0) ypos--;
        }
    }

    /**
     * Getters for mob properties.
     * @return the respective property value
     */
    public int getxPos() { return xpos; }
    public int getyPos() { return ypos; }
    public int getLastXPos() { return lastxpos; }
    public int getLastYPos() { return lastypos; }
    public int setLastXPos(int x) { return lastxpos = x; }
    public int setLastYPos(int y) { return lastypos = y; }

    public String getName() { return name; }
    public Image getImage() { return img; }

    /**
     * Check if the mob is adjacent to a human
     * meant to check if the mob is being hunted, really bad system, needs to be re-worked entirely.
     * @return true if adjacent to a human tile, false otherwise
     */
    public boolean isHunted() {
        if ((ypos + 1 < maxHeight && map[ypos + 1][xpos] == 2) ||
            (ypos - 1 >= 0 && map[ypos - 1][xpos] == 2) ||
            (xpos + 1 < maxWidth && map[ypos][xpos + 1] == 2) ||
            (xpos - 1 >= 0 && map[ypos][xpos - 1] == 2)) {
            return true;
        } else {
            return false;
        }
    }
}