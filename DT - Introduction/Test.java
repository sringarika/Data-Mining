import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Flynn on 04/04/2017.
 */
public class Test {
    // public static String rootPath = "/Users/Flynn/Desktop/eBusiness/Task 11/";
    public static String filePath;
    private static List<Introduction> introductionList;
    private static List<Introduction> trainData;
    private static List<Introduction> testData;
    private static final int TIMES = 5;

    public static void loadData() {
        try {
            introductionList = new ArrayList<>();
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine().trim()) != null) {
                if (line.length() > 0) {
                    if (line.indexOf("@") == -1) {
                        String[] parts = line.split(",");
                        if (parts.length == 9) {
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
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void formTrainTestData() {
        testData = new ArrayList<>();
        trainData = new ArrayList<>();
        Set<Integer> testIndex = new HashSet<>();
        int testSize = introductionList.size() / TIMES;
        int length = introductionList.size();
        while (testIndex.size() < testSize) {
            int index = (int) (Math.random() * length);
            if (!testIndex.contains(index)) {
                testIndex.add(index);
                testData.add(introductionList.get(index));
            }
        }

        for (int i = 0; i < length; i++) {
            if (!testIndex.contains(i)) {
                trainData.add(introductionList.get(i));
            }
        }
    }

    public static void main(String[] args) {
        filePath = args[0];
        loadData();
        double sum = 0.0;
        for (int i = 0; i < TIMES; i++) {
            formTrainTestData();
            DecisionTree dt = new DecisionTree(50);
            TreeNode root = new TreeNode();
            dt.constructDecisionTree(introductionList, 0, root, new HashSet<>());
            int correct = 0, wrong = 0, total = 0;

            for (Introduction s : testData) {
                total++;
                String label = dt.getClassification(root, s);
                if (label.equals(s.getLabel())) {
                    correct++;
                } else {
                    wrong++;
                }
            }
            System.out.println("Correct:" + correct + ", Wrong: " + wrong + ", Total:" + total);
            double correctRatio = ((double) correct) / total;
            System.out.println(correctRatio);
            sum += correctRatio * correctRatio;
        }
        sum /= TIMES;
        sum = Math.sqrt(sum);
        System.out.println("Accuracy: " + sum);
    }
}
