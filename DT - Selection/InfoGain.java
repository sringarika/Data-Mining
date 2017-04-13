import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Flynn on 04/04/2017.
 */
public class InfoGain {
    // Helper class
    public class NumericPair {
        public double value;
        public String lable;

        public NumericPair(double value, String lable) {
            this.lable = lable;
            this.value = value;
        }
    }

    // Helper class
    public class NumericResult {
        public double spiltPoint;
        public double EA;
        public String splitLabel;

        public NumericResult (double spiltPoint, double EA, String splitLabel) {
            this.spiltPoint = spiltPoint;
            this.EA = EA;
            this.splitLabel = splitLabel;
        }
    }

    public double numericSplit;

    // Calculate sample entropy
    public double getSampleEntropy(List<Selection> selectionList) {
        double sum = 0.0;
        Map<String, Integer> map = new HashMap<>();

        for (Selection s : selectionList) {
            if (!map.containsKey(s.getLabel())) {
                map.put(s.getLabel(), 0);
            }
            map.put(s.getLabel(), map.get(s.getLabel()) + 1);
        }

        double total = (double) selectionList.size();

        for (String key : map.keySet()) {
            double p = map.get(key) / total;
            sum -= ((p) * (Math.log(p) / Math.log(2)));
        }

        return sum;
    }

    // Form big map
    private void formBigMap(String bigMapKey, Selection s, Map<String, Map<String, Integer>> bigMap) {
        if (!bigMap.containsKey(bigMapKey)) {
            bigMap.put(bigMapKey, new HashMap<>());
        }
        Map<String, Integer> smallMap = bigMap.get(bigMapKey);
        String label = s.getLabel();
        if (!smallMap.containsKey(label)) {
            smallMap.put(label, 0);
        }
        smallMap.put(label, smallMap.get(label) + 1);
    }

    // Calculate entropy when attribute is discrete
    public double getDiscreteEntropy(int attribute, List<Selection> selectionList) {
        double sum = 0.0;
        Map<String, Map<String, Integer>> bigMap = new HashMap<>();

        switch(attribute) {
            case 1:
                for (Selection s : selectionList) {
                    formBigMap(s.getType(), s, bigMap);
                }
                break;
            case 2:
                for (Selection s : selectionList) {
                    formBigMap(s.getLifeStyle(), s, bigMap);
                }
                break;
        }

        for (String bigMapKey : bigMap.keySet()) {
            Map<String, Integer> smallMap = bigMap.get(bigMapKey);
            double subSum = 0.0, total = 0.0;
            for (String smallMapKey : smallMap.keySet()) {
                total += smallMap.get(smallMapKey);
            }

            for (String smallMapKey : smallMap.keySet()) {
                double p = smallMap.get(smallMapKey) / total;
                subSum -= (p * (Math.log(p) / Math.log(2)));
            }
            sum += subSum;
        }

        return sum;
    }

