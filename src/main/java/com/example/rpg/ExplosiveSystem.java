package com.example.rpg;

import java.util.*;

public class ExplosiveSystem {

    private final int[][] map;
    private final List<Explosive> explosives;

    public ExplosiveSystem(int[][] map, List<Explosive> explosives) {
        this.map = map;
        this.explosives = explosives;
    }

    public void update() {
        for (Explosive e : new ArrayList<>(explosives)) {
            detonate(e);
            explosives.remove(e);
        }
    }

    private void detonate(Explosive e) {
        int r = e.getRadius();
        int cx = e.getXPos();
        int cy = e.getYPos();

        for (int y = cy - r; y <= cy + r; y++) {
            for (int x = cx - r; x <= cx + r; x++) {
                if (y >= 0 && y < map.length &&
                    x >= 0 && x < map[0].length &&
                    Math.hypot(x - cx, y - cy) <= r) {
                    if (map[y][x] != 5 && map[y][x] != 6 && map[y][x] != 7) {
                        map[y][x] = 23;
                    }
                }
            }
        }
    }
}