package FlappyBird.Controller;

import FlappyBird.AI.Population;
import FlappyBird.Entity.Bird;
import FlappyBird.Entity.Pipe;
import FlappyBird.Renderer.Renderer;

import java.util.ArrayList;

public class GameController {

    public static final int TICKS_PER_SECOND = 60;
    private static final double NS_PER_TICK = 1000000000.0 / TICKS_PER_SECOND;
    private static final int LOOP_DELAY = 10;

    public static int PILLAR_DISTANCE = 350;
    public static int MOVEMENT_SPEED = 3;

    private ArrayList<Pipe> pipes = new ArrayList<>();
    private double timeScale = 1.0;
    private Population population;
    private Renderer renderer;
    private Thread thread;
    private int highestScore = 0;
    private int currentScore = 0;
    private boolean running = false;
    private boolean loadSave = false;
    private boolean reset = false;
    private boolean showDetail = false;
    private int ticksPerSecond = 0;
    private int ticks = 0;

    public GameController() {
        renderer = new Renderer(this);
        population = new Population(this, 5);
        initialize();
    }

    public void draw() {
        renderer.draw();
    }

    private void setupThread() {
        thread = new Thread(() -> {

            double delta = 0;
            long second = System.currentTimeMillis();
            long now = System.nanoTime();

            while (running) {

                if (timeScale < LOOP_DELAY) {
                    try {
                        Thread.sleep((int) (LOOP_DELAY / timeScale));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (loadSave) {
                    loadSave = false;
                    population.loadSave();
                    setupPipe();
                    highestScore = 0;
                    currentScore = 0;
                }

                if (reset) {
                    reset = false;
                    population.createNewGeneration();
                    setupPipe();
                    highestScore = 0;
                    currentScore = 0;
                }

                delta -= (now - (now = System.nanoTime())) / NS_PER_TICK * timeScale;

                while (delta > 1) {
                    ticks++;
                    delta -= 1;
                    update();
                    renderer.update();
                }
                if (second + 1000 < System.currentTimeMillis()) {
                    second += 1000;
                    ticksPerSecond = ticks;
                    ticks = 0;
                }
                draw();
            }
        });
    }

    private void initialize() {
        setupPipe();
        population.createNewGeneration();
    }

    private void setupPipe() {
        pipes.clear();
        pipes.add(new Pipe(600));
        pipes.add(new Pipe(950));
        pipes.add(new Pipe(1300));
        pipes.add(new Pipe(1650));
    }

    public void changeMode() {
        population.changeMode();
    }

    public boolean isHumanPlay() {
        return population.isHumanPlay();
    }

    public void controlBirds() {
        for (Bird bird : population.getBirds()) {
            bird.fly();
        }
    }

    private void update() {
        for (Pipe pipe : pipes) {
            pipe.move(MOVEMENT_SPEED);
        }
        population.update();
        if (!population.isAlive()) {
            setupPipe();
            highestScore = Math.max(highestScore, currentScore);
            currentScore = 0;
            population.createNextGeneration();
        }
        checkPillar();
    }

    private void checkPillar() {
        Pipe pipe = pipes.get(0);
        if (!pipe.isPass() && pipe.getX() < Bird.POSITION_X - Bird.SIZE.getY() / 2f) {
            pipe.setPass(true);
            currentScore++;
        }
        if (pipe.getRoundX() < 0) {
            pipes.remove(0);
            pipes.add(new Pipe(pipes.get(pipes.size() - 1).getRoundX() + PILLAR_DISTANCE));
        }
    }

    public Pipe getClosestPipe() {
        return pipes.get(0).isPass() ? pipes.get(1) : pipes.get(0);
    }

    public void stop() {
        running = false;
    }

    public void start() {
        running = true;
        setupThread();
        thread.start();
    }

    public ArrayList<Bird> getBirds() {
        return population.getBirds();
    }

    public ArrayList<Pipe> getPipes() {
        return pipes;
    }

    public int getGeneration() {
        return population.getGeneration();
    }

    public int getHighestFitness() {
        return population.getHighestFitness();
    }

    public int getCurrentFitness() {
        return population.getCurrentFitness();
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public Population getPopulation() {
        return population;
    }

    public void setTimeScale(double timeScale) {
        this.timeScale = Math.max(0.5, timeScale);
    }

    public void setPopulationSize(int size) {
        population.setSize(size);
    }

    public boolean isRunning() {
        return running;
    }

    public void loadSave() {
        loadSave = true;
    }

    public double getTimeScale() {
        return timeScale;
    }

    public void createNewGeneration() {
        reset = true;
    }

    public void setShowDetail(boolean show) {
        showDetail = show;
    }

    public boolean isShowDetail() {
        return showDetail;
    }

    public int getTicksPerSecond() {
        return ticksPerSecond;
    }
}
