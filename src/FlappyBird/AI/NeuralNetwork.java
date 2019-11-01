package FlappyBird.AI;

import java.util.ArrayList;

public class NeuralNetwork {

    private double[][] weight1;
    private double[][] weight2;
    private int input;
    private int hidden;
    private int output;

    public NeuralNetwork(int inputNode, int hiddenNode, int outputNode) {
        input = inputNode;
        hidden = hiddenNode;
        output = outputNode;
        weight1 = new double[inputNode][hiddenNode];
        weight2 = new double[hiddenNode][outputNode];
        randomWeights();
    }

    public void randomWeights() {
        for (int i = 0; i < input; i++) {
            for (int j = 0; j < hidden; j++) {
                weight1[i][j] = Math.random() * 2 - 1;
            }
        }
        for (int i = 0; i < hidden; i++) {
            for (int j = 0; j < output; j++) {
                weight2[i][j] = Math.random() * 2 - 1;
            }
        }
    }

    public double calculate(double[][] input) {
        double[][] hiddenInput = multiply(input, weight1);
        double[][] hiddenOutput = sigmoid(hiddenInput);
        double[][] result = sigmoid(multiply(hiddenOutput, weight2));
        return result[0][0];
    }

    private double[][] multiply(double[][] input, double[][] matrix) {
        double[][] result = new double[input.length][matrix[0].length];
        for (int i = 0; i < result.length; i++)
            for (int j = 0; j < result[0].length; j++)
            {
                result[i][j] = 0;
                for (int k = 0; k < input[0].length; k++) {
                    result[i][j] = result[i][j] + input[i][k] * matrix[k][j];
                }
            }
        return result;
    }

    private double[][] sigmoid(double[][] array)
    {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = sigmoid(array[i][j]);
            }
        }
        return array;
    }

    private double sigmoid(double x)
    {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public ArrayList<Double> encode() {
        ArrayList<Double> result = new ArrayList<>();
        for (double[] doubles : weight1) {
            for (int i = 0; i < doubles.length; i++) {
                result.add(doubles[i]);
            }
        }
        for (double[] doubles : weight2) {
            for (int i = 0; i < doubles.length; i++) {
                result.add(doubles[i]);
            }
        }
        return result;
    }

    public void decode(ArrayList<Double> gene) {
        int index = 0;
        for (int i = 0; i < weight1.length; i++) {
            for (int j = 0; j < weight1[0].length; j++) {
                weight1[i][j] = index < gene.size() ? gene.get(index) : 0;
                index++;
            }
        }
        for (int i = 0; i < weight2.length; i++) {
            for (int j = 0; j < weight2[0].length; j++) {
                weight2[i][j] = index < gene.size() ? gene.get(index) : 0;
                index++;
            }
        }
    }
}
