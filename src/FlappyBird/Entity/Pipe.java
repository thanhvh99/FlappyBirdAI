package FlappyBird.Entity;

import FlappyBird.Main;
import FlappyBird.Point.PointF;

import java.util.Random;

public class Pipe {

    public static final int HEIGHT = 160;
    public static final int WIDTH = 120;

    private static final Random random = new Random();

    private PointF position;
    private boolean pass = false;

    public Pipe(float x) {
        position = new PointF(x, random.nextInt(Main.SKY.height - HEIGHT * 2) + HEIGHT);
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    public int getRoundX() {
        return position.getRoundX();
    }

    public int getRoundY() {
        return position.getRoundY();
    }

    public void move(float distance) {
        position.substract(distance, 0);
    }
}
