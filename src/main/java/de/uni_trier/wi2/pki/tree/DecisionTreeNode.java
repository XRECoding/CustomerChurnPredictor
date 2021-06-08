package de.uni_trier.wi2.pki.tree;

import java.util.HashMap;


public class DecisionTreeNode {                         // Class for representing a node in the decision tree.
    protected DecisionTreeNode parent;                  // The parent node in the decision tree.
    protected int attributeIndex;                       // The attribute index to check.
    protected HashMap<String, DecisionTreeNode> splits; // The checked split condition values and the nodes for these conditions.

    /**
     * Constructor for a new DecisionTreeNode
     *
     * @param index The attribute index of the node
     */

    public DecisionTreeNode(int index) {
        this.attributeIndex = index;
        splits = new HashMap<>();
    }

    /*
        Getter and Setter methods for class variables
     */

    public void setParent(DecisionTreeNode parent) {
        this.parent = parent;
    }

    public int getAttributeIndex() {
        return attributeIndex;
    }

    public DecisionTreeNode getParent() {
        return parent;
    }

    public HashMap<String, DecisionTreeNode> getSplits(){
        return splits;
    }
}