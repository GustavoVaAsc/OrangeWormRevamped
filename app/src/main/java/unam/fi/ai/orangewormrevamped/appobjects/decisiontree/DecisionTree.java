package unam.fi.ai.orangewormrevamped.appobjects.decisiontree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import unam.fi.ai.orangewormrevamped.appobjects.Route;

public class DecisionTree {
    private DecisionTreeNode root;

    // Public method to train tree on dataset
    public void train(List<RouteInstance> data) {
        List<String> attributes = Arrays.asList("hour", "dayType", "lastRoute", "distance");
        this.root = buildTree(data, attributes);
    }

    // Predict route for a single instance
    public String predict(RouteInstance instance) {
        return classify(instance, root);
    }

    private String classify(RouteInstance instance, DecisionTreeNode node) {
        if (node.isLeaf) return node.predictedRoute;

        if (node.attribute.equals("distance")) {
            if (instance.distance <= node.threshold) {
                return classify(instance, node.children.get("leq"));
            } else {
                return classify(instance, node.children.get("gt"));
            }
        } else {
            String attrValue = getAttributeValue(instance, node.attribute);
            DecisionTreeNode child = node.children.get(attrValue);
            if (child == null) {
                // If unseen attribute value, fallback to majority class at node
                return majorityClass(node);
            }
            return classify(instance, child);
        }
    }

    private String majorityClass(DecisionTreeNode node) {
        // Simplified: in real tree keep distribution in nodes
        // Here just return any leaf below or null
        if (node.isLeaf) return node.predictedRoute;
        for (DecisionTreeNode child : node.children.values()) {
            String c = majorityClass(child);
            if (c != null) return c;
        }
        return null;
    }

    private DecisionTreeNode buildTree(List<RouteInstance> data, List<String> attributes) {
        DecisionTreeNode node = new DecisionTreeNode();

        // If all data same label, make leaf
        if (allSameLabel(data)) {
            node.isLeaf = true;
            node.predictedRoute = data.get(0).routeName;
            return node;
        }

        if (attributes.isEmpty()) {
            node.isLeaf = true;
            node.predictedRoute = majorityLabel(data);
            return node;
        }

        // Select best attribute and (if numeric) best threshold
        SplitResult split = selectBestAttribute(data, attributes);

        if (split.bestAttribute == null) {
            node.isLeaf = true;
            node.predictedRoute = majorityLabel(data);
            return node;
        }

        node.attribute = split.bestAttribute;

        if (split.bestAttribute.equals("distance")) {
            node.threshold = split.bestThreshold;
            // Split on numeric threshold
            List<RouteInstance> leftSplit = data.stream()
                    .filter(d -> d.distance <= split.bestThreshold)
                    .collect(Collectors.toList());
            List<RouteInstance> rightSplit = data.stream()
                    .filter(d -> d.distance > split.bestThreshold)
                    .collect(Collectors.toList());

            node.children.put("leq", buildTree(leftSplit, attributes));
            node.children.put("gt", buildTree(rightSplit, attributes));
        } else {
            // Categorical split
            Map<String, List<RouteInstance>> partitions = partitionByAttribute(data, split.bestAttribute);
            for (Map.Entry<String, List<RouteInstance>> entry : partitions.entrySet()) {
                node.children.put(entry.getKey(), buildTree(entry.getValue(), removeAttribute(attributes, split.bestAttribute)));
            }
        }

        return node;
    }

    private boolean allSameLabel(List<RouteInstance> data) {
        String first = data.get(0).routeName;
        for (RouteInstance d : data) {
            if (!d.routeName.equals(first)) return false;
        }
        return true;
    }

