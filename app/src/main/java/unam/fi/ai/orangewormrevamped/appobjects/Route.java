package unam.fi.ai.orangewormrevamped.appobjects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Route {
    private ArrayList<Integer> station_list;
    private String name;
    private int number_of_stations;
    private List<Integer> usage_hours;
    private List<Boolean> weekend_flags;


    public Route(String name, ArrayList<Integer> station_list, List<Integer> usage_hours, List<Boolean> weekend_flags){
        this.name = name;
        this.station_list = station_list;
        this.number_of_stations = station_list.size();
        this.usage_hours = usage_hours;
        this.weekend_flags = weekend_flags;
    }


    public void useRoute() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        boolean isWeekend = (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);

        usage_hours.add(currentHour);
        weekend_flags.add(isWeekend);
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

    public List<Boolean> getWeekendFlags() {
        return weekend_flags;
    }

}