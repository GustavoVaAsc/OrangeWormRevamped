package unam.fi.ai.orangewormrevamped.appobjects;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
//import java.util.concurrent.ThreadLocalRandom;

public class User {
    // TODO: Encapsulate attributes
    private String username;
    private String password;
    HashMap<Integer,Station> station_db; // K: station_id, V: Station object
    ArrayList<Route> saved_routes;
    Graph subway;
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

    // Add context parameter
    public void loadGraph(Context context) {
        HashSet<String> loaded_lines = new HashSet<>();
        String[] files = {"L1.csv", "L2.csv", "L3.csv", "L4.csv", "L5.csv", "L6.csv", "L7.csv", "L8.csv", "L9.csv", "LA.csv", "LB.csv", "L12.csv"};

        try {
            AssetManager asset_manager = context.getAssets();

            for (String fileName : files) {
                InputStream is = asset_manager.open("dbfiles/" + fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                System.out.println("Reading file: "+fileName);
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
                            station_db.get(id).getLines().addAll(line_set);
                        } else {
                            Station station = new Station(id, name, line_set, lat, lon);
                            station_db.put(id, station);
                        }

                        // Populate ordered station list per line
                        for (String lineName : line_parse) {
                            if (!line_station_ids.containsKey(lineName)) {
                                line_station_ids.put(lineName, new ArrayList<>());
                            }
                            line_station_ids.get(lineName).add(id);
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

        /*
        * for(int i=1; i<=163; i++){
            System.out.println("ID: "+i);
            System.out.println("Name: "+this.station_db.get(i).getName());
        }
        * */

    }


    public void createGraph(){
        this.subway = new Graph(false,true);

        for(String line : line_sizes.keySet()){
            ArrayList<Integer> ids = new ArrayList<>();

            // Collect all stations for this line
            for(Station s : station_db.values()){
                if(s.getLines().contains(line)){
                    ids.add(s.getId());
                }
            }

            ids.sort(Integer::compare);

            for(int i=0; i<ids.size()-1; i++){
                if(Objects.equals(line, "2") && i==6){ // Hidalgo
                    addTransfer(30, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "2") && i==14){ // Pino Suarez
                    addTransfer(11, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "3") && i==8){ // Balderas
                    addTransfer(8, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "4") && i==6){ // Candelaria
                    addTransfer(13, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "5") && i==0){ // Pantitlan L5
                    this.subway.addEdge(ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "5") && i==6){ // Consulado
                    addTransfer(66, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "5") && i==9){ // La Raza
                    addTransfer(47, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "6") && i==8){ // Deportivo 18 de marzo
                    addTransfer(45, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "6") && i==10){ // Martin Carrera
                    this.subway.addEdge(ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "7") && i==0){ // El Rosario
                    this.subway.addEdge(ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "7") && i==4){ // Tacuba
                    addTransfer(2, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "7") && i==9){ // Tacubaya L7
                    addTransfer(2, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "8") && i==1){ // Bellas Artes
                    addTransfer(31, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "8") && i==3){ // Salto del Agua
                    addTransfer(9, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "8") && i==6){ // Chabacano L8
                    addTransfer(35, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "8") && i==8){ // Santa Anita
                    addTransfer(71, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "9") && i==0){ // Tacubaya L9
                    this.subway.addEdge(2, ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "9") && i==3){ // Centro Medico
                    addTransfer(53, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "9") && i==5){ // Chabacano L9
                    addTransfer(35, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "9") && i==6){ // Jamaica
                    this.subway.addEdge(ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "9") && i==11){ // Pantitlan L9
                    this.subway.addEdge(ids.get(i), 20, transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "A") && i==0){ // Pantitlan LA
                    this.subway.addEdge(20, ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "B") && i==11){ // Oceania
                    addTransfer(74, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "B") && i==14){ // San Lazaro
                    addTransfer(14, ids.get(i), 68, transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "A") && i==15){ // Morelos
                    this.subway.addEdge(68, ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "B") && i==0){ // Garibaldi
                    addTransfer(101, ids.get(i), 49, transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "B") && i==19){ // Guerrero
                    this.subway.addEdge(49, ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "12") && i==0){ // Mixcoac
                    this.subway.addEdge(99, ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "12") && i==3){ // Zapata
                    addTransfer(57, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "12") && i==6){ // Ermita
                    addTransfer(41, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }
                if(Objects.equals(line, "12") && i==8){ // Atlalilco
                    addTransfer(111, ids.get(i), ids.get(i+1), transfer_times.get(line));
                    i++;
                    continue;
                }

                this.subway.addEdge(ids.get(i), ids.get(i+1), transfer_times.get(line));
            }
        }

        ArrayList<Integer> dummy = this.subway.leastStationsPath(1,99);
    }

    public void addTransfer(int transfer, int connection1, int connection2, int weight){
        this.subway.addEdge(connection1,transfer,weight);
        this.subway.addEdge(transfer,connection2,weight);
    }

    public void addNewRoute(Route to_add){
        this.saved_routes.add(to_add);
    }

    public HashMap<Integer, Station> getStation_db() {
        return station_db;
    }
}
