package KI_Hausarbeit;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class AlgorithmData {
    private final long[] bestFitnessData;
    private final double[] averageFitnessData;

    public AlgorithmData(int numOfGenerations) {
        bestFitnessData = new long[numOfGenerations];
        averageFitnessData = new double[numOfGenerations];
    }

    public void addDataForGeneration(int generation, long shortestPathLength, double averagePathLength) {
        bestFitnessData[generation] += shortestPathLength;
        averageFitnessData[generation] += averagePathLength;
    }

    public void writeAverageData(int numOfRuns, String filename) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("statistics/" + filename))) {
            for (int i = 0; i < bestFitnessData.length; i++) {
                writer.write(String.valueOf(bestFitnessData[i] / (double) numOfRuns));
                writer.write(',');
                writer.write(String.valueOf(averageFitnessData[i] / (double) numOfRuns));
                writer.newLine();
            }
        }
    }
}
