package com.example.rpg;
import org.reflections.Reflections;
import java.util.Set;

public class MobLoader {
    public static void loadAllMobs() {
        Reflections reflections = new Reflections("com.example.rpg.mobScripts");

        Set<Class<? extends mob>> mobClasses = reflections.getSubTypesOf(mob.class);

        for (Class<? extends mob> mobClass : mobClasses) {
            try {
                Class.forName(mobClass.getName()); // trigger static blocks
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
