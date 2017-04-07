import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * Created by yiwang on 4/6/17.
 */
public class ProjectBKNNBinary {



    public static void main(String[] args) {
        List<double[]> dtrain =GetNormalizedTrainData();
        List<double[]> dtest = GetNormalizedTestData();
        String[] result = ComputeKnn(dtrain, dtest);

        for (int i = 0; i < result.length; i ++) {
            System.out.println("result " + i + " is " + result[i]);
        }

    }


    public static List<double[]> GetNormalizedTrainData () {
        String csvFile = "/Users/yiwang/Documents/YiWang/Ebiz/Task 11/task11b/attachments/trainProdIntro.binary.csv";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        int count = 0;
        String[] data;
        List<String[]> list = new ArrayList<String[]>();
        //set up similarity matrix for service type
        Map<String,Integer> service_map =new HashMap<String,Integer>();
        service_map.put("Loan", 0);
        service_map.put("Bank_Account", 1);
        service_map.put("CD", 2);
        service_map.put("Mortgage", 3);
        service_map.put("Fund", 4);

        Map<String,Integer> customer_map =new HashMap<String,Integer>();
        customer_map.put("Business", 0);
        customer_map.put("Professional", 1);
        customer_map.put("Student", 2);
        customer_map.put("Doctor", 3);
        customer_map.put("Other", 4);

        Map<String,Integer> size_map =new HashMap<String,Integer>();
        size_map.put("Small", 0);
        size_map.put("Medium", 1);
        size_map.put("Large", 2);

        Map<String,Integer> promotion_map =new HashMap<String,Integer>();
        promotion_map.put("Full", 0);
        promotion_map.put("Web&Email", 1);
        promotion_map.put("Web", 2);
        promotion_map.put("None", 3);


        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                count++;
                data = line.split(csvSplitBy);
                System.out.println("line " + count + ": " + data[0] + " " + data[1] + ".");
                list.add(data);
                System.out.println("list " + count + ": " + list.size() + ".");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //normalize numerical values first: monthly fee, buddget, interest_rate, period
        //normalize monthly fee value
        // put fee value into an array
        double[] feeRaw = new double[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a double
            String f = list.get(i)[2];
            double newF = Double.parseDouble(f);
            feeRaw[i] = newF;
        }
        //get the max and min of Fee array.
        double maxFee = getMaxValue(feeRaw);
        double minFee = getMinValue(feeRaw);
        //get normalized value for fee as an array.
        double[] feeNormalized = new double[count];
        for (int i = 0; i < count; i ++) {
            double norm = (feeRaw[i] - minFee) / (maxFee - minFee);
            feeNormalized[i] = norm;
            System.out.println("normalized fee " + i + " " + feeNormalized[i]);
        }
        System.out.println("Fee value normalization completed");

        //normalize budget value
        // put budget value into an array
        double[] budgetRaw = new double[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a double
            String b = list.get(i)[3];
            double newB = Double.parseDouble(b);
            budgetRaw[i] = newB;
        }
        //get the max and min of budget array.
        double maxBudget = getMaxValue(budgetRaw);
        double minBudget = getMinValue(budgetRaw);
        //get normalized value for budget as an array.
        double[] budgetNormalized = new double[count];
        for (int i = 0; i < count; i ++) {
            double norm = (budgetRaw[i] - minBudget) / (maxBudget - minBudget);
            budgetNormalized[i] = norm;
            System.out.println("normalized budget " + i + " " + budgetNormalized[i]);
        }
        System.out.println("Budget value normalization completed");

        //normalize interest rate value
        // put rate value into an array
        double[] rateRaw = new double[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a double
            String r = list.get(i)[6];
            double newR = Double.parseDouble(r);
            rateRaw[i] = newR;
        }
        //get the max and min of Rat array.
        double maxRate = getMaxValue(rateRaw);
        double minRate = getMinValue(rateRaw);
        //get normalized value for credit as an array.
        double[] rateNormalized = new double[count];
        for (int i = 0; i < count; i ++) {
            double norm = (rateRaw[i] - minRate) / (maxRate - minRate);
            rateNormalized[i] = norm;
            System.out.println("normalized rate " + i + " " + rateNormalized[i]);
        }
        System.out.println("Interest rate value normalization completed");

        //normalize period value
        // put period value into an array
        double[] periodRaw = new double[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a double
            String p = list.get(i)[7];
            double newP = Double.parseDouble(p);
            periodRaw[i] = newP;
        }
        //get the max and min of period array.
        double maxPeriod = getMaxValue(periodRaw);
        double minPeriod = getMinValue(periodRaw);
        //get normalized value for credit as an array.
        double[] periodNormalized = new double[count];
        for (int i = 0; i < count; i ++) {
            double norm = (periodRaw[i] - minPeriod) / (maxPeriod - minPeriod);
            periodNormalized[i] = norm;
            System.out.println("normalized period " + i + " " + periodNormalized[i]);
        }
        System.out.println("Period normalization completed");

        //convert symbolic data-> not using 2d array yet, that is for KNN calculation
        //convert service type.
        int[] serviceConverted = new int[count];
        for (int i = 0; i < count; i++) {
            serviceConverted[i] = service_map.get(list.get(i)[0]);
            System.out.println("convert service " + i + " " + serviceConverted[i]);
        }
        System.out.println("service type conversion completed");

        //convert Customer type.
        int[] customerConverted = new int[count];
        for (int i = 0; i < count; i++) {
            customerConverted[i] = customer_map.get(list.get(i)[1]);
            System.out.println("convert customer " + i + " " + customerConverted[i]);
        }
        System.out.println("Customer type conversion completed");

        //convert size type.
        int[] sizeConverted = new int[count];
        for (int i = 0; i < count; i++) {
            sizeConverted[i] = size_map.get(list.get(i)[4]);
            System.out.println("convert size " + i + " " + sizeConverted[i]);
        }
        System.out.println("size type conversion completed");

        //convert promotion type.
        int[] promotionConverted = new int[count];
        for (int i = 0; i < count; i++) {
            promotionConverted[i] = promotion_map.get(list.get(i)[5]);
            System.out.println("convert promotion " + i + " " + sizeConverted[i]);
        }
        System.out.println("promotion type conversion completed");

        //generate new list for normalized/conversion.
        int dataCount = 0;
        List<double[]> newList = new ArrayList<double[]>();
        for (int i = 0 ; i < count; i++) {
            double[] newData = new double[9];
            newData[0] = serviceConverted[i];
            newData[1] = customerConverted[i];
            newData[2] = feeNormalized[i];
            newData[3] = budgetNormalized[i];
            newData[4] = sizeConverted[i];
            newData[5] = promotionConverted[i];
            newData[6] = rateNormalized[i];
            newData[7] = periodNormalized[i];
            newData[8] = Double.parseDouble(list.get(i)[8]);
            dataCount++;
            newList.add(newData);
            System.out.println("new data bean created： " + newList.get(i)[6]);
        }

        System.out.println("Train data normalization completed " + dataCount);
        return newList;

    }



