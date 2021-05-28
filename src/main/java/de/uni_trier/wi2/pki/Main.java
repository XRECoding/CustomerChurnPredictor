package de.uni_trier.wi2.pki;

import de.uni_trier.wi2.pki.io.CSVReader;
import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.preprocess.Categorizer;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;
import de.uni_trier.wi2.pki.util.EntropyUtils;
import de.uni_trier.wi2.pki.util.ID3Utils;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("rawtypes")

public class Main {

    public static void main(String[] args) {
        // TODO: this should be the main executable method for your project

        try {
            // reading input data
            List<String[]> linkedList = CSVReader.readCsvToArray("churn_data.csv", ";", true);
            // categorizing input data into CSVAttributes
            LinkedList<CSVAttribute[]> newList = Categorizer.categorize(linkedList);

            List<Double> entropyList = EntropyUtils.calcInformationGain(newList, 10);

//            DecisionTreeNode root = ID3Utils.createTree(newList, 10);

//            ID3Utils.printTree(root);


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

}
