package knn;

public class TestKnn {
    public static void main(String[] args) {
        knn k = new knn();
        System.out.println(k.computeKnn(DataProcessor.trainSet(), DataProcessorTest.testSet()));
    }
}
