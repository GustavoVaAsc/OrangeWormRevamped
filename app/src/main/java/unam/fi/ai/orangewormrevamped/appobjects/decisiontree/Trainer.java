package unam.fi.ai.orangewormrevamped.appobjects.decisiontree;

import java.util.ArrayList;
import java.util.List;

public class Trainer {
    public void invokeTrainer(){
        List<RouteInstance> trainingData = new ArrayList<>();
        trainingData.add(new RouteInstance(8, "weekday", "RouteA", 5.0, "RouteA"));
        trainingData.add(new RouteInstance(9, "weekday", "RouteB", 8.2, "RouteB"));
        // ... add more training data

        DecisionTree tree = new DecisionTree();
        tree.train(trainingData);

        RouteInstance query = new RouteInstance(8, "weekday", "RouteA", 4.5, null);
        String predicted = tree.predict(query);
        System.out.println("Recommended route: " + predicted);

    }
}
