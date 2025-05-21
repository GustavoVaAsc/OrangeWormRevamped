package unam.fi.ai.orangewormrevamped.appobjects.decisiontree;

import java.util.HashMap;
import java.util.Map;

public class DecisionTreeNode {
    String attribute; // attribute name to split on, null if leaf
    Map<String, DecisionTreeNode> children; // for categorical splits
    double threshold; // for numeric attribute split
    boolean isLeaf;
    String predictedRoute; // for leaf nodes

    public DecisionTreeNode() {
        this.children = new HashMap<>();
        this.isLeaf = false;
    }
}
