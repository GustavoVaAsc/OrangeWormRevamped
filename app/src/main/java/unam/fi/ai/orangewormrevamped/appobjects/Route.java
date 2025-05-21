package unam.fi.ai.orangewormrevamped.appobjects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Route {
    private ArrayList<Integer> station_list;
    private String name;
    private int number_of_stations;
    private List<Integer> usage_hours; // Stores hours when this route was used

    public Route(String name, ArrayList<Integer> station_list){
        this.name = name;
        this.station_list = station_list;
        this.number_of_stations = station_list.size();
        this.usage_hours = new ArrayList<>();
    }

    public void useRoute() {
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        usage_hours.add(currentHour);
    }

    public List<Integer> getUsageHours() {
        return usage_hours;
    }

    public String getName() {
        return name;
    }

    public Integer queryStationList(int i){
        return this.station_list.get(i);
    }
    public Integer getNumberOfStations(){
        return this.number_of_stations;
    }

    public ArrayList<Integer> getStation_list() {
        return station_list;
    }
}