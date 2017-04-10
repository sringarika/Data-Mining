import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by yiwang on 4/8/17.
 */
public class ProjectAWeighted {

    public static void main(String[] args) {

        List<double[]> allData = GetNormalizedTrainData();
        allData = GetRandomizedOrder(allData);

        double[] DEFAULTWEIGHT = { 1, 1, 1, 1, 1, 1 };
        double[] updatedRate = {1,1,1,1,1,1};
        double initialRate = Prediction(allData, DEFAULTWEIGHT, 5);
        double temp = initialRate;
        double newRate = 0;
        System.out.println("initial rate : " + initialRate);

        for (int i = 1; i < 201; i ++) {
            updatedRate[0] = i/100;
            //System.out.println("weight 1 : " + (double)i/100);
                for (int a = 1; a < 201; a++) {
                    updatedRate[1] = a / 100;
                    for (int b = 1; b < 201; b++) {
                        updatedRate[2] = b / 100;
                            for (int c = 1; c < 201; c ++) {
                                updatedRate[3] = c / 100;
                                    for (int d = 1; d < 201; d++ ) {
                                        updatedRate[4] = d / 100;
                                        for (int e = 1; e < 201; e++ ) {
                                            updatedRate[5] = e / 100;
                                                newRate = Prediction(allData, updatedRate, 5);
                                                if (newRate > temp) {
                                                    temp = newRate;
                                                    System.out.println("rate: " + temp);
                                                }
                                        }
                                    }
                            }
                    }
                }
        }


    }


    public static double Prediction(List<double[]> allData, double[] w, int folds) {
        double[] accuracy = new double[folds];


        for (int i = 0; i < folds; i++) {
            List<double[]> dtrain = GetCVTrainData(allData,folds,i);

            List<double[]> dtest = GetCVTestData(allData,folds,i);
//            for (int b = 0; b <  dtest.size(); b++ ) {
//                System.out.println("test result: " + dtest.get(b)[6]);
//            }
            double[] result = ComputeKnn(dtest, dtrain, w);
//            for (int b = 0; b < result.length; b++ ) {
//                System.out.println(result[b]);
//            }

            //System.out.println("size of the result: " + result.length);
            int accurateNumber = 0;
            for (int a = 0; a < result.length; a++) {
                if (result[a] == dtest.get(a)[6]) {
                    //System.out.println("Train prediction " + result[a] + " matches test data type " + + dtest.get(a)[0] + " with result " + dtest.get(a)[6]);
                    accurateNumber++;
                    accuracy[i] = (double)accurateNumber / 37;
                    //System.out.println("Train prediction " + accurateNumber);
                } else {
                    //System.out.println("Train prediction " + result[a] + " doesn't match test data type " + + dtest.get(a)[0] + " with result " + dtest.get(a)[6]);
                }
            }

            //accuracy[i] = accurateNumber / result.length;
            //System.out.println("Train prediction accuracy is " + accuracy);
        }

        double weightedError =GetWeightedError(accuracy);

        return weightedError;
    }

