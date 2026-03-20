package KI_Hausarbeit;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        int numOfRuns = 20;
        int numOfSeconds = 1;
        int numOfGenerations = 1000;

        CityNode[] nodes5Cities = createCityNodes("src/main/resources/5_cities.txt");
        CityNode[] nodes20Cities = createCityNodes("src/main/resources/20_cities.txt");

        GeneticAlgorithm[] geneticAlgorithms = new GeneticAlgorithm[]{

                // k = 2
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1,  GeneticAlgorithm.MutationType.REVERSE, 1,  90, 2),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1,  GeneticAlgorithm.MutationType.REVERSE, 5, 90, 2),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1,  GeneticAlgorithm.MutationType.REVERSE, 10, 90, 2),

                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5,  GeneticAlgorithm.MutationType.REVERSE, 1,  90, 2),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5,  GeneticAlgorithm.MutationType.REVERSE, 5, 90, 2),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5,  GeneticAlgorithm.MutationType.REVERSE, 10, 90, 2),

                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 1,  90, 2),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 5, 90, 2),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 10, 90, 2),

                // k = 8
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1,  GeneticAlgorithm.MutationType.REVERSE, 1,  90, 8),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1,  GeneticAlgorithm.MutationType.REVERSE, 5, 90, 8),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1,  GeneticAlgorithm.MutationType.REVERSE, 10, 90, 8),

                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5,  GeneticAlgorithm.MutationType.REVERSE, 1,  90, 8),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5,  GeneticAlgorithm.MutationType.REVERSE, 5, 90, 8),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5,  GeneticAlgorithm.MutationType.REVERSE, 10, 90, 8),

                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 1,  90, 8),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 5, 90, 8),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 10, 90, 8),

                // k = 4
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1,  GeneticAlgorithm.MutationType.REVERSE, 1,  90, 4),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1,  GeneticAlgorithm.MutationType.REVERSE, 5, 90, 4),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1,  GeneticAlgorithm.MutationType.REVERSE, 10, 90, 4),

                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5,  GeneticAlgorithm.MutationType.REVERSE, 1,  90, 4),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5,  GeneticAlgorithm.MutationType.REVERSE, 5, 90, 4),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5,  GeneticAlgorithm.MutationType.REVERSE, 10, 90, 4),

                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 1,  90, 4),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 5, 90, 4),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 10, 90, 4)
        };

/*
        GeneticAlgorithm[] geneticAlgorithms = new GeneticAlgorithm[]{


                // k = 2
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1, GeneticAlgorithm.MutationType.REVERSE, 1, 90, 2),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1, GeneticAlgorithm.MutationType.REVERSE, 5, 90, 2),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1, GeneticAlgorithm.MutationType.REVERSE, 10, 90, 2),

                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5, GeneticAlgorithm.MutationType.REVERSE, 1, 90, 2),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5, GeneticAlgorithm.MutationType.REVERSE, 5, 90, 2),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5, GeneticAlgorithm.MutationType.REVERSE, 10, 90, 2),

                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 1, 90, 2),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 5, 90, 2),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 10, 90, 2),


                // k = 4
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1, GeneticAlgorithm.MutationType.REVERSE, 1, 90, 4),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1, GeneticAlgorithm.MutationType.REVERSE, 5, 90, 4),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1, GeneticAlgorithm.MutationType.REVERSE, 10, 90, 4),

                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5, GeneticAlgorithm.MutationType.REVERSE, 1, 90, 4),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5, GeneticAlgorithm.MutationType.REVERSE, 5, 90, 4),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5, GeneticAlgorithm.MutationType.REVERSE, 10, 90, 4),

                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 1, 90, 4),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 5, 90, 4),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 10, 90, 4),

                // k = 8
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1, GeneticAlgorithm.MutationType.REVERSE, 1, 90, 8),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1, GeneticAlgorithm.MutationType.REVERSE, 5, 90, 8),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 1, GeneticAlgorithm.MutationType.REVERSE, 10, 90, 8),

                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5, GeneticAlgorithm.MutationType.REVERSE, 1, 90, 8),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5, GeneticAlgorithm.MutationType.REVERSE, 5, 90, 8),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 5, GeneticAlgorithm.MutationType.REVERSE, 10, 90, 8),

                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 1, 90, 8),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 5, 90, 8),
                new GeneticAlgorithm(nodes20Cities, 500, numOfGenerations, GeneticAlgorithm.SelectionType.TOURNAMENT, 10, GeneticAlgorithm.MutationType.REVERSE, 10, 90, 8)
        };
*/

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

        /*// Run for a certain time
        for (int i = 0; i < numOfRuns; i++) {
            for (GeneticAlgorithm alg : geneticAlgorithms) {
                alg.run(true, numOfSeconds);
            }
        }
        for (GeneticAlgorithm alg : geneticAlgorithms) {
            System.out.println(alg.getSmallestCostAllRuns() + " " + alg.getAverageSmallestCostOfAllRuns(numOfRuns) + " " + alg.getFilename());
        }*/
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