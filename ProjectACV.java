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
        int folds =5;
        List<double[]> allData = GetNormalizedTrainData();
        allData = GetRandomizedOrder(allData);

        for (int i = 0; i < folds; i++) {
            List<double[]> dtrain = GetCVTrainData(allData,folds,i);
//            for (int b = 0; b <  dtrain.size(); b++ ) {
//                System.out.println(dtrain.get(b)[0]);
//            }
            List<double[]> dtest = GetCVTestData(allData,folds,i);
            double[] result = ComputeKnn(dtrain, dtest);
            for (int b = 0; b < result.length; b++ ) {
                System.out.println(result[b]);
            }


            int accurateNumber = 0;
            for (int a = 0; a < result.length; a++) {
                if (result[a] == dtest.get(a)[6]) {
                    System.out.println("Train prediction " + result[a] + " matches test data type " + + dtest.get(a)[0] + " with result " + dtest.get(a)[6]);
                    accurateNumber++;
                } else {
                    System.out.println("Train prediction " + result[a] + " doesn't match test data type " + + dtest.get(a)[0] + " with result " + dtest.get(a)[6]);
                }
            }

            accuracy = accurateNumber / result.length;
            System.out.println("Train prediction accuracy is " + accuracy);
        }
    }


    public static List<double[]> GetNormalizedTrainData () {
        String csvFile = "/Users/yiwang/Documents/YiWang/Ebiz/Task 11/task11a/attachments/trainProdSelection.csv";
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

        return newList;
    }

    public static List<double[]> GetCVTestData (List<double[]> allData, int folds, int order) {
        List<double[]> dtest = new ArrayList<>();
        int total = allData.size();
        int startIndex = total / folds * (order);
        int endIndex = startIndex + total / folds;
        System.out.println("total " + total);
        System.out.println("order " + order);
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
        System.out.println("total " + total);
        System.out.println("order " + order);
        System.out.println("start index " + startIndex);
        System.out.println("end index " + endIndex);
        dstart = allData.subList(0, startIndex);
        dend = allData.subList(startIndex, endIndex);

        dtrain.addAll(dstart);
        dtrain.addAll(dend);
        System.out.println("train data size " + dtrain.size());
        return  dtrain;
    }




    public static double[] ComputeKnn(List<double[]> dtrain, List<double[]> dtest)  {
        double[] result = new double[dtest.size()];

        Map<Double,Double> map1 = new HashMap<>();

        Double[] d  = new Double[dtrain.size()];
        for (int i = 0; i < dtest.size(); i++) {
            for (int j = 0;  j < dtrain.size(); j++) {
                d[j] = ComputeEDistance(dtrain.get(j), dtest.get(i));
                map1.put(d[j], dtrain.get(j)[dtrain.get(j).length-1]);
            }

            Double[] sortedDistance = d.clone();
            Arrays.sort(sortedDistance, Collections.reverseOrder());
            System.arraycopy(sortedDistance, 0, sortedDistance, 0, 3);

            double c1 = map1.get(sortedDistance[0]);
            double c2 = map1.get(sortedDistance[1]);
            double c3 = map1.get(sortedDistance[2]);


            if(c1 == c2 && c1 != c3) {
                if((sortedDistance[0] + sortedDistance[1]) > sortedDistance[2]) {
                    result[i] =  c1;
                    System.out.println("go here 01");
                }
                else {
                    result[i] =  c3;
                    System.out.println("go here 02");
                }
            }

            if(c2 == c3 && c2 != c1) {
                if((sortedDistance[1] + sortedDistance[2]) > sortedDistance[0]) {
                    result[i] =  c2;
                    System.out.println("go here 03");
                }
                else {
                    result[i] =  c1;
                    System.out.println("go here 04");
                }
            }

            if(c1 == c3 && c1 != c2) {
                if((sortedDistance[0] + sortedDistance[2]) > sortedDistance[1]) {
                    result[i] =  c1;
                    System.out.println("go here 05");
                }
                else {
                    result[i] =  c2;
                    System.out.println("go here 06");
                }
            }
            else {
                result[i] = c1;
            }

        }

        System.out.println("KNN Calculated");
        return result;


    }

    public static double ComputeEDistance(double[] train, double[] test) {

        double d = 0;
        double similarity;

        for(int i = 0; i < test.length; i++) {


            if (i >= 2) {

                d+= Math.pow((train[i] - test[i]),2);

            }else {
                if (test[i] == train[i]) {
                    similarity = 1;

                }else {

                    similarity = 0;
                }

                d+= 1 - similarity;
            }
        }

        d = Math.sqrt(d);
        d = 1.0/d;

        return d;
    }
}