    public static double GetWeightedError(double[] r) {
        double sum = 0;

        for (int i = 0; i < r.length; i++) {
            sum += Math.pow(r[i], 2);
        }
        double rms = Math.sqrt(sum / r.length);
        return rms;
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
            br = new BufferedReader (new FileReader(csvFile));
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
            if (norm > 1) {
                System.out.println("normalization error " + i + " " + norm); }
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
            if (norm > 1) {
                System.out.println("normalization error " + i + " " + norm); }
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
            if (norm > 1) {
                System.out.println("normalization error " + i + " " + norm); }
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
            if (norm > 1) {
                System.out.println("normalization error " + i + " " + norm); }
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


    public static List<double[]> GetRandomizedOrder(List<double[]> allData){
        List<double[]> newList = new ArrayList<>();
        for (int i = 0; i < allData.size(); i++) {
            newList.add(i, allData.get(i));
        }
        Collections.shuffle(newList);
        System.out.println("Randomization completed");
        return newList;

    }

    public static List<double[]> GetCVTestData (List<double[]> allData, int folds, int order) {
        List<double[]> dtest = new ArrayList<>();
        int total = allData.size();
        int startIndex = total / folds * (order);
        int endIndex = startIndex + total / folds;
        //System.out.println("total " + total);
        //System.out.println("order " + order);
        //System.out.println("start index " + startIndex);
        //System.out.println("end index " + endIndex);
        dtest = allData.subList(startIndex, endIndex);
        //System.out.println("test data size " + dtest.size());


        return  dtest;
    }

    public static List<double[]> GetCVTrainData (List<double[]> allData, int folds, int order) {
        List<double[]> dstart = new ArrayList<>();
        List<double[]> dend = new ArrayList<>();
        List<double[]> dtrain = new ArrayList<>();
        int total = allData.size();
        int startIndex = total / folds * (order);
        int endIndex = startIndex + total / folds;
        dstart = allData.subList(0, startIndex);
        dend = allData.subList(endIndex, allData.size()-1);
        dtrain.addAll(dstart);
        dtrain.addAll(dend);

        //System.out.println("train data size " + dtrain.size());
        return  dtrain;
    }




    public static double[] ComputeKnn(List<double[]> dtest, List<double[]> dtrain, double[] w)  {
        double[] result = new double[dtest.size()];
        Map<Double,Double> map1 = new HashMap<>();

        Double[] d  = new Double[dtrain.size()];
        for (int i = 0; i < dtest.size(); i++) {
            for (int j = 0;  j < dtrain.size(); j++) {
                d[j] = ComputeEDistance(dtrain.get(j), dtest.get(i), w);
                map1.put(d[j], dtrain.get(j)[dtrain.get(j).length-1]);
            }

            Double[] sortedDistance = d.clone();
            Arrays.sort(sortedDistance, Collections.reverseOrder());
            //System.arraycopy(sortedDistance, 0, sortedDistance, 0, 3);
//            for (int w = 0; w< 3; w++) {
//                System.out.println("sorted Distance: " + sortedDistance[w]);
//            }

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
            //System.out.println("label 1: " + dsum[0]);
            labelList.put(2, dsum[1]);
            //System.out.println("label 2: " + dsum[1]);
            labelList.put(3, dsum[2]);
            //System.out.println("label 3: " + dsum[2]);
            labelList.put(4, dsum[3]);
            //System.out.println("label 4: " + dsum[3]);
            labelList.put(5, dsum[4]);
            //System.out.println("label 5: " + dsum[4]);

            double max = dsum[0];
            int index = 0;
            for(int e = 1; e < dsum.length; e++) {
                if(dsum[e] > max) {
                    max = dsum[e];
                    index = e+1;
                } else {
                    index = 1;
                }
            }

            result[i] = index;
            //System.out.println("result label " + result[i]);
        }




        System.out.println("KNN Calculated");
        return result;


    }


    public static double ComputeEDistance(double[] test, double[] train, double[] w) {

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
        //System.out.println("customerSIM: " + customerSim[(int)train[0]][(int)test[0]]);
        distanceSum = (1 - customerSim[(int)test[0]][(int)train[0]]) * w[0];
        //System.out.println("distanceSum customerSim " + distanceSum);
        //calculate similarity for lifestyle
        //System.out.println("lifestyleSIM: " + lifestyleSim[(int)train[1]][(int)test[1]]);
        distanceSum += (1 - lifestyleSim[(int)test[1]][(int)train[1]]) * w[1];
        //System.out.println("distanceSum lifestyleSim " + distanceSum);


        for (int i = 2; i < 6; i++ ) {
            distanceSum += (Math.pow((test[i]-train[i]), 2)) * w[i];

        }

        if (distanceSum > 6) {
            System.out.println("distanceSum " + distanceSum + " is wrong" );
        }

        double distance = 1 / Math.sqrt(distanceSum);

        //System.out.println("distance " + distance );

        return distance;
    }
}
