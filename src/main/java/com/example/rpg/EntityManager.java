package com.example.rpg;

import java.util.ArrayList;
import java.util.Random;

/**
 * Manages all game entities including NPCs and mobs.
 * Handles spawning, updating, drawing, and tracking of all entities.
 * 
 * @author Carsen Gafford
 * @version alpha v0.2.0
 */
public class EntityManager {

    private final ArrayList<Npc> npcList = new ArrayList<>();
    private final ArrayList<mob> mobList = new ArrayList<>();
    private int[][] map;

    private static final Random rand = new Random();

    public EntityManager(int[][] map) {
        this.map = map;
    }

    public void spawnNpc(int x, int y, ArrayList<String> names) {
        if (names.isEmpty()) {
            System.err.println("Error: Names list is empty.");
            return;
        }

        Npc npc = new Npc(y, x, names.get(rand.nextInt(names.size())), map);
        npcList.add(npc);
        drawNpc(npc);
    }

    public void updateNpcs() {
        for (Npc npc : new ArrayList<>(npcList)) {
            npc.setLastXPos(npc.getxPos());
            npc.setLastYPos(npc.getyPos());
            npc.move();
            drawNpc(npc);
        }
    }

    public void drawNpc(Npc npc) {
        map[npc.getyPos()][npc.getxPos()] = 2;
    }

    public void removeNpc(Npc npc) {
        npcList.remove(npc);
    }

    public ArrayList<Npc> getNpcList() {
        return npcList;
    }

    public void spawnMob(int x, int y) {
        mob randomMob = mob.createRandomMob();
        if (randomMob == null) return;

        randomMob.setPosition(y, x);
        randomMob.setMap(map);
        mobList.add(randomMob);
        drawMob(randomMob);
    }

    public void updateMobs() {
        for (mob m : new ArrayList<>(mobList)) {
            m.setLastXPos(m.getxPos());
            m.setLastYPos(m.getyPos());

            if (m.isHunted()) {
                removeMob(m);
            } else {
                m.move();
                drawMob(m);
            }
        }
    }

    public void drawMob(mob mob) {
        map[mob.getyPos()][mob.getxPos()] = 4;
    }

    public void removeMob(mob mob) {
        mobList.remove(mob);
    }

    public ArrayList<mob> getMobList() {
        return mobList;
    }

    public void setMap(int[][] newMap) {
        this.map = newMap;
        for (mob m : mobList) {
            m.setMap(newMap);
        }
    }
}
