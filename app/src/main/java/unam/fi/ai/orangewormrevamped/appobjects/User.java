package unam.fi.ai.orangewormrevamped.appobjects;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.Math;
import java.util.List;
import java.util.Set;
import unam.fi.ai.orangewormrevamped.appobjects.heuristics.HaversineHeuristic;
import unam.fi.ai.orangewormrevamped.appobjects.heuristics.Heuristic;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
//import java.util.concurrent.ThreadLocalRandom;

public class User {
    // TODO: Encapsulate attributes
    private String username;
    private String password;
    HashMap<Integer,Station> station_db; // K: station_id, V: Station object
    ArrayList<Route> saved_routes;
    public Graph subway;
    HashMap<String,Integer> line_sizes;
    HashMap<String,Integer> transfer_times;
    HashMap<String, ArrayList<Integer>> line_station_ids;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.station_db = new HashMap<>();
        this.line_sizes = new HashMap<>();
        this.transfer_times = new HashMap<>();
        this.line_station_ids = new HashMap<>();
        this.saved_routes = new ArrayList<>();

        // TODO: Change this to array + for loop
        line_sizes.put("1",20);
        line_sizes.put("2",24);
        line_sizes.put("3",21);
        line_sizes.put("4",10);
        line_sizes.put("5",13);
        line_sizes.put("6",11);
        line_sizes.put("7",14);
        line_sizes.put("8",19);
        line_sizes.put("9",12);
        line_sizes.put("A",10);
        line_sizes.put("B",21);
        line_sizes.put("12",20);

