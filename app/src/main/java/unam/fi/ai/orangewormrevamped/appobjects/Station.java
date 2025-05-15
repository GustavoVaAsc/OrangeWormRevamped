package unam.fi.ai.orangewormrevamped.appobjects;
import java.util.HashSet;
public class Station {
    private int id;
    private String name;
    // TODO: Add logo image
    private int logoResId;  // resource ID of the drawable
    private HashSet<Integer> lines;
    private double latitude;
    private double longitude;
    public Station(int id, String name, int logoResId, HashSet<Integer> lines, double latitude, double longitude){
        this.id = id;
        this.name = name;
        this.logoResId = logoResId;
        this.lines = lines;
        this.latitude = latitude;
        this.longitude = longitude;
    }


}
