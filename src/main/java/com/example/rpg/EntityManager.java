package com.example.rpg;

import java.util.ArrayList;
import java.util.Random;

/**
 * Manages all game entities including NPCs and mobs.
 * Handles spawning, drawing, and tracking of all entities.
 */
public class EntityManager {
    
    private ArrayList<Npc> npcList = new ArrayList<>();
    private ArrayList<mob> mobList = new ArrayList<>();
    private int[][] map;
    private static final Random rand = new Random();

    /**
     * Constructs an EntityManager with a reference to the game map.
     * @param map The game map array.
     */
    public EntityManager(int[][] map) {
        this.map = map;
    }

    /**
     * Spawns an NPC at the specified coordinates if the names list is not empty.
     * @param x X position to spawn at.
     * @param y Y position to spawn at.
     * @param names ArrayList of available NPC names.
     */
    public void spawnNpc(int x, int y, ArrayList<String> names) {
        if (!names.isEmpty()) {
            npcList.add(new Npc(y, x, names.get(rand.nextInt(names.size())), map));
            if (npcList.get(npcList.size() - 1).getName().equals("Bodger")) {
                System.out.println("Bodger Spawned");
            }
            drawNpc(npcList.get(npcList.size() - 1));
        } else {
            System.err.println("Error: Names list is empty. Cannot spawn NPC.");
        }
    }

    /**
     * Draws an NPC on the map at its current position.
     * @param guy The NPC to be drawn.
     */
    public void drawNpc(Npc guy) {
        map[guy.getyPos()][guy.getxPos()] = 2;
    }

    /**
     * Spawns a mob at the specified coordinates with a random type and name.
     * @param x X position to spawn at.
     * @param y Y position to spawn at.
     */
    public void spawnMob(int x, int y) {
        mob randomMob = mob.createRandomMob();

        if (randomMob != null) {
            randomMob.setPosition(y, x);
            randomMob.setMap(map);

            mobList.add(randomMob);
            drawMob(randomMob);
        }
    }

    /**
     * Draws a mob on the map at its current position.
     * @param guy The mob to be drawn.
     */
    public void drawMob(mob guy) {
        map[guy.getyPos()][guy.getxPos()] = 4;
    }

    /**
     * Gets the list of all NPCs.
     * @return ArrayList of NPCs.
     */
    public ArrayList<Npc> getNpcList() {
        return npcList;
    }

    /**
     * Gets the list of all mobs.
     * @return ArrayList of mobs.
     */
    public ArrayList<mob> getMobList() {
        return mobList;
    }

    /**
     * Removes an NPC from the list.
     * @param npc The NPC to remove.
     */
    public void removeNpc(Npc npc) {
        npcList.remove(npc);
    }

    /**
     * Removes a mob from the list.
     * @param mob The mob to remove.
     */
    public void removeMob(mob mob) {
        mobList.remove(mob);
    }

    /**
     * Updates the map reference (useful if the map is regenerated).
     * @param newMap The new map array.
     */
    public void setMap(int[][] newMap) {
        this.map = newMap;
        for (mob m : mobList) {
            m.setMap(newMap);
        }
    }
}
