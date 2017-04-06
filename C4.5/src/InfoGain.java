import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Flynn on 04/04/2017.
 */
public class InfoGain {
    public class EntropyResult {
        public String discreteSplit;
        public double continuousSplit;
        public double entropy;

        public EntropyResult(String discreteSplit, double entropy) {
            this.discreteSplit = discreteSplit;
            this.entropy = entropy;
        }

        public EntropyResult(double continuousSplit, double entropy) {
            this.continuousSplit = continuousSplit;
            this.entropy = entropy;
        }
    }

    public class NumericPair {
        public double value;
        public String lable;

        public NumericPair(double value, String lable) {
            this.lable = lable;
            this.value = value;
        }
    }

    public class NumericResult {
        public double spiltPoint;
        public double EA;

        public NumericResult (double spiltPoint, double EA) {
            this.spiltPoint = spiltPoint;
            this.EA = EA;
        }
    }

    private List<Selection> selectionList;
    public double numericSplit;

    public InfoGain(List<Selection> selectionList) {
        this.selectionList = selectionList;
    }

    public double getSampleEntropy() {
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

    public double getDiscreteEntropy(int attribute) {
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

//    public void formTreeMap(double key, Selection s, Map<Double, Map<String, Integer>> treeMap) {
//        if (!treeMap.containsKey(key)) {
//            treeMap.put(key, new HashMap<>());
//        }
//        Map<String, Integer> smallMap = treeMap.get(key);
//        if (!smallMap.containsKey(s.getLabel())) {
//            smallMap.put(s.getLabel(), 0);
//        }
//        smallMap.put(s.getLabel(), smallMap.get(s.getLabel()) + 1);
//    }

    public NumericResult getNumericEntropy(int attribute) {
        double sum = 0.0;
        double max = (double) Integer.MIN_VALUE;
        double splitPoit = 0.0;

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

        for (int i = 0; i < pairList.size() - 1; i++) {
            NumericPair pair = pairList.get(i);
            leftMap.put(pair.lable, leftMap.get(pair.lable) + 1);
            rightMap.put(pair.lable, rightMap.get(pair.lable) - 1);

            double leftSum = 0.0;
            for (String key : leftMap.keySet()) {
                if (leftMap.get(key) > 0) {
                    double p = ((double)leftMap.get(key) / (i + 1));
                    leftSum -= (p * (Math.log(p) / Math.log(2)));
                }
            }
            leftSum *= ((i + 1.0) / selectionList.size());

            double rightSum = 0.0;
            for (String key : rightMap.keySet()) {
                if (rightMap.get(key) > 0) {
                    double p = ((double)leftMap.get(key) / (selectionList.size() - 1 - i));
                    rightSum -= (p * (Math.log(p) / Math.log(2)));
                }
            }
            rightSum *= ((selectionList.size() - 1.0 - i) / selectionList.size());

            sum = leftSum + rightSum;
            if (sum > max) {
                max = sum;
                splitPoit = pairList.get(i).value;
            }
        }

        return new NumericResult(splitPoit, max);
    }

    private double getSplitInfo(int attribute) {
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

    public double getGainRatio(int attribute, double ES, double EA) {
        double splitInfo = getSplitInfo(attribute);
        double infoGain = ES - EA;
        return infoGain / splitInfo;
    }
}
