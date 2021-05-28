package de.uni_trier.wi2.pki.util;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;

import java.util.*;

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
    public static DecisionTreeNode createTree(LinkedList<CSVAttribute[]> examples, int labelIndex) {

        int attributesLeft = examples.size();
        DecisionTreeNode newNode = new DecisionTreeNode(1);

        switch (attributesLeft){
            case 1:
                return null;        // recursion base case;
            case 2:
//                System.out.println("leaf");
                CSVAttribute[] classArray = examples.get(labelIndex);
                int count = 0;
                for (int i = 0; i < classArray.length; i++) {
                    if (classArray[i].getCategory().equals("1")){
                        count++;
                    }
                }
                examples.remove(0);
//                System.out.println(count + " " + classArray.length);
                if (count > classArray.length){
                    newNode.setClassifier(1);
                }else {
                    newNode.setClassifier(0);
                }
                return newNode;
            default:
//                System.out.println("not leaf");
                List<Double> entropyList = EntropyUtils.calcInformationGain(examples, labelIndex);
                int attributeIndex = 0;
                double max = entropyList.get(0);
                for (int i = 1; i < entropyList.size(); i++) {
                    if (max < entropyList.get(i)){
                        max = entropyList.get(i);
                        attributeIndex = i;
                    }
                }

                LinkedList<CSVAttribute[]> clone = (LinkedList<CSVAttribute[]>) examples.clone();
                clone.remove(attributeIndex);
                for (int i = 0; i < examples.get(attributeIndex).length; i++) {
                    if (!newNode.getSplits().containsKey(examples.get(attributeIndex)[i].getCategory().toString())){
                        DecisionTreeNode child = createTree(clone, labelIndex-1);
                        newNode.getSplits().put(examples.get(attributeIndex)[i].getCategory().toString(), child);
                    }
                }

                for (Map.Entry<String, DecisionTreeNode> child : newNode.getSplits().entrySet()) {
                    if (child.getValue() != null){
                        child.getValue().setParent(newNode);
                    }
//                        child.getValue().setParent(newNode);
                }


                return newNode;
        }
    }


    public static void printTree(DecisionTreeNode root){
        System.out.println(root.getClassifier());
        String output = "";
        System.out.println(output);
    }

}
