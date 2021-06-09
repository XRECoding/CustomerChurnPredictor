package de.uni_trier.wi2.pki.postprocess;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;
import java.util.Collection;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

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
     * @return  The root of the best decision tree.
     */
    public static DecisionTreeNode performCrossValidation(List<CSVAttribute[]> dataset, int labelAttribute, BiFunction<List<CSVAttribute[]>, Integer, DecisionTreeNode> trainFunction, int numFolds) {
        int range = dataset.size() / numFolds;                  // calculate the size of one fold

        DecisionTreeNode bestTree = null;
        Double bestAccuracy = 0.0;


        for (int i = 0; i < numFolds; i++) {
            int startIndex = range * i;                         
            int endIndex = range * (i+1);

            if (i == numFolds-1) endIndex = dataset.size();         // puts the few leftover elements into the last fold
            
            List<CSVAttribute[]> trainingData = dataset.subList(0, startIndex);             // Create left sublist
            trainingData.addAll(dataset.subList(endIndex, dataset.size()));                 // Add right sublist to left sublist
            DecisionTreeNode root = trainFunction.apply(trainingData, labelAttribute);      // train the tree


            double currentAccuracy = calculateAccuracy(root, dataset.subList(startIndex, endIndex), labelAttribute);        // calculate the accuracy of the tree; the sublist equals the testData fold
            if (currentAccuracy > bestAccuracy){        // if the accuracy is greater than the max accuracy
                bestTree = root;                        // save the current tree as the new max and
                bestAccuracy = currentAccuracy;         // save the current accuracy as the new max
            }

        }

        return bestTree;
    }

    /**
     * Performs a cross-validation with the specified dataset and the function to train the model.
     *
     * @param root                  the decision tree root.
     * @param validationExamples    the test dataset.
     * @param labelAttributeId      the label attribute.
     * @return  The calculated accuracy of the tree.
     */
    public static double calculateAccuracy(DecisionTreeNode root, Collection<CSVAttribute[]> validationExamples, int labelAttributeId){
        double correctClassification = 0;

        for (CSVAttribute[] array : validationExamples){        // for every data entry
            if (array[labelAttributeId].getCategory().toString().equals(CrossValidator.consultTree(root, array))){      // compare if the resulting label of the tree is equal to the given label

                correctClassification++;
            }
        }

        return correctClassification / validationExamples.size();
    }


    /**
     * Performs a cross-validation with the specified dataset and the function to train the model.
     *
     * @param root                  the decision tree root.
     * @param array                 the entry to test.
     * @return  The resulting label for the entry.
     */
    public static String consultTree(DecisionTreeNode root, CSVAttribute[] array) {
        int attributeID = root.getAttributeIndex();
        DecisionTreeNode child = root.getSplits().get(array[attributeID].getCategory().toString());

        if (child != null) return consultTree(child, array);
        return root.getSplits().entrySet().iterator().next().getKey();

    }
}