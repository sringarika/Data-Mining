import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by yiwang on 4/7/17.
 */
public class ProjectACV {

    public static void main(String[] args) {
        double accuracy;
        int folds = 6;
        List<double[]> allData = GetNormalizedTrainData();
        allData = GetRandomizedOrder(allData);
        List<Double> errors = new ArrayList<>();
        for (int i = 0; i < folds; i++) {
            List<double[]> dtrain = GetCVTrainData(allData,folds,i);
//            for (int b = 0; b <  dtrain.size(); b++ ) {
//                System.out.println(dtrain.get(b)[0]);
//            }
            List<double[]> dtest = GetCVTestData(allData,folds,i);
            double[] result = ComputeKnn(dtrain, dtest);
//            for (int b = 0; b < result.length; b++ ) {
//                System.out.println(result[b]);
//            }


           // int accurateNumber = 0;
            double error = 0;
            for (int a = 0; a < result.length; a++) {
                if (result[a] == dtest.get(a)[6]) {
                    System.out.println("Train prediction " + result[a] + " matches test data type " + + dtest.get(a)[6] + " with result " + dtest.get(a)[6]);
                  //  accurateNumber++;
                } else {
                    System.out.println("Train prediction " + result[a] + " doesn't match test data type " + + dtest.get(a)[6] + " with result " + dtest.get(a)[6]);
                    ++error;
                }
            }

            //accuracy = accurateNumber / result.length;
            errors.add(error);
           // System.out.println("Train prediction accuracy is " + accuracy);
             System.out.println("Train prediction Error for fold " + i + " " + error);
             

        }
        
        double sumError = 0;
        for (int i = 0; i < errors.size(); i++) {
            sumError = sumError + errors.get(i);
        }
        double averageErrorPercentage = sumError/allData.size() * 100;
        double averageAccuracyPercentage = 100 - averageErrorPercentage;
        System.out.println("Train prediction Average Error for 6 folds " + averageErrorPercentage);
        System.out.println("Train prediction Average Accuracy for 6 folds " + averageAccuracyPercentage);


    }


