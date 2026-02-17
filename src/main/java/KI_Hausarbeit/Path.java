package KI_Hausarbeit;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private final List<CityNode> cityNodes;
    private long cost;

    public Path(){
        this.cityNodes = new ArrayList<>();
        this.cost = 0;
    }
    public Path(List<CityNode> newPath){
        this.cityNodes = newPath;
        calcCost();
    }

    public void calcCost(){
        cost = 0;
        for (int i = 1; i < cityNodes.size(); i++) {
            cost += cityNodes.get(i-1).distanceToCity(cityNodes.get(i));
        }
        cost += cityNodes.get(cityNodes.size()-1).distanceToCity(cityNodes.get(0));
    }

    public double calcFitness(){
        return (double) 1/ cost;
    }

    public long getCost(){
        return cost;
    }

    public List<CityNode> getCityNodes(){
        return cityNodes;
    }

    public CityNode getCityNode(int index){
        return cityNodes.get(index);
    }

    public int getSize(){
        return cityNodes.size();
    }

    @Override
    public String toString() {
        return String.valueOf(cost);
    }

}
