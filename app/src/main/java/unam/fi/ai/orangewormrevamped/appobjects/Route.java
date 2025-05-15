package unam.fi.ai.orangewormrevamped.appobjects;
import java.util.ArrayList;

public class Route {
    private ArrayList<Integer> station_list;
    private String name;
    private int number_of_stations;
    private int current_time;

    public Route(String name, ArrayList<Integer> station_list){
        this.name = name;
        this.station_list = station_list;
        this.number_of_stations = station_list.size();
    }

}
