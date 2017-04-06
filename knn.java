package knn;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;



public class knn {
   
   
   public List<String> computeKnn(List<double[]> dtrain, List<double[]> dtest)  {
       Map<Double,Double> map1 = new HashMap<>();
       List<String> res = new ArrayList<>();
       Double[] d  = new Double[dtrain.size()];
       for (int i = 0; i < dtest.size(); i++) {
           for (int j = 0;  j < dtrain.size(); j++) {
               d[j] = computeInverseEuclideanDistance(dtrain.get(j), dtest.get(i));
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
              res.add("C" + c1);
          }
          else {
              res.add("C" + c3);
          }
      }
      
      else if(c2 == c3 && c2 != c1) {
          if((sortedDistance[1] + sortedDistance[2]) > sortedDistance[0]) {
              res.add("C" + c2);
          }
          else {
              res.add("C" + c1);
          }
      }

      else if(c1 == c3 && c1 != c2) {
          if((sortedDistance[0] + sortedDistance[2]) > sortedDistance[1]) {
              res.add("C" + c1);
          }
          else {
              res.add("C" + c2);
              
          }
      }
      else {
          res.add("C" + c1);
      }
       
   }
       return res;
   } 
   
   
   public double computeInverseEuclideanDistance(double[] a, double[] b) {
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

