import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flynn on 03/04/2017.
 */
public class TreeNode {
    private int attribute;
    private String discreteValue;
    private double splitPoint;
    private List<TreeNode> children;
    private String label;

    public TreeNode() {
        children = new ArrayList<>();
    }
    public TreeNode(int attribute, double splitPoint, List<TreeNode> children, String label) {
        this.attribute = attribute;
        this.splitPoint = splitPoint;
        this.children = children;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Attribute: ").append(attribute).append(", Split Point: ").append(splitPoint)
                .append(", Label: ").append(label);
        return sb.toString();
    }
}
