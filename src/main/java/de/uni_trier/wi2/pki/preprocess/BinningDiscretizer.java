package de.uni_trier.wi2.pki.preprocess;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.io.attr.Continuously;
import java.util.LinkedList;

@SuppressWarnings({"rawtypes", "unchecked"})

/**
 * Class that holds logic for discretizing values.
 */
public class BinningDiscretizer {

    /**
     * Discretizes a collection of examples according to the number of bins and the respective attribute ID.
     *
     * @param numberOfBins Specifies the number of numeric ranges that the data will be split up in.
     * @param examples     The list of examples to discretize.
     * @param attributeId  The ID of the attribute to discretize.
     * @return the list of discretized examples.
     */
    public static LinkedList<CSVAttribute[]> discretize(int numberOfBins, LinkedList<CSVAttribute[]> examples, int attributeId) {
        CSVAttribute[] array = examples.get(attributeId);
        CSVAttribute min = array[0];
        CSVAttribute max = array[0];

        // Looping through the attribute that should be discretized, finding min and max
        for (int i = 1; i < array.length; i++) {
            min = (min.compareTo(array[i]) == -1)? array[i] : min;
            max = (max.compareTo(array[i]) == 1)? array[i] : max;
        }

        // Calculating interval size
        double minValue = Double.parseDouble(min.getValue().toString());
        double maxValue = Double.parseDouble(max.getValue().toString());

        double intervalSize =  (maxValue - minValue)/numberOfBins;

        // classify
        for (int i = 0; i < array.length; i++) {
            Continuously object = (Continuously) array[i];
            double value = Double.parseDouble(object.getValue().toString());
            int category = (int) ((value-minValue) / (intervalSize + 1));
            object.setCategory(category);
        }

        return examples;
    }
}