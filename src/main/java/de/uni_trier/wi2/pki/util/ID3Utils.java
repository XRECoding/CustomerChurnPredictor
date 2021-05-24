package de.uni_trier.wi2.pki.util;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;
import java.util.*;

@SuppressWarnings("rawtypes")

/**
 * Utility class for creating a decision tree with the ID3 algorithm.
 */
public class ID3Utils {

    /**
     * Create the decision tree given the example and the index of the label attribute.
     *
     * @param examples   The examples to train with. This is a collection of arrays.
     * @param labelIndex The label of the attribute that should be used as an index.
     * @return The root node of the decision tree
     */
    public static DecisionTreeNode createTree(LinkedList<CSVAttribute[]> examples, int labelIndex) {        // changed collection to linked list
        if (examples.size() == 1) return null;                          // Rekursionsanker

        // calculate entropy gain for all attributes
        LinkedList<Double> entropyList = (LinkedList<Double>) EntropyUtils.calcInformationGain(examples, labelIndex);

        // find attribute with best entropy gain
        double maxGain = entropyList.get(0);
        int attributeIndex = 0;

        for (int i = 1; i < entropyList.size(); i++) {
            if (entropyList.get(i) > maxGain) { maxGain = entropyList.get(i); attributeIndex = i; }
        }
   
        examples.remove(attributeIndex);

        // finding all unique values of attribute with best entropy gain
        HashMap<String, String> uniqueValues = new HashMap<>();

        for (int i = 0; i < examples.get(attributeIndex).length; i++) {
            uniqueValues.put(examples.get(attributeIndex)[i].getCategory().toString(), "mir egal");
        }

        // create node for best attribute
        DecisionTreeNode root = new DecisionTreeNode(createTree(examples, labelIndex-1), attributeIndex);




        /* TODO
            1. calculate entropy
            2. create node and set to root
            3. choose attribute with highest gain
            4. if only + / - class
                    return root
            5. for every category
                5a. create treeNode
                5b. examples = examples - attribute used
                5c. createTree(examples, labelIndex -1)
         */

        return root;
    }

}
