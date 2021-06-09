package de.uni_trier.wi2.pki;

import de.uni_trier.wi2.pki.io.CSVReader;
import de.uni_trier.wi2.pki.io.XMLWriter;
import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.postprocess.CrossValidator;
import de.uni_trier.wi2.pki.postprocess.ReducedErrorPruner;
import de.uni_trier.wi2.pki.preprocess.Categorizer;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;
import de.uni_trier.wi2.pki.util.ID3Utils;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")

public class Main {
    public static double[] intervalSizes;

    public static void main(String[] args) {
        try {
            // reading input data
            List<String[]> linkedList = CSVReader.readCsvToArray("churn_data.csv", ";", true);

            // initializing intervalSize array
            intervalSizes = new double[linkedList.size()];

            // categorizing input data into CSVAttributes
            List<CSVAttribute[]> newList = Categorizer.categorize(linkedList, 2);

            // splitting data into training data (80%) and test data (20%)
            List<CSVAttribute[]> trainingData = newList.subList(0, (int) Math.round(newList.size()*0.8));
            List<CSVAttribute[]> testData = new LinkedList<>(newList.subList((int) Math.round(newList.size()*0.8), newList.size()));

            // create tree via CrossValidation
            DecisionTreeNode root = CrossValidator.performCrossValidation(trainingData, 10, ID3Utils::createTree,5);
            double finalAccuracy = CrossValidator.calculateAccuracy(root, testData, 10);
            System.out.println("The learned tree has an accuracy of " + finalAccuracy);

            // writing the resulting decision tree into xml
            XMLWriter.writeXML("src/main/resources/LearnedDecisionTree.xml", root);

            // prune the tree
            ReducedErrorPruner reducedErrorPruner = new ReducedErrorPruner();
            reducedErrorPruner.prune(root, testData, 10);

            // writing the resulting decision tree into xml
            XMLWriter.writeXML("src/main/resources/PrunedDecisionTree.xml", root);

            // calculate and print final accuracy
            finalAccuracy = CrossValidator.calculateAccuracy(root, testData, 10);
            System.out.println("The pruned tree has an accuracy of " + finalAccuracy);


        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }
}
