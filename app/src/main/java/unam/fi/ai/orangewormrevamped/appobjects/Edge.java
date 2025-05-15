package unam.fi.ai.orangewormrevamped.appobjects;

public class Edge implements Comparable<Edge> {
    private int from;
    private int to;
    private int weight;

    public Edge(int from, int to, int weight){
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other_edge){
        return Integer.compare(this.weight, other_edge.weight);
    }

}
