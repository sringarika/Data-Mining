package knn;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

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
    System.out.println(trainingSet.get(0).toString(1));
    breader.close();

}
}
