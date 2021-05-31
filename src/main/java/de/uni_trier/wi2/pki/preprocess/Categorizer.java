package de.uni_trier.wi2.pki.preprocess;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.io.attr.Categoric;
import de.uni_trier.wi2.pki.io.attr.Continuously;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("rawtypes")

public class Categorizer {
    public static List<CSVAttribute[]> categorize(List<String[]> linkedList){   
        // Switch from rows to colummns
        List<List<String>> a = IntStream.range(0, linkedList.get(0).length).mapToObj(x -> linkedList.stream().map(y -> y[x]).collect(Collectors.toList())).collect(Collectors.toList()); // TODO change position

        // Check what is Categoric
        Boolean[] b = a.stream().map(x -> isCategoric(x.stream().distinct().collect(Collectors.toList()))).toArray(Boolean[]::new);

        // Create every attribute from every row to CSVAttribute
        List<CSVAttribute[]> c = linkedList.stream().map(x -> IntStream.range(0, x.length).mapToObj(y -> (b[y])? new Categoric(x[y]) : new Continuously(x[y])).toArray(CSVAttribute[]::new)).collect(Collectors.toList());


        for (int i = 0; i < linkedList.get(0).length; i++) 
            if (c.get(0)[i] instanceof Continuously)
                BinningDiscretizer.discretize(3, c, i);

        return c;
    }

    // checks if the column is categoric
    public static boolean isCategoric(List<String> list) {
        try {
            Double.parseDouble(list.get(0));
        } catch (Exception e) {
            return true;
        }
        return list.size() == 2;
    }
}