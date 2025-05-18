package unam.fi.ai.orangewormrevamped.appobjects;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.InputStream;
import java.io.InputStreamReader;

public class User {
    private String username;
    private String password;
    HashMap<Integer,Station> station_db; // K: station_id, V: Station object
    ArrayList<Route> saved_routes;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.station_db = new HashMap<>();
    }

    // Add context parameter
    public void loadGraph(Context context) {
        station_db = new HashMap<>();
        HashSet<String> loadedLines = new HashSet<>();
        String[] files = {"L1.csv", "L2.csv", "L3.csv", "L4.csv", "L5.csv", "L6.csv", "L7.csv", "L8.csv", "L9.csv", "LA.csv", "LB.csv", "L12.csv"};

        try {
            AssetManager assetManager = context.getAssets();

            for (String fileName : files) {
                InputStream is = assetManager.open("dbfiles/" + fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                String line;
                boolean firstLine = true;
                while ((line = br.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }

                    String[] parts = line.split(",", -1);
                    if (parts.length != 5) {
                        System.err.println("Skipping malformed line in " + fileName + ": " + line);
                        continue;
                    }

                    try {
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[1];
                        String[] line_parse = parts[2].split("-");
                        HashSet<String> line_set = new HashSet<>(Arrays.asList(line_parse));
                        double lat = Double.parseDouble(parts[3]);
                        double lon = Double.parseDouble(parts[4]);

                        if (station_db.containsKey(id)) {
                            // Merge lines for same station
                            station_db.get(id).getLines().addAll(line_set);
                        } else {
                            Station station = new Station(id, name, line_set, lat, lon);
                            station_db.put(id, station);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing line in " + fileName + ": " + line);
                    }
                }

                br.close();
            }

            System.out.println("Loaded " + station_db.size() + " stations.");
        } catch (IOException e) {
            System.err.println("Error reading station files: " + e.getMessage());
        }
    }


    public void addNewRoute(Route to_add){
        this.saved_routes.add(to_add);
    }

    public HashMap<Integer, Station> getStation_db() {
        return station_db;
    }
}
