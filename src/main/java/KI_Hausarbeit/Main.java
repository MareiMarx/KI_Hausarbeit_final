package KI_Hausarbeit;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        int numOfRuns = 2;
        int numOfSeconds = 5;
        int numOfGenerations = 1000;

        CityNode[] nodes5Cities = createCityNodes("src/main/resources/5_cities.txt");
        CityNode[] nodes20Cities = createCityNodes("src/main/resources/20_cities.txt");

        GeneticAlgorithm[] geneticAlgorithms = new GeneticAlgorithm[] {
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT,
                        2, GeneticAlgorithm.MutationType.SWAP, 5, 90),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.ROULETTE,
                        2, GeneticAlgorithm.MutationType.REVERSE, 5, 90)
        };

        // Run for specific number of generations and save average data for each generation to file
        for (int i = 0; i < numOfRuns; i++) {
            for (GeneticAlgorithm alg : geneticAlgorithms) {
                alg.run(false, -1);
            }
        }
        for (GeneticAlgorithm alg : geneticAlgorithms) {
            alg.writeResults(numOfRuns);
            System.out.println(alg.getSmallestCostAllRuns() + " " + alg.getFilename());
        }

        for (GeneticAlgorithm alg : geneticAlgorithms) {
            alg.reset();
        }
        System.out.println("-------------------------------------------");

        // Run for a certain time
        for (int i = 0; i < numOfRuns; i++) {
            for (GeneticAlgorithm alg : geneticAlgorithms) {
                alg.run(true, numOfSeconds);
            }
        }
        for (GeneticAlgorithm alg : geneticAlgorithms) {
            System.out.println(alg.getSmallestCostAllRuns() + " " + alg.getAverageSmallestCostOfAllRuns(numOfRuns) + " " + alg.getFilename());
        }
    }

    static CityNode[] createCityNodes(String filename) throws Exception {
        List<String> cityList = GeonamesApi.readCitiesFromFile(filename);
        CityNode[] nodes = new CityNode[cityList.size()];
        for (int i = 0; i < cityList.size(); i++) {
            nodes[i] = GeonamesApi.getNodeFromCityname(cityList.get(i));
        }

        return nodes;
    }
}
