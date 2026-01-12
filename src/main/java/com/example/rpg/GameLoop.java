package com.example.rpg;

import java.util.*;
import javafx.animation.AnimationTimer;

/**
 * Main game loop handling NPC updates, rendering, and camera movement.
 * @author Carsen Gafford
 * @version alpha v0.2.0
 */
public class GameLoop {

    private final EntityManager entityManager;
    private final GameRenderer renderer;
    private final FireSystem fireSystem;
    private final ExplosiveSystem explosiveSystem;

    public GameLoop(EntityManager entityManager, GameRenderer renderer, FireSystem fireSystem, ExplosiveSystem explosiveSystem) {
        this.entityManager = entityManager;
        this.renderer = renderer;
        this.fireSystem = fireSystem;
        this.explosiveSystem = explosiveSystem;
    }

    public void startNpcTimer() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                entityManager.updateNpcs();
                fireSystem.update();
                explosiveSystem.update();
                entityManager.updateMobs();
            }
        }, 0, 500);
    }

    public void startRenderLoop() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                renderer.renderMap(entityManager);
            }
        }, 0, 8);
    }

    public void startCameraLoop(InputHandler input, GameRenderer renderer) {
        new AnimationTimer() {
            private long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dt = (now - last) / 1e9;
                last = now;

                input.updateCamera(dt);
                renderer.setCameraX(input.getCameraX());
                renderer.setCameraY(input.getCameraY());
            }
        }.start();
    }
}
