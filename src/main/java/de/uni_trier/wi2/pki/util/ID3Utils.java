package de.uni_trier.wi2.pki.util;


import java.util.*;
import java.util.stream.Collectors;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import de.uni_trier.wi2.pki.postprocess.ReducedErrorPruner;
import javax.xml.parsers.ParserConfigurationException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;
import static de.uni_trier.wi2.pki.Main.intervalSizes;

@SuppressWarnings("rawtypes")

/**
 * Utility class for creating a decision tree with the ID3 algorithm.
 */
public class ID3Utils {

    /**
     * Create the decision tree given the example and the index of the label attribute.
     *
     * @param examples   The examples to train with. This is a collection of arrays.
     * @param labelIndex The label of the attribute that should be used as an index.
     * @return The parent node of the decision tree
     */
    public static DecisionTreeNode createTree(List<CSVAttribute[]> examples, int labelIndex) {
        return treeUtils(examples, labelIndex, new boolean[examples.get(0).length]);
    }

    /**
     * Create the decision tree given the example and the index of the label attribute.
     *
     * @param examples   The examples to train with. This is a collection of arrays.
     * @param labelIndex The label of the attribute that should be used as an index.
     * @param visited    An array which keeps track which attribute has been used for this branch.
     * @return The parent node of the subtree.
     */
    public static DecisionTreeNode treeUtils(List<CSVAttribute[]> examples, int labelIndex, boolean visited[]) {
        // <------------------- calculate gain for all attributes and find best gain ------------------->
        List<Double> entropyList = EntropyUtils.calcInformationGain(examples, labelIndex);
        int bestIndex = -1;


        for (int i = 0; i < entropyList.size(); i++) {
            if (visited[i] || i == labelIndex) continue;        // skip if the attribute has been used before or is the label attribute
            if (bestIndex == -1) bestIndex = i;
            else if (entropyList.get(bestIndex) < entropyList.get(i))      
                bestIndex = i;                                                        
        }

        visited[bestIndex] = true;      // set the visited boolean for the attribute to true

        // <------------------- create branches using the best attribute ------------------->
        DecisionTreeNode parent = new DecisionTreeNode(bestIndex);                                // Create new node with the used attributeID
        if (moreOptions(visited, labelIndex) && entropyList.get(bestIndex) != 0.0) {                            // TODO
            for (Map.Entry<String, List<CSVAttribute[]>> entry : splitData(examples, bestIndex).entrySet()) {   // for every possible category of the attribute
                DecisionTreeNode child = treeUtils(entry.getValue(), labelIndex, visited.clone());              // create child with recursive call

                parent.getSplits().put(entry.getKey(), child);          // add the child to the parent.splits
                child.setParent(parent);                                // add the parent reference to the child
            }
            return parent;
        } 
        parent.getSplits().put(getMajority(examples,labelIndex), null);     // add leaf with majority label

        return parent;
    }

////////////////////// Helper Functions //////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns all unique values in a list at a given index.
     *
     * @param examples      The examples to use. This is a list of arrays.
     * @param index         The index of the attribute to use.
     * @return The list containing all unique values.
     */
    public  static List<String> getDistinct(List<CSVAttribute[]> examples, int index) {
        return examples.stream().filter(x -> x[index] != null).map(x -> x[index].getCategory().toString())
            .distinct().collect(Collectors.toList());
    }

    /**
     * Returns the majority label of the dataset.
     *
     * @param data      The dataset. This is a list of arrays.
     * @param keyIndex  The index of the label attribute.
     * @return The majority label as a String.
     */
    public static String getMajority(List<CSVAttribute[]> data, int keyIndex) {
        Map<String, Integer> map = new HashMap<>();     // initialize hashMap

        data.stream().forEach(x -> {        // count the labels
            String key = x[keyIndex].getCategory().toString();
            if (map.get(key) == null) map.put(key, 1);
            else map.put(key, map.get(key)+1);
        });

        return Collections.max(map.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();  // return the label with highest count
    }


    /**
     * Returns a map. Keys are the unique values of the used attribute. Values are the resulting data subsets.
     *
     * @param data      The entire dataset. This is a list of arrays.
     * @param index     The index of the attribute to use.
     * @return The Map with unique values mapped to their data subset.
     */
    public static Map<String, List<CSVAttribute[]>> splitData(List<CSVAttribute[]> data, int index) {
        List<String> buckets = getDistinct(data, index);            //
        
        Map<String, List<CSVAttribute[]>> map = new HashMap<>();
        for (String bucket : buckets) map.put(bucket, new LinkedList<>());

        data.stream().forEach(array -> map.get(array[index]
            .getCategory().toString()).add(array));
        return map;
    }

    public static boolean moreOptions(boolean array[], int index) {
        for (int i = 0; i < array.length; i++)
            if (i == index) continue;           // TODO is this necesarry? we set array[index] = true in line 46
            else if (array[i] == false) return true; 
        return false;
    }

}

