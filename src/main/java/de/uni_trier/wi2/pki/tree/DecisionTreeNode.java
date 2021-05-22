package de.uni_trier.wi2.pki.tree;

import java.util.HashMap;


public class DecisionTreeNode {                     // Class for representing a node in the decision tree.
    protected DecisionTreeNode parent;              // The parent node in the decision tree.
    protected int attributeIndex;                   // The attribute index to check.
    HashMap<String, DecisionTreeNode> splits;       // The checked split condition values and the nodes for these conditions.

    
    public DecisionTreeNode(DecisionTreeNode parten, int index) {
        this.attributeIndex = index;
        this.parent = parten;
        splits = new HashMap<>();
    }

    public int getAttributeIndex() {
        return attributeIndex;
    }

    public DecisionTreeNode getParent() {
        return parent;
    }
}
