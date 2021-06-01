package de.uni_trier.wi2.pki.util;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("rawtypes")

/**
 * Contains methods that help with computing the entropy.
 */

public class test {

    /**
     * Calculates the information gain for all attributes
     *
     * @param matrix     Matrix of the training data (example data), e.g. ArrayList<String[]>
     * @param labelIndex the index of the attribute that contains the class. If the dataset is [Temperature,Weather,PlayFootball] and you want to predict playing
     *                   football, than labelIndex is 2
     * @return the information gain for each attribute
     */

    public static List<Double> calcInformationGain(List<CSVAttribute[]> matrix, int labelIndex) {     // changed Collection to LinkedList
         
        // Integer a[] = countInstance(matrix.get(1)).entrySet().stream().mapToInt(x ->  Integer.valueOf(x.getValue().toString())).boxed().toArray(Integer[]::new);
        sortInstances(matrix.get(1)).entrySet().forEach(x -> System.out.println(x.getValue().size()));

        /*
        IntStream.range(0, examples.get(index).length).filter(i -> examples.get(index)[i].equals(a))
        .mapToObj(i -> examples.get(index)[i]).toArray();
        */
        sortToIndex(matrix.get(1), matrix.get(labelIndex)).entrySet().forEach(x -> System.out.println("KEY: " + x.getKey() + " | Value" + x.getValue().size()));
        /*
        IntStream.range(0, examples.get(index).length).filter(i -> examples.get(index)[i].equals(a))
        .mapToObj(i -> examples.get(index)[i]).toArray();
        */
        
        for (CSVAttribute csvAttributes : getDistinct(matrix.get(1))) {
             Stream.of(matrix.get(1)).filter(x -> x.getCategory().equals(csvAttributes.getCategory())).forEach(x -> System.out.println(x.getCategory()));;
            //System.out.println(a.length);
            System.out.println(csvAttributes.getCategory());
            
        }

        /*
        for (Integer integer : a) {
            System.out.println("a " + integer);
        }
        System.out.println("H" + H(1, 1,1));
        countInstance(matrix.get(2)).entrySet().stream().forEach(System.out::println);
        */

        return null;
    }

////////////////////////// Helper Functions ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Map<Object, List<CSVAttribute>> sortInstances(CSVAttribute[] array) {
        return Stream.of(array).collect(Collectors.groupingBy(CSVAttribute::getCategory, Collectors.toList()));
    }

    public static Map<Object, List<CSVAttribute>> sortToIndex(CSVAttribute[] array, CSVAttribute[] index) {
        AtomicInteger counter = new AtomicInteger(-1);
        return Stream.of(array).collect(Collectors.groupingBy(x -> index[counter.incrementAndGet()].getCategory(), Collectors.toList()));
    }

    public static Collection<CSVAttribute> getDistinct(CSVAttribute[] array) {
        return Stream.of(array).collect(Collectors.toMap(CSVAttribute::getCategory, p -> p, (p, q) -> p)).values();
    }

    public static Double H(Integer... l) {
        Double count = Double.valueOf(Stream.of(l).reduce(0, Integer::sum));     // Get the sum of all attributes
        return Stream.of(l).mapToDouble(x -> -x/count*log2(x/count)).sum();      // Return the sum for each H(x) from args
    }

    public static double log2(double value) {
        return (Math.log(value) / Math.log(2));     // returns the log to the base of 2
    }
}