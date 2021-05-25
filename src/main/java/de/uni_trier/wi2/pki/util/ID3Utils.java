package de.uni_trier.wi2.pki.util;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.io.attr.Continuously;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public static DecisionTreeNode createTree(LinkedList<CSVAttribute[]> examples, int labelIndex, int a) {        // changed collection to linked list
        if (examples.size() == 1) return null;                          // Rekursionsanker

        List<Double> entropyList = EntropyUtils.calcInformationGain(examples, labelIndex);          // calculate entropy gain for all attributes
        int attributeIndex = 0;

        for (int i = 1; i < entropyList.size(); i++) 
            if (entropyList.get(i) > entropyList.get(attributeIndex))
                attributeIndex = i; 

        DecisionTreeNode curNode = new DecisionTreeNode(1);           // TODO set Index
        int numBuckets = getBuckets(examples.get(attributeIndex));

        examples.remove(attributeIndex);

        System.out.println("Start");
        for (int i = 0; i < numBuckets; i++) {
            LinkedList<CSVAttribute[]> clonedList = (LinkedList<CSVAttribute[]>) examples.clone();
            System.out.println("~~> " + i + " child: " + labelIndex + " parent: " + a);
            DecisionTreeNode child = createTree(clonedList, labelIndex-1, a + 1);
            if (child == null) continue;
            child.setParent(curNode);
            curNode.splits.put(String.valueOf(i), child);                 // TODO Key Index
        }

        System.out.println("ENDE\n");
        return curNode;
    }

    // finding all unique values of attribute with best entropy gain
    public static int getBuckets(CSVAttribute[] array) { 
        return (array[0] instanceof Continuously)? 
        Stream.of(array).collect(Collectors.toMap(CSVAttribute::getCategory, p -> p, (p, q) -> p)).values().size():                                     // TODO return number of bins is hardcoded to 5
        Stream.of(array).collect(Collectors.toMap(CSVAttribute::getValue, p -> p, (p, q) -> p)).values().size();
    } 
}

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
