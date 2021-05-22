package de.uni_trier.wi2.pki;

import de.uni_trier.wi2.pki.io.CSVReader;
import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.preprocess.BinningDiscretizer;
import de.uni_trier.wi2.pki.preprocess.Categorizer;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // TODO: this should be the main executable method for your project

        try {
            // reading input data
            List<String[]> linkedList = CSVReader.readCsvToArray("churn_data.csv", ";", true);
            // categorizing input data into CSVAttributes
            LinkedList<CSVAttribute[]> newList = Categorizer.categorize(linkedList);
            // discretize data
//            newList = BinningDiscretizer.discretize(5, newList, 0);
//            newList = BinningDiscretizer.discretize(5, newList, 3);
//            newList = BinningDiscretizer.discretize(5, newList, 4);
//            newList = BinningDiscretizer.discretize(5, newList, 5);
//            newList = BinningDiscretizer.discretize(5, newList, 6);
//            newList = BinningDiscretizer.discretize(5, newList, 9);

//            for (CSVAttribute[] array : newList) {
//                System.out.println(array[9762].getValue());
//            }


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
