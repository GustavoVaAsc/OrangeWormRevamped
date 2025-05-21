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
    public int h_function(double x1, double x2, double y1, double y2){
        double latitude_diff = Math.toRadians(y2-y1);
        double longitude_diff = Math.toRadians(x2-x1);

        // Haversine(a) = sin^2(a/2)

        double a = haversine(latitude_diff) + Math.cos(Math.toRadians(y1)) * Math.cos(Math.toRadians(y2)) * haversine(longitude_diff);
        double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));

        double result = ((EARTH_RADIUS*c)/AVG_SPEED)/60;
        return (int) result;
    }
}
