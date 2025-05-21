package unam.fi.ai.orangewormrevamped.appobjects.decisiontree;

import java.util.ArrayList;
import java.util.List;

public class Trainer {
    public void invokeTrainer(List<RouteInstance> trainingData, DecisionTree tree) {
        // Train the decision tree
        tree.train(trainingData);

        // Sample query to test prediction
        RouteInstance query = new RouteInstance(8, "weekday", "RouteA", 4.5, null);
        String predicted = tree.predict(query);
        System.out.println("Recommended route: " + predicted);
    }

}