    public static List<double[]> GetNormalizedTestData () {
        String csvFile = "/Users/yiwang/Documents/YiWang/Ebiz/Task 11/task11b/attachments/testProdIntro.binary.csv";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        int count = 0;
        String[] data;
        List<String[]> list = new ArrayList<String[]>();
        //set up similarity matrix for service type
        Map<String,Integer> service_map =new HashMap<String,Integer>();
        service_map.put("Loan", 0);
        service_map.put("Bank_Account", 1);
        service_map.put("CD", 2);
        service_map.put("Mortgage", 3);
        service_map.put("Fund", 4);

        //set up similarity matrix for customer type
        Map<String,Integer> customer_map =new HashMap<String,Integer>();
        customer_map.put("Business", 0);
        customer_map.put("Professional", 1);
        customer_map.put("Student", 2);
        customer_map.put("Doctor", 3);
        customer_map.put("Other", 4);

        Map<String,Integer> size_map =new HashMap<String,Integer>();
        size_map.put("Small", 0);
        size_map.put("Medium", 1);
        size_map.put("Large", 2);

        Map<String,Integer> promotion_map =new HashMap<String,Integer>();
        promotion_map.put("Full", 0);
        promotion_map.put("Web&Email", 1);
        promotion_map.put("Web", 2);
        promotion_map.put("None", 3);





        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                count++;
                data = line.split(csvSplitBy);
                System.out.println("line " + count + ": " + data[0] + " " + data[1] + ".");
                list.add(data);
                System.out.println("list " + count + ": " + list.size() + ".");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //normalize numerical values first: monthly fee, buddget, interest_rate, period
        //normalize monthly fee value
        // put fee value into an array
        double[] feeRaw = new double[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a double
            String f = list.get(i)[2];
            double newF = Double.parseDouble(f);
            feeRaw[i] = newF;
        }
        //get the max and min of Fee array.
        double maxFee = getMaxValue(feeRaw);
        double minFee = getMinValue(feeRaw);
        //get normalized value for fee as an array.
        double[] feeNormalized = new double[count];
        for (int i = 0; i < count; i ++) {
            double norm = (feeRaw[i] - minFee) / (maxFee - minFee);
            feeNormalized[i] = norm;
            System.out.println("normalized fee " + i + " " + feeNormalized[i]);
        }
        System.out.println("Fee value normalization completed");

        //normalize budget value
        // put budget value into an array
        double[] budgetRaw = new double[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a double
            String b = list.get(i)[3];
            double newB = Double.parseDouble(b);
            budgetRaw[i] = newB;
        }
        //get the max and min of budget array.
        double maxBudget = getMaxValue(budgetRaw);
        double minBudget = getMinValue(budgetRaw);
        //get normalized value for budget as an array.
        double[] budgetNormalized = new double[count];
        for (int i = 0; i < count; i ++) {
            double norm = (budgetRaw[i] - minBudget) / (maxBudget - minBudget);
            budgetNormalized[i] = norm;
            System.out.println("normalized budget " + i + " " + budgetNormalized[i]);
        }
        System.out.println("Budget value normalization completed");

        //normalize interest rate value
        // put rate value into an array
        double[] rateRaw = new double[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a double
            String r = list.get(i)[6];
            double newR = Double.parseDouble(r);
            rateRaw[i] = newR;
        }
        //get the max and min of Rat array.
        double maxRate = getMaxValue(rateRaw);
        double minRate = getMinValue(rateRaw);
        //get normalized value for credit as an array.
        double[] rateNormalized = new double[count];
        for (int i = 0; i < count; i ++) {
            double norm = (rateRaw[i] - minRate) / (maxRate - minRate);
            rateNormalized[i] = norm;
            System.out.println("normalized rate " + i + " " + rateNormalized[i]);
        }
        System.out.println("Interest rate value normalization completed");

        //normalize period value
        // put period value into an array
        double[] periodRaw = new double[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a double
            String p = list.get(i)[7];
            double newP = Double.parseDouble(p);
            periodRaw[i] = newP;
        }
        //get the max and min of period array.
        double maxPeriod = getMaxValue(periodRaw);
        double minPeriod = getMinValue(periodRaw);
        //get normalized value for credit as an array.
        double[] periodNormalized = new double[count];
        for (int i = 0; i < count; i ++) {
            double norm = (periodRaw[i] - minPeriod) / (maxPeriod - minPeriod);
            periodNormalized[i] = norm;
            System.out.println("normalized period " + i + " " + periodNormalized[i]);
        }
        System.out.println("Period normalization completed");



        //convert symbolic data-> not using 2d array yet, that is for KNN calculation
        //convert service type.
        int[] serviceConverted = new int[count];
        for (int i = 0; i < count; i++) {
            serviceConverted[i] = service_map.get(list.get(i)[0]);
            System.out.println("convert service " + i + " " + serviceConverted[i]);
        }
        System.out.println("service type conversion completed");

        //convert Customer type.
        int[] customerConverted = new int[count];
        for (int i = 0; i < count; i++) {
            customerConverted[i] = customer_map.get(list.get(i)[1]);
            System.out.println("convert customer " + i + " " + customerConverted[i]);
        }
        System.out.println("Customer type conversion completed");

        //convert size type.
        int[] sizeConverted = new int[count];
        for (int i = 0; i < count; i++) {
            sizeConverted[i] = size_map.get(list.get(i)[4]);
            System.out.println("convert size " + i + " " + sizeConverted[i]);
        }
        System.out.println("size type conversion completed");

        //convert promotion type.
        int[] promotionConverted = new int[count];
        for (int i = 0; i < count; i++) {
            promotionConverted[i] = promotion_map.get(list.get(i)[5]);
            System.out.println("convert promotion " + i + " " + sizeConverted[i]);
        }
        System.out.println("promotion type conversion completed");

        //generate new list for normalized/conversion.
        int dataCount = 0;
        List<double[]> newList = new ArrayList<double[]>();
        for (int i = 0 ; i < count; i++) {
            double[] newData = new double[8];
            newData[0] = serviceConverted[i];
            newData[1] = customerConverted[i];
            newData[2] = feeNormalized[i];
            newData[3] = budgetNormalized[i];
            newData[4] = sizeConverted[i];
            newData[5] = promotionConverted[i];
            newData[6] = rateNormalized[i];
            newData[7] = periodNormalized[i];
            dataCount ++;
            newList.add(newData);
            System.out.println("new data bean created： " + newList.get(i)[6]);
        }
        System.out.println("Test data normalization completed " + dataCount);
        return newList;
    }