    public static List<double[]> GetNormalizedTrainData () {
        //String csvFile = "/Users/yiwang/Documents/YiWang/Ebiz/Task 11/task11a/attachments/trainProdSelection.csv";
        String csvFile = "trainProdSelection.csv";
        BufferedReader br = null;
        String line= "";
        String csvSplitBy = ",";
        int count = 0;
        String[] data;
        List<String[]> list = new ArrayList<String[]>();

        //set up similarity matrix for customer type.
        Map<String,Integer> customer_map =new HashMap<String,Integer>();
        customer_map.put("student", 0);
        customer_map.put("engineer", 1);
        customer_map.put("librarian", 2);
        customer_map.put("professor", 3);
        customer_map.put("doctor", 4);

        //set up similarity matrix for customer type.
        Map<String,Integer> lifestyle_map =new HashMap<String,Integer>();
        lifestyle_map.put("spend<<saving", 0);
        lifestyle_map.put("spend<saving", 1);
        lifestyle_map.put("spend>saving", 2);
        lifestyle_map.put("spend>>saving", 3);


        try {
            br = new BufferedReader (new FileReader (csvFile));
            while( ( line = br.readLine () ) != null ) {
                count++;
                data = line.split(csvSplitBy);
                //System.out.println ("line " + count + ": " + data[0] + " " + data[1] + ".");
                list.add(data);
                //System.out.println ("list " + count + ": " + list.size() + ".");
                //System.out.println ("list " + count + ": " + list.get(count-1)[0] + ".");
            }

        } catch  (IOException e) {
            e.printStackTrace();
        }

        //normalize vacation value
        // put vacation value into an array
        double[] vacationRaw = new double[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a double
            String v = list.get(i)[2];
            double newV = Double.parseDouble(v);
            vacationRaw[i] = newV;
        }
        //get the max and min of Vacation array.
        double maxVacation = getMaxValue(vacationRaw);
        double minVacation = getMinValue(vacationRaw);
        //get normalized value for Vacation as an array.
        double[] vacationNormalized = new double[count];
        for (int i = 0; i < count; i ++) {
            double norm = (vacationRaw[i] - minVacation) / (maxVacation - minVacation);
            vacationNormalized[i] = norm;
            //System.out.println("normalized vacation " + i + " " + vacationNormalized[i]);
        }
        System.out.println("Vacation value normalization completed");


        //normalize credit value
        // put credit value into an array
        double[] creditRaw = new double[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a double
            String c = list.get(i)[3];
            double newC = Double.parseDouble(c);
            creditRaw[i] = newC;
        }
        //get the max and min of Vacation array.
        double maxCredit = getMaxValue(creditRaw);
        double minCredit = getMinValue(creditRaw);
        //get normalized value for credit as an array.
        double[] creditNormalized = new double[count];
        for (int i = 0; i < count; i ++) {
            double norm = (creditRaw[i] - minCredit) / (maxCredit - minCredit);
            creditNormalized[i] = norm;
            //System.out.println("normalized credit " + i + " " + creditNormalized[i]);
        }
        System.out.println("Credit value normalization completed");

        //normalize salary value
        // put salary value into an array
        double[] salaryRaw = new double[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a double
            String s = list.get(i)[4];
            double newS = Double.parseDouble(s);
            salaryRaw[i] = newS;
        }
        //get the max and min of Salary array.
        double maxSalary = getMaxValue(salaryRaw);
        double minSalary = getMinValue(salaryRaw);
        //get normalized value for credit as an array.
        double[] salaryNormalized = new double[count];
        for (int i = 0; i < count; i ++) {
            double norm = (salaryRaw[i] - minSalary) / (maxSalary - minSalary);
            salaryNormalized[i] = norm;
            //System.out.println("normalized salary " + i + " " + salaryNormalized[i]);
        }
        System.out.println("Salary value normalization completed");

        //normalize property value
        // put property value into an array
        double[] propertyRaw = new double[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a double
            String p = list.get(i)[5];
            double newP = Double.parseDouble(p);
            propertyRaw[i] = newP;
        }
        //get the max and min of Property array.
        double maxProperty = getMaxValue(propertyRaw);
        double minProperty = getMinValue(propertyRaw);
        //get normalized value for credit as an array.
        double[] propertyNormalized = new double[count];
        for (int i = 0; i < count; i ++) {
            double norm = (propertyRaw[i] - minProperty) / (maxProperty - minProperty);
            propertyNormalized[i] = norm;
            //System.out.println("normalized property " + i + " " + propertyNormalized[i]);
        }
        System.out.println("Property value normalization completed");

        //normalize customer type -  symbolic data based on similarity matrix provided
        //normalize lifestyle -  symbolic data based on similarity matrix provided
        // the above two steps are skipped here since it is zero across the board.

        //parse classifier label
        double[] newLabel = new double[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a double
            String l = list.get(i)[6];
            double newL = Double.parseDouble(l.substring(1));
            //System.out.println(newL);
            newLabel[i] = newL;
        }

        //convert customer type.
        int[] customerConverted = new int[count];
        for (int i = 0; i < count; i++) {
            customerConverted[i] = customer_map.get(list.get(i)[0]);
            //System.out.println("convert customer " + i + " " + customerConverted[i]);
        }
        System.out.println("customer conversion completed");

        //convert  lifestyle type.
        int[] lifestyleConverted = new int[count];
        for (int i = 0; i < count; i++) {
            lifestyleConverted[i] = lifestyle_map.get(list.get(i)[1]);
            //System.out.println("convert lifestyle " + i + " " + lifestyleConverted[i]);
        }
        System.out.println("lifestyle conversion completed");

        //Generate new list for normalized data;
        List<double[]> newList = new ArrayList<double[]>();
        for (int i = 0 ; i < count; i++) {
            double[] newData = new double[7];
            newData[0] = customerConverted[i];
            newData[1] = lifestyleConverted[i];
            newData[2] = vacationNormalized[i];
            newData[3] = creditNormalized[i];
            newData[4] = salaryNormalized[i];
            newData[5] = propertyNormalized[i];
            newData[6] = newLabel[i];

            newList.add(newData);
            //System.out.println("new data bean created： " + newList.get(i)[6]);
        }
        System.out.println("Whole data normalization completed");
            return newList;
    }

