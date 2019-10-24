package FlappyBird.Renderer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ScrollingImage {

    public enum Direction {UP, DOWN, LEFT, RIGHT}

    private int x;
    private int y;
    private int width;
    private int height;
    private float xOffset;
    private float yOffset;
    private float speed;
    private Direction direction;
    private BufferedImage image;

    public ScrollingImage(int x, int y, int width, int height, float speed, Direction direction, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.direction = direction;
        this.image = image;
    }

    public void draw(Graphics graphics) {
        graphics.drawImage(image, (int) xOffset, (int) yOffset, width, height, null);
        switch (direction) {
            case UP: graphics.drawImage(image, (int) x, (int) yOffset + height, width, height, null); break;
            case DOWN: graphics.drawImage(image, (int) xOffset, (int) yOffset - height, width, height, null); break;
            case LEFT: graphics.drawImage(image, (int) xOffset + width, (int) yOffset, width, height, null); break;
            case RIGHT: graphics.drawImage(image, (int) xOffset - width, (int) yOffset, width, height, null); break;
        }
    }

    public void update() {
        switch (direction) {
            case UP: yOffset -= speed; break;
            case DOWN: yOffset += speed; break;
            case LEFT: xOffset -= speed; break;
            case RIGHT: xOffset += speed; break;
        }
        if (Math.abs(x - xOffset) >= width) xOffset = x;
        if (Math.abs(y - yOffset) >= height) yOffset = y;
    }

}
