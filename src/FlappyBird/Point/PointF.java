package FlappyBird.Point;

import FlappyBird.Main;

public class PointF {

    private float x;
    private float y;

    public PointF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getRoundX() {
        return Math.round(x);
    }

    public int getRoundY() {
        return Math.round(y);
    }

    public void substract(float x, float y) {
        this.x -= x;
        this.y -= y;
    }

    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void clampY(float min, float max) {
        y = Math.min(y, max);
        y = Math.max(y, min);
    }

    public float distance(PointF other) {
        return (float) Math.sqrt((other.getX() - x) * (other.getX() - x) + (other.getY() - y) * (other.getY() - y));
    }

    public float distance(float x, float y) {
        return (float) Math.sqrt((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y));
    }
}
