package com.example.rpg;

import java.io.File;
import java.util.*;

/**
 * Loads names from a text file.
 * @author Carsen Gafford
 * @version alpha v0.2.0
 */
public class NameLoader {

    public static List<String> load(File file) {
        List<String> names = new ArrayList<>();
        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNextLine()) {
                names.add(scan.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }
}
