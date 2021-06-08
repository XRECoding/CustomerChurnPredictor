package de.uni_trier.wi2.pki.postprocess;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;
import de.uni_trier.wi2.pki.util.EntropyUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
        System.out.println(treeAccuracy);
        consultTree(trainedDecisionTree, trainedDecisionTree, validationExamples, keys, treeAccuracy);
    }

    public static boolean consultTree(DecisionTreeNode root, DecisionTreeNode node, Collection<CSVAttribute[]> examples, List<String> keys, double acc) {
        if (node == null) return true;

        boolean isValidForPruning = true;
        for (DecisionTreeNode curNode : node.getSplits().values())
            if (!consultTree(root, curNode, examples, keys, acc))
                isValidForPruning = false;
        
        if (!isValidForPruning) return false;

        String posi = null;
        for (Map.Entry<String, DecisionTreeNode> curNode: node.getParent().getSplits().entrySet()) {
            if (curNode.getValue() == node) posi = curNode.getKey();
        }

        for (String key : keys) {
            DecisionTreeNode newNode = clone(node).getSplits().put(key, null);
            node.getParent().getSplits().put(posi, newNode);

            Double newAcc = CrossValidator.calculateAccuracy(root, examples, 10);
            if (newAcc < acc) { 
                node.getParent().getSplits().put(posi, node);
                return false; 
            }
        }
        return true;
    }


    public static DecisionTreeNode clone(DecisionTreeNode node)  {
        DecisionTreeNode clone = new DecisionTreeNode(node.getAttributeIndex());
        clone.setParent(node.getParent());
        clone.setSplits(node.getSplits()); 
        return clone;
    }
}