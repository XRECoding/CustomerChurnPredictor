package de.uni_trier.wi2.pki.postprocess;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;

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
        consultTree(trainedDecisionTree, trainedDecisionTree, validationExamples, keys, treeAccuracy, labelAttributeId);
    }

    public static boolean consultTree(DecisionTreeNode root, DecisionTreeNode node, Collection<CSVAttribute[]> examples, List<String> keys, double acc, int labelAttributeId) {
        if (node == null) return true;                                                              // Anker

        boolean isValidForPruning = true;
        for (DecisionTreeNode curNode : node.getSplits().values())                                  // Geher alle Zweige entlang
            if (!consultTree(root, curNode, examples, keys, acc, labelAttributeId))                 // Falls die unteren Zweige nicht kombiniert werden können
                isValidForPruning = false;                                                          // Dann soll auch nicht der obere Zweig kombiniert werden
        
        if (!isValidForPruning) return false;                                                       // Gehe die Rekursion zum momentanen Zweig nach oben

        String posi = null;                                                                         // Suche die Refernz zum mometanen Knoten "node"
        Set<Entry<String, DecisionTreeNode>> parentMap = node.getParent().getSplits().entrySet();   //...
        for (Map.Entry<String, DecisionTreeNode> curNode : parentMap)                               //...
            if (curNode.getValue() == node) posi = curNode.getKey();                                //...


        for (String key : keys) {                                                                   // Versuche die Knoten zu kombinieren in dem wir jede Möglichkeit ausprobieren
            DecisionTreeNode newNode = clone(node).getSplits().put(key, null);                      // Erstelle einen Klon vom momentanen Knoten
            node.getParent().getSplits().put(posi, newNode);                                        // Setze den Klon als neue Verzweigung

            if (acc > CrossValidator.calculateAccuracy(root, examples, labelAttributeId)) {         // Falls die neue Verzeiung genau so gut ist wie die alte, dann lassen wir sie betsehen
                node.getParent().getSplits().put(posi, node);                                       // Falls nicht, dann machen wir es rückgängig 
                return false;                                                                       // Und sagen der Rekursion zur momentanen Verzeigung das wir oben nicht kombinieren können. 
            }
        }
        return true;                                                                                // Gebe zurück das wir erfolgreich kombiniert haben und gegebenen falls oben weiter kombinieren können.
    }


    public static DecisionTreeNode clone(DecisionTreeNode node)  {
        DecisionTreeNode clone = new DecisionTreeNode(node.getAttributeIndex());
        clone.setParent(node.getParent());
        clone.setSplits(node.getSplits()); 
        return clone;
    }
}