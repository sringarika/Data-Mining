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
    private List<Introduction> introductionList;
    private int maxDepth;
    private double ES;
    private String rootPath;
    private String introductionTrainDataPath;
    private Map<Integer, Integer> usageMap;
    private final int MAX_USAGE = 30;

    public class HelperPair {
        public String discrete;
        public double numeric;
        public Introduction introduction;
        public HelperPair(String discrete, double numeric, Introduction introduction) {
            this.discrete = discrete;
            this.numeric = numeric;
            this.introduction = introduction;
        }
    }

    public List<Introduction> getIntroductionList() {
        return introductionList;
    }

    public void setIntroductionList(List<Introduction> introductionList) {
        this.introductionList = introductionList;
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
        introductionList = new ArrayList<>();
        usageMap = new HashMap<>();
        rootPath = new String("/Users/Flynn/Desktop/eBusiness/Task 11/");
        introductionTrainDataPath = rootPath + "trainProdSelection.arff";
        for (int i = 5; i <= 8; i++) {
            usageMap.put(i, 0);
        }
    }

    public DecisionTree(int maxDepth) {
        this.maxDepth = maxDepth;
        usageMap = new HashMap<>();
        introductionList = new ArrayList<>();
        rootPath = new String("/Users/Flynn/Desktop/eBusiness/Task 11/");
        introductionTrainDataPath = rootPath + "trainProdSelection.arff";
        for (int i = 5; i <= 8; i++) {
            usageMap.put(i, 0);
        }
    }

    public void loadIntroductionTrainData() {
        try {
            FileInputStream fis = new FileInputStream(introductionTrainDataPath);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine().trim()) != null) {
                if (line.length() > 0) {
                    if (line.indexOf("@") == -1) {
                        String[] parts = line.split(",");
                        if (parts.length == 7) {
                            Introduction introduction = new Introduction();
                            introduction.setServiceType(parts[0]);
                            introduction.setCustomer(parts[1]);
                            introduction.setMonthlyFee(Double.parseDouble(parts[2]));
                            introduction.setAdvertisementBudget(Double.parseDouble(parts[3]));
                            introduction.setSize(parts[4]);
                            introduction.setPromotion(parts[5]);
                            introduction.setInterestRate(Double.parseDouble(parts[6]));
                            introduction.setPeriod(Double.parseDouble(parts[7]));
                            introduction.setLabel(parts[8]);

                            introductionList.add(introduction);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean shouldTerminate(int depth, List<Introduction> dataSet, Set<Integer> usedAttributes) {
        if (depth >= maxDepth) {
            return true;
        }
        int count = 0;
        for (Integer i : usageMap.keySet()) {
            if (usageMap.get(i) >= MAX_USAGE) {
                count++;
            }
        }
        if (count == 4 && usedAttributes.size() == 4) {
            return true;
        }
        Set<String> set = new HashSet<>();
        for (Introduction s : dataSet) {
            set.add(s.getLabel());
        }

        if (set.size() <= 1) {
            return true;
        }

        return false;
    }

    public void constructDecisionTree(List<Introduction> introductionList, int depth, TreeNode root,
                                      Set<Integer> usedAttributes) {
        if (shouldTerminate(depth, introductionList, usedAttributes)) {
            Map<String, Integer> labelMap = new HashMap<>();
            for (Introduction s : introductionList) {
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
        double ES = infoGain.getSampleEntropy(introductionList);
        double maxGain = (double) Integer.MIN_VALUE;
        int attribute = -1;
        double splitPoint = 0.0;
        String splitLabel = new String();

        for (int i = 1; i <= 4; i++) {
            if (!usedAttributes.contains(i)) {
                double EA = infoGain.getDiscreteEntropy(i, introductionList);
                // double gainRatio = ES - EA;
                double gainRatio = infoGain.getGainRatio(i, ES, EA, introductionList);

                if (gainRatio > maxGain) {
                    maxGain = gainRatio;
                    attribute = i;
                }
            }
        }

        for (int i = 5; i <= 8; i++) {
            if (usageMap.get(i) <= MAX_USAGE) {
                InfoGain.NumericResult result = infoGain.getNumericEntropy(i, introductionList);
                double EA = result.EA;
                // double gainRatio = ES - EA;
                double gainRatio = infoGain.getGainRatio(i, ES, EA, introductionList);

                if (gainRatio > maxGain) {
                    maxGain = gainRatio;
                    attribute = i;
                    splitPoint = result.spiltPoint;
                    splitLabel = result.splitLabel;
                }
            }
        }
        root.setAttribute(attribute);

        if (attribute <= 4) {
            usedAttributes.add(attribute);
            Map<String, List<Introduction>> valueMap = new HashMap<>();
            switch (attribute) {
                case 1:
                    for (Introduction s : introductionList) {
                        if (!valueMap.containsKey(s.getServiceType())) {
                            valueMap.put(s.getServiceType(), new ArrayList<>());
                        }
                        valueMap.get(s.getServiceType()).add(s);
                    }
                    break;
                case 2:
                    for (Introduction s : introductionList) {
                        if (!valueMap.containsKey(s.getCustomer())) {
                            valueMap.put(s.getCustomer(), new ArrayList<>());
                        }
                        valueMap.get(s.getCustomer()).add(s);
                    }
                    break;
                case 3:
                    for (Introduction s : introductionList) {
                        if (!valueMap.containsKey(s.getSize())) {
                            valueMap.put(s.getSize(), new ArrayList<>());
                        }
                        valueMap.get(s.getSize()).add(s);
                    }
                    break;
                case 4:
                    for (Introduction s : introductionList) {
                        if (!valueMap.containsKey(s.getPromotion())) {
                            valueMap.put(s.getPromotion(), new ArrayList<>());
                        }
                        valueMap.get(s.getPromotion()).add(s);
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
            usageMap.put(attribute, usageMap.get(attribute) + 1);
            TreeNode leftNode = new TreeNode();
            leftNode.setSplitPoint(splitPoint);
            TreeNode rightNode = new TreeNode();
            rightNode.setSplitPoint(splitPoint);
            List<Introduction> leftList = new ArrayList<>();
            List<Introduction> rightList = new ArrayList<>();
            List<HelperPair> helperPairs = new ArrayList<>();

            switch (attribute) {
                case 5:
                    for (Introduction s : introductionList) {
                        helperPairs.add(new HelperPair(null, s.getMonthlyFee(), s));
                    }
                    break;
                case 6:
                    for (Introduction s : introductionList) {
                        helperPairs.add(new HelperPair(null, s.getAdvertisementBudget(), s));
                    }
                    break;
                case 7:
                    for (Introduction s : introductionList) {
                        helperPairs.add(new HelperPair(null, s.getInterestRate(), s));
                    }
                    break;
                case 8:
                    for (Introduction s : introductionList) {
                        helperPairs.add(new HelperPair(null, s.getPeriod(), s));
                    }
                    break;
            }

            for (HelperPair pair : helperPairs) {
                if (pair.numeric < splitPoint) {
                    leftList.add(pair.introduction);
                } else if (pair.numeric == splitPoint) {
                    if (pair.introduction.getLabel().compareTo(splitLabel) <= 0) {
                        leftList.add(pair.introduction);
                    } else {
                        rightList.add(pair.introduction);
                    }
                } else {
                    rightList.add(pair.introduction);
                }
            }

            constructDecisionTree(leftList, depth + 1, leftNode, usedAttributes);
            constructDecisionTree(rightList, depth + 1, rightNode, usedAttributes);
            root.getChildren().add(leftNode);
            root.getChildren().add(rightNode);
        }
    }

    public String getClassification(TreeNode root, Introduction i) {
        if (root.getLabel() != null) {
            return root.getLabel();
        }

        List<TreeNode> children = root.getChildren();
        String discreteValue = null;
        double numericValue = 0.0;
        String ret = new String();

        switch (root.getAttribute()) {
            case 1:
                discreteValue = i.getServiceType();
                break;
            case 2:
                discreteValue = i.getCustomer();
                break;
            case 3:
                discreteValue = i.getSize();
                break;
            case 4:
                discreteValue = i.getPromotion();
                break;
            case 5:
                numericValue = i.getMonthlyFee();
                break;
            case 6:
                numericValue = i.getAdvertisementBudget();
                break;
            case 7:
                numericValue = i.getInterestRate();
                break;
            case 8:
                numericValue = i.getPeriod();
                break;
        }

        if (discreteValue == null) {
            if (children.size() > 0) {
                double judgement = children.get(0).getSplitPoint();
                if (numericValue <= judgement) {
                    ret = getClassification(children.get(0), i);
                } else {
                    ret = getClassification(children.get(1), i);
                }
            }
        } else {
            for (TreeNode child : children) {
                if (discreteValue.equals(child.getDiscreteValue())) {
                    ret = getClassification(child, i);
                }
            }
        }

        return ret;
    }
}
