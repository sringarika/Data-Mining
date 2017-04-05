import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class DataProcessor {

    public static void main(String[] args) {
       String csvFile = "/Users/yiwang/Documents/YiWang/Ebiz/Task 11/task11a/attachments/trainProdSelection.csv";
        BufferedReader br = null;
        String line= "";
        String csvSplitBy = ",";
        int count = 0;
        String[] data;
        List<String[]> list = new ArrayList<String[]>();

        try {
            br = new BufferedReader (new FileReader (csvFile));
            while( ( line = br.readLine () ) != null ) {
                count++;
                data = line.split(csvSplitBy);
                System.out.println ("line " + count + ": " + data[0] + " " + data[1] + ".");
                list.add(data);
                System.out.println ("list " + count + ": " + list.size() + ".");
                System.out.println ("list " + count + ": " + list.get(count-1)[0] + ".");
            }

        } catch  (IOException e) {
            e.printStackTrace();
        }

        //normalize vacation value
            // put vacation value into an array
        float[] vacationRaw = new float[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a float
            String v = list.get(i)[2];
            float newV = Float.parseFloat(v);
            vacationRaw[i] = newV;
        }
            //get the max and min of Vacation array.
        float maxVacation = getMaxValue(vacationRaw);
        float minVacation = getMinValue(vacationRaw);
            //get normalized value for Vacation as an array.
        float[] vacationNormalized = new float[count];
        for (int i = 0; i < count; i ++) {
            float norm = (vacationRaw[i] - minVacation) / (maxVacation - minVacation);
            vacationNormalized[i] = norm;
            System.out.println("normalized vacation " + i + " " + vacationNormalized[i]);
        }
        System.out.println("Vacation value normalization completed");


        //normalize credit value
            // put credit value into an array
        float[] creditRaw = new float[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a float
            String c = list.get(i)[3];
            float newC = Float.parseFloat(c);
            creditRaw[i] = newC;
        }
            //get the max and min of Vacation array.
        float maxCredit = getMaxValue(creditRaw);
        float minCredit = getMinValue(creditRaw);
            //get normalized value for credit as an array.
        float[] creditNormalized = new float[count];
        for (int i = 0; i < count; i ++) {
            float norm = (creditRaw[i] - minCredit) / (maxCredit - minCredit);
            creditNormalized[i] = norm;
            System.out.println("normalized credit " + i + " " + creditNormalized[i]);
        }
        System.out.println("Credit value normalization completed");

        //normalize salary value
            // put salary value into an array
        float[] salaryRaw = new float[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a float
            String s = list.get(i)[4];
            float newS = Float.parseFloat(s);
            salaryRaw[i] = newS;
        }
            //get the max and min of Salary array.
        float maxSalary = getMaxValue(salaryRaw);
        float minSalary = getMinValue(salaryRaw);
            //get normalized value for credit as an array.
        float[] salaryNormalized = new float[count];
        for (int i = 0; i < count; i ++) {
            float norm = (salaryRaw[i] - minSalary) / (maxSalary - minSalary);
            salaryNormalized[i] = norm;
            System.out.println("normalized salary " + i + " " + salaryNormalized[i]);
        }
        System.out.println("Salary value normalization completed");

        //normalize property value
        // put property value into an array
        float[] propertyRaw = new float[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a float
            String p = list.get(i)[5];
            float newP = Float.parseFloat(p);
            propertyRaw[i] = newP;
        }
        //get the max and min of Property array.
        float maxProperty = getMaxValue(propertyRaw);
        float minProperty = getMinValue(propertyRaw);
        //get normalized value for credit as an array.
        float[] propertyNormalized = new float[count];
        for (int i = 0; i < count; i ++) {
            float norm = (propertyRaw[i] - minProperty) / (maxProperty - minProperty);
            propertyNormalized[i] = norm;
            System.out.println("normalized property " + i + " " + propertyNormalized[i]);
        }
        System.out.println("Property value normalization completed");

        //normalize customer type -  symbolic data based on similarity matrix provided
        //normalize lifestyle -  symbolic data based on similarity matrix provided
        // the above two steps are skipped here since it is zero across the board.

        //parse classifier label
        float[] newLabel = new float[count];
        for (int i = 0 ; i < count; i++) {
            //get value and then convert into a float
            String l = list.get(i)[6];
            float newL = Float.parseFloat(l.substring(1));
            System.out.println(newL);
            newLabel[i] = newL;
        }

        //Generate new list for normalized data;
        List<float[]> newList = new ArrayList<float[]>();
        for (int i = 0 ; i < count; i++) {
            float[] newData = new float[7];
            newData[0] = 0;
            newData[1] = 0;
            newData[2] = vacationNormalized[i];
            newData[3] = creditNormalized[i];
            newData[4] = salaryNormalized[i];
            newData[5] = propertyNormalized[i];
            newData[6] = newLabel[i];

            newList.add(newData);
            System.out.println("new data bean created： " + newList.get(i)[6]);
        }
        System.out.println("Whole data normalization completed");
    }

    public static float getMaxValue(float[] array) {
        float max = array[0];
        for (int i = 1; i < array.length; i ++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        System.out.println("max value of this array is " + max);
        return max;
    }

    public static float getMinValue(float[] array) {
        float min = array[0];
        for (int i = 1; i < array.length; i ++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        System.out.println("min value of this array is " + min);
        return min;
    }
}
