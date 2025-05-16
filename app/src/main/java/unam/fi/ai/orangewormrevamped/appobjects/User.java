package unam.fi.ai.orangewormrevamped.appobjects;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
public class User {
    private String username;
    private String password;
    HashMap<Integer,Station> station_db; // K: station_id, V: Station object
    ArrayList<Route> saved_routes;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public void loadGraph(){
        // Load line 1
        // Load line 2
        // Load line 3
        // Load line 4
        // Load line 5
        // Load line 6
        // Load line 7
        // Load line 8
        // Load line 9
        // Load line 10 (A)
        // Load line 11 (B)
        // Load line 12
    }

    public void addNewRoute(Route to_add){
        this.saved_routes.add(to_add);
    }
}
