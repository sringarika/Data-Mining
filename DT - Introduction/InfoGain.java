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
        public String label;

        public NumericPair(double value, String label) {
            this.label = label;
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

    // Calculate sample entropy
    public double getSampleEntropy(List<Introduction> introductionList) {
        double sum = 0.0;
        Map<String, Integer> map = new HashMap<>();

        for (Introduction i : introductionList) {
            if (!map.containsKey(i.getLabel())) {
                map.put(i.getLabel(), 0);
            }
            map.put(i.getLabel(), map.get(i.getLabel()) + 1);
        }

        double total = (double) introductionList.size();

        for (String key : map.keySet()) {
            double p = map.get(key) / total;
            sum -= ((p) * (Math.log(p) / Math.log(2)));
        }

        return sum;
    }

    // Form big map
    private void formBigMap(String bigMapKey, Introduction i, Map<String, Map<String, Integer>> bigMap) {
        if (!bigMap.containsKey(bigMapKey)) {
            bigMap.put(bigMapKey, new HashMap<>());
        }
        Map<String, Integer> smallMap = bigMap.get(bigMapKey);
        String label = i.getLabel();
        if (!smallMap.containsKey(label)) {
            smallMap.put(label, 0);
        }
        smallMap.put(label, smallMap.get(label) + 1);
    }

    // Calculate entropy when attribute is discrete
    public double getDiscreteEntropy(int attribute, List<Introduction> introductionList) {
        double sum = 0.0;
        Map<String, Map<String, Integer>> bigMap = new HashMap<>();

        switch(attribute) {
            case 1:
                for (Introduction i : introductionList) {
                    formBigMap(i.getServiceType(), i, bigMap);
                }
                break;
            case 2:
                for (Introduction i : introductionList) {
                    formBigMap(i.getCustomer(), i, bigMap);
                }
                break;
            case 3:
                for (Introduction i : introductionList) {
                    formBigMap(i.getSize(), i, bigMap);
                }
                break;
            case 4:
                for (Introduction i : introductionList) {
                    formBigMap(i.getPromotion(), i, bigMap);
                }
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
    public NumericResult getNumericEntropy(int attribute, List<Introduction> introductionList) {
        double sum;
        double max = (double) Integer.MIN_VALUE;
        double splitPoint = (double) Integer.MIN_VALUE;
        String splitLabel = new String();

        List<NumericPair> pairList = new ArrayList<>();
        switch(attribute) {
            case 5:
                for (Introduction i : introductionList) {
                    pairList.add((new NumericPair(i.getMonthlyFee(), i.getLabel())));
                }
                break;
            case 6:
                for (Introduction i : introductionList) {
                    pairList.add((new NumericPair(i.getAdvertisementBudget(), i.getLabel())));
                }
                break;
            case 7:
                for (Introduction i : introductionList) {
                    pairList.add((new NumericPair(i.getInterestRate(), i.getLabel())));
                }
                break;
            case 8:
                for (Introduction i : introductionList) {
                    pairList.add((new NumericPair(i.getPeriod(), i.getLabel())));
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
            if (!smallMap.containsKey(pair.label)) {
                smallMap.put(pair.label, 0);
            }
            smallMap.put(pair.label, smallMap.get(pair.label) + 1);
        }
        Map<String, Integer> rightMap = new HashMap<>();
        for (Introduction i : introductionList) {
            if (!rightMap.containsKey(i.getLabel())) {
                rightMap.put(i.getLabel(), 0);
            }
            rightMap.put(i.getLabel(), rightMap.get(i.getLabel()) + 1);
        }
        Map<String, Integer> leftMap = new HashMap<>();
        for (String key : rightMap.keySet()) {
            leftMap.put(key, 0);
        }

        int index = 0;
        double leftTotal = 0.0, rightTotal = introductionList.size();
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
                    leftSum *= ((double) (leftTotal) / introductionList.size());

                    double rightSum = 0.0;
                    for (String key : rightMap.keySet()) {
                        if (rightMap.get(key) > 0) {
                            double p = ((double) rightMap.get(key) / (rightTotal));
                            rightSum -= (p * (Math.log(p) / Math.log(2)));
                        }
                    }
                    rightSum *= ((double) (rightTotal) / introductionList.size());

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
    private double getSplitInfo(int attribute, List<Introduction> introductionList) {
        double sum = 0.0;
        Map<String, Integer> map = new HashMap<>();
        switch (attribute) {
            case 1:
                for(Introduction i : introductionList) {
                    if (!map.containsKey(i.getServiceType())) {
                        map.put(i.getServiceType(), 0);
                    }
                    map.put(i.getServiceType(), map.get(i.getServiceType()) + 1);
                }
                break;
            case 2:
                for(Introduction s : introductionList) {
                    if (!map.containsKey(s.getCustomer())) {
                        map.put(s.getCustomer(), 0);
                    }
                    map.put(s.getCustomer(), map.get(s.getCustomer()) + 1);
                }
                break;
            case 3:
                for(Introduction s : introductionList) {
                    if (!map.containsKey(s.getSize())) {
                        map.put(s.getSize(), 0);
                    }
                    map.put(s.getSize(), map.get(s.getSize()) + 1);
                }
                break;
            case 4:
                for(Introduction s : introductionList) {
                    if (!map.containsKey(s.getPromotion())) {
                        map.put(s.getPromotion(), 0);
                    }
                    map.put(s.getPromotion(), map.get(s.getPromotion()) + 1);
                }
                break;
            case 5:
                for (Introduction s : introductionList) {
                    String key = String.valueOf(s.getMonthlyFee());
                    if (!map.containsKey(key)) {
                        map.put(key, 0);
                    }
                    map.put(key, map.get(key) + 1);
                }
                break;
            case 6:
                for (Introduction s : introductionList) {
                    String key = String.valueOf(s.getAdvertisementBudget());
                    if (!map.containsKey(key)) {
                        map.put(key, 0);
                    }
                    map.put(key, map.get(key) + 1);
                }
                break;
            case 7:
                for (Introduction s : introductionList) {
                    String key = String.valueOf(s.getInterestRate());
                    if (!map.containsKey(key)) {
                        map.put(key, 0);
                    }
                    map.put(key, map.get(key) + 1);
                }
                break;
            case 8:
                for (Introduction s : introductionList) {
                    String key = String.valueOf(s.getPeriod());
                    if (!map.containsKey(key)) {
                        map.put(key, 0);
                    }
                    map.put(key, map.get(key) + 1);
                }
                break;
        }

        double total = (double) introductionList.size();
        for (String key : map.keySet()) {
            double p = map.get(key) / total;
            sum -= (p * (Math.log(p) / Math.log(2)));
        }

        return sum;
    }

    // Calculate gain ratio
    public double getGainRatio(int attribute, double ES, double EA, List<Introduction> introductionList) {
        double splitInfo = getSplitInfo(attribute, introductionList);
        double infoGain = ES - EA;
        return infoGain / splitInfo;
    }
}
