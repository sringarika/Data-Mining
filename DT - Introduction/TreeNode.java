import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flynn on 03/04/2017.
 */
public class TreeNode {
    // Which attribute this node split on
    private int attribute = -1;
    // If the attribute is discrete, which value goes to this node
    private String discreteValue;
    // If the attribute is numeric, which number is used to split data set
    private double splitPoint;
    // If the attribute is numeric, which label goes to this node (only used when constucting decision tree)
    private String splitLabel;
    // Children nodes of this node
    private List<TreeNode> children;
    // If this node is leaf node, which label it gives to test data
    private String label;

    public TreeNode() {
        // attribute = -1;
        children = new ArrayList<>();
    }
    public TreeNode(int attribute, double splitPoint, String splitLabel, List<TreeNode> children, String label) {
        this.attribute = attribute;
        this.splitPoint = splitPoint;
        this.children = children;
        this.splitLabel = splitLabel;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getSplitPoint() {
        return splitPoint;
    }

    public int getAttribute() {
        return attribute;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public void setSplitPoint(double splitPoint) {
        this.splitPoint = splitPoint;
    }

    public String getDiscreteValue() {
        return discreteValue;
    }

    public void setDiscreteValue(String discreteValue) {
        this.discreteValue = discreteValue;
    }

    public String getSplitLabel() {
        return splitLabel;
    }

    public void setSplitLabel(String splitLabel) {
        this.splitLabel = splitLabel;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Attribute: ").append(attribute).append(", Discrete Value: ").append(discreteValue)
                .append(", Split Point: ").append(splitPoint)
                .append(", Label: ").append(label);
        return sb.toString();
    }
}
