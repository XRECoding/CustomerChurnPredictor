package de.uni_trier.wi2.pki;

import de.uni_trier.wi2.pki.io.CSVReader;
import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.preprocess.Categorizer;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;
import de.uni_trier.wi2.pki.util.ID3Utils;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("rawtypes")

public class Main {

    public static void main(String[] args) {
        try {
            // reading input data
            List<String[]> linkedList = CSVReader.readCsvToArray("churn_data.csv", ";", true);
            
            // categorizing input data into CSVAttributes
            List<CSVAttribute[]> newList = Categorizer.categorize(linkedList);

            // Create Tree
            DecisionTreeNode root =  ID3Utils.createTree(newList, 10);
            DFS(root);
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

    public static void DFS(DecisionTreeNode node) {
        if (node == null) return;

        System.out.println(node.getAttributeIndex());
        DFS(node.getSplits().entrySet().iterator().next().getValue());
    }
}
