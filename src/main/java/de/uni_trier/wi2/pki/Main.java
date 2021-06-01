package de.uni_trier.wi2.pki;

import de.uni_trier.wi2.pki.io.CSVReader;
import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.preprocess.BinningDiscretizer;
import de.uni_trier.wi2.pki.preprocess.Categorizer;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;
import de.uni_trier.wi2.pki.util.EntropyUtils;
import de.uni_trier.wi2.pki.util.ID3Utils;
import de.uni_trier.wi2.pki.util.newTest;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("rawtypes")

public class Main {

    public static void main(String[] args) {
        // TODO: this should be the main executable method for your project

        try {
            // reading input data
            List<String[]> linkedList = CSVReader.readCsvToArray("churn_data.csv", ";", true);
            
            // categorizing input data into CSVAttributes
            
            List<CSVAttribute[]> newList = Categorizer.categorize(linkedList);
            EntropyUtils.calcInformationGain(newList, 10).forEach(System.out::println);
            System.out.println();
            newTest.calcInformationGain(newList, 10).forEach(System.out::println);
            DecisionTreeNode root =  ID3Utils.createTree(newList, 10);
            DFS(root);
            




            //BinningDiscretizer.discretize(numberOfBins, examples, attributeId)

            //ID3Utils.createTree(newList, 10);



        }catch (IOException e){
            System.out.println(e.getStackTrace());
        }


        /*
            1. CSVReader
            2. BinningDiscretizer
            3. construct tree
            4. crossValidator
            5. Pruning
         */

    }

    public static void DFS(DecisionTreeNode node) {
        if (node == null) return;

        System.out.println(node.getAttributeIndex());
        DFS(node.splits.entrySet().iterator().next().getValue());
    }
}
