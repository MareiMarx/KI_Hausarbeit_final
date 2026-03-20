package KI_Hausarbeit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Path {
    // Order of cities visited (the tour)
    private final List<CityNode> cityNodesTour;
    // Total distance of this tour
    private double cost;

    public Path() {
        this.cityNodesTour = new ArrayList<>();
        this.cost = 0;
    }

    /**
     * IMPORTANT: Expects a VALID permutation tour (each city once).
     * This constructor takes the list reference as-is.
     * If you want safety, pass a new ArrayList<>(list) when calling.
     */
    public Path(List<CityNode> newPath) {
        this.cityNodesTour = newPath;
        calcCost();
    }

    /**
     * Copy constructor (deep copy of the list; CityNode assumed immutable/reference-stable).
     */
    public Path(Path other) {
        this.cityNodesTour = new ArrayList<>(other.cityNodesTour);
        this.cost = other.cost; // cost is valid because tour is identical
    }

    /**
     * Creates a deep copy of this Path (list copy).
     */
    public Path copy() {
        return new Path(this);
    }

    /**
     * Tour length:
     * Sums distance from city 0→1, 1→2, ..., n-2→n-1, and last → first.
     */
    public void calcCost() {
        cost = 0;

        int n = cityNodesTour.size();
        if (n <= 1) {
            cost = 0;
            return;
        }

        for (int i = 1; i < n; i++) {
            cost += cityNodesTour.get(i - 1).distanceToCity(cityNodesTour.get(i));
        }
        cost += cityNodesTour.get(n - 1).distanceToCity(cityNodesTour.get(0));
    }

    /**
     * Fitness for minimization problems like TSP.
     * Higher is better.
     */
    public double calcFitness() {
        // avoid division by zero
        return 1.0 / (cost + 1e-9);
    }

    public double getCost() {
        return cost;
    }

    /**
     * Returns the INTERNAL list (mutable).
     * You currently rely on this for mutations in the GA.
     * Make sure to call calcCost() after any modification.
     */
    public List<CityNode> getCityNodesTour() {
        return cityNodesTour;
    }

    /**
     * Optional safe read-only view (useful for debugging/printing without accidental edits).
     */
    public List<CityNode> getCityNodesTourView() {
        return Collections.unmodifiableList(cityNodesTour);
    }

    public CityNode getCityNode(int index) {
        return cityNodesTour.get(index);
    }

    public int getSize() {
        return cityNodesTour.size();
    }

    @Override
    public String toString() {
        return String.valueOf(cost);
    }
}