    private String majorityLabel(List<RouteInstance> data) {
        Map<String, Long> counts = data.stream()
                .collect(Collectors.groupingBy(d -> d.routeName, Collectors.counting()));
        return Collections.max(counts.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    private List<String> removeAttribute(List<String> attrs, String toRemove) {
        return attrs.stream().filter(a -> !a.equals(toRemove)).collect(Collectors.toList());
    }

    private String getAttributeValue(RouteInstance instance, String attribute) {
        switch (attribute) {
            case "hour": return String.valueOf(instance.hour);
            case "dayType": return instance.dayType;
            case "lastRoute": return instance.lastRoute;
            default: return null;
        }
    }

    private Map<String, List<RouteInstance>> partitionByAttribute(List<RouteInstance> data, String attribute) {
        Map<String, List<RouteInstance>> map = new HashMap<>();
        for (RouteInstance d : data) {
            String val = getAttributeValue(d, attribute);
            map.computeIfAbsent(val, k -> new ArrayList<>()).add(d);
        }
        return map;
    }

    // Encapsulate info about best attribute and threshold
    private static class SplitResult {
        String bestAttribute;
        double bestThreshold; // only for numeric
        double bestGain;

        SplitResult(String attr, double threshold, double gain) {
            this.bestAttribute = attr;
            this.bestThreshold = threshold;
            this.bestGain = gain;
        }
    }

    private SplitResult selectBestAttribute(List<RouteInstance> data, List<String> attributes) {
        double baseEntropy = entropy(data);
        String bestAttr = null;
        double bestGain = Double.NEGATIVE_INFINITY;
        double bestThreshold = 0;

        for (String attr : attributes) {
            if (attr.equals("distance")) {
                // Try multiple thresholds for numeric attribute
                List<Double> values = data.stream().map(d -> d.distance).distinct().sorted().collect(Collectors.toList());
                for (int i = 0; i < values.size() - 1; i++) {
                    double threshold = (values.get(i) + values.get(i + 1)) / 2;
                    double gain = infoGainNumeric(data, "distance", threshold, baseEntropy);
                    if (gain > bestGain) {
                        bestGain = gain;
                        bestAttr = attr;
                        bestThreshold = threshold;
                    }
                }
            } else {
                double gain = infoGainCategorical(data, attr, baseEntropy);
                if (gain > bestGain) {
                    bestGain = gain;
                    bestAttr = attr;
                    bestThreshold = 0;
                }
            }
        }
        return new SplitResult(bestAttr, bestThreshold, bestGain);
    }

    private double entropy(List<RouteInstance> data) {
        Map<String, Long> counts = data.stream()
                .collect(Collectors.groupingBy(d -> d.routeName, Collectors.counting()));

        double entropy = 0;
        int total = data.size();

        for (long count : counts.values()) {
            double p = (double) count / total;
            entropy -= p * (Math.log(p) / Math.log(2));
        }
        return entropy;
    }

    private double infoGainCategorical(List<RouteInstance> data, String attribute, double baseEntropy) {
        Map<String, List<RouteInstance>> partitions = partitionByAttribute(data, attribute);
        double weightedEntropy = 0;
        int total = data.size();

        for (List<RouteInstance> subset : partitions.values()) {
            weightedEntropy += ((double) subset.size() / total) * entropy(subset);
        }
        return baseEntropy - weightedEntropy;
    }

    private double infoGainNumeric(List<RouteInstance> data, String attribute, double threshold, double baseEntropy) {
        List<RouteInstance> leq = data.stream().filter(d -> d.distance <= threshold).collect(Collectors.toList());
        List<RouteInstance> gt = data.stream().filter(d -> d.distance > threshold).collect(Collectors.toList());

        double weightedEntropy = 0;
        int total = data.size();

        weightedEntropy += ((double) leq.size() / total) * entropy(leq);
        weightedEntropy += ((double) gt.size() / total) * entropy(gt);

        return baseEntropy - weightedEntropy;
    }

    public List<RouteInstance> convertRoutesToInstances(List<Route> routes) {
        List<RouteInstance> instances = new ArrayList<>();
        List<String> recentRoutes = new ArrayList<>();

        for (Route route : routes) {
            List<Integer> hours = route.getUsageHours();
            List<Boolean> weekendFlags = route.getWeekendFlags();

            for (int i = 0; i < hours.size(); i++) {
                int hour = hours.get(i);
                String dayType = weekendFlags.get(i) ? "weekend" : "weekday";
                String lastRoute = recentRoutes.isEmpty() ? "none" : recentRoutes.get(recentRoutes.size() - 1);
                int distance = route.getNumberOfStations();
                String routeName = route.getName();

                RouteInstance instance = new RouteInstance(hour, dayType, lastRoute, distance, routeName);
                instances.add(instance);

                recentRoutes.add(routeName);
                if (recentRoutes.size() > 5) {
                    recentRoutes.remove(0);
                }
            }
        }

        return instances;
    }

}