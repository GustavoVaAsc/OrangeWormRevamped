package unam.fi.ai.orangewormrevamped.appobjects;
import android.graphics.LinearGradient;

import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;

import unam.fi.ai.orangewormrevamped.appobjects.heuristics.Heuristic;

public class Graph {
    private HashMap<Integer, ArrayList<Pair>> adjacency_list;
    private HashSet<Integer> vertices; // Vertices set
    private boolean directed; // Flag for directed graph
    private boolean weighted; // Flag for weighted graph
    private ArrayList<Integer> distances;
    private ArrayList<Integer> predecessors;
    public Graph(boolean directed, boolean weighted){
        this.adjacency_list = new HashMap<>();
        this.directed = directed;
        this.weighted = weighted;
        this.vertices = new HashSet<>();
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
    public void optimalPath(int source, Heuristic heuristic){
        this.distances = new ArrayList<Integer>(vertices.size()+1);
        this.predecessors = new ArrayList<Integer>(vertices.size()+1);

        Collections.addAll(this.distances, Integer.MAX_VALUE); // MAX_VALUE is INF
        Collections.addAll(this.predecessors, -1);

        this.distances.set(source, 0);
        this.predecessors.set(source, 0);
        PriorityQueue<Pair> open_set = new PriorityQueue<Pair>();

        Pair first_element = new Pair(source, 0);
        open_set.add(first_element);

        while(!open_set.isEmpty()){
            int u = open_set.peek().getFirst();
            int d = open_set.peek().getSecond();
            open_set.poll();

            if(d > this.distances.indexOf(u)) continue;

            for(Pair e : this.adjacency_list.get(u)){
                int v = e.getFirst();
                int w = e.getSecond();

                if (this.distances.indexOf(u) + w < this.distances.indexOf(v)){
                    this.distances.set(v, this.distances.indexOf(u) + w);
                    this.predecessors.set(v,u);
                    Pair new_pair = new Pair(v, this.distances.indexOf(v));
                    open_set.add(new_pair);
                }
            }
        }

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
                System.out.println("Now visiting: "+v);
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


    public void resetPredeccesors(){
        this.predecessors.clear();
    }

    public HashMap<Integer,ArrayList<Pair>> getAdjacency_list(){return this.adjacency_list;}
}
