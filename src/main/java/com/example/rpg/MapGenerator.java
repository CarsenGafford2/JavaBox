package com.example.rpg;

import java.util.Random;

/**
 * Generates procedural game maps using OpenSimplex2S noise.
 */
public class MapGenerator {
    
    private static final Random rand = new Random();

    /**
     * Generates a procedural map using OpenSimplex2S noise.
     * @param width The width of the map.
     * @param height The height of the map.
     * @return A 2D array representing the generated map.
     */
    public static int[][] generateMap(int width, int height) {
        int[][] generatedMap = new int[height][width];
        long seed = 100 + (long) (rand.nextDouble() * (1000000 - 100));
    
        for (int y = 0; y < height; y++) {
            int percent = (y * 100) / height;
            System.out.print("\rGenerating map... " + percent + "%");
        
            for (int x = 0; x < width; x++) {
                double value = OpenSimplex2S.noise2(seed, x * 0.05, y * 0.05);
                
                //reference codex doc for better labeled values
                if (value < -0.6) {
                    generatedMap[y][x] = 5; // Water (5)
                } else if (value < -0.5) {
                    generatedMap[y][x] = 6; // Sand (6)
                } else if (value < 0.1) {
                    generatedMap[y][x] = 0; // Tree (3)
                } else {
                    generatedMap[y][x] = 3; // Grass (0)
                }
            }
            for (int i = 0; i < 35; i++) {
                int x = rand.nextInt(width);
                int b = rand.nextInt(height);
                if (generatedMap[b][x] == 0) {
                    generatedMap[b][x] = 1; // Rock (1)
                }
            }
        }
    
        return generatedMap;
    }

    /**
     * Creates a clean copy of the map (without NPCs or mobs).
     * @param sourceMap The map to copy.
     * @return A new 2D array that is a copy of the source map.
     */
    public static int[][] createCleanMapCopy(int[][] sourceMap) {
        int[][] cleanMap = new int[sourceMap.length][sourceMap[0].length];
        for (int i = 0; i < sourceMap.length; i++) {
            for (int j = 0; j < sourceMap[i].length; j++) {
                cleanMap[i][j] = sourceMap[i][j];
            }
        }
        return cleanMap;
    }
}
