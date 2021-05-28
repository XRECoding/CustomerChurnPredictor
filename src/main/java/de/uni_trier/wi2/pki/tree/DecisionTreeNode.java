package de.uni_trier.wi2.pki.tree;

import java.util.HashMap;

/**
 * Class for representing a node in the decision tree.
 */
public class DecisionTreeNode {

    protected DecisionTreeNode parent;              // The parent node in the decision tree.
    protected int attributeIndex;                   // The attribute index to check.
    HashMap<String, DecisionTreeNode> splits;       // The checked split condition values and the nodes for these conditions.
    private int classifier;

    public DecisionTreeNode(int attributeIndex){
        this.attributeIndex = attributeIndex;
        splits = new HashMap<>();
    }

    public int getClassifier() {
        return classifier;
    }

    public void setClassifier(int classifier){
        this.classifier = classifier;
    }

    public HashMap<String, DecisionTreeNode> getSplits() {
        return splits;
    }

    public void setSplits(HashMap<String, DecisionTreeNode> splits) {
        this.splits = splits;
    }

    public DecisionTreeNode getParent(){
        return parent;
    }

    public void setParent(DecisionTreeNode parent){
        this.parent = parent;
    }
}
