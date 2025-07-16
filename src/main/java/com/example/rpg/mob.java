package com.example.rpg;
import java.util.*;

import javafx.scene.image.Image;

/**
 * Class representing a mobile entity (mob) in the RPG game.
 * Handles movement, image loading, and interaction with the game map.
 * Similar to NPC but with random movement and simpler logic.
 * 
 * @author Carsen Gafford
 * @version alpha v0.2.2
 */
public class mob {

    private int xpos;
    private int ypos;
    private int maxWidth;
    private int maxHeight;
    private int[][] map;
    private String name;
    private String type;
    private Image img;
    private String right;
    private String left;
    private Image rightImage;
    private Image leftImage;
    private int varient = 0;

    public mob(int xpos, int ypos, String name, String type, int[][] map) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.name = name;
        this.maxHeight = map.length;
        this.maxWidth = map[0].length;
        this.map = map;
        this.type = type;

        Random rand = new Random();
        this.varient = rand.nextInt(2);
        this.right = checkAndLoadTexture("mods/mob/" + this.type + this.varient + ".png", "res/mob/" + this.type + this.varient + ".png");
        this.left = checkAndLoadTexture("mods/mob/" + this.type + this.varient + "r" + ".png", "res/mob/" + this.type + this.varient + "r" + ".png");
        this.rightImage = new Image(right);
        this.leftImage = new Image(left);
        this.img = rightImage;
    }

    /**
     * Check for a texture in the mod directory first, then load from default if not found.
     * @param modPath the path to the mod texture
     * @param defaultPath the path to the default texture
     * @return the URL of the loaded texture
     * @throws IllegalArgumentException if neither texture is found
     */
    private String checkAndLoadTexture(String modPath, String defaultPath) {
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
                img = rightImage;
            }
        } else if (temp == 1) {
            if (xpos - 1 >= 0 && map[ypos][xpos - 1] == 0) {
                xpos--;
                img = leftImage;
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