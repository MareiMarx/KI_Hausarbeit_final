package KI_Hausarbeit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Population {
    private double sumOfAllPathLength;
    private double sumOfAllFitnesses;

    private final List<Path> paths;

    public Population() {
        this.paths = new ArrayList<>();
        this.sumOfAllPathLength = 0;
        this.sumOfAllFitnesses = 0;
    }

    public Population(List<Path> paths) {
        this.paths = new ArrayList<>(paths); // defensive copy
        recalcSums();
    }

    public int getSize() {
        return paths.size();
    }

    public void generateFirstGeneration(CityNode[] cityNodes, int populationSize) {
        paths.clear();
        sumOfAllPathLength = 0;
        sumOfAllFitnesses = 0;

        for (int i = 0; i < populationSize; i++) {
            List<CityNode> cityNodesCopy = new ArrayList<>(List.of(cityNodes));
            Collections.shuffle(cityNodesCopy);
            Path randomPath = new Path(cityNodesCopy);
            addPath(randomPath);
        }
    }

    /**
     * Adds a Path to this population and updates cached sums.
     */
    private void addPath(Path path) {
        paths.add(path);
        sumOfAllPathLength += path.getCost();
        sumOfAllFitnesses += path.calcFitness();
    }

    /**
     * Calculates the average Path Cost of this Population.
     */
    public double calcAverageCost() {
        if (paths.isEmpty()) return 0;
        return sumOfAllPathLength / paths.size();
    }

    /**
     * Returns the best n Paths of this population (lowest cost).
     * Does NOT reorder the population permanently.
     */
    public List<Path> getBestPaths(int n) {
        if (n <= 0) return List.of();
        n = Math.min(n, paths.size());

        List<Path> copy = new ArrayList<>(paths);
        copy.sort(Comparator.comparingDouble(Path::getCost));
        return copy.subList(0, n);
    }

    public Path getPath(int index) {
        return paths.get(index);
    }

    public double getSumOfAllFitnesses() {
        return sumOfAllFitnesses;
    }

    /**
     * Recalculates cached sums (useful if you ever change paths in-place).
     */
    private void recalcSums() {
        sumOfAllPathLength = 0;
        sumOfAllFitnesses = 0;

        for (Path p : paths) {
            sumOfAllPathLength += p.getCost();
            sumOfAllFitnesses += p.calcFitness();
        }
    }
}
