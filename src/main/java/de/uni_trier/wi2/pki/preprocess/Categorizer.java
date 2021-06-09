package de.uni_trier.wi2.pki.preprocess;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.io.attr.Categoric;
import de.uni_trier.wi2.pki.io.attr.Continuously;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("rawtypes")

public class Categorizer {

    /**
     * Categorizes the input data into categoric and continously attributes.
     * Calls BinningDiscretizer.discretize for continously attributes.
     *
     * @param list            The input dataset. This is a list of string arrays.
     * @param numberOfBins    The number of bins to use when discretizing.
     * @return List of CSVAttribute arrays. Each array is a column from the original data.
     */
    public static List<CSVAttribute[]> categorize(List<String[]> list, int numberOfBins){
        // Switch from rows to colummns
        List<List<String>> columns = IntStream.range(0, list.get(0).length)
            .mapToObj(x -> list.stream().map(y -> y[x])
            .collect(Collectors.toList())).collect(Collectors.toList());

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
                BinningDiscretizer.discretize(numberOfBins, csvAttributes, i);         // Buckets

        return csvAttributes;
    }

////////////////////////// Helper Functions ////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Checks if the attribute is categoric.
     *
     * @param list     The data of the attribute to check.
     * @return  Returns a bollean. The boolean is true if the attribute is categoric.
     */
    public static boolean isCategoric(List<String> list) {
        try {
            Double.parseDouble(list.get(0));
        } catch (Exception e) {
            return true;
        }
        return list.size() == 2;
    }
}