package com.example.rpg.mobScripts;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.example.rpg.mob;

import javafx.scene.image.Image;

public class cow extends mob {

    static {
        mob.register(cow::new);
    }

    private static final Image img = new Image(cow.class.getResourceAsStream("/com/example/rpg/res/mob/cow0.png"));
    InputStream input = cow.class.getResourceAsStream("/com/example/rpg/names/cowNames.txt");
    Scanner scan = new Scanner(input);
    private ArrayList<String> cowNames = new ArrayList<>();

    public cow(int xpos, int ypos, int[][] map) {    
        super(xpos, ypos, map, img);
    
        try {
            while (scan.hasNextLine()) {
                cowNames.add(scan.nextLine());
            }
            scan.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        Random rand = new Random();
        name = cowNames.get(rand.nextInt(cowNames.size()));

    }

    public cow() {
        super(0, 0, new int[1][1], new Image(checkAndLoadTexture("../mods/mob/cow.png", "../res/mob/cow.png")));
        name = "DebugCow";
    }
}
