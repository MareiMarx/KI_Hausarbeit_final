package KI_Hausarbeit;

import java.util.*;

public class GeneticAlgorithm {

    public enum MutationType {
        SWAP, REVERSE
    }

    public enum SelectionType {
        TOURNAMENT, ROULETTE
    }

    private AlgorithmData algorithmData;

    private Population population;
    private final CityNode[] nodes;
    private final int cityCount;

    private final int populationSize;
    private final int numOfGenerations;
    private final int elitistNumber;
    private final int mutationRate;
    private final MutationType mutationType;
    private final int recombinationRate;
    private final Random random = new Random();
    private final SelectionType selectionType;

    // neue Variable für die Turniergröße k
    private final int tournamentSize;

    private double smallestCostAllRuns = Double.MAX_VALUE;
    private double totalCostAllRuns = 0;

    public GeneticAlgorithm(CityNode[] nodes,
                            int populationSize,
                            int numOfGenerations,
                            SelectionType selectionType,
                            int mutationRate,
                            MutationType mutationType,
                            int elitistNumber,
                            int recombinationRate,
                            int tournamentSize) {

        if (nodes == null || nodes.length == 0) {
            throw new IllegalArgumentException("nodes must not be null or empty");
        }
        if (populationSize <= 0) {
            throw new IllegalArgumentException("populationSize must be > 0");
        }
        if (numOfGenerations <= 0) {
            throw new IllegalArgumentException("numOfGenerations must be > 0");
        }
        if (elitistNumber < 0 || elitistNumber > populationSize) {
            throw new IllegalArgumentException("elitistNumber must be between 0 and populationSize");
        }
        if (mutationRate < 0 || mutationRate > 100) {
            throw new IllegalArgumentException("mutationRate must be between 0 and 100");
        }
        if (recombinationRate < 0 || recombinationRate > 100) {
            throw new IllegalArgumentException("recombinationRate must be between 0 and 100");
        }
        if (selectionType == SelectionType.TOURNAMENT && (tournamentSize < 2 || tournamentSize > populationSize)) {
            throw new IllegalArgumentException("tournamentSize must be between 2 and populationSize");
        }

        this.nodes = nodes;
        this.cityCount = nodes.length;
        this.populationSize = populationSize;
        this.population = new Population();
        this.numOfGenerations = numOfGenerations;
        this.elitistNumber = elitistNumber;
        this.mutationRate = mutationRate;
        this.mutationType = mutationType;
        this.selectionType = selectionType;
        this.recombinationRate = recombinationRate;
        this.tournamentSize = tournamentSize;

        this.algorithmData = new AlgorithmData(numOfGenerations);
    }

    public void reset() {
        algorithmData = new AlgorithmData(numOfGenerations);
        smallestCostAllRuns = Double.MAX_VALUE;
        totalCostAllRuns = 0;
    }

    public double getSmallestCostAllRuns() {
        return smallestCostAllRuns;
    }

    public double getAverageSmallestCostOfAllRuns(int numOfRuns) {
        return totalCostAllRuns / (double) numOfRuns;
    }

    public void run(boolean timebound, int numOfSeconds) {
        population.generateFirstGeneration(nodes, populationSize);
        List<Path> bestPaths = null;

        if (timebound) {
            int generation = 0;
            long startTime = System.currentTimeMillis();

            while ((System.currentTimeMillis() - startTime) / 1000 < numOfSeconds) {
                bestPaths = runGeneration(generation++, false);
            }
        } else {
            for (int i = 0; i < numOfGenerations; i++) {
                bestPaths = runGeneration(i, true);
            }
        }

        if (bestPaths != null && !bestPaths.isEmpty()) {
            double smallestCost = bestPaths.get(0).getCost();
            smallestCostAllRuns = Math.min(smallestCostAllRuns, smallestCost);
            totalCostAllRuns += smallestCost;
        }
    }

    private List<Path> runGeneration(int generation, boolean saveGenerationData) {
        List<Path> elites = population.getBestPaths(elitistNumber);

        Path best = population.getBestPaths(1).get(0);
        double averageCost = population.calcAverageCost();

        if (saveGenerationData) {
            algorithmData.addDataForGeneration(generation, best.getCost(), averageCost);
        }

        List<Path> children = recombine();
        mutate(children);

        children.addAll(elites);
        population = new Population(children);

        return List.of(best);
    }

    public void writeResults(int numOfRuns) throws Exception {
        algorithmData.writeAverageData(numOfRuns, getFilename());
    }

    /**
     * Selects one Path from the current population, dependent on the SelectionType.
     *
     * @return the selected Path
     */
    private Path selectionPhase() {
        return switch (selectionType) {
            case ROULETTE -> rouletteSelection();
            case TOURNAMENT -> tournamentSelection();
        };
    }

