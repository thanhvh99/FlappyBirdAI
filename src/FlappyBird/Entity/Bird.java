package FlappyBird.Entity;

import FlappyBird.AI.NeuralNetwork;
import FlappyBird.Controller.GameController;
import FlappyBird.Main;
import FlappyBird.Point.Point;
import FlappyBird.Point.PointF;
import FlappyBird.Renderer.Sprite;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Bird implements Comparable<Bird> {

    public static Point SIZE = new Point(60, 40);
    public static final int POSITION_X = 200;

    private static final float GRAVITY = -40f;
    private static final float JUMP_STRENGTH = -GRAVITY * 6.5f;
    private static final Random random = new Random();

    private NeuralNetwork brain;
    private PointF position;
    private float velocity = 0;
    private double rotate = 0;
    private float acceleration = 0;
    private boolean alive = true;
    private int fitness = 0;
    private int state = 0;
    private int color;

    public Bird() {
        brain = new NeuralNetwork(2, 3, 1);
        position = new PointF(POSITION_X, Main.SKY.height / 2f);
        color = random.nextInt(3);
    }

    @Override
    public int compareTo(Bird o) {
        return o.fitness - fitness;
    }

    public BufferedImage getImage() {
        return Sprite.bird[color][state];
    }

    public float getX() {
        return position.getX();
    }

    public NeuralNetwork getBrain() {
        return brain;
    }

    public float getY() {
        return position.getY();
    }

    public int getFitness() {
        return fitness;
    }

    public int getRoundX() {
        return position.getRoundX();
    }

    public int getRoundY() {
        return position.getRoundY();
    }

    public void updateWithInput(float distanceX, float distanceY) {
        fitness++;
        double[][] input = new double[1][2];
        input[0][0] = distanceX;
        input[0][1] = distanceY;
        if (brain.calculate(input) > 0.5) {
            fly();
        }
        updateTransform();
        position.substract(0, velocity / GameController.TICKS_PER_SECOND);
        position.clampY(0, Main.SKY.height);
        updateAnimation();
    }

    public void update() {
        fitness++;
        updateTransform();
        position.substract(0, velocity / GameController.TICKS_PER_SECOND);
        position.clampY(0, Main.SKY.height);
        updateAnimation();
    }

    private void updateAnimation() {
        if (fitness % (GameController.TICKS_PER_SECOND / 5) == 0) {
            state++;
            if (state == 3) {
                state = 0;
            }
        }
    }

    private void updateTransform() {
        acceleration += GRAVITY / 60f;
        velocity += acceleration;
        rotate = Math.max(velocity / JUMP_STRENGTH * Math.PI / 4, - Math.PI / 4);
    }

    public boolean isAlive() {
        return alive;
    }

    public void dead() {
        alive = false;
    }

    public void fly() {
        acceleration = 0;
        velocity = JUMP_STRENGTH;
    }

    public double getRotate() {
        return rotate;
    }

    public boolean isCollide(ArrayList<Pipe> pipes) {
        for (Pipe pipe : pipes) {
            if (pipe.getRoundX() < position.getX() - SIZE.getX() / 2f) {
                continue;
            }
            if (pipe.getRoundX() - Pipe.WIDTH > position.getX() + SIZE.getX() / 2f) {
                break;
            }
            if (Math.abs(pipe.getRoundY() - position.getY()) < (Pipe.HEIGHT - SIZE.getY()) / 2f) {
                continue;
            }
            return true;
        }
        return position.getY() + SIZE.getY() / 2f > Main.SKY.height;
    }

}
