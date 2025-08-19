package com.example.rpg.mobScripts;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.example.rpg.mob;

import javafx.scene.image.Image;

public class sheep extends mob {

    private static final Image img = new Image(sheep.class.getResourceAsStream("/com/example/rpg/res/mob/sheep0.png"));
    InputStream input = sheep.class.getResourceAsStream("/com/example/rpg/names/sheepNames.txt");
    Scanner scan = new Scanner(input);
    private ArrayList<String> names = new ArrayList<>();

    public sheep(int xpos, int ypos, int[][] map) {
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

    public sheep() {
        super(0, 0, new int[1][1], new Image(checkAndLoadTexture("../mods/mob/sheep0.png", "../res/mob/sheep0.png")));
        name = "Debug";
    }

    static {
        mob.register(sheep::new);
    }
}