    // Calculate entropy when attribute is numeric
    public NumericResult getNumericEntropy(int attribute, List<Selection> selectionList) {
        // System.out.println(selectionList.size());
        double sum;
        double max = (double) Integer.MIN_VALUE;
        double splitPoint = (double) Integer.MIN_VALUE;
        String splitLabel = new String();

        List<NumericPair> pairList = new ArrayList<>();
        switch(attribute) {
            case 3:
                for (Selection s : selectionList) {
                    pairList.add((new NumericPair(s.getVacation(), s.getLabel())));
                }
                break;
            case 4:
                for (Selection s : selectionList) {
                    pairList.add((new NumericPair(s.geteCredit(), s.getLabel())));
                }
                break;
            case 5:
                for (Selection s : selectionList) {
                    pairList.add((new NumericPair(s.getSalary(), s.getLabel())));
                }
                break;
            case 6:
                for (Selection s : selectionList) {
                    pairList.add((new NumericPair(s.getProperty(), s.getLabel())));
                }
                break;
        }

        Collections.sort(pairList, new Comparator<NumericPair>() {
            @Override
            public int compare(NumericPair o1, NumericPair o2) {
                double ret = o1.value - o2.value;
                if (ret < 0) {
                    return -1;
                } else if (ret == 0) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        Map<Double, Map<String, Integer>> bigMap = new TreeMap<>();
        for (NumericPair pair : pairList) {
            if (!bigMap.containsKey(pair.value)) {
                bigMap.put(pair.value, new TreeMap<>());
            }
            Map<String, Integer> smallMap = bigMap.get(pair.value);
            if (!smallMap.containsKey(pair.lable)) {
                smallMap.put(pair.lable, 0);
            }
            smallMap.put(pair.lable, smallMap.get(pair.lable) + 1);
        }
        Map<String, Integer> rightMap = new HashMap<>();
        for (Selection s : selectionList) {
            if (!rightMap.containsKey(s.getLabel())) {
                rightMap.put(s.getLabel(), 0);
            }
            rightMap.put(s.getLabel(), rightMap.get(s.getLabel()) + 1);
        }
        Map<String, Integer> leftMap = new HashMap<>();
        for (String key : rightMap.keySet()) {
            leftMap.put(key, 0);
        }

        int index = 0;
        double leftTotal = 0.0, rightTotal = selectionList.size();
        for (Double d : bigMap.keySet()) {
            if (index++ < bigMap.size() - 1) {
                Map<String, Integer> smallMap = bigMap.get(d);
                for (String s : smallMap.keySet()) {
                    int count = smallMap.get(s);

                    leftMap.put(s, leftMap.get(s) + count);
                    leftTotal += count;
                    rightMap.put(s, rightMap.get(s) - count);
                    rightTotal -= count;

                    double leftSum = 0.0;
                    for (String key : leftMap.keySet()) {
                        if (leftMap.get(key) > 0) {
                            double p = ((double) leftMap.get(key) / (leftTotal));
                            leftSum -= (p * (Math.log(p) / Math.log(2)));
                        }
                    }
                    leftSum *= ((double) (leftTotal) / selectionList.size());

                    double rightSum = 0.0;
                    for (String key : rightMap.keySet()) {
                        if (rightMap.get(key) > 0) {
                            double p = ((double) rightMap.get(key) / (rightTotal));
                            rightSum -= (p * (Math.log(p) / Math.log(2)));
                        }
                    }
                    rightSum *= ((double) (rightTotal) / selectionList.size());

                    sum = leftSum + rightSum;
                    if (sum > max) {
                        max = sum;
                        splitPoint = d;
                        splitLabel = s;
                    }
                }
            }
        }

        return new NumericResult(splitPoint, max, splitLabel);
    }

    // Calculate split info
    private double getSplitInfo(int attribute, List<Selection> selectionList) {
        double sum = 0.0;
        Map<String, Integer> map = new HashMap<>();
        switch (attribute) {
            case 1:
                for(Selection s : selectionList) {
                    if (!map.containsKey(s.getType())) {
                        map.put(s.getType(), 0);
                    }
                    map.put(s.getType(), map.get(s.getType()) + 1);
                }
                break;
            case 2:
                for(Selection s : selectionList) {
                    if (!map.containsKey(s.getLifeStyle())) {
                        map.put(s.getLifeStyle(), 0);
                    }
                    map.put(s.getLifeStyle(), map.get(s.getLifeStyle()) + 1);
                }
                break;
            case 3:
                for(Selection s : selectionList) {
                    String key = String.valueOf(s.getVacation());
                    if (!map.containsKey(key)) {
                        map.put(key, 0);
                    }
                    map.put(key, map.get(key) + 1);
                }
                break;
            case 4:
                for (Selection s : selectionList) {
                    String key = String.valueOf(s.geteCredit());
                    if (!map.containsKey(key)) {
                        map.put(key, 0);
                    }
                    map.put(key, map.get(key) + 1);
                }
                break;
            case 5:
                for (Selection s : selectionList) {
                    String key = String.valueOf(s.getSalary());
                    if (!map.containsKey(key)) {
                        map.put(key, 0);
                    }
                    map.put(key, map.get(key) + 1);
                }
                break;
            case 6:
                for (Selection s : selectionList) {
                    String key = String.valueOf(s.getProperty());
                    if (!map.containsKey(key)) {
                        map.put(key, 0);
                    }
                    map.put(key, map.get(key) + 1);
                }
                break;
        }

        double total = (double) selectionList.size();
        for (String key : map.keySet()) {
            double p = map.get(key) / total;
            sum -= (p * (Math.log(p) / Math.log(2)));
        }

        return sum;
    }

    // Calculate gain ratio
    public double getGainRatio(int attribute, double ES, double EA, List<Selection> selectionList) {
        double splitInfo = getSplitInfo(attribute, selectionList);
        double infoGain = ES - EA;
        return infoGain / splitInfo;
    }
}
