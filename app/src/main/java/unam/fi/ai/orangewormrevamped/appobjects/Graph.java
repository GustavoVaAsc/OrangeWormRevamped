package unam.fi.ai.orangewormrevamped.appobjects;
import android.graphics.LinearGradient;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;

import unam.fi.ai.orangewormrevamped.appobjects.heuristics.HaversineHeuristic;
import unam.fi.ai.orangewormrevamped.appobjects.heuristics.Heuristic;

public class Graph {
    private HashMap<Integer, ArrayList<Pair>> adjacency_list;
    private HashSet<Integer> vertices; // Vertices set
    private boolean directed; // Flag for directed graph
    private boolean weighted; // Flag for weighted graph
    private ArrayList<Integer> distances;
    private ArrayList<Integer> predecessors;
    private HashMap<Integer,Station> station_db;
    private HashMap<String,Integer> reverse_db;

    public Graph(boolean directed, boolean weighted){
        this.adjacency_list = new HashMap<>();
        this.directed = directed;
        this.weighted = weighted;
        this.vertices = new HashSet<>();
        this.reverse_db = new HashMap<>();
    }

    public void addEdge(int from, int to, int weight){
        vertices.add(from);
        vertices.add(to);

        if (!this.weighted){
            weight = 0;
        }

        Pair forward = new Pair(to, weight);
        adjacency_list.computeIfAbsent(from, k -> new ArrayList<>()).add(forward);

        if (!this.directed){
            Pair backward = new Pair(from, weight); // use a new object
            adjacency_list.computeIfAbsent(to, k -> new ArrayList<>()).add(backward);
        }
    }



    /*
     optimalPath() is the A* implementation, calculates the distances and
     sets the predecessors for the optimal path, based on the selected heuristic

     g(n) = { if we take a station transfer:
                     train advance (time in minutes) + transfer time (in minutes)
              else:
                     train advance (time in minutes)
            }
     */


    // TODO:  Add heuristic calculation, and convert this function into arraylist
    public ArrayList<Integer> optimalPath(int source, int goal, Heuristic heuristic) {
        //System.out.println("This is AStar");
        int n = vertices.size() + 1;

        double source_lat = station_db.get(source).getLatitude();
        double source_long = station_db.get(source).getLongitude();

        double goal_lat = station_db.get(goal).getLatitude();
        double goal_long = station_db.get(goal).getLongitude();

        distances = new ArrayList<>(Collections.nCopies(n, Integer.MAX_VALUE));
        predecessors = new ArrayList<>(Collections.nCopies(n, -1));

        distances.set(source, 0);
        predecessors.set(source, -1);

        PriorityQueue<Pair> openSet = new PriorityQueue<>();

        int fScoreSource = heuristic.h_function(source_long, goal_long, source_lat, goal_lat);
        openSet.add(new Pair(source, fScoreSource));

        while (!openSet.isEmpty()) {
            Pair current = openSet.poll();
            int u = current.getFirst();

            //if (u == goal) break; // Early exit if goal is reached

            for (Pair edge : adjacency_list.getOrDefault(u, new ArrayList<>())) {
                int v = edge.getFirst();
                int weight = edge.getSecond();

                //System.out.println("Now visiting: "+v);

                double v_lat = station_db.get(v).getLatitude();
                double v_long = station_db.get(v).getLongitude();

                int tentative_gScore = distances.get(u) + weight;

                if (tentative_gScore < distances.get(v)) {
                    int fScore = tentative_gScore + heuristic.h_function(v_long, goal_long, v_lat, goal_lat);
                    distances.set(v, tentative_gScore+fScore);
                    predecessors.set(v, u);

                    openSet.add(new Pair(v, fScore));
                }
            }
        }
        // Reconstruct path from destination to source using predecessors
        ArrayList<Integer> route = new ArrayList<>();
        int current = goal;

        if (predecessors.get(current) == -1 && current != source) {
            // No path found
            return route;
        }

        while (current != -1) {
            route.add(current);
            current = predecessors.get(current);
        }

        for(int i=1; i<=163;i++){
            System.out.println("Distance of " + i + " is: " + distances.get(i));
        }

        Collections.reverse(route);
        return route;
    }


     /*

     leastStationsPath() is the BFS implementation, calculates the route
     with the minimum amount of stations.

     */

    public ArrayList<Integer> leastStationsPath(int source, int destination) {
        // Initialize predecessors list with -1
        this.predecessors = new ArrayList<>(Collections.nCopies(vertices.size() + 1, -1));
        boolean[] visited = new boolean[vertices.size() + 1];
        Queue<Integer> q = new LinkedList<>();

        visited[source] = true;
        q.add(source);

        while (!q.isEmpty()) {
            int u = q.poll();
            for (Pair p : this.adjacency_list.get(u)) {
                int v = p.getFirst();
                //System.out.println("Now visiting: "+v);
                if (!visited[v]) {
                    visited[v] = true;
                    predecessors.set(v, u);
                    q.add(v);
                    if (v == destination) break;
                }
            }
        }

        // Reconstruct path from destination to source using predecessors
        ArrayList<Integer> route = new ArrayList<>();
        int current = destination;

        if (predecessors.get(current) == -1 && current != source) {
            // No path found
            return route;
        }

        while (current != -1) {
            route.add(current);
            current = predecessors.get(current);
        }

        Collections.reverse(route);
        return route;
    }

    public int calculateTransferTime(ArrayList<Integer> route, Heuristic heuristic) {
        if (route == null || route.size() < 2) return 0;

        int totalTransferTime = 0;
        int goal = route.get(route.size() - 1); // final destination

        Station goalStation = station_db.get(goal);

        for (int i = 0; i < route.size() - 1; i++) {
            int from = route.get(i);
            int to = route.get(i + 1);
            boolean found = false;

            for (Pair neighbor : adjacency_list.getOrDefault(from, new ArrayList<>())) {
                if (neighbor.getFirst() == to) {
                    int weight = neighbor.getSecond(); // g(n)

                    // h(n): heuristic from 'to' to goal
                    int hScore = heuristic.h_function(
                            station_db.get(to).getLongitude(), goalStation.getLongitude(),
                            station_db.get(to).getLatitude(), goalStation.getLatitude()
                    );
                    totalTransferTime += weight + hScore;
                    System.out.println("Weight: "+weight);
                    System.out.println("Heuristic: "+ hScore);
                    System.out.println("Transfer time: "+totalTransferTime);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Warning: Edge not found between " + from + " and " + to);
            }
        }

        return totalTransferTime;
    }


    public void resetPredeccesors(){
        this.predecessors.clear();
    }

    public HashMap<Integer,ArrayList<Pair>> getAdjacency_list(){return this.adjacency_list;}

    public void setStation_db(HashMap<Integer,Station> a){this.station_db = a;}

    public void setValueOnReverseDB(String s, Integer x){
        this.reverse_db.put(s,x);
    }

    public String queryByNameOnDB(Integer id){
        return this.station_db.get(id).getName();
    }

    public Station queryStation(Integer id){
        return this.station_db.get(id);
    }

    public Integer queryReverseDB(String s){
        return this.reverse_db.get(s);
    }

    public Integer queryDistance(Integer u){
        return this.distances.get(u);
    }

}
