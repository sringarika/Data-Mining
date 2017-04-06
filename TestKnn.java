package knn;

import java.util.List;

public class TestKnn {
    public static void main(String[] args) {
        knn k = new knn();
        List<String> result = k.computeKnn(DataProcessor.trainSet(), DataProcessorTest.testSet());
        
        for ( int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i));
        }
    }
}
