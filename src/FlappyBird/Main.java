package FlappyBird;

import FlappyBird.Controller.GameController;
import FlappyBird.Renderer.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Main extends Canvas {

    private static Main main;

    public static final Dimension SCREEN = new Dimension(1120, 630);
    public static final Dimension MONITOR = Toolkit.getDefaultToolkit().getScreenSize();
    public static final Dimension SKY = new Dimension(SCREEN.width, SCREEN.height - Sprite.ground.getHeight());

    private JFrame jFrame;
    private GameController controller;

    private Main() {
        setupJFrame();
        setupListener();
        controller = new GameController();
    }

    private void setupListener() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 'd': controller.setTimeScale(controller.getTimeScale() + 0.5); break;
                    case 'a': controller.setTimeScale(controller.getTimeScale() - 0.5); break;
                    case 'w': controller.setPopulationSize(controller.getPopulation().getSize() + 1); break;
                    case 's': controller.setPopulationSize(controller.getPopulation().getSize() - 1); break;
                    case 'l': controller.loadSave(); break;
                    case 'r': controller.createNewGeneration(); break;
                    case 'm': controller.changeMode(); break;
                    case 'i': controller.setShowDetail(!controller.isShowDetail()); break;
                    case ' ': controller.controlBirds(); break;
                    case 'p':
                        if (controller.isRunning()) {
                            controller.stop();
                        } else {
                            controller.start();
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private void setupJFrame() {
        jFrame = new JFrame();
        jFrame.setLayout(new BorderLayout());
        jFrame.getContentPane().setPreferredSize(SCREEN);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setTitle("Flappy Bird");
        jFrame.setResizable(false);
        jFrame.setVisible(true);
        jFrame.setLocation((MONITOR.width - SCREEN.width) / 2, (MONITOR.height - SCREEN.height) / 5);
        jFrame.add(this, BorderLayout.CENTER);

        jFrame.pack();
        requestFocus();

        if (getBufferStrategy() == null) {
            createBufferStrategy(3);
        }
    }

    public static void main(String[] args) {
        main = new Main();
        main.controller.start();
    }

    public static void writeDataToFile(ArrayList<Double> data) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt"));
            for (int i = 0; i < data.size(); i++) {
                writer.write(Double.toString(data.get(i)));
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Double> readDataFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
            ArrayList<Double> result = new ArrayList<>();
            String string;
            while ((string = reader.readLine()) != null) {
                result.add(Double.parseDouble(string));
            }
            reader.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Canvas getCanvas() {
        return main;
    }
}
