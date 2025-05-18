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
    public Station(int id, String name, String logo_file, HashSet<String> lines, double latitude, double longitude){
        this.id = id;
        this.name = name;
        this.logo_file = logo_file;
        this.lines = lines;
        this.latitude = latitude;
        this.longitude = longitude;
    }


}
