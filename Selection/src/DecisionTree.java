import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Flynn on 04/04/2017.
 */
public class DecisionTree {
    private List<Selection> selectionList;
    private int maxDepth;
    private double ES;
    private String rootPath;
    private String selectionTrainDataPath;

    public class HelperPair {
        public String discrete;
        public double numeric;
        public Selection selection;
        public HelperPair(String discrete, double numeric, Selection selection) {
            this.discrete = discrete;
            this.numeric = numeric;
            this.selection = selection;
        }
    }

    public List<Selection> getSelectionList() {
        return selectionList;
    }

    public void setSelectionList(List<Selection> selectionList) {
        this.selectionList = selectionList;
    }

    public double getES() {
        return ES;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setES(double ES) {
        this.ES = ES;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public DecisionTree() {
        selectionList = new ArrayList<>();
        rootPath = new String("/Users/Flynn/Desktop/eBusiness/Task 11/");
        selectionTrainDataPath = rootPath + "trainProdSelection.arff";
    }

    public DecisionTree(int maxDepth) {
        this.maxDepth = maxDepth;
        selectionList = new ArrayList<>();
        rootPath = new String("/Users/Flynn/Desktop/eBusiness/Task 11/");
        selectionTrainDataPath = rootPath + "trainProdSelection.arff";
    }

    public void loadSelectionTrainData() {
        try {
            FileInputStream fis = new FileInputStream(selectionTrainDataPath);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine().trim()) != null) {
                if (line.length() > 0) {
                    if (line.indexOf("@") == -1) {
                        String[] parts = line.split(",");
                        if (parts.length == 7) {
                            Selection selection = new Selection();
                            selection.setType(parts[0]);
                            selection.setLifeStyle(parts[1]);
                            selection.setVacation(Double.parseDouble(parts[2]));
                            selection.seteCredit(Double.parseDouble(parts[3]));
                            selection.setSalary(Double.parseDouble(parts[4]));
                            selection.setProperty(Double.parseDouble(parts[5]));
                            selection.setLabel(parts[6]);

                            selectionList.add(selection);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean shouldTerminate(int depth, List<Selection> dataSet, Set<Integer> usedAttributes) {
        if (depth >= maxDepth) {
            return true;
        }
//        if (usedAttributes.size() == maxDepth) {
//            return true;
//        }

//        if (dataSet.size() <= 2) {
//            return true;
//        }
        Set<String> set = new HashSet<>();
        for (Selection s : dataSet) {
            set.add(s.getLabel());
        }

        if (set.size() <= 1) {
            return true;
        }

        return false;
    }

    public void constructDecisionTree(List<Selection> selectionList, int depth, TreeNode root,
                                      Set<Integer> usedAttributes) {
        if (shouldTerminate(depth, selectionList, usedAttributes)) {
            Map<String, Integer> labelMap = new HashMap<>();
            for (Selection s : selectionList) {
                if (!labelMap.containsKey(s.getLabel())) {
                    labelMap.put(s.getLabel(), 0);
                }
                labelMap.put(s.getLabel(), labelMap.get(s.getLabel()) + 1);
            }

            int max = Integer.MIN_VALUE;
            String classification = new String();
            for (String key : labelMap.keySet()) {
                if (labelMap.get(key) > max) {
                    max = labelMap.get(key);
                    classification = key;
                }
            }

            root.setLabel(classification);
            return;
        }

        InfoGain infoGain = new InfoGain();
        double ES = infoGain.getSampleEntropy(selectionList);
        double maxGain = (double) Integer.MIN_VALUE;
        int attribute = 0;
        double splitPoint = 0.0;
        String splitLabel = new String();

        for (int i = 1; i <= 2; i++) {
            if (!usedAttributes.contains(i)) {
                double EA = infoGain.getDiscreteEntropy(i, selectionList);
                double gainRatio = ES - EA;
                // double gainRatio = infoGain.getGainRatio(i, ES, EA, selectionList);

                if (gainRatio > maxGain) {
                    maxGain = gainRatio;
                    attribute = i;
                }
            }
        }

        for (int i = 3; i <= 6; i++) {
            // if (!usedAttributes.contains(i)) {
                InfoGain.NumericResult result = infoGain.getNumericEntropy(i, selectionList);
                double EA = result.EA;
                double gainRatio = ES - EA;
                // double gainRatio = infoGain.getGainRatio(i, ES, EA, selectionList);

                if (gainRatio > maxGain) {
                    maxGain = gainRatio;
                    attribute = i;
                    splitPoint = result.spiltPoint;
                    splitLabel = result.splitLabel;
                }
            // }
        }
        root.setAttribute(attribute);
        // System.out.println("point: " + splitPoint + " label: " + splitLabel);
        if (attribute <= 2) {
            usedAttributes.add(attribute);
        }

        if (attribute <= 2) {
            Map<String, List<Selection>> valueMap = new HashMap<>();
            switch (attribute) {
                case 1:
                    for (Selection s : selectionList) {
                        if (!valueMap.containsKey(s.getType())) {
                            valueMap.put(s.getType(), new ArrayList<>());
                        }
                        valueMap.get(s.getType()).add(s);
                    }
                    break;
                case 2:
                    for (Selection s : selectionList) {
                        if (!valueMap.containsKey(s.getLifeStyle())) {
                            valueMap.put(s.getLifeStyle(), new ArrayList<>());
                        }
                        valueMap.get(s.getLifeStyle()).add(s);
                    }
                    break;
            }

            for (String key : valueMap.keySet()) {
                TreeNode node = new TreeNode();
                node.setDiscreteValue(key);
                constructDecisionTree(valueMap.get(key), depth + 1, node, usedAttributes);
                root.getChildren().add(node);
            }
        } else {
            TreeNode leftNode = new TreeNode();
            leftNode.setSplitPoint(splitPoint);
            TreeNode rightNode = new TreeNode();
            rightNode.setSplitPoint(splitPoint);
            List<Selection> leftList = new ArrayList<>();
            List<Selection> rightList = new ArrayList<>();
            List<HelperPair> helperPairs = new ArrayList<>();

            switch (attribute) {
                case 3:
                    for (Selection s : selectionList) {
                        helperPairs.add(new HelperPair(null, s.getVacation(), s));
                    }
                    break;
                case 4:
                    for (Selection s : selectionList) {
                        helperPairs.add(new HelperPair(null, s.geteCredit(), s));
                    }
                    break;
                case 5:
                    for (Selection s : selectionList) {
                        helperPairs.add(new HelperPair(null, s.getSalary(), s));
                    }
                    break;
                case 6:
                    for (Selection s : selectionList) {
                        helperPairs.add(new HelperPair(null, s.getProperty(), s));
                    }
                    break;
            }

            for (HelperPair pair : helperPairs) {
                if (pair.numeric < splitPoint) {
                    leftList.add(pair.selection);
                } else if (pair.numeric == splitPoint) {
                    if (pair.selection.getLabel().compareTo(splitLabel) <= 0) {
                        leftList.add(pair.selection);
                    } else {
                        rightList.add(pair.selection);
                    }
                } else {
                    rightList.add(pair.selection);
                }
            }

            constructDecisionTree(leftList, depth + 1, leftNode, usedAttributes);
            constructDecisionTree(rightList, depth + 1, rightNode, usedAttributes);
            root.getChildren().add(leftNode);
            root.getChildren().add(rightNode);
        }
    }

    public String getClassification(TreeNode root, Selection s) {
        if (root.getLabel() != null) {
            return root.getLabel();
        }

        List<TreeNode> children = root.getChildren();
        String discreteValue = null;
        double numericValue = 0.0;
        String ret = new String();

        switch (root.getAttribute()) {
            case 1:
                discreteValue = s.getType();
                break;
            case 2:
                discreteValue = s.getLifeStyle();
                break;
            case 3:
                numericValue = s.getVacation();
                break;
            case 4:
                numericValue = s.geteCredit();
                break;
            case 5:
                numericValue = s.getSalary();
                break;
            case 6:
                numericValue = s.getProperty();
                break;
        }

        if (discreteValue == null) {
            double judgement = children.get(0).getSplitPoint();
            if (numericValue <= judgement) {
                ret = getClassification(children.get(0), s);
            } else {
                ret = getClassification(children.get(1), s);
            }
        } else {
            for (TreeNode child : children) {
                if (discreteValue.equals(child.getDiscreteValue())) {
                    ret = getClassification(child, s);
                }
            }
        }

        return ret;
    }
}
