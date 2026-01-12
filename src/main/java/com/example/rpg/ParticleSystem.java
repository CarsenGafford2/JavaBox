package com.example.rpg;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manages particle effects including wind particles and falling leaves.
 */
public class ParticleSystem {
    
    private static final int WIND_PARTICLE_COUNT = 20;
    private static final int LEAF_PARTICLE_COUNT = 30;
    private final List<WindParticle> windParticles = new ArrayList<>();
    private final List<LeafParticle> leafParticles = new ArrayList<>();
    private boolean overlayInitialized = false;

    /**
     * Initializes the particle system with wind and leaf particles.
     * @param canvasWidth The width of the canvas.
     * @param canvasHeight The height of the canvas.
     */
    public void initialize(double canvasWidth, double canvasHeight) {
        if (!overlayInitialized) {
            windParticles.clear();
            for (int i = 0; i < WIND_PARTICLE_COUNT; i++) {
                windParticles.add(new WindParticle(canvasWidth, canvasHeight));
            }
            leafParticles.clear();
            for (int i = 0; i < LEAF_PARTICLE_COUNT; i++) {
                leafParticles.add(new LeafParticle(canvasWidth, canvasHeight));
            }
            overlayInitialized = true;
        }
    }

    /**
     * Updates and renders all particles on the canvas.
     * @param gc The GraphicsContext to render to.
     * @param canvasWidth The width of the canvas.
     * @param canvasHeight The height of the canvas.
     */
    public void renderParticles(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        // Animate and draw wind particles (white swirling lines)
        gc.save();
        gc.setGlobalAlpha(0.25);
        gc.setStroke(javafx.scene.paint.Color.WHITE);
        gc.setLineWidth(2.0);
        for (WindParticle p : windParticles) {
            p.update(canvasWidth, canvasHeight);
            gc.strokeOval(p.x, p.y, 18 + 8 * Math.sin(p.phase), 8 + 4 * Math.cos(p.phase));
        }
        gc.restore();

        // Animate and draw falling leaves (brown/orange/yellow ellipses)
        for (LeafParticle leaf : leafParticles) {
            leaf.update(canvasWidth, canvasHeight);
            gc.save();
            gc.setGlobalAlpha(0.7);
            gc.setFill(leaf.color);
            gc.translate(leaf.x, leaf.y);
            gc.rotate(leaf.angle);
            gc.fillOval(-leaf.width / 2, -leaf.height / 2, leaf.width, leaf.height);
            gc.restore();
        }
    }

    /**
     * Leaf particle that falls with rotation.
     */
    static class LeafParticle {
        double x, y, vy, vx, angle, angularVelocity, width, height;
        javafx.scene.paint.Color color;
        Random r = new Random();

        LeafParticle(double canvasWidth, double canvasHeight) {
            x = r.nextDouble() * canvasWidth;
            y = r.nextDouble() * canvasHeight;
            vx = 0.5 + r.nextDouble() * 3;
            vy = 0.2 + r.nextDouble() * 0.4;
            angle = r.nextDouble() * 360;
            angularVelocity = -1.0 + r.nextDouble() * 2;
            width = 8 + r.nextDouble() * 4;
            height = 3 + r.nextDouble() * 2;
            javafx.scene.paint.Color[] colors = {
                javafx.scene.paint.Color.web("#2e8b57"),
                javafx.scene.paint.Color.web("#228b22"),
                javafx.scene.paint.Color.web("#006400"),
                javafx.scene.paint.Color.web("#3cb371"),
                javafx.scene.paint.Color.web("#66cdaa")
            };
            color = colors[r.nextInt(colors.length)];
        }

        void update(double canvasWidth, double canvasHeight) {
            x += vx + Math.sin(angle * Math.PI / 180) * 0.05;
            y += vy;
            angle += angularVelocity;
            if (y > canvasHeight) {
                y = 0;
                x = r.nextDouble() * canvasWidth;
            }
            if (x > canvasWidth) x = 0;
            if (x < 0) x = canvasWidth;
        }
    }

    /**
     * Wind particle that moves in swirling patterns.
     */
    static class WindParticle {
        double x, y, vx, vy, phase;

        WindParticle(double width, double height) {
            Random r = new Random();
            x = r.nextDouble() * width;
            y = r.nextDouble() * height;
            vx = 0.5 + r.nextDouble() * 1.5;
            vy = -0.5 + r.nextDouble();
            phase = r.nextDouble() * Math.PI * 2;
        }

        void update(double width, double height) {
            x += vx + Math.sin(phase) * 0.5;
            y += vy + Math.cos(phase) * 0.5;
            phase += 0.05;
            if (x > width) x = 0;
            if (y > height) y = 0;
            if (x < 0) x = width;
            if (y < 0) y = height;
        }
    }
}