        String[] lines = {"1","2","3","4","5","6","7","8","9","A","B","12"};
        for (String line : lines) {
            // Random integer between 3 and 12
            transfer_times.put(line, 3 + (int)(Math.random() * 10));
        }


    }

    private boolean edgeExists(int u, int v, int weight) {
        for (Pair p : this.subway.getAdjacency_list().get(u)) {
            if (p.getFirst() == v && p.getSecond() == weight) return true;
        }
        return false;
    }

    // Add context parameter
    public void loadGraph(Context context) {
        String[] files = {"L1.csv", "L2.csv", "L3.csv", "L4.csv", "L5.csv", "L6.csv", "L7.csv", "L8.csv", "L9.csv", "LA.csv", "LB.csv", "L12.csv"};

        try {
            AssetManager assetManager = context.getAssets();

            for (String fileName : files) {
                InputStream is = assetManager.open("dbfiles/" + fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                System.out.println("Reading file: " + fileName);

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
                        String file_name = UserManager.current_user.toLowerSnake(name);
                        String file = "icons/L"+line_set.iterator().next()+"/"+file_name+".png";
                        System.out.println("This is the file: "+file);
                        // Merge or create Station
                        Station station;
                        if (station_db.containsKey(id)) {
                            station = station_db.get(id);
                            station.getLines().addAll(line_set);
                        } else {
                            station = new Station(id, name, line_set, lat, lon, file);
                            station_db.put(id, station);
                        }

                        String currentLine = fileName.replace(".csv", "").toUpperCase();
                        line_station_ids.computeIfAbsent(currentLine, k -> new ArrayList<>());
                        ArrayList<Integer> ids = line_station_ids.get(currentLine);
                        if (ids.isEmpty() || ids.get(ids.size() - 1) != id) {
                            ids.add(id);
                        }


                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing line in " + fileName + ": " + line);
                    }
                }

                br.close();
            }

            System.out.println("Loaded " + station_db.size() + " unique stations.");
        } catch (IOException e) {
            System.err.println("Error reading station files: " + e.getMessage());
        }
    }

    public void createGraph() {
        this.subway = new Graph(false, true);
        this.subway.setStation_db(this.station_db);

        for(int i=1; i<=163; i++){
            Station to_reverse = this.station_db.get(i);
            this.subway.setValueOnReverseDB(toLowerSnake(to_reverse.getName()),i);
        }

        Set<String> addedEdges = new HashSet<>();

        for (Map.Entry<String, ArrayList<Integer>> entry : line_station_ids.entrySet()) {
            ArrayList<Integer> stationIds = entry.getValue();

            for (int i = 0; i < stationIds.size() - 1; i++) {
                int from = stationIds.get(i);
                int to = stationIds.get(i + 1);

                String edgeKey1 = from + "-" + to;
                String edgeKey2 = to + "-" + from;

                if (!addedEdges.contains(edgeKey1) && !addedEdges.contains(edgeKey2)) {
                    subway.addEdge(from, to, 3 + (int)(Math.random() * 10));
                    addedEdges.add(edgeKey1);
                    addedEdges.add(edgeKey2);
                }
            }
        }
        /*
        // Optional: print adjacency list sizes
        for (int i = 1; i <= 163; i++) {
            ArrayList<Pair> adj = subway.getAdjacency_list().get(i);
            int size = (adj != null) ? adj.size() : 0;
            System.out.println("adj[" + i + "] size: " + size);
            if(i==2){
                System.out.println("Adjacencies of 2:");
                for(Pair u: adj){
                    System.out.println(u.getFirst());
                }
            }
            if(i==20){
                System.out.println("Adjacencies of 2:");
                for(Pair u: adj){
                    System.out.println(u.getFirst());
                }
            }
        }
         */
        ArrayList<Integer> dummy = this.subway.leastStationsPath(1,99);

        for(Integer dum : dummy){
            System.out.println("Name: "+this.station_db.get(dum).getName());
        }
        this.subway.resetPredeccesors();
        Heuristic euclidean = new HaversineHeuristic();
        ArrayList<Integer> dummy2 = this.subway.optimalPath(1,99,euclidean);

        System.out.println("Dummy size: "+dummy2.size());

        for(Integer dum : dummy2){
            if(dum == 0) continue;
            System.out.println("Name: "+this.station_db.get(dum).getName());
        }



    }


    public void useRoute(String routeName) {
        for (Route route : saved_routes) {
            if (route.getName().equals(routeName)) {
                route.useRoute();
                break;
            }
        }
    }

    public List<String> recommendedRoutes(int currentHour) {
        Map<String, Map<Integer, Integer>> frequencyMap = new HashMap<>();

        // 1. Build frequency table: routeName -> hour -> usageCount
        for (Route route : saved_routes) {
            String name = route.getName();
            frequencyMap.putIfAbsent(name, new HashMap<>());
            for (int hour : route.getUsageHours()) {
                frequencyMap.get(name).merge(hour, 1, Integer::sum);
            }
        }

        // 2. Collect valid routes with usage count > 3 at currentHour
        List<Map.Entry<String, Integer>> validRoutes = new ArrayList<>();

        for (Map.Entry<String, Map<Integer, Integer>> entry : frequencyMap.entrySet()) {
            int count = entry.getValue().getOrDefault(currentHour, 0);
            if (count > 3) {
                validRoutes.add(new AbstractMap.SimpleEntry<>(entry.getKey(), count));
            }
        }

        // 3. Sort routes by usage count (descending)
        validRoutes.sort((a, b) -> b.getValue() - a.getValue());

        // 4. Collect route names
        List<String> recommended = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : validRoutes) {
            recommended.add(entry.getKey());
        }

        return recommended.isEmpty()
                ? Collections.singletonList("No strong recommendations at this hour")
                : recommended;
    }


    public String toLowerSnake(String s){
        HashMap<Character,Character> character_handler = new HashMap<>();
        character_handler.put('á','a');
        character_handler.put('é','e');
        character_handler.put('í','i');
        character_handler.put('ó','o');
        character_handler.put('ú','u');
        character_handler.put('ñ','n');
        character_handler.put(' ','_');
        StringBuilder result = new StringBuilder(s.toLowerCase());

        for(int i=0; i<result.length(); i++){
            if(character_handler.containsKey(result.charAt(i))){
                result.setCharAt(i,character_handler.get(result.charAt(i)));
            }
        }
        return result.toString();
    }
    public void addNewRoute(Route r){
        this.saved_routes.add(r);
    }

    public Integer getNumberOfRoutes(){
        return this.saved_routes.size();
    }

    public HashMap<Integer, Station> getStation_db() {
        return station_db;
    }

    public ArrayList<Route> getSavedRoutes(){
        return this.saved_routes;
    }

    public HashMap<String, Integer> getTransfer_times() {
        return transfer_times;
    }
}