    /**
     * Selects one parent based on probability:
     * fitness / totalFitness.
     *
     * @return the selected Path
     */
    private Path rouletteSelection() {
        double[] probabilities = new double[populationSize];
        double totalFitness = population.getSumOfAllFitnesses();

        for (int i = 0; i < populationSize; i++) {
            probabilities[i] = population.getPath(i).calcFitness() / totalFitness;
        }

        double spin = random.nextDouble();

        for (int i = 0; i < populationSize; i++) {
            spin -= probabilities[i];
            if (spin < 0) {
                return population.getPath(i);
            }
        }

        return population.getPath(populationSize - 1);
    }

    /**
     * Tournament Selection with configurable tournament size k.
     * Randomly picks k distinct individuals from the current population
     * and returns the fittest one.
     *
     * @return the selected parent
     */
    private Path tournamentSelection() {
        Path best = null;
        Set<Integer> chosenIndices = new HashSet<>();

        while (chosenIndices.size() < tournamentSize) {
            chosenIndices.add(random.nextInt(populationSize));
        }

        for (int index : chosenIndices) {
            Path candidate = population.getPath(index);
            if (best == null || candidate.calcFitness() > best.calcFitness()) {
                best = candidate;
            }
        }

        return best;
    }

    /**
     * Creates offspring using Order Crossover with two cut points.
     *
     * @return list of generated offspring
     */
    private List<Path> recombine() {
        List<Path> nextPopulation = new ArrayList<>();

        for (int i = 0; i < populationSize - elitistNumber; i++) {
            boolean crossOver = random.nextInt(100) < recombinationRate;

            Path parent1 = selectionPhase();

            if (!crossOver) {
                nextPopulation.add(parent1);
                continue;
            }

            Path parent2 = selectionPhase();
            CityNode[] child = new CityNode[cityCount];

            int cutIndexStart = random.nextInt(cityCount - 1);
            int cutIndexEnd = random.nextInt(cutIndexStart + 1, cityCount);

            List<CityNode> parent2Sublist =
                    parent2.getCityNodesTour().subList(cutIndexStart, cutIndexEnd + 1);

            for (int j = cutIndexStart; j <= cutIndexEnd; j++) {
                child[j] = parent2.getCityNode(j);
            }

            int indexChild = (cutIndexEnd == cityCount - 1) ? 0 : cutIndexEnd + 1;
            int indexParent = (cutIndexEnd == cityCount - 1) ? 0 : cutIndexEnd + 1;

            while (indexChild < cutIndexStart || indexChild > cutIndexEnd) {
                CityNode nodeParent1 = parent1.getCityNode(indexParent);

                if (!parent2Sublist.contains(nodeParent1)) {
                    child[indexChild] = nodeParent1;
                    indexChild = (indexChild + 1) % cityCount;
                }

                indexParent = (indexParent + 1) % cityCount;
            }

            Path childPath = new Path(new ArrayList<>(Arrays.stream(child).toList()));
            nextPopulation.add(childPath);
        }

        return nextPopulation;
    }

    /**
     * Mutates children depending on mutation rate and recalculates path cost.
     *
     * @param paths children to mutate
     */
    private void mutate(List<Path> paths) {
        for (Path p : paths) {
            if (random.nextInt(100) < mutationRate) {
                mutate(p);
                p.calcCost();
            }
        }
    }

    /**
     * Mutates one path.
     *
     * @param path the child to mutate
     */
    private void mutate(Path path) {
        int firstIndex = random.nextInt(path.getSize() - 1);
        int secondIndex = random.nextInt(firstIndex + 1, path.getSize());

        switch (mutationType) {
            case SWAP -> Collections.swap(path.getCityNodesTour(), firstIndex, secondIndex);

            case REVERSE -> {
                List<CityNode> tour = path.getCityNodesTour();
                int i = firstIndex;
                int j = secondIndex;

                while (i < j) {
                    Collections.swap(tour, i, j);
                    i++;
                    j--;
                }
            }
        }
    }

    private void log(double maxFitness, double averageFitness) {
    }

    public String getFilename() {
        return "cityCount=" + cityCount +
                ",populationSize=" + populationSize +
                ",numOfGenerations=" + numOfGenerations +
                ",elitistNumber=" + elitistNumber +
                ",mutationRate=" + mutationRate +
                ",mutationType=" + mutationType +
                ",recombinationRate=" + recombinationRate +
                ",selectionType=" + selectionType +
                ",tournamentSize=" + tournamentSize;
    }
}