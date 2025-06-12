package unam.fi.ai.orangewormrevamped.appobjects.heuristics;
import java.lang.Math;

// h_function uses Haversine distance

// V = D/t -> t = D/V
public class HaversineHeuristic implements Heuristic{
    private static final double EARTH_RADIUS = 6371.0; // km
    private static final double AVG_SPEED = 60.5; // km/h

    public double haversine(double a){
        return Math.sin(a/2) * Math.sin(a/2);
    }
    public int h_function(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = haversine(dLat)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * haversine(dLon);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c; // in km

        double timeInHours = distance / AVG_SPEED;
        double timeInMinutes = timeInHours * 60;

        return (int) timeInMinutes;
    }

}
