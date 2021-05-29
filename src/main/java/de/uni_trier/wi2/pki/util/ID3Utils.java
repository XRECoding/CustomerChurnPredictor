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
    public static DecisionTreeNode createTree(List<CSVAttribute[]> examples, int labelIndex) {        // changed collection to linked list
        if (examples.size() == 0) return null;

        // calculate gain for all attributes and find best gain
        List<Double> entropyList = EntropyUtils.calcInformationGain(examples, labelIndex);
        double max = entropyList.get(0);
        int attributeIndex = 0;

        for (int i = 1; i < entropyList.size(); i++) {
            if (max < entropyList.get(i)){
                max = entropyList.get(i);
                attributeIndex = i;
            }
        }

        DecisionTreeNode root = new DecisionTreeNode(attributeIndex);

        List<String> hashMap = examples.stream().map(x -> x[labelIndex].getCategory().toString()).distinct().toList();
        System.out.println(hashMap);
        /*
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i < examples.size(); i++) {
            hashMap.put(examples.get(i)[labelIndex].getCategory().toString(), "");
        }
        */

        if (hashMap.size() == 1){   // all rows have the same class
            // root.getSplits().put((hashMap.iterator().next()), null); Faster
            if (examples.get(0)[labelIndex].getCategory().toString().equals("1")){
                root.getSplits().put("+", null);
            } else {
                root.getSplits().put("-", null);
            }

            return root;
        }
        int rama = attributeIndex;
        List<String> intervalMap = examples.stream().map(x -> x[rama].getCategory().toString()).distinct().toList();
        //System.out.println(intervalMap);
        
        /*
        HashMap<String, String> intervalMap = new HashMap<>();
        for (int i = 0; i < examples.size(); i++) {
            intervalMap.put(examples.get(i)[attributeIndex].getCategory().toString(), "");
        }
        */

        //for (Map.Entry<String, String> entry : intervalMap.entrySet()) {
        for (String object : intervalMap) {
            int count = attributeIndex;
            List<CSVAttribute[]> clone = examples.stream().filter(x -> !x[count].getCategory().toString().equals(object)).collect(Collectors.toList());

            /*
            DecisionTreeNode child = createTree(clone, labelIndex);
            root.getSplits().put(object, child);
            if (child == null) continue;
            child.setParent(root);
            */
        }

        return root;
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
