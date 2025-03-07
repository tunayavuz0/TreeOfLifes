public interface TreeInterface {

    void addNode(TreeOfLifesNode node);

    void addEdge(int sourceId, int targetId);

    TreeOfLifesNode getNode(int nodeId);

    void printSubtree(int nodeId);

    void printAncestorPath(int nodeId);

    void findMostRecentCommonAncestor(int nodeId1, int nodeId2);

    void traverseTreePreOrderToFile();

    int calculateHeight();

    int calculateBreadth();

    int calculateDegree();

    void printLongestEvolutionaryPaths();
}
