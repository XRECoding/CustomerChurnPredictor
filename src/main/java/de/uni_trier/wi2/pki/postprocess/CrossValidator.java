package de.uni_trier.wi2.pki.postprocess;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;
import de.uni_trier.wi2.pki.util.ID3Utils;

import java.sql.Date;
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

        for (int i = 0; i < numFolds; i++) {
            int startIndex = range * i;                         
            int endIndex = range * (i+1);
            if (i == numFolds-1) endIndex = dataset.size();
            List<CSVAttribute[]> trainingData = Stream.concat(dataset.subList(0, startIndex).stream(), dataset.subList(endIndex+1, dataset.size()).stream()).collect(Collectors.toList());
            DecisionTreeNode root = trainFunction.apply(trainingData, labelAttribute);
            for (CSVAttribute[] entry : dataset.subList(startIndex, endIndex))
                DFS(root, entry);
        }
        
        return null;
    }

    public static String DFS(DecisionTreeNode node, CSVAttribute[] array) {
        if (node.getSplits().entrySet().iterator().next().getValue() == null) {
            return node.getSplits().entrySet().iterator().next().getKey();
        }

        for (Map.Entry<String, DecisionTreeNode> entry : node.getSplits().entrySet()) {
            int attribute = entry.getValue().getAttributeIndex();
            if (array[attribute].getCategory() == entry.getKey())
                return DFS(entry.getValue(), array);
        }

        return null;
    }
}
