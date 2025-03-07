import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Tree implements TreeInterface {
    private Map<Integer, TreeOfLifesNode> nodes = new HashMap<>();
    private TreeOfLifesNode root;

    // Add a node
    public void addNode(TreeOfLifesNode node) {
        nodes.put(node.node_id, node);
        if (root == null) {
            root = node; // The first node is considered as the root
        }
    }

    // Add an edge
    public void addEdge(int sourceId, int targetId) {
        TreeOfLifesNode parent = nodes.get(sourceId);
        TreeOfLifesNode child = nodes.get(targetId);
        if (parent != null && child != null) {
            parent.children.add(child);
            child.parent = parent;
        }
    }

    // Get a node
    public TreeOfLifesNode getNode(int nodeId) {
        return nodes.get(nodeId);
    }

    // Print a subtree (Pre-order)
    public void printSubtree(int nodeId) {
        TreeOfLifesNode node = nodes.get(nodeId);
        if (node != null) {
            printSubtreeWithExtinctStatus(node, "", true);
        } else {
            System.out.println("Node with ID " + nodeId + " not found.");
        }
    }
    private void printSubtreeWithExtinctStatus(TreeOfLifesNode node, String prefix, boolean isLast) {
        if (node == null) return;
        // Determine the extinct status
        String extinctStatus = node.extinct ? "-" : "+";
        // Print the current node
        System.out.println(prefix + (isLast ? "└── " : "├── ") + node.node_id + "-" + node.node_name + " (" + extinctStatus + ")");
        // Update the prefix for child nodes
        String childPrefix = prefix + (isLast ? "    " : "│   ");
        for (int i = 0; i < node.children.size(); i++) {
            printSubtreeWithExtinctStatus(node.children.get(i), childPrefix, i == node.children.size() - 1);
        }
    }

    // Print the ancestor path
    public void printAncestorPath(int nodeId) {
        TreeOfLifesNode node = nodes.get(nodeId);
        if (node == null) {
            System.out.println("Node with ID " + nodeId + " not found.");
            return;
        }

        List<TreeOfLifesNode> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.parent;
        }

        Collections.reverse(path);
        for (TreeOfLifesNode ancestor : path) {
            String indent = "-".repeat(path.indexOf(ancestor));
            System.out.println(indent + ancestor.node_id + "-" + ancestor.node_name + " (" + (ancestor.extinct ? "-" : "+") + ")");
        }
    }

    // Find the most recent common ancestor for two nodes
    public void findMostRecentCommonAncestor(int nodeId1, int nodeId2) {
        TreeOfLifesNode node1 = nodes.get(nodeId1);
        TreeOfLifesNode node2 = nodes.get(nodeId2);

        if (node1 == null || node2 == null) {
            System.out.println("One or both nodes not found.");
            return;
        }
        // Add node1's ancestors to a hash set
        Set<TreeOfLifesNode> ancestors1 = new HashSet<>();
        while (node1 != null) {
            ancestors1.add(node1);
            node1 = node1.parent;
        }
        // Check if any of node2's ancestors exist in the set
        while (node2 != null) {
            if (ancestors1.contains(node2)) {
                System.out.println("The most recent common ancestor is: " + node2.node_id + "-" + node2.node_name);
                return;
            }
            node2 = node2.parent;
        }
        System.out.println("No common ancestor found.");
    }

    // Perform pre-order traversal and save to a file
    public void traverseTreePreOrderToFile() {
        try {
            List<String> preOrderList = new ArrayList<>();
            traversePreOrder(root, preOrderList);
            Files.write(Paths.get("pre-order.txt"), preOrderList);
            System.out.println("Pre-order traversal saved to pre-order.txt.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private void traversePreOrder(TreeOfLifesNode node, List<String> preOrderList) {
        if (node == null)
            return;

        preOrderList.add(node.node_id + "-" + node.node_name);
        // çocuklarında gez soldan sağa
        for (TreeOfLifesNode child : node.children) {
            traversePreOrder(child, preOrderList);
        }
    }

    // Calculate the height of the tree
    public int calculateHeight() {
        return calculateHeight(root);
    }

    private int calculateHeight(TreeOfLifesNode node) {
        if (node == null || node.children.isEmpty()) {
            return 0;
        }
        int maxHeight = 0;
        for (TreeOfLifesNode child : node.children) {
            maxHeight = Math.max(maxHeight, calculateHeight(child));
        }
        return maxHeight + 1;
    }

    // Calculate the breadth of the tree (number of leaves)
    public int calculateBreadth() {
        int leafCount = 0;
        for (TreeOfLifesNode node : nodes.values()) {
            if (node.children.isEmpty()) {
                leafCount++;
            }
        }
        return leafCount;
    }

    // Calculate the degree of the tree
    public int calculateDegree() {
        int maxDegree = 0;
        for (TreeOfLifesNode node : nodes.values()) { // Iterate over all nodes
            int childCount = node.children.size();
            if (childCount > maxDegree) {
                maxDegree = childCount;
            }
        }
        return maxDegree;
    }

    // Print all longest evolutionary paths
    public void printLongestEvolutionaryPaths() {
        List<List<TreeOfLifesNode>> longestPaths = new ArrayList<>();
        findAllLongestPaths(root, new ArrayList<>(), longestPaths);
        if (longestPaths.isEmpty()) {
            System.out.println("No paths found.");
            return;
        }
        System.out.println("Longest Evolutionary Path(s):");
        for (List<TreeOfLifesNode> path : longestPaths) {
            for (TreeOfLifesNode node : path) {
                System.out.print(node.node_id + "-" + node.node_name + " ");
            }
            System.out.println();
        }
    }

    private void findAllLongestPaths(TreeOfLifesNode current, List<TreeOfLifesNode> path, List<List<TreeOfLifesNode>> longestPaths) {
        if (current == null) {
            return;
        }

        path.add(current);

        if (current.children.isEmpty()) {
            if (longestPaths.isEmpty() || path.size() > longestPaths.get(0).size()) {
                // If this path is longer than any previously found paths, reset the list
                longestPaths.clear();
                longestPaths.add(new ArrayList<>(path));
            } else if (path.size() == longestPaths.get(0).size()) {
                // If this path is equal to the longest, add it to the list
                longestPaths.add(new ArrayList<>(path));
            }
        }

        for (TreeOfLifesNode child : current.children) {
            findAllLongestPaths(child, path, longestPaths);
        }

        path.remove(path.size() - 1);
    }

}
