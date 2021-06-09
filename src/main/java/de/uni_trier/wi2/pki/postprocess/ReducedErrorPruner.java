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
    static double acc = 0.0;
    /**
     * Prunes the given decision tree in-place.
     *
     * @param trainedDecisionTree The decision tree to prune.
     * @param validationExamples  the examples to validate the pruning with.
     * @param labelAttributeId    The label attribute.
     */

    public void prune(DecisionTreeNode trainedDecisionTree, Collection<CSVAttribute[]> validationExamples, int labelAttributeId) {
        List<String> keys = validationExamples.stream().map(x -> x[labelAttributeId].getCategory().toString())
                                .distinct().collect(Collectors.toList());
         
        acc = CrossValidator.calculateAccuracy(trainedDecisionTree, validationExamples, labelAttributeId);
        consultTree(trainedDecisionTree, trainedDecisionTree, validationExamples, keys, labelAttributeId);
    }

    public static boolean consultTree(DecisionTreeNode root, DecisionTreeNode node, Collection<CSVAttribute[]> examples, List<String> keys, int labelAttributeId) {
        if (node == null) return true;                                                              // Anker

        boolean isValidForPruning = true;
        for (DecisionTreeNode curNode : node.getSplits().values())                                  // Geher alle Zweige entlang
            if (!consultTree(root, curNode, examples, keys, labelAttributeId))                      // Falls die unteren Zweige nicht kombiniert werden können,
                isValidForPruning = false;                                                          // dann soll auch nicht der obere Zweig kombiniert werden
        
        if (!isValidForPruning) return false;                                                       // Gehe die Rekursion zum momentanen Zweig nach oben


        String posi = null;                                                                         // Suche die Refernz zum momentanen Knoten "node"
        Set<Entry<String, DecisionTreeNode>> parentMap = node.getParent().getSplits().entrySet();   //...
        for (Map.Entry<String, DecisionTreeNode> curNode : parentMap)                               //...
            if (curNode.getValue() == node) posi = curNode.getKey();                                //...


        DecisionTreeNode bestChange = null;
        for (String key : keys) {                                                                   // Versuche die Knoten zu kombinieren in dem wir jede Möglichkeit ausprobieren
            DecisionTreeNode newNode = clone(node, key);                                            // Erstelle einen Klon vom momentanen Knoten
            node.getParent().getSplits().put(posi, newNode);                                        // Setze den Klon als neue Verzweigung

            double newAcc = CrossValidator.calculateAccuracy(root, examples, labelAttributeId);

            if (newAcc >= acc) {                                                                    // Falls die neue acc nicht schlechter als die alte ist
                acc = newAcc;
                bestChange = newNode;                                                                    
            }
            node.getParent().getSplits().put(posi, node);                                           // setze den Baum wieder auf den alten standt.
        }

        if (bestChange != null) {                                                                   // Falls wir einen guten Change gefunden haben dann übernehmen wir ihn.
            node.getParent().getSplits().put(posi, bestChange);
            return true;                                                                            // Gebe zurück das wir erfolgreich kombiniert haben
        }
        return false;                                                                               // Gebe zurück das wir nicht erfolgreich kombiniert haben
    }



    public static DecisionTreeNode clone(DecisionTreeNode node, String key)  {
        DecisionTreeNode clone = new DecisionTreeNode(node.getAttributeIndex());
        clone.setParent(node.getParent());
        clone.getSplits().put(key, null);
        return clone;
    }
}

