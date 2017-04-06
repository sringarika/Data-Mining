package knn;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;



public class knn {
   
   
   public String computeKnn(List<double[]> dtrain, List<double[]> dtest)  {
       Map<Double,Double> map1 = new HashMap<>();

       Double[] d  = new Double[dtrain.size()];
       for (int i = 0; i < dtest.size(); i++) {
           for (int j = 0;  j < dtrain.size(); j++) {
               d[j] = computeEuclideanDistance(dtrain.get(j), dtest.get(i));
               map1.put(d[j], dtrain.get(j)[dtrain.get(j).length-1]);
       }
      
   }
       Double[] sortedDistance = d.clone();
       Arrays.sort(sortedDistance, Collections.reverseOrder());
      System.arraycopy(sortedDistance, 0, sortedDistance, 0, 3);
      
      double c1 = map1.get(sortedDistance[0]);
      double c2 = map1.get(sortedDistance[1]);
      double c3 = map1.get(sortedDistance[2]);
      
     
      if(c1 == c2 && c1 != c3) {
          if((sortedDistance[0] + sortedDistance[1]) > sortedDistance[2]) {
              return "C" + c1;
          }
          else {
              return "C" + c3;
          }
      }
      
      if(c2 == c3 && c2 != c1) {
          if((sortedDistance[1] + sortedDistance[2]) > sortedDistance[0]) {
              return "C" + c2;
          }
          else {
              return "C" + c1;
          }
      }

      if(c1 == c3 && c1 != c2) {
          if((sortedDistance[0] + sortedDistance[2]) > sortedDistance[1]) {
              return "C" + c1;
          }
          else {
              return "C" + c2;
          }
      }
      else {
          return "C" + c1;
      }
       
   }
   
   
   
   public double computeEuclideanDistance(double[] a, double[] b) {
       double d = 0; 
       double similarity;
       
       for(int i = 0; i < b.length; i++) {
              
               
               if (i >= 2) {
                   
           d+= Math.pow((a[i] - b[i]),2);
               
               }else {
                       if (a[i] == b[i]) {
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

