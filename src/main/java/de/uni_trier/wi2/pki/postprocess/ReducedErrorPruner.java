package de.uni_trier.wi2.pki.postprocess;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;
import java.util.Collection;

@SuppressWarnings("rawtypes")

/**
 * Prunes a trained decision tree in a post-pruning way.
 */
public class ReducedErrorPruner {

    /**
     * Prunes the given decision tree in-place.
     *
     * @param trainedDecisionTree The decision tree to prune.
     * @param validationExamples  the examples to validate the pruning with.
     * @param labelAttributeId    The label attribute.
     */
    public void prune(DecisionTreeNode trainedDecisionTree, Collection<CSVAttribute[]> validationExamples, int labelAttributeId) {
        double treeAccuracy = CrossValidator.calculateAccuracy(trainedDecisionTree, validationExamples, labelAttributeId);


    }

    public void consultTree(DecisionTreeNode node) {
        if (node == null) {System.out.println("Hello"); return;}

        for (DecisionTreeNode curNode : node.getSplits().values()) {
            consultTree(curNode);
        }

    }

    /*
public static void consultTree(root, node) {
    if (node == null) then
        return true; fi

    isValidForPruning <- true;
    for (DecisionTreeNode curNode : node.getSplits().values()) do
        isValidForPruning <-  consultTree(curNode);
    od

    if (!isValidForPruning) then return false; fi

    for (bucket e buckets) do
        IndexOfNode <- node.getAttributeIndex();
        newNode <- node.clone().getSplits.put(bucket, null)
        node.getParent().getSplits().put(IndexOfNode, newNode);

        acc = calculateAccuracy(root, examples, labelId);
        if (acc is worst) then
            node.getParent().getSplits().put(IndexOfOldNode, node);
            return false;
        fi;
    od
    return true;
}
     */

}
