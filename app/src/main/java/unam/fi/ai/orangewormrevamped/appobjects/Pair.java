package unam.fi.ai.orangewormrevamped.appobjects;

public class Pair implements Comparable<Pair> {
    private int first;
    private int second;

    public Pair(int x, int y){
        this.first = x;
        this.second = y;
    }

    public int getFirst(){return this.first;}
    public int getSecond(){return this.second;}
    public void setFirst(int x){this.first = x;}
    public void setSecond(int x){this.second = x;}

    @Override
    public int compareTo(Pair other_pair){
        return Integer.compare(this.second, other_pair.second);
    }
}
