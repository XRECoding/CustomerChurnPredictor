package de.uni_trier.wi2.pki.preprocess;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.io.attr.Categoric;
import de.uni_trier.wi2.pki.io.attr.Continuously;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("rawtypes")

public class Categorizer {
    public static List<CSVAttribute[]> categorize(List<String[]> list){   
        // Switch from rows to colummns
        List<List<String>> columns = IntStream.range(0, list.get(0).length)
            .mapToObj(x -> list.stream().map(y -> y[x])
            .collect(Collectors.toList())).collect(Collectors.toList());    // TODO change position

        // Check what is Categoric
        Boolean[] csvBooleans = columns.stream().map(x -> isCategoric(x.stream()
            .distinct().collect(Collectors.toList()))).toArray(Boolean[]::new);

        // Create every attribute from every row to CSVAttribute
        List<CSVAttribute[]> csvAttributes = list.stream().map(x -> IntStream.range(0, x.length)
            .mapToObj(y -> (csvBooleans[y])? new Categoric(x[y]) : new Continuously(x[y]))
            .toArray(CSVAttribute[]::new)).collect(Collectors.toList());

        // Set Buckets for each continuously attribute
        for (int i = 0; i < list.get(0).length; i++) 
            if (csvAttributes.get(0)[i] instanceof Continuously)
                BinningDiscretizer.discretize(2, csvAttributes, i);         // Buckets

        return csvAttributes;
    }

////////////////////////// Helper Functions ////////////////////////////////////////////////////////////////////////////////////////////////////////

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