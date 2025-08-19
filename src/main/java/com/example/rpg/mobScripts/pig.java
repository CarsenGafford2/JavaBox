package com.example.rpg.mobScripts;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.example.rpg.mob;

import javafx.scene.image.Image;

public class pig extends mob {

    private static final Image img = new Image(sheep.class.getResourceAsStream("/com/example/rpg/res/mob/pig.png"));
    InputStream input = cow.class.getResourceAsStream("/com/example/rpg/names/pigNames.txt");
    Scanner scan = new Scanner(input);
    private ArrayList<String> names = new ArrayList<>();

    public pig() {
        super(0, 0, new int[1][1], img);
        name = "Debug";

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

    static {
        mob.register(pig::new);
    }
}