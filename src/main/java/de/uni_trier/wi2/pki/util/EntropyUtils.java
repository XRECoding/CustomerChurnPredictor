package de.uni_trier.wi2.pki.util;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")

public class EntropyUtils {

    /**
     * Create the decision tree given the example and the index of the label attribute.
     *
     * @param matrix        The examples to calculate the information gain with. This is a list of arrays.
     * @param labelIndex    The index of the label attribute.
     * @return A list with the information gain for each attribute.
     */
    public static List<Double> calcInformationGain(List<CSVAttribute[]> matrix, int labelIndex) { 
        Map<Integer, Map<String, Map<String, Integer>>> map = new HashMap<>();              // Create Map with reference
        for (int i = 0; i < matrix.get(0).length; i++) map.put(i, new HashMap<>());         // Initialize for all Attributes


        matrix.stream().forEach(array -> {                                                  // Stream over matrix to fill map
            for (int i = 0; i < array.length; i++) {                                        // Iterate over each entry
                String bucket = array[i].getCategory().toString();                          // Create reference with the category    ~> Interval
                String key = array[labelIndex].getCategory().toString();                    // Create reference with the labelindex  ~> 1/0

                if (map.get(i).get(bucket) == null)                                         // Create reference if there is none
                    map.get(i).put(bucket, new HashMap<>());                            
                if (map.get(i).get(bucket).get(key) == null)                                // Create a start point if there is none
                    map.get(i).get(bucket).put(key, 1);                                     // Start counting with
                else
                    map.get(i).get(bucket).put(key, map.get(i).get(bucket).get(key)+1);     // Count all keys (~> 1/0) for each bucket
            } 
        });


        Double r = HE(map.get(labelIndex).values().stream().map(x -> x.entrySet()           // Stream over the map with labelindex
            .iterator().next().getValue().intValue()).collect(Collectors.toList()));        // Compute H(E)

        return map.entrySet().stream().filter(x -> (x.getKey() != labelIndex))              // Stream over map and every bucket
            .mapToDouble(x -> r - x.getValue().entrySet().stream().mapToDouble(y ->         // Compute H(Ei) and R(A) for each bucket
            H(y.getValue(), matrix.size())).sum()).boxed().collect(Collectors.toList());    // return a List with the entropy
    }


////////////////////////// Helper Functions ////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Calculates Entropy
     *
     * @param l        List containing the #occurrence per label.
     * @return Returns the entropy.
     */
    public static Double HE(List<Integer> l) {
        Double count = Double.valueOf(l.stream().reduce(0, Integer::sum));                  // Get the sum of all attributes
        return l.stream().mapToDouble(x -> -x/count*log2(x/count)).sum();                   // Return H(E) for the labelindex
    }

    /**
     * Calculates rest Entropy per interval
     *
     * @param map      A map containing the #occurance per label for the given interval.
     * @param n        The total entries in this interval.
     * @return Returns the rest entropy for the given interval.
     */
    public static Double H(Map<String, Integer> map, int n) {
        Double count = Double.valueOf(map.values().stream().reduce(0, Integer::sum));       // Get the sum of all attributes
        Double h = map.values().stream().mapToDouble(x -> -x/count*log2(x/count)).sum();    // Compute H(e) for each e e bucket
        return (count/n) * h;                                                               // return R(A) per interval
    }

    /**
     * Calculates the logarithm to the base of 2
     *
     * @param value      The value to calculate the log2 for.
     * @return Returns the log2(x).
     */
    public static double log2(double value) {
        return (Math.log(value) / Math.log(2));
    }
}