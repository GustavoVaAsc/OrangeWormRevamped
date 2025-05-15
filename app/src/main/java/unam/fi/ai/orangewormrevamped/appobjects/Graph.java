package unam.fi.ai.orangewormrevamped.appobjects;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.HashSet;
public class Graph {
    private ArrayList<Edge> edges; // Edge List
    private HashSet<Integer> vertices; // Vertices set
    private boolean directed; // Flag for directed graph
    private boolean weighted; // Flag for weighted graph
    private ArrayList<Integer> distances;
    private ArrayList<Integer> predecessors;
    public Graph(boolean directed, boolean weighted){
        this.edges =  new ArrayList<Edge>();
        this.directed = directed;
        this.weighted = weighted;
    }

    public void addEdge(int from, int to, int weight){
        vertices.add(from);
        vertices.add(to);

        if (!this.weighted){
            weight = 0;
        }

        Edge to_add = new Edge(from,to,weight);
        edges.add(to_add);

        if(!this.directed){
            to_add = new Edge(to,from,weight);
            edges.add(to_add);
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

    public void optimalPath(){

    }

    // TO-DO: Implement BFS to get the path with the least stations
}
