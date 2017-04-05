package knn;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import weka.core.Instance;
import weka.core.Instances;

//import weka.core.converters.ConverterUtils.DataSource;

public class knn {
   
   public static void main(String[] args) throws Exception {
       BufferedReader breader = null;
       breader = new BufferedReader(new FileReader("trainProdSelection.arff"));
   
    Instances train = new Instances(breader);
    train.setClassIndex(train.numAttributes() - 1);
    List<Instance> trainingSet = new ArrayList<Instance>();
    int count = 1;
    /*for(int i = 0; i < 185; i++) {
        System.out.println(train.instance(i)); System.out.println(count++); } */
   for(int i = 0; i < 185; i++) {
        trainingSet.add(train.instance(i)); 
   }
  /* for(int i = 0; i < 185; i++) {
       System.out.println(trainingSet.get(i)); System.out.println(count++); 
       }*/
    System.out.println(trainingSet.get(0));
    breader.close();

}
   
   public int computeKnn(List<double[]> dtrain, List<double[]> dtest)  {
       int kvote = 1;
       for (int i = 0; i < dtest.size(); i++) {
           double[] inverseDistanceArray = computeInverseEuclideanDistance(dtrain, dtest.get(i));
           double[] arrayBackup = inverseDistanceArray.clone();
           Arrays.sort(inverseDistanceArray);
          System.arraycopy(inverseDistanceArray, 0, inverseDistanceArray, 0, 3);
           
       }
       return kvote;
   }
   public double[] computeInverseEuclideanDistance(List<double[]> a  , double[] b) {
       double[] d = new double[a.size()]; 
       double similarity;
       
       for(int i = 0; i < b.length; i++) {
           for (int j = 0; j < a.size(); j++) {
              
               
               if (i >= 2) {
                   
           d[j]+= Math.pow((a.get(j)[i] - b[i]),2);
               
               }else {
                       if (a.get(j)[i] == b[i]) {
                           similarity = 1;
                       
                       }else {
                       
                           similarity = 0;
                       }
                  
                       d[j]+= 1 - similarity;
               }
           
             
              d[j] = Math.sqrt(d[j]);
              d[j] = 1.0/d[j];
           }
          
       }
       
       
       return d;
   }
   
}