    public static double getMaxValue(double[] array) {
        double max = array[0];
        for (int i = 1; i < array.length; i ++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        System.out.println("max value of this array is " + max);
        return max;
    }

    public static double getMinValue(double[] array) {
        double min = array[0];
        for (int i = 1; i < array.length; i ++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        System.out.println("min value of this array is " + min);
        return min;
    }

    public static String[] ComputeKnn(List<double[]> dtrain, List<double[]> dtest)  {
        String[] result = new String[dtest.size()];

        Map<Double,Double> distanceMap = new HashMap<>();
        Double[] d  = new Double[dtrain.size()];
        for (int i = 0; i < dtest.size(); i++) {
            for (int j = 0; j < dtrain.size(); j++) {
                d[j] = ComputeEDistance(dtrain.get(j), dtest.get(i));
                distanceMap.put(d[j], dtrain.get(j)[dtrain.get(j).length-1]);
            }
            Double[] sortedDistance = d.clone();
            Arrays.sort(sortedDistance, Collections.reverseOrder());
            System.arraycopy(sortedDistance, 0, sortedDistance, 0, 5);

            double success = 0;
            double failure = 0;
            for (int a = 0; a <  5; a ++) {
                if(distanceMap.get(sortedDistance[a]) == 1) {
                    success += sortedDistance[a];
                } else {
                    failure += sortedDistance[a];
                }
            }

            if (success > failure) {
                result[i] = "1";
            } else {result[i] = "0";}
        }

        return result;


    }

    public static double ComputeEDistance(double[] train, double[] test) {

        Map<String,Integer> service_map =new HashMap<String,Integer>();
        service_map.put("Loan", 0);
        service_map.put("Bank_Account", 1);
        service_map.put("CD", 2);
        service_map.put("Mortgage", 3);
        service_map.put("Fund", 4);
        double[][] serviceSim = new double[5][5];
        serviceSim[0][0] = 1;
        serviceSim[0][1] = 0;
        serviceSim[0][2] = 0.1;
        serviceSim[0][3] = 0.3;
        serviceSim[0][4] = 0.2;
        serviceSim[1][0] = 0;
        serviceSim[1][1] = 1;
        serviceSim[1][2] = 0;
        serviceSim[1][3] = 0;
        serviceSim[1][4] = 0;
        serviceSim[2][0] = 0.1;
        serviceSim[2][1] = 0;
        serviceSim[2][2] = 1;
        serviceSim[2][3] = 0.2;
        serviceSim[2][4] = 0.2;
        serviceSim[3][0] = 0.3;
        serviceSim[3][1] = 0;
        serviceSim[3][2] = 0.2;
        serviceSim[3][3] = 1;
        serviceSim[3][4] = 0.1;
        serviceSim[4][0] = 0.2;
        serviceSim[4][1] = 0;
        serviceSim[4][2] = 0.2;
        serviceSim[4][3] = 0.1;
        serviceSim[4][4] = 1;

        //set up similarity matrix for customer type
        Map<String,Integer> customer_map =new HashMap<String,Integer>();
        customer_map.put("Business", 0);
        customer_map.put("Professional", 1);
        customer_map.put("Student", 2);
        customer_map.put("Doctor", 3);
        customer_map.put("Other", 4);
        double[][] customerSim = new double[5][5];
        customerSim[0][0] = 1;
        customerSim[0][1] = 0.2;
        customerSim[0][2] = 0.1;
        customerSim[0][3] = 0.2;
        customerSim[0][4] = 0;
        customerSim[1][0] = 0.2;
        customerSim[1][1] = 1;
        customerSim[1][2] = 0.2;
        customerSim[1][3] = 0.1;
        customerSim[1][4] = 0;
        customerSim[2][0] = 0.1;
        customerSim[2][1] = 0.2;
        customerSim[2][2] = 1;
        customerSim[2][3] = 0.1;
        customerSim[2][4] = 0;
        customerSim[3][0] = 0.2;
        customerSim[3][1] = 0.1;
        customerSim[3][2] = 0.1;
        customerSim[3][3] = 1;
        customerSim[3][4] = 0;
        customerSim[4][0] = 0;
        customerSim[4][1] = 0;
        customerSim[4][2] = 0;
        customerSim[4][3] = 0;
        customerSim[4][4] = 1;

        Map<String,Integer> size_map =new HashMap<String,Integer>();
        size_map.put("Small", 0);
        size_map.put("Medium", 1);
        size_map.put("Large", 2);
        double[][] sizeSim = new double[5][5];
        sizeSim[0][0]= 1;
        sizeSim[0][1]= 0.1;
        sizeSim[0][2]= 0;
        sizeSim[1][0]= 0.1;
        sizeSim[1][1]= 1;
        sizeSim[0][2]= 0.1;
        sizeSim[0][0]= 0;
        sizeSim[0][1]= 0.1;
        sizeSim[0][2]= 1;

        Map<String,Integer> promotion_map =new HashMap<String,Integer>();
        promotion_map.put("Full", 0);
        promotion_map.put("Web&Email", 1);
        promotion_map.put("Web", 2);
        promotion_map.put("None", 3);
        double[][] promotionSim = new double[5][5];
        promotionSim[0][0] = 1;
        promotionSim[0][1] = 0.8;
        promotionSim[0][2] = 0;
        promotionSim[0][3] = 0;
        promotionSim[1][0] = 0.8;
        promotionSim[1][1] = 1;
        promotionSim[1][2] = 0.1;
        promotionSim[1][3] = 0.5;
        promotionSim[2][0] = 0;
        promotionSim[2][1] = 0.1;
        promotionSim[2][2] = 1;
        promotionSim[2][3] = 0.4;
        promotionSim[3][0] = 0;
        promotionSim[3][1] = 0.5;
        promotionSim[3][2] = 0.4;
        promotionSim[3][3] = 1;

        int count = train.length;
        double distanceSum = 0;

        //calculate similarity for service
        distanceSum = 1- serviceSim[(int)train[0]][(int)test[0]];

        //calculate similarity for customer
        distanceSum += 1- serviceSim[(int)train[1]][(int)test[1]];

        //calculate similarity for size
        distanceSum += 1- serviceSim[(int)train[4]][(int)test[4]];

        //calculate similarity for promotion
        distanceSum += 1- serviceSim[(int)train[5]][(int)test[5]];


        //calculate similarity for monthly fee and budget
        for (int i = 2; i < 4; i++) {
            distanceSum += Math. pow ((train[i]-test[i]), 2);
        }
        //calculate similarity for interest rate and period
        for (int i = 6; i < 8; i++) {
            distanceSum += Math. pow ((train[i]-test[i]), 2);
        }

        double distance = 1 / distanceSum;

        return distance;
    }
}