    public static double getMaxValue(double[] array) {
        double max = array[0];
        for (int i = 1; i < array.length; i ++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        //System.out.println("max value of this array is " + max);
        return max;
    }

    public static double getMinValue(double[] array) {
        double min = array[0];
        for (int i = 1; i < array.length; i ++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        //System.out.println("min value of this array is " + min);
        return min;
    }


    public static List<double[]> GetRandomizedOrder(List<double[]> trainData){
        List<double[]> newList = new ArrayList<>();
        for (int i = 0; i < trainData.size(); i++) {
            newList.add(i, trainData.get(i));
        }
        Collections.shuffle(newList);
        System.out.println("Randomization completed");
        return newList;

    }

    public static List<double[]> GetCVTestData (List<double[]> allData, int folds, int order) {
        List<double[]> dtest = new ArrayList<>();
        int total = allData.size();
        System.out.println(total);
        int startIndex = total / folds * (order);
        int endIndex = startIndex + total / folds;
        //System.out.println("total " + total);
        //System.out.println("order " + order);
        System.out.println("start index " + startIndex);
        System.out.println("end index " + endIndex);
        dtest = allData.subList(startIndex, endIndex);
        System.out.println("test data size " + dtest.size());


        return  dtest;
    }

    public static List<double[]> GetCVTrainData (List<double[]> allData, int folds, int order) {
        List<double[]> dstart = new ArrayList<>();
        List<double[]> dend = new ArrayList<>();
        List<double[]> dtrain = new ArrayList<>();
        int total = allData.size();
        int startIndex = total / folds * (order);
        int endIndex = startIndex + total / folds;
        //System.out.println("total " + total);
        //System.out.println("order " + order);
        //System.out.println("start index " + startIndex);
        //System.out.println("end index " + endIndex);
        dstart = allData.subList(0, startIndex);
        dend = allData.subList(endIndex, allData.size());

        dtrain.addAll(dstart);
        //System.out.println("start size " + dstart.size());
        dtrain.addAll(dend);
        //System.out.print("end size " + dend.size());
        System.out.println("train data size " + dtrain.size());
        return  dtrain;
    }




    public static double[] ComputeKnn(List<double[]> dtrain, List<double[]> dtest)  {
        double[] result = new double[dtest.size()];

        Map<Double,Double> map1 = new HashMap<>();

        System.out.println("train data size: " + dtrain.size());
        System.out.println("test data size: "  + dtest.size());


        Double[] d  = new Double[dtrain.size()];
        for (int i = 0; i < dtest.size(); i++) {
            for (int j = 0;  j < dtrain.size(); j++) {
                d[j] = ComputeEDistance(dtrain.get(j), dtest.get(i));
//                System.out.print("e-distance: " + d[j]);
//                System.out.print(" index " + (dtrain.get(j).length-1));
//                System.out.println(" label: " + dtrain.get(j)[dtrain.get(j).length-1]);
                map1.put(d[j], dtrain.get(j)[dtrain.get(j).length-1]);
            }

            Double[] sortedDistance = d.clone();
            Arrays.sort(sortedDistance, Collections.reverseOrder());
            System.arraycopy(sortedDistance, 0, sortedDistance, 0, 3);

            double[] dsum = new double[5];

            Map<Integer,Double> labelList =new HashMap<Integer,Double>();

            for (int a = 0; a < 3; a++) {
                String label = Double.toString(map1.get(sortedDistance[a]));
                switch(label.charAt(0)) {
                    case '1': dsum[0] += sortedDistance[a];break;
                    case '2': dsum[1] += sortedDistance[a];break;
                    case '3': dsum[2] += sortedDistance[a];break;
                    case '4': dsum[3] += sortedDistance[a];break;
                    case '5': dsum[4] += sortedDistance[a];break;
                    default: System.out.println("no label");
                }
            }

            labelList.put(1, dsum[0]);
            labelList.put(2, dsum[1]);
            labelList.put(3, dsum[2]);
            labelList.put(4, dsum[3]);
            labelList.put(5, dsum[4]);


            Arrays.sort(dsum);
            result[i] = new Double(getKeyFromValue(labelList, dsum[4]).toString());




        }




        System.out.println("KNN Calculated");
        return result;


    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    public static double ComputeEDistance(double[] train, double[] test) {

        //set up similarity matrix for customer type.
        Map<String,Integer> customer_map =new HashMap<String,Integer>();
        customer_map.put("student", 0);
        customer_map.put("engineer", 1);
        customer_map.put("librarian", 2);
        customer_map.put("professor", 3);
        customer_map.put("doctor", 4);
        double[][] customerSim = new double[5][5];
        customerSim[0][0] = 1;
        customerSim[0][1] = 0;
        customerSim[0][2] = 0;
        customerSim[0][3] = 0;
        customerSim[0][4] = 0;
        customerSim[1][0] = 0;
        customerSim[1][1] = 1;
        customerSim[1][2] = 0;
        customerSim[1][3] = 0;
        customerSim[1][4] = 0;
        customerSim[2][0] = 0;
        customerSim[2][1] = 0;
        customerSim[2][2] = 1;
        customerSim[2][3] = 0;
        customerSim[2][4] = 0;
        customerSim[3][0] = 0;
        customerSim[3][1] = 0;
        customerSim[3][2] = 0;
        customerSim[3][3] = 1;
        customerSim[3][4] = 0;
        customerSim[4][0] = 0;
        customerSim[4][1] = 0;
        customerSim[4][2] = 0;
        customerSim[4][3] = 0;
        customerSim[4][4] = 1;

        //set up similarity matrix for customer type.
        Map<String,Integer> lifestyle_map =new HashMap<String,Integer>();
        lifestyle_map.put("spend<<saving", 0);
        lifestyle_map.put("spend<saving", 1);
        lifestyle_map.put("spend>saving", 2);
        lifestyle_map.put("spend>>saving", 3);
        double[][] lifestyleSim = new double[4][4];
        lifestyleSim[0][0] = 1;
        lifestyleSim[0][1] = 0;
        lifestyleSim[0][2] = 0;
        lifestyleSim[0][3] = 0;
        lifestyleSim[1][0] = 0;
        lifestyleSim[1][1] = 1;
        lifestyleSim[1][2] = 0;
        lifestyleSim[1][3] = 0;
        lifestyleSim[2][0] = 0;
        lifestyleSim[2][1] = 0;
        lifestyleSim[2][2] = 1;
        lifestyleSim[2][3] = 0;
        lifestyleSim[3][0] = 0;
        lifestyleSim[3][1] = 0;
        lifestyleSim[3][2] = 0;
        lifestyleSim[3][3] = 1;


        double distanceSum = 0;
        //calculate similarity for customer
        distanceSum = 1 - customerSim[(int)train[0]][(int)test[0]];
        //calculate similarity for lifestyle
        distanceSum += 1 - lifestyleSim[(int)train[1]][(int)test[1]];

        for (int i = 2; i < 6; i++ ) {
            distanceSum += Math.pow((train[i]-test[i]), 2);
        }
        distanceSum = Math.sqrt(distanceSum);
        double distance = 1 / distanceSum;



        return distance;
    }
}
