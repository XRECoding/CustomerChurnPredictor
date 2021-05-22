package de.uni_trier.wi2.pki.preprocess;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.io.attr.Categoric;
import de.uni_trier.wi2.pki.io.attr.Continuously;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Categorizer {

    public static LinkedList<CSVAttribute[]> categorize(List<String[]> linkedList){
//        long start = System.currentTimeMillis();
//        System.out.println("Anfang: " + 0);

        LinkedList<CSVAttribute[]> output = new LinkedList<>();     // output list

        // making a hashmap for every column of the dataset
        Map<String,Integer>[] mapArray = new HashMap[linkedList.get(0).length];

        for (int n = 0; n < mapArray.length; n++){
            mapArray[n] = new HashMap<String,Integer>();
        }


        // count unique values for each column (for isCategoric check)
        for (String[] element : linkedList){
            for (int i = 0; i < element.length; i++){
                if (mapArray[i].get(element[i]) == null){
                    mapArray[i].put(element[i], 1);
                }else{
                    mapArray[i].put(element[i], mapArray[i].get(element[i])+1);
                }
            }
        }

//        long stop1 = System.currentTimeMillis();
//        long ausgabe = stop1 - start;
//        System.out.println("Unique values have been counted in " + ausgabe + " milliseconds");

        // check if column is categoric and create Attributes respectively
//        for (int i = 0; i < mapArray.length; i++) {
//            CSVAttribute[] array = new CSVAttribute[linkedList.size()];     // represents column
//            int index = 0;
//            if (isCategoric(mapArray[i])){
//                for (int j = 0; j < linkedList.size(); j++) {
//                    array[index] = new Categoric(linkedList.get(j)[i]);
//                    index++;
//                }
//            }else{
//                for (int j = 0; j < linkedList.size(); j++) {
//                    array[index] = new Continuously(linkedList.get(j)[i]);
//                    index++;
//                }
//            }
//
//            output.add(array);
//        }

        AtomicInteger i = new AtomicInteger(0);
        for (; i.intValue() < mapArray.length; i.incrementAndGet()) {
            CSVAttribute[] array = new CSVAttribute[linkedList.size()];
            AtomicInteger index = new AtomicInteger(0);

            if (isCategoric(mapArray[i.intValue()])){
                linkedList.stream().forEach(x -> {array[index.incrementAndGet()-1] = new Categoric(x[i.intValue()]);});
                output.add(array);
            }else{
                linkedList.stream().forEach(x -> {array[index.incrementAndGet()-1] = new Continuously(x[i.intValue()]);});
                output.add(array);
                // column is continuously and has to be discretized
                BinningDiscretizer.discretize(5, output, output.size()-1);      // TODO number of bins is hardcoded to 5
            }
        }

        CSVAttribute[] test = output.get(0);
        for (int j = 0; j < 100; j++) {
            Continuously asd = (Continuously) test[j];
            System.out.println(asd.getCategory());
        }



//        long stop2 = System.currentTimeMillis();
//        ausgabe = stop2 - stop1;
//        System.out.println("Nested for-loop done in " + ausgabe + " milliseconds");

        return output;
    }


    // checks if the column is categoric
    public static boolean isCategoric(Map<String, Integer> map) {
        try {
            for (Map.Entry<String, Integer> element : map.entrySet()) {
                Double.parseDouble(element.getKey());
                break;
            }
        } catch (Exception e) {
            return true;
        }
        return map.size() == 2;
    }
}
