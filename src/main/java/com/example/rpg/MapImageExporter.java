package com.example.rpg;

import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Exports a 2D map array as an image file.
 * @author Carsen Gafford
 * @version alpha v0.2.0
 */
public class MapImageExporter {

    public static void export(int[][] map, String filename) {
        BufferedImage img = new BufferedImage(
                map[0].length, map.length, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                img.setRGB(x, y, tileColor(map[y][x]).getRGB());
            }
        }

        try {
            ImageIO.write(img, "png", new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Color tileColor(int tile) {
        switch (tile) {
            case 0:
                return new Color(34, 139, 34);
            case 1:
                return Color.GRAY;
            case 2:
                return Color.RED;
            case 3:
                return new Color(0, 100, 0);
            case 4:
                return Color.BLUE;
            default:
                return Color.BLACK;
        }
    }
}
