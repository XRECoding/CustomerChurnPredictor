package de.uni_trier.wi2.pki.util;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")

public class EntropyUtils {
    public static List<Double> calcInformationGain(List<CSVAttribute[]> matrix, int labelIndex) { 
        Map<Integer, Map<String, Map<String, Integer>>> map = new HashMap<>();              // Create Map with refrences
        for (int i = 0; i < matrix.get(0).length; i++) map.put(i, new HashMap<>());         // Set refreces for all Attributes


        matrix.stream().forEach(array -> {                                                  // Stream over matrix to fill map
            for (int i = 0; i < array.length; i++) {                                        // Iterate over each entry
                String bucket = array[i].getCategory().toString();                          // Create refrence with the category    ~> Interval
                String key = array[labelIndex].getCategory().toString();                    // Create refrence with the labelindex  ~> 1/0

                if (map.get(i).get(bucket) == null)                                         // Create refrence if there is none
                    map.get(i).put(bucket, new HashMap<>());                            
                if (map.get(i).get(bucket).get(key) == null)                                // Create a startpoint if there is none
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

    public static Double HE(List<Integer> l) {
        Double count = Double.valueOf(l.stream().reduce(0, Integer::sum));                  // Get the sum of all attributes
        return l.stream().mapToDouble(x -> -x/count*log2(x/count)).sum();                   // Return H(E) for the labelindex
    }

    public static Double H(Map<String, Integer> map, int n) {
        Double count = Double.valueOf(map.values().stream().reduce(0, Integer::sum));       // Get the sum of all attributes
        Double h = map.values().stream().mapToDouble(x -> -x/count*log2(x/count)).sum();    // Compute H(e) for each e e bucket
        return (count/n) * h;                                                               // return R(A)
    }

    public static double log2(double value) {
        return (Math.log(value) / Math.log(2));                                             // returns the log to the base of 2
    }
}

// TODO Algorithms is not able to work with "divide by zero". ~> Unsure if the Algorithm knows what to do when multiplying by zero