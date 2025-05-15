package unam.fi.ai.orangewormrevamped.appobjects.heuristics;

public class ZeroHeuristic implements Heuristic {
    public int h_function(double x1, double x2, double y1, double y2){
        return 0;
    }
}
