package com.example.rpg.mobScripts;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.example.rpg.mob;

import javafx.scene.image.Image;

public class chicken extends mob {

    static {
        mob.register(chicken::new);
    }

    private static final Image img = new Image(chicken.class.getResourceAsStream("/com/example/rpg/res/mob/chicken0.png"));
    InputStream input = chicken.class.getResourceAsStream("/com/example/rpg/names/chickenNames.txt");
    Scanner scan = new Scanner(input);
    private ArrayList<String> names = new ArrayList<>();

    public chicken(int xpos, int ypos, int[][] map) {
        super(xpos, ypos, map, img);
    
        try {
            while (scan.hasNextLine()) {
                names.add(scan.nextLine());
            }
            scan.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        Random rand = new Random();
        name = names.get(rand.nextInt(names.size()));

    }

    public chicken() {
        super(0, 0, new int[1][1], new Image(checkAndLoadTexture("../mods/mob/chicken0.png", "../res/mob/chicken0.png")));
        name = "Debug";
    }
}
