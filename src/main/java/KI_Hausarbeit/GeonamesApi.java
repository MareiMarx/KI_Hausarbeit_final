package KI_Hausarbeit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class GeonamesApi {
    private static final String USERNAME = "Leo17";
    private static final String BASE_URL = "http://api.geonames.org/searchJSON?";
    private static final String MAX_ROWS = "1";
    private static final String CITY_QUERY = "q=%s&maxRows=%s&username=%s";


    private GeonamesApi() {
    }

    /**
     * Returns a node from a given city name. Spaces must be replaced by -
     *
     * @param cityName
     * @return a Node with id, name, longitute and lotitude
     * @throws Exception
     */
    public static CityNode getNodeFromCityname(String cityName) throws Exception {
        String urlStr = String.format(BASE_URL + CITY_QUERY, cityName, MAX_ROWS, USERNAME);
        String response = sendGetRequest(urlStr);
        return parseCityResponse(response);
    }

    public static String sendGetRequest(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        conn.disconnect();

        return content.toString();
    }

    public static CityNode parseCityResponse(String response) throws Exception {
        JSONObject json = new JSONObject(response);
        JSONArray geonames = json.getJSONArray("geonames");
        if (!geonames.isEmpty()) {
            JSONObject city = geonames.getJSONObject(0);
            String name = city.getString("name");
            double latitude = city.getDouble("lat");
            double longitude = city.getDouble("lng");
            return new CityNode(name, latitude, longitude);
        } else {
            throw new Exception("City not found");
        }
    }

    public static List<String> readCitiesFromFile(String filename) throws IOException {
        List<String> cities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                cities.add(line);
            }
        }
        return cities;
    }
}