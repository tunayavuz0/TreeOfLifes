public interface TreeOfLifesNodeInterface {

    int getNodeId();

    String getNodeName();

    int getChildCount();

    boolean isLeafNode();

    boolean hasTolorgLink();

    String getLink();

    boolean isExtinct();

    boolean hasConfidence();

    boolean hasPhylesis();

    void printSubtree(String prefix, boolean isLast);
}




