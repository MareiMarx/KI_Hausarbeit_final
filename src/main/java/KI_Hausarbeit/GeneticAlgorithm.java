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

    private long smallestCostAllRuns = Long.MAX_VALUE;
    private long totalCostAllRuns = 0;

    public GeneticAlgorithm(CityNode[] nodes, int populationSize, int numOfGenerations, SelectionType selectionType, int mutationRate, MutationType mutationType, int elitistNumber, int recombinationRate) {
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

        algorithmData = new AlgorithmData(numOfGenerations);
    }

    public void reset() {
        algorithmData = new AlgorithmData(numOfGenerations);
        smallestCostAllRuns = Long.MAX_VALUE;
        totalCostAllRuns = 0;
    }

    public long getSmallestCostAllRuns() {
        return smallestCostAllRuns;
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

        long smallestCost = bestPaths.get(0).getCost();
        smallestCostAllRuns = Long.min(smallestCostAllRuns, smallestCost);
        totalCostAllRuns += smallestCost;
    }

    private List<Path> runGeneration(int generation, boolean saveGenerationData) {
        List<Path> bestPaths = population.getBestPaths(elitistNumber);
        double averageCost = population.calcAverageCost();

        if (saveGenerationData) {
            algorithmData.addDataForGeneration(generation, bestPaths.get(0).getCost(), averageCost);
        }

        List<Path> children = recombine();
        mutate(children);

        children.addAll(bestPaths);
        population = new Population(children);

        return bestPaths;
    }

    public double getAverageSmallestCostOfAllRuns(int numOfRuns) {
        return totalCostAllRuns / (double) numOfRuns;
    }

    public void writeResults(int numOfRuns) throws Exception{
        algorithmData.writeAverageData(numOfRuns, getFilename());
    }

    /**
     * Selects one Path from the current population, dependent on the {@link SelectionType} of this Algorithm.
     * @return the selected Path
     */
    private Path selectionPhase() {
            Path selectedPath = new Path();
            switch (selectionType){
                case ROULETTE -> selectedPath = rouletteSelection();
                case TOURNAMENT -> selectedPath = tournamentSelection();
            }
            return selectedPath;
    }

    /**
     * Selects one Parent based on probability, which calculated through fitness divided by totalFitness.
     *
     * @return the selected Path
     */
    private Path rouletteSelection(){
        double[] probabilities = new double[populationSize];

        for (int i = 0; i < populationSize; i++) {
            probabilities[i] = population.getPath(i).calcFitness()/ population.getSumOfAllFitnesses();
        }
        double spin = random.nextDouble();

        for (int i = 0; i < populationSize; i++) {
            spin -= probabilities[i];
            if(spin < 0){
                return population.getPath(i);
            }
        }
        return population.getPath(populationSize-1);
    }

    /**
     * Soft Tournament Selection, Elitist doesn't have to be picked for the recombination process.
     * Picks two random individuals from current generation and selects the fittest one.
     *
     * @return the selected parent.
     */
    private Path tournamentSelection() {
        int firstIndex = random.nextInt(populationSize);
        int secondIndex = random.nextInt(populationSize);

        while (firstIndex == secondIndex) {
            secondIndex = random.nextInt(populationSize);
        }

        Path first = population.getPath(firstIndex);
        Path second = population.getPath(secondIndex);

        return first.calcFitness() < second.calcFitness() ? second : first;
    }

    /**
     * Creates a List of offspring with Order Crossover Recombination with two point cut.
     *
     * @return List of generated offspring
     */
    private List<Path> recombine() {
        List<Path> nextPopulation = new ArrayList<>();
        boolean crossOver;
        Path childPath;
        for (int i = 0; i < populationSize - elitistNumber; i++) {

            /* Choose if crossover should happen */
            crossOver = random.nextInt(0, 100) <= recombinationRate;
            Path parent1 = selectionPhase();
            if (!crossOver) {
                nextPopulation.add(parent1);
                continue;
            }

            Path parent2 = selectionPhase();
            CityNode[] child = new CityNode[cityCount];

            /* random cutting points */
            int cutIndexStart = random.nextInt(cityCount - 1);
            int cutIndexEnd = random.nextInt(cutIndexStart + 1, cityCount);

            /* Child gets recombinationGene from parent 2, e.g. child: _ _ _ 7 2 3 _*/
            List<CityNode> parent2Sublist = parent2.getCityNodes().subList(cutIndexStart, cutIndexEnd + 1); // end exclusive
            for (int j = cutIndexStart; j <= cutIndexEnd; j++) { //child gets recombinationGene
                child[j] = parent2.getCityNode(j);
            }

            /* Child gets remaining nodes from parent 1, starting at the end index, e.g. _ _ _ 7 2 3 1
             *  to preserve the parent's order of nodes */
            int indexChild = cutIndexEnd == cityCount - 1 ? 0 : cutIndexEnd + 1;
            int indexParent = cutIndexEnd == cityCount - 1 ? 0 : cutIndexEnd + 1;
            while (indexChild < cutIndexStart || indexChild > cutIndexEnd) {

                CityNode nodeParent1 = parent1.getCityNode(indexParent);
                if (!parent2Sublist.contains(nodeParent1)) {
                    child[indexChild] = nodeParent1;
                    indexChild = (indexChild + 1) % cityCount; //if cityCound is reached, switch to 0
                }
                indexParent = (indexParent + 1) % cityCount;
            }
            childPath = new Path(new ArrayList<>(Arrays.stream(child).toList()));

            nextPopulation.add(childPath);
        }

        return nextPopulation;
    }

    /**
     * Mutates a number of children depending on mutation rate and recalculates their cost of path.
     *
     * @param paths children to mutate
     */
    private void mutate(List<Path> paths) {
        for (int i = 0; i < paths.size(); i++) {

            int rand = random.nextInt(0, 100);

            if (rand < mutationRate) {
                int index = random.nextInt(paths.size());
                mutate(paths.get(index));
                paths.get(index).calcCost();
            }
        }
    }

    /**
     * Inverts a sub-sequence of the child
     *
     * @param path the child which is supposed to mutate
     */
    private void mutate(Path path) {
        int firstIndex = random.nextInt(path.getSize() - 1);
        int secondIndex = random.nextInt(firstIndex + 1, path.getSize());

        switch (mutationType) {
            case SWAP -> Collections.swap(path.getCityNodes(), firstIndex, secondIndex);
            case REVERSE -> {
                for (int i = secondIndex; i <= secondIndex / 2; i++) {
                    CityNode temp = path.getCityNodes().get(secondIndex - i); //last
                    path.getCityNodes().set(secondIndex - i, path.getCityNodes().get(i));
                    path.getCityNodes().set(i, temp);
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
                ",selectionType=" + selectionType;
    }
}
