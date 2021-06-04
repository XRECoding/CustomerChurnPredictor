package de.uni_trier.wi2.pki.postprocess;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

@SuppressWarnings("rawtypes")

/**
 * Contains util methods for performing a cross-validation.
 */
public class CrossAnpassung {

    /**
     * Performs a cross-validation with the specified dataset and the function to train the model.
     *
     * @param dataset        the complete dataset to use.
     * @param labelAttribute the label attribute.
     * @param trainFunction  the function to train the model with.
     * @param numFolds       the number of data folds.
     */
    public static DecisionTreeNode performCrossValidation(List<CSVAttribute[]> dataset, int labelAttribute, BiFunction<List<CSVAttribute[]>, Integer, DecisionTreeNode> trainFunction, int numFolds) {
        int range = dataset.size() / numFolds;                  // calculate the size of one fold
        DecisionTreeNode currTree = null;
        Double currAccuracy = 0.0;

        for (int i = 0; i < numFolds; i++) {
            int startIndex = range * i;                         
            int endIndex = range * (i+1);
            if (i == numFolds-1) endIndex = dataset.size();
            
            List<CSVAttribute[]> trainingData = dataset.subList(0, startIndex);             // Create left sublist
            trainingData.addAll(dataset.subList(endIndex, dataset.size()));                 // Add right sublist to left sublist
            DecisionTreeNode newTree = trainFunction.apply(trainingData, labelAttribute);


            LinkedList<String> resultList = new LinkedList<>();
            for (CSVAttribute[] entry : dataset.subList(startIndex, endIndex)) {
                resultList.add(consultTree(newTree, entry));
            }
            
            int iterator = startIndex;
            double correctClassification = 0;
            for (String result : resultList) {
                if (result.equals(dataset.get(iterator)[labelAttribute].getCategory().toString())) {
                    correctClassification++;
                }
                iterator++;
            }


            double sampleSize = endIndex-startIndex;           // used to calculate newAccuracy later on
            double newAccuracy = correctClassification / sampleSize;
            currTree = (newAccuracy > currAccuracy)? newTree : currTree;
        }        
        return currTree;
    }


    public static String consultTree(DecisionTreeNode node, CSVAttribute[] array) {
        int attributeID = node.getAttributeIndex();
        DecisionTreeNode child = node.getSplits().get(array[attributeID].getCategory().toString());

        if (child != null) return consultTree(child, array);
        return node.getSplits().entrySet().iterator().next().getKey();
    }
}