package KI_Hausarbeit;

/**
 * Stores the information for a City in a TSP problem and calculates distances between cities.
 */
public record CityNode(String name, double latitude, double longitude) {

    /**
     * Calculates the great-circle distance using the Haversine formula.
     *
     * @param city the other city
     * @return distance to other city
     */
    public double distanceToCity(CityNode city) {
        double lat1 = latitude;
        double lat2 = city.latitude;

        // Haversine-Formula for calculating distance on Earth
        final int earthRadius = 6371; // Radius of Earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(city.longitude - longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (long) (earthRadius * c);
    }

    @Override
    public String toString() {
        return String.format("{%s, lat=%s lon=%s}", name, latitude, longitude);
    }
}
