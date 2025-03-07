import java.util.ArrayList;
import java.util.List;

public class TreeOfLifesNode implements TreeOfLifesNodeInterface {
    public int node_id;
    public String node_name;
    public int child_nodes;
    public boolean leaf_nodes;
    public boolean tolorg_link;
    public boolean extinct;
    public int confidence;
    public int phylesis;
    public List<TreeOfLifesNode> children = new ArrayList<>();
    public TreeOfLifesNode parent;

    // Link alanı
    public String link;

    @Override
    public int getNodeId() {
        return node_id;
    }

    @Override
    public String getNodeName() {
        return node_name;
    }

    @Override
    public int getChildCount() {
        return child_nodes;
    }

    @Override
    public boolean isLeafNode() {
        return leaf_nodes;
    }

    @Override
    public boolean hasTolorgLink() {
        return tolorg_link;
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public boolean isExtinct() {
        return extinct;
    }

    @Override
    public boolean hasConfidence() {
        return confidence == 1;
    }

    @Override
    public boolean hasPhylesis() {
        return phylesis == 1;
    }

    @Override
    public void printSubtree(String prefix, boolean isLast) {
        System.out.println(prefix + (isLast ? "-" : "+") + node_id + "-" + node_name + " (" + (leaf_nodes ? "-" : "+") + ")");
        // Çocukları yazdır
        for (int i = 0; i < children.size(); i++) {
            children.get(i).printSubtree(prefix + (isLast ? "   " : "|  "), i == children.size() - 1);
        }
    }

    @Override
    public String toString() {
        return "TreeOfLifesNode{" +
                "node_id=" + node_id +
                ", node_name='" + node_name + '\'' +
                ", children=" + children.size() +
                ", link='" + link + '\'' +  // Linki burada gösteriyoruz
                '}';
    }
}
