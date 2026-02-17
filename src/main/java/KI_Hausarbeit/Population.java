package KI_Hausarbeit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Population {
    private double sumOfAllPathLength;

    private double sumOfAllFitnesses;

    private final List<Path> paths;

    private int populationSize;

    public Population(){
        paths = new ArrayList<>();
    }

    public Population(List<Path> paths){
        this.paths = paths;
        sumOfAllPathLength = paths.stream().mapToDouble(Path::getCost).sum();
        populationSize = paths.size();
        sumOfAllFitnesses = paths.stream().mapToDouble(Path::calcFitness).sum();
    }

    public void generateFirstGeneration(CityNode[] cityNodes, int populationSize){
        this.populationSize = populationSize;
        for (int i = 0; i < populationSize; i++) {
            List<CityNode> cityNodesCopy = new ArrayList<>(List.of(cityNodes));
            Collections.shuffle(cityNodesCopy);
            Path randomPath = new Path(cityNodesCopy);
            addPath(randomPath);
        }
    }

    /**
     * Adds a Path to this population and adds its costOfPath to the totalCostOfPath of this Population.
     * @param path the path to add
     */
    private void addPath(Path path){
        sumOfAllPathLength += path.getCost();
        paths.add(path);
        sumOfAllFitnesses += path.calcFitness();
    }

    /**
     * Calculates the average Path Cost of this Population.
     * @return average cost of path
     */
    public double calcAverageCost(){
        return sumOfAllPathLength / populationSize;
    }

    /**
     * Returns the best Paths of this population by sorting and returning the n first.
     * @param n number of Individuals to return
     * @return best n Paths.
     */
    public List<Path> getBestPaths(int n){
        paths.sort(Comparator.comparingDouble(Path::getCost));
        return paths.subList(0, n+1);
    }

    public Path getPath(int index){
        return paths.get(index);
    }

    public double getSumOfAllFitnesses() {
        return sumOfAllFitnesses;
    }

}
