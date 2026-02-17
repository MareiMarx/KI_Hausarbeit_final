package KI_Hausarbeit;

import java.util.ArrayList;
import java.util.List;

public class Path {
    // Order of cities visited (the tour)
    private final List<CityNode> cityNodesTour;
    // Total distance of this tour
    private double cost;

    public Path(){
        this.cityNodesTour = new ArrayList<>();
        this.cost = 0;
    }
    public Path(List<CityNode> newPath){
        this.cityNodesTour = newPath;
        calcCost();
    }

    /**
     * Tour length:
     * Sums distance from city 0→1, 1→2, ..., n-2→n-1
     */
    public void calcCost(){
        cost = 0;
        for (int i = 1; i < cityNodesTour.size(); i++) {
            cost += cityNodesTour.get(i-1).distanceToCity(cityNodesTour.get(i));
        }
        cost += cityNodesTour.get(cityNodesTour.size()-1).distanceToCity(cityNodesTour.get(0));
    }

    public double calcFitness(){
        return (double) 1/ cost;
    }

    public double getCost(){
        return cost;
    }

    public List<CityNode> getCityNodesTour(){
        return cityNodesTour;
    }

    public CityNode getCityNode(int index){
        return cityNodesTour.get(index);
    }

    public int getSize(){
        return cityNodesTour.size();
    }

    @Override
    public String toString() {
        return String.valueOf(cost);
    }

}
