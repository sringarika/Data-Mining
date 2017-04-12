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
    public static String filePath = "";
    private static List<Selection> selectionList;
    private static List<Selection> selectionTrainData;
    private static List<Selection> selectionTestData;
    private static final int TIMES = 5;

    public static void loadData() {
        try {
            selectionList = new ArrayList<>();
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine().trim()) != null) {
                if (line.length() > 0) {
                    if (line.indexOf("@") == -1) {
                        String[] parts = line.split(",");
                        if (parts.length == 7) {
                            Selection s = new Selection();
                            s.setType(parts[0]);
                            s.setLifeStyle(parts[1]);
                            s.setVacation(Double.parseDouble(parts[2]));
                            s.seteCredit(Double.parseDouble(parts[3]));
                            s.setSalary(Double.parseDouble(parts[4]));
                            s.setProperty(Double.parseDouble(parts[5]));
                            s.setLabel(parts[6]);

                            selectionList.add(s);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void formTrainTestData() {
        selectionTestData = new ArrayList<>();
        selectionTrainData = new ArrayList<>();
        Set<Integer> testIndex = new HashSet<>();
        int testSize = selectionList.size() / TIMES;
        int length = selectionList.size();
        while (testIndex.size() < testSize) {
            int index = (int) (Math.random() * length);
            if (!testIndex.contains(index)) {
                testIndex.add(index);
                selectionTestData.add(selectionList.get(index));
            }
        }

        for (int i = 0; i < length; i++) {
            if (!testIndex.contains(i)) {
                selectionTrainData.add(selectionList.get(i));
            }
        }
    }

    public static void main(String[] args) {
        filePath = args[0];
        loadData();
        DecisionTree dt = new DecisionTree(110);
        TreeNode root = new TreeNode();
        dt.constructDecisionTree(selectionList, 0, root, new HashSet<>());
        double sum = 0.0;
        for (int i = 0; i < TIMES; i++) {
            formTrainTestData();
            int correct = 0, wrong = 0, total = 0;
            for (Selection s : selectionTestData) {
                total++;
                String label = dt.getClassification(root, s);
                if (label.equals(s.getLabel())) {
                    correct++;
                } else {
                    wrong++;
                }
            }
            double correctRatio = ((double) correct) / total;
            sum += correctRatio * correctRatio;
        }
        sum /= TIMES;
        sum = Math.sqrt(sum);
        System.out.println("Accuracy: " + sum);
    }
}
