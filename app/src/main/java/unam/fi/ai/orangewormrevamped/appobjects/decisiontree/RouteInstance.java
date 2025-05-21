package unam.fi.ai.orangewormrevamped.appobjects.decisiontree;

import java.util.List;

public class RouteInstance {
    public int hour;           // discretized hour (e.g., 0-23)
    public String dayType;     // e.g., "weekday", "weekend", "holiday"
    public String lastRoute;   // e.g., "RouteA", "RouteB"
    public double distance;    // numeric, will discretize in split
    public String routeName;   // target label to predict

    public RouteInstance(int hour, String dayType, String lastRoute, double distance, String routeName) {
        this.hour = hour;
        this.dayType = dayType;
        this.lastRoute = lastRoute;
        this.distance = distance;
        this.routeName = routeName;
    }
}

