package KI_Hausarbeit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Speichert kumulierte Fitness-Statistiken pro Generation über mehrere Runs.
 *
 * - Für jeden Run werden pro Generation Best- und Durchschnittswerte addiert.
 * - Beim Schreiben werden daraus Mittelwerte (Summe / numOfRuns) berechnet.
 */
public class AlgorithmData {

    //Kumulierte Best-Fitness pro Generation (Summe über alle Runs).
    private final double[] bestFitnessSum;
    //Kumulierte Durchschnitts-Fitness pro Generation (Summe über alle Runs).
    private final double[] avgFitnessSum;

    /**
     * @param numOfGenerations Anzahl der Generationen (muss > 0 sein)
     */
    public AlgorithmData(int numOfGenerations) {
        if (numOfGenerations <= 0) {
            throw new IllegalArgumentException("numOfGenerations must be > 0");
        }
        this.bestFitnessSum = new double[numOfGenerations];
        this.avgFitnessSum = new double[numOfGenerations];
    }

    /**
     * Addiert Daten für eine bestimmte Generation (kumuliert über Runs).
     *
     * @param generation Index der Generation (0-basiert)
     * @param bestFitness z.B. kürzeste Pfadlänge in dieser Generation
     * @param averageFitness z.B. durchschnittliche Pfadlänge in dieser Generation
     */
    public void addDataForGeneration(int generation, double bestFitness, double averageFitness) {
        validateGenerationIndex(generation);

        // Kumulieren (Summe über Runs)
        bestFitnessSum[generation] += bestFitness;
        avgFitnessSum[generation] += averageFitness;
    }

    /**
     * Schreibt die Mittelwerte pro Generation in eine CSV-Datei.
     * Format: BestFitnessMean,AverageFitnessMean
     *
     * @param numOfRuns Anzahl der Runs (muss > 0 sein)
     * @param filename Dateiname (z.B. "run_stats.csv")
     * @throws IOException wenn das Schreiben fehlschlägt
     */
    public void writeAverageData(int numOfRuns, String filename) throws IOException {
        if (numOfRuns <= 0) {
            throw new IllegalArgumentException("numOfRuns must be > 0");
        }
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("filename must not be null/blank");
        }

        // Sicherstellen, dass das Ausgabe-Verzeichnis existiert
        Path dir = Paths.get("statistics");
        Files.createDirectories(dir);

        Path outFile = dir.resolve(filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile.toFile()))) {
            // CSV Header (hilfreich für Excel/Plotting)
            writer.write("BestFitnessMean,AverageFitnessMean");
            writer.newLine();

            for (int gen = 0; gen < bestFitnessSum.length; gen++) {
                double bestMean = bestFitnessSum[gen] / (double) numOfRuns;
                double avgMean = avgFitnessSum[gen] / (double) numOfRuns;

                writer.write(String.valueOf(bestMean));
                writer.write(',');
                writer.write(String.valueOf(avgMean));
                writer.newLine();
            }
        }
    }

    /** @return Anzahl der Generationen, für die Daten gespeichert werden. */
    public int getNumOfGenerations() {
        return bestFitnessSum.length;
    }

    private void validateGenerationIndex(int generation) {
        if (generation < 0 || generation >= bestFitnessSum.length) {
            throw new IllegalArgumentException(
                    "generation must be in [0, " + (bestFitnessSum.length - 1) + "], but was " + generation
            );
        }
    }
}

