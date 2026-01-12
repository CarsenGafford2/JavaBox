package com.example.rpg;

import javafx.scene.image.Image;

/**
 * Manages texture loading and caching for the game.
 * Handles modded textures and falls back to default textures.
 */
public class TextureManager {
    
    // Texture paths
    private String grass;
    private String rock;
    private String tree;
    private String water;
    private String sand;
    private String brick;
    private String wood;
    private String bedTop;
    private String bedBottom;
    private String nightstand;
    private String door;
    private String chair;
    private String tableBleft;
    private String tableBright;
    private String tableTright;
    private String tableTleft;
    private String shelterBleft;
    private String shelterBright;
    private String shelterTright;
    private String shelterTleft;
    private String explosive;
    private String flame;
    private String burntGrass;
    private String burntTree;

    // Image objects
    private Image grassImg;
    private Image rockImg;
    private Image treeImage;
    private Image waterImage;
    private Image sandImage;
    private Image brickImage;
    private Image woodImage;
    private Image bedTopImage;
    private Image bedBottomImage;
    private Image nightstandImage;
    private Image doorImage;
    private Image chairImage;
    private Image tableBleftImage;
    private Image tableBrightImage;
    private Image tableTrightImage;
    private Image tableTleftImage;
    private Image shelterBleftImage;
    private Image shelterBrightImage;
    private Image shelterTrightImage;
    private Image shelterTleftImage;
    private Image explosiveImage;
    private Image flameImage;
    private Image burntGrassImage;
    private Image burntTreeImage;
    
    public Image[] imageArray = new Image[24];

    /**
     * Initializes the texture manager and loads all textures.
     */
    public TextureManager() {
        loadTexturePaths();
        loadImages();
        initializeImageArray();
    }

    /**
     * Loads all texture paths, checking for modded versions first.
     */
    private void loadTexturePaths() {
        grass = checkForModTexture("mods/textures/grass.png", "res/textures/grass.png");
        rock = checkForModTexture("mods/textures/rock.png", "res/textures/rock.png");
        tree = checkForModTexture("mods/textures/tree.png", "res/textures/tree.png");
        water = checkForModTexture("mods/textures/water.png", "res/textures/water.png");
        sand = checkForModTexture("mods/textures/sand.png", "res/textures/sand.png");
        brick = checkForModTexture("mods/textures/brick.png", "res/textures/brick.png");
        wood = checkForModTexture("mods/textures/wood.png", "res/textures/wood.png");
        bedTop = checkForModTexture("mods/textures/bedTop.png", "res/textures/bedTop.png");
        bedBottom = checkForModTexture("mods/textures/bedBottom.png", "res/textures/bedBottom.png");
        nightstand = checkForModTexture("mods/textures/nightstand.png", "res/textures/nightstand.png");
        door = checkForModTexture("mods/textures/door.png", "res/textures/door.png");
        chair = checkForModTexture("mods/textures/chair.png", "res/textures/chair.png");
        tableBleft = checkForModTexture("mods/textures/tableBleft.png", "res/textures/tableBleft.png");
        tableBright = checkForModTexture("mods/textures/tableBright.png", "res/textures/tableBright.png");
        tableTright = checkForModTexture("mods/textures/tableTright.png", "res/textures/tableTright.png");
        tableTleft = checkForModTexture("mods/textures/tableTleft.png", "res/textures/tableTleft.png");
        shelterBleft = checkForModTexture("mods/textures/shelterBleft.png", "res/textures/shelterBleft.png");
        shelterBright = checkForModTexture("mods/textures/shelterBright.png", "res/textures/shelterBright.png");
        shelterTright = checkForModTexture("mods/textures/shelterTright.png", "res/textures/shelterTright.png");
        shelterTleft = checkForModTexture("mods/textures/shelterTleft.png", "res/textures/shelterTleft.png");
        explosive = checkForModTexture("mods/textures/explosive.png", "res/textures/explosive.png");
        flame = checkForModTexture("mods/textures/flame.png", "res/textures/flame.png");
        burntGrass = checkForModTexture("mods/textures/burntGrass.png", "res/textures/burntGrass.png");
        burntTree = checkForModTexture("mods/textures/burntTree.png", "res/textures/burntTree.png");
    }

