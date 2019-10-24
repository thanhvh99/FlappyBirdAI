package FlappyBird.Renderer;

import FlappyBird.Controller.GameController;
import FlappyBird.Entity.Bird;
import FlappyBird.Entity.Pipe;
import FlappyBird.Main;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

public class Renderer {

    private GameController controller;
    private ScrollingImage background;
    private ScrollingImage ground;
    private Font font;

    public Renderer(GameController controller) {
        this.controller = controller;
        background = new ScrollingImage(0, 0, Main.SCREEN.width, Main.SCREEN.height, GameController.MOVEMENT_SPEED * 0.5f, ScrollingImage.Direction.LEFT, Sprite.background);
        ground = new ScrollingImage(0, Main.SKY.height, Main.SCREEN.width, Sprite.ground.getHeight(), GameController.MOVEMENT_SPEED, ScrollingImage.Direction.LEFT, Sprite.ground);
        font = new Font(null, Font.PLAIN, 25);
    }

    public void update() {
        background.update();
        ground.update();
    }

    public void draw() {
        BufferStrategy bufferStrategy = Main.getCanvas().getBufferStrategy();
        Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
        graphics.setFont(font);

        //  background
        background.draw(graphics);

        //  pillar
        for (Pipe pipe : controller.getPipes()) {
            int x = pipe.getRoundX();
            int y = pipe.getRoundY();
            graphics.drawImage(Sprite.pillar, x - Pipe.WIDTH, y - Pipe.HEIGHT / 2, Pipe.WIDTH, -Sprite.pillar.getHeight(), null);
            graphics.drawImage(Sprite.pillar, x - Pipe.WIDTH, y + Pipe.HEIGHT / 2, Pipe.WIDTH, Sprite.pillar.getHeight(), null);
        }

        // bird
        AffineTransform transform = graphics.getTransform();
        for (Bird bird : controller.getBirds()) {
            if (bird.isAlive()) {
                int x = bird.getRoundX();
                int y = bird.getRoundY();
                graphics.rotate(-bird.getRotate(), x, y);
                graphics.drawImage(bird.getImage(), x - Bird.SIZE.getX() / 2, y - Bird.SIZE.getY() / 2, Bird.SIZE.getX(), Bird.SIZE.getY(), null);
                graphics.rotate(bird.getRotate(), x, y);
            }
        }

        //  ground
        ground.draw(graphics);

        //  info
        if (controller.isShowDetail()) {
            graphics.drawString("Generation: " + controller.getGeneration(), 50, Main.SCREEN.height - 70);
            graphics.drawString("Time scale: " + controller.getTimeScale(), 50, Main.SCREEN.height - 40);
            graphics.drawString("Ticks / second: " + controller.getTicksPerSecond(), 50, Main.SCREEN.height - 10);

            graphics.drawString("Population: " + controller.getPopulation().getAlive() + " / " + controller.getPopulation().getSize(), 400, Main.SCREEN.height - 70);
            graphics.drawString("Highest fitness: " + controller.getHighestFitness(), 400, Main.SCREEN.height - 40);
            graphics.drawString("Current fitness: " + controller.getCurrentFitness(), 400, Main.SCREEN.height - 10);

            graphics.drawString("Player: " + (controller.isHumanPlay() ? "Human" : "Computer"), 750, Main.SCREEN.height - 70);
            graphics.drawString("Highest score: " + controller.getHighestScore(), 750, Main.SCREEN.height - 40);
            graphics.drawString("Current score: " + controller.getCurrentScore(), 750, Main.SCREEN.height - 10);
        } else {
            graphics.drawString("Highest score: " + controller.getHighestScore(), 20, Main.SCREEN.height - 40);
            graphics.drawString("Current score: " + controller.getCurrentScore(), 20, Main.SCREEN.height - 10);
        }

        bufferStrategy.show();
    }
}
