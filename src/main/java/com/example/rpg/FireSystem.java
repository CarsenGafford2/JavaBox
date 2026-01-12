package com.example.rpg;

import java.util.*;

public class FireSystem {

    private final int[][] map;
    private final Random rand = new Random();

    public FireSystem(int[][] map) {
        this.map = map;
    }

    public void update() {
        List<int[]> newFlames = new ArrayList<>();
        double spreadChance = 0.6;

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == 23) {
                    map[y][x] = 25;
                    trySpread(y + 1, x, newFlames, spreadChance);
                    trySpread(y - 1, x, newFlames, spreadChance);
                    trySpread(y, x + 1, newFlames, spreadChance);
                    trySpread(y, x - 1, newFlames, spreadChance);
                }
            }
        }

        for (int[] pos : newFlames) {
            map[pos[0]][pos[1]] = 23;
        }
    }

    private void trySpread(int y, int x, List<int[]> flames, double chance) {
        if (y >= 0 && y < map.length &&
            x >= 0 && x < map[0].length &&
            map[y][x] == 3 &&
            rand.nextDouble() < chance) {
            flames.add(new int[]{y, x});
        }
    }
}