    /**
     * Loads all images from the texture paths.
     */
    private void loadImages() {
        grassImg = new Image(grass);
        rockImg = new Image(rock);
        treeImage = new Image(tree);
        waterImage = new Image(water);
        sandImage = new Image(sand);
        brickImage = new Image(brick);
        woodImage = new Image(wood);
        bedTopImage = new Image(bedTop);
        bedBottomImage = new Image(bedBottom);
        nightstandImage = new Image(nightstand);
        doorImage = new Image(door);
        chairImage = new Image(chair);
        tableBleftImage = new Image(tableBleft);
        tableBrightImage = new Image(tableBright);
        tableTrightImage = new Image(tableTright);
        tableTleftImage = new Image(tableTleft);
        shelterBleftImage = new Image(shelterBleft);
        shelterBrightImage = new Image(shelterBright);
        shelterTrightImage = new Image(shelterTright);
        shelterTleftImage = new Image(shelterTleft);
        explosiveImage = new Image(explosive);
        flameImage = new Image(flame);
        burntGrassImage = new Image(burntGrass);
        burntTreeImage = new Image(burntTree);
    }

    /**
     * Initializes the image array for easy access by tile type.
     */
    private void initializeImageArray() {
        int in = 0;
        imageArray[in++] = grassImg;
        imageArray[in++] = rockImg;
        imageArray[in++] = treeImage;
        imageArray[in++] = waterImage;
        imageArray[in++] = sandImage;
        imageArray[in++] = brickImage;
        imageArray[in++] = woodImage;
        imageArray[in++] = bedTopImage;
        imageArray[in++] = bedBottomImage;
        imageArray[in++] = nightstandImage;
        imageArray[in++] = doorImage;
        imageArray[in++] = chairImage;
        imageArray[in++] = tableBleftImage;
        imageArray[in++] = tableBrightImage;
        imageArray[in++] = tableTrightImage;
        imageArray[in++] = tableTleftImage;
        imageArray[in++] = shelterBleftImage;
        imageArray[in++] = shelterBrightImage;
        imageArray[in++] = shelterTrightImage;
        imageArray[in++] = shelterTleftImage;
        imageArray[in++] = explosiveImage;
        imageArray[in++] = flameImage;
        imageArray[in++] = burntGrassImage;
        imageArray[in++] = burntTreeImage;
    }

    /**
     * Checks for a modded texture first, if not found, defaults to the built-in texture.
     * @param modPath The path to the modded texture.
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

    // Getters for all images
    public Image getGrassImg() { return grassImg; }
    public Image getRockImg() { return rockImg; }
    public Image getTreeImage() { return treeImage; }
    public Image getWaterImage() { return waterImage; }
    public Image getSandImage() { return sandImage; }
    public Image getBrickImage() { return brickImage; }
    public Image getWoodImage() { return woodImage; }
    public Image getBedTopImage() { return bedTopImage; }
    public Image getBedBottomImage() { return bedBottomImage; }
    public Image getNightstandImage() { return nightstandImage; }
    public Image getDoorImage() { return doorImage; }
    public Image getChairImage() { return chairImage; }
    public Image getTableBleftImage() { return tableBleftImage; }
    public Image getTableBrightImage() { return tableBrightImage; }
    public Image getTableTrightImage() { return tableTrightImage; }
    public Image getTableTleftImage() { return tableTleftImage; }
    public Image getShelterBleftImage() { return shelterBleftImage; }
    public Image getShelterBrightImage() { return shelterBrightImage; }
    public Image getShelterTrightImage() { return shelterTrightImage; }
    public Image getShelterTleftImage() { return shelterTleftImage; }
    public Image getExplosiveImage() { return explosiveImage; }
    public Image getFlameImage() { return flameImage; }
    public Image getBurntGrassImage() { return burntGrassImage; }
    public Image getBurntTreeImage() { return burntTreeImage; }
}
