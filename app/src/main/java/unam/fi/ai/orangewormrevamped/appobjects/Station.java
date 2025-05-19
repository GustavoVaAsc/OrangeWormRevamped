package unam.fi.ai.orangewormrevamped.appobjects;
import java.util.HashSet;

public class Station {
    private int id;
    private String name;
    // TODO: Add logo image
    private String logo_file;  // resource ID of the drawable
    private HashSet<String> lines;
    private double latitude;
    private double longitude;
    // String logo_file (add this)
    public Station(int id, String name, HashSet<String> lines, double latitude, double longitude){
        this.id = id;
        this.name = name;
        this.logo_file = logo_file;
        this.lines = lines;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName(){return this.name;}
    public int getId(){return this.id;}
    public HashSet<String> getLines(){return this.lines;}
    public double getLatitude(){return this.latitude;}
    public double getLongitude(){return this.longitude;}
}
