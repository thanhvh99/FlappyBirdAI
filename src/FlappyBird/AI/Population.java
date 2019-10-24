package FlappyBird.AI;

import FlappyBird.Controller.GameController;
import FlappyBird.Entity.Bird;
import FlappyBird.Entity.Pipe;
import FlappyBird.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Population {

    private static final double MUTATION_RATE = 0.3;
    private static final Random random = new Random();
    private static final int MIN_POPULATION = 5;

    private int highestFitness;
    private int currentFitness;
    private int size;
    private int generation = 0;
    private int alive = 0;
    private boolean humanPlay = true;
    private ArrayList<Bird> birds = new ArrayList<>();
    private GameController controller;
    private ArrayList<Double> save;

    public Population(GameController controller, int size) {
        this.size = Math.max(size, MIN_POPULATION);
        this.controller = controller;
        save = Main.readDataFromFile();
        if (save == null) {
            save = new ArrayList<>();
            save.add(0.0);
        }
    }

    public void changeMode() {
        humanPlay = !humanPlay;
    }

    public boolean isHumanPlay() {
        return humanPlay;
    }

    public int getGeneration() {
        return generation;
    }

    public int getHighestFitness() {
        return highestFitness;
    }

    public int getCurrentFitness() {
        return currentFitness;
    }

    public void update() {
        alive = 0;
        if (humanPlay) {
            for (Bird bird : birds) {
                if (bird.isAlive()) {
                    alive++;
                    bird.update();
                    if (bird.isCollide(controller.getPipes())) {
                        bird.dead();
                    }
                }
            }
        } else {
            Pipe closestPipe = controller.getClosestPipe();
            for (Bird bird : birds) {
                if (bird.isAlive()) {
                    alive++;
                    bird.updateWithInput(closestPipe.getX() + Bird.SIZE.getY() / 2f - bird.getX(), closestPipe.getY() - bird.getY());
                    if (bird.isCollide(controller.getPipes())) {
                        bird.dead();
                    }
                }
            }
        }
        currentFitness = birds.get(0).getFitness();
    }

    public void createNewGeneration() {
        birds.clear();
        generation = 0;
        highestFitness = 0;
        currentFitness = 0;
        for (int i = 0; i < size; i++) {
            birds.add(new Bird());
        }
    }

    public void createNextGeneration() {
        generation++;
        Collections.sort(birds);
        Bird bestBird = birds.get(0);
        if (bestBird.getFitness() > highestFitness) {
            highestFitness = bestBird.getFitness();
            if (highestFitness > save.get(0)) {
                save = bestBird.getBrain().encode();
                save.add(0, (double) highestFitness);
                Main.writeDataToFile(save);
            }
        }
        Bird bird = new Bird();
        ArrayList<Double> nextGene = crossover(bestBird, birds.size() > 1 ? birds.get(1) : bird);
        ArrayList<Double> bestGene = bestBird.getBrain().encode();
        birds.clear();

        bird.getBrain().decode(nextGene);
        birds.add(bird);

        for (int i = 1; i < size; i++) {
            bird = new Bird();
            switch (i % 3) {
                case 0: bird.getBrain().decode(smallMutate(nextGene)); break;
                case 1: bird.getBrain().decode(bigMutate(nextGene)); break;
                case 2: bird.getBrain().decode(smallMutate(bestGene)); break;
            }
            birds.add(bird);
        }
    }

    private ArrayList<Double> crossover(Bird bird1, Bird bird2) {
        ArrayList<Double> gene1 = bird1.getBrain().encode();
        ArrayList<Double> gene2 = bird2.getBrain().encode();

        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < gene1.size(); i++) {
            result.add((gene1.get(i) + gene2.get(i)) / 2.0);
        }
        return result;
    }

    private ArrayList<Double> bigMutate(ArrayList<Double> gene) {
        boolean mutate = false;
        while (!mutate) {
            for (int i = 0; i < gene.size(); i++) {
                if (Math.random() < MUTATION_RATE) {
                    gene.set(i, Math.random() * 2 - 1);
                    mutate = true;
                }
            }
        }
        return gene;
    }

    private ArrayList<Double> smallMutate(ArrayList<Double> gene) {
        boolean mutate = false;
        while (!mutate) {
            for (int i = 0; i < gene.size(); i++) {
                if (Math.random() < MUTATION_RATE) {
                    double current = gene.get(i);
                    double add = (random.nextInt(21) - 10) / 100.0;
                    gene.set(i, current + add);
                    mutate = true;
                }
            }
        }
        return gene;
    }

    public ArrayList<Bird> getBirds() {
        return birds;
    }

    public boolean isAlive() {
        for (Bird bird : birds) {
            if (bird.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public int getSize() {
        return size;
    }

    public int getAlive() {
        return alive;
    }

    public void setSize(int size) {
        this.size = Math.max(size, MIN_POPULATION);
    }

    public void loadSave() {
        birds.clear();
        Bird bird = new Bird();
        highestFitness = save.get(0).intValue();
        save.remove(0);
        bird.getBrain().decode(save);
        save.add(0, (double) highestFitness);
        birds.add(bird);
    }
}
