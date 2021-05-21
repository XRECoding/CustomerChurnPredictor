package de.uni_trier.wi2.pki;

import de.uni_trier.wi2.pki.io.CSVReader;
import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.io.attr.Categoric;
import de.uni_trier.wi2.pki.preprocess.BinningDiscretizer;
import de.uni_trier.wi2.pki.preprocess.Categorizer;
import de.uni_trier.wi2.pki.util.EntropyUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // TODO: this should be the main executable method for your project


        try {
            // reading input data
            List<String[]> linkedList = CSVReader.readCsvToArray("churn_data.csv", ";", true);
            // categorizing input data into CSVAttributes
            LinkedList<CSVAttribute[]> newList = Categorizer.categorize(linkedList);
            // discretize data
            newList = BinningDiscretizer.discretize(5, newList, 0);
            newList = BinningDiscretizer.discretize(5, newList, 3);
            newList = BinningDiscretizer.discretize(5, newList, 4);
            newList = BinningDiscretizer.discretize(5, newList, 5);
            newList = BinningDiscretizer.discretize(5, newList, 6);
            newList = BinningDiscretizer.discretize(5, newList, 9);

            EntropyUtils.calcInformationGain(newList, 10);
            HashMap<String, Integer> hashMap = new HashMap();

//            for (int i = 0; i < 11; i++) {
//                for (int j = 0; j < linkedList.size(); j++) {
//                    hashMap.put(linkedList.get(j)[i], 0);
//                }
//                System.out.println(hashMap.size());
//                hashMap = new HashMap<>();
//            }
//            System.out.println("____\n" + linkedList.size());


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
