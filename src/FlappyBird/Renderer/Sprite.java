package FlappyBird.Renderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Sprite {

    private static Sprite sprite = new Sprite();

    public static BufferedImage pillar;
    public static BufferedImage background;
    public static BufferedImage ground;
    public static BufferedImage[][] bird;

    private Sprite() {
        try {
            background = ImageIO.read(Sprite.class.getResource("/Image/background.png"));
            ground = ImageIO.read(Sprite.class.getResource("/Image/ground.png"));
            pillar = ImageIO.read(Sprite.class.getResource("/Image/pillar.png"));
            BufferedImage tmp = ImageIO.read(Sprite.class.getResource("/Image/bird.png"));
            bird = new BufferedImage[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    bird[i][j] = tmp.getSubimage(j * 34, i * 24, 34, 24);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
