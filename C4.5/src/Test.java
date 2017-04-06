import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Flynn on 04/04/2017.
 */
public class Test {
    public static void main(String[] args) {
        DecisionTree dt = new DecisionTree();
        dt.loadSelectionTrainData();
        List<Selection> selectionList = dt.getSelectionList();
        TreeNode root = new TreeNode();
        dt.constructDecisionTree(selectionList, 0, root);


    }
}
