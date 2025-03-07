import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Tree tree = new Tree();
        Scanner input = new Scanner(System.in);

        while (true) {

            System.out.println("\033[1;32m");
            System.out.println("█████████████████████████████████████████████████████████████████");
            System.out.println("█                                                               █");
            System.out.println("█               🌳 Welcome to the Tree of Life 🌳               █");
            System.out.println("█                                                               █");
            System.out.println("█████████████████████████████████████████████████████████████████\033[0m");

            System.out.println("\n\033[1;34m--- Tree of Life Program Menu ---\033[0m");
            System.out.println("\033[1;33m1️⃣\033[0m. 📂 Load dataset");
            System.out.println("\033[1;33m2️⃣\033[0m. 🔍 Search for species");
            System.out.println("\033[1;33m3️⃣\033[0m. 🧭 Traverse the tree in pre-order and save to file");
            System.out.println("\033[1;33m4️⃣\033[0m. 🌿 Print the subtree of a given species");
            System.out.println("\033[1;33m5️⃣\033[0m. 🧬 Print the ancestor path of a given species");
            System.out.println("\033[1;33m6️⃣\033[0m. 🤝 Find the most recent common ancestor of two species");
            System.out.println("\033[1;33m7️⃣\033[0m. 📊 Calculate height, degree, and breadth of the tree");
            System.out.println("\033[1;33m8️⃣\033[0m. 🏞 Print the longest evolutionary path");
            System.out.println("\033[1;33m9️⃣\033[0m. ❌ Exit");

            System.out.print("\033[1;36mChoose an option: \033[0m");

            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    loadDataset(tree);
                    break;
                case 2:
                    searchSpecies(tree, input);
                    break;
                case 3:
                    tree.traverseTreePreOrderToFile();
                    break;
                case 4:
                    printSubtree(tree, input);
                    break;
                case 5:
                    printAncestorPath(tree, input);
                    break;
                case 6:
                    findCommonAncestor(tree, input);
                    break;
                case 7:
                    calculateTreeMetrics(tree);
                    break;
                case 8:
                    tree.printLongestEvolutionaryPaths();
                    break;
                case 9:
                    System.out.println("\033[1;31mExiting program... Goodbye! 🌟\033[0m");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void loadDataset(Tree tree) {
        try {
            Scanner nodeScanner = new Scanner(new File("treeoflife_nodes.csv"));
            Scanner linkScanner = new Scanner(new File("treeoflife_links.csv"));

            // Load nodes
            nodeScanner.nextLine();
            while (nodeScanner.hasNextLine()) {
                String[] data = nodeScanner.nextLine().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (data.length < 8) {
                    System.out.println("Warning: Skipping incomplete row: " + String.join(",", data));
                    continue; // Skip rows with incomplete data
                }

                TreeOfLifesNode node = new TreeOfLifesNode();
                node.node_id = Integer.parseInt(data[0]);
                node.node_name = data[1]; // node_name

                // Parse child_nodes as an integer
                node.child_nodes = Integer.parseInt(data[2]);

                // Set leaf_node as a boolean
                node.leaf_nodes = data[3].trim().equals("1"); // true if 1, otherwise false

                node.tolorg_link = data[4].trim().equals("1");
                node.extinct = data[5].trim().equals("1");

                // Parse confidence and phylesis as integers
                node.confidence = Integer.parseInt(data[6].trim());
                node.phylesis = Integer.parseInt(data[7].trim());

                // If tolorg_link exists, create a link
                if (node.tolorg_link) {
                    node.link = "http://tolweb.org/" + node.node_name.replace(" ", "_") + "/" + node.node_id;
                }

                tree.addNode(node);
            }

            // Load edges
            linkScanner.nextLine();
            while (linkScanner.hasNextLine()) {
                String[] link = linkScanner.nextLine().split(",");
                int source = Integer.parseInt(link[0]);
                int target = Integer.parseInt(link[1]);

                TreeOfLifesNode parent = tree.getNode(source);
                TreeOfLifesNode child = tree.getNode(target);

                if (parent != null && child != null) {
                    tree.addEdge(source, target); // Add connection
                }
            }

            System.out.println("Dataset loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Dataset files not found.");
        }
    }

    private static void searchSpecies(Tree tree, Scanner input) {
        System.out.print("Enter species ID: ");
        int id = input.nextInt();
        TreeOfLifesNode node = tree.getNode(id);

        if (node != null) {

            System.out.println("Id: " + node.node_id);
            System.out.println("Name: " + node.node_name);
            System.out.println("Child count: " + node.children.size());
            System.out.println("Leaf node: " + (node.leaf_nodes ? "yes" : "no"));

            if (node.tolorg_link && node.link != null) {
                System.out.println("Link: " + node.link);
            }
            else
                System.out.println("\033[1;31mLink: There is no such link.\033[0m");

            // Handle extinct information with if-else
            if (node.extinct) {
                System.out.println("Extinct: Yes (Extinct species)");
            } else {
                System.out.println("Extinct: No (Living species)");
            }
            //Confidence Information
            if (node.confidence == 0) {
                System.out.println("Confidence: Confident Position [0]");
            }
            else if (node.confidence == 1) {
                System.out.println("Confidence: Problematic Position [1]");
            }
            else if (node.confidence == 2) {
                System.out.println("Confidence: Unspecified Position [2]");
            }
            else
                System.out.println("Invalid confidence level");

            // Phylesis information
            if (node.confidence == 0) {
                System.out.println("Phylesis: Monophyletic [0]");
            }
            else if (node.confidence == 1) {
                System.out.println("Phylesis: Uncertain Monophyly [1]");
            }
            else if (node.confidence == 2) {
                System.out.println("Phylesis: Not Monophyletic [2]");
            }
            else
                System.out.println("Invalid confidence level");

        }
        else {
            System.out.println("Species not found.");
        }
    }

    private static void printSubtree(Tree tree, Scanner input) {
        System.out.print("Enter species ID: ");
        int id = input.nextInt();
        tree.printSubtree(id);
    }

    private static void printAncestorPath(Tree tree, Scanner input) {
        System.out.print("Enter species ID: ");
        int id = input.nextInt();
        tree.printAncestorPath(id);
    }

    private static void findCommonAncestor(Tree tree, Scanner input) {
        System.out.print("Enter first species ID: ");
        int id1 = input.nextInt();
        System.out.print("Enter second species ID: ");
        int id2 = input.nextInt();
        tree.findMostRecentCommonAncestor(id1, id2);
    }

    private static void calculateTreeMetrics(Tree tree) {
        System.out.println("Tree height: " + tree.calculateHeight());
        System.out.println("Tree degree: " + tree.calculateDegree());
        System.out.println("Tree breadth: " + tree.calculateBreadth());
    }
}
