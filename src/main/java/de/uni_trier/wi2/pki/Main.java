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
import java.util.List;

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
            List<CSVAttribute[]> newList = Categorizer.categorize(linkedList);

            // create tree via CrossValidation
            DecisionTreeNode root = CrossValidator.performCrossValidation(newList, 10, ID3Utils::createTree,5);

            // writing the resulting decision tree into xml
            XMLWriter.writeXML("D:\\Dokumente\\ZZZ.xml", root);

            // prune the tree
            ReducedErrorPruner reducedErrorPruner = new ReducedErrorPruner();
            reducedErrorPruner.prune(root, newList, 10);


        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

    public static void DFS(DecisionTreeNode node) {
        
        if (node.getSplits().entrySet().iterator().next().getValue() == null) {
            System.out.println(node.getSplits().entrySet().iterator().next().getKey());
            return;
        }
        System.out.println(node.getAttributeIndex());
        DFS(node.getSplits().entrySet().iterator().next().getValue());
    }

}
