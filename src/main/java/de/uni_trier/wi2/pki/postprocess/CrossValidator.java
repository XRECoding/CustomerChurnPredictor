package de.uni_trier.wi2.pki.postprocess;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;
import de.uni_trier.wi2.pki.util.ID3Utils;

import java.sql.Date;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("rawtypes")

/**
 * Contains util methods for performing a cross-validation.
 */
public class CrossValidator {

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

        double[] accuracyArray = new double[numFolds];

        for (int i = 0; i < numFolds; i++) {
            int startIndex = range * i;                         
            int endIndex = range * (i+1);
            if (i == numFolds-1) endIndex = dataset.size();
            double sampleSize = endIndex-startIndex;           // used to calculate accuracy later on

            List<CSVAttribute[]> trainingData = Stream.concat(dataset.subList(0, startIndex).stream(), dataset.subList(endIndex, dataset.size()).stream()).collect(Collectors.toList());

            DecisionTreeNode root = trainFunction.apply(trainingData, labelAttribute);


            LinkedList<String> resultList = new LinkedList<>();
            for (CSVAttribute[] entry : dataset.subList(startIndex, endIndex)){
                resultList.add(consultTree(root, entry));
            }


            double correctClassification = 0;
            for (String result : resultList) {
                if (result.equals(dataset.get(startIndex)[labelAttribute].getCategory().toString())){
                    correctClassification++;
                }
                startIndex++;
            }

            double accuracy = correctClassification / sampleSize;
            accuracyArray[i] = accuracy;
        }

        double finalAccuracy = Arrays.stream(accuracyArray).average().orElse(Double.NaN);
        System.out.println("The learned DecisionTree has an accuracy of " +  finalAccuracy);
        
        return null;
    }

    public static String consultTree(DecisionTreeNode node, CSVAttribute[] array) {
        int attributeID = node.getAttributeIndex();
        DecisionTreeNode child = node.getSplits().get(array[attributeID].getCategory().toString());

        if (child != null) return consultTree(child, array);
        return node.getSplits().entrySet().iterator().next().getKey();
    }
}
