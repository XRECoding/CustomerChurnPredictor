package de.uni_trier.wi2.pki.postprocess;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    public static void prune(DecisionTreeNode trainedDecisionTree, Collection<CSVAttribute[]> validationExamples, int labelAttributeId) {
        List<String> keys = validationExamples.stream().map(x -> x[labelAttributeId].getCategory().toString())
                                              .distinct().collect(Collectors.toList());

        double treeAccuracy = CrossValidator.calculateAccuracy(trainedDecisionTree, validationExamples, labelAttributeId);


    }

    public static boolean consultTree(DecisionTreeNode root, DecisionTreeNode node, Collection<CSVAttribute[]> examples, List<String> keys, double acc) {
        if (node == null) return true;

        boolean isValidForPruning = true;
        for (DecisionTreeNode curNode : node.getSplits().values())
            if (!consultTree(root, curNode, examples, keys, acc))
                isValidForPruning = false;
        
        if (!isValidForPruning) return false;

        for (String key : keys) {
            int indexOfNode = node.getAttributeIndex();
            DecisionTreeNode newNode = new DecisionTreeNode(indexOfNode);
    
            newNode.getSplits().put(key, null);

            node.getParent().getSplits().put(IndexOfNode, newNode);
        }
    }

    public  static List<String> getDistinct(List<CSVAttribute[]> examples, int index) {
        return examples.stream().filter(x -> x[index] != null).map(x -> x[index].getCategory().toString())
            .distinct().collect(Collectors.toList());
    }

}
