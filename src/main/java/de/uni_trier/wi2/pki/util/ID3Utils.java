package de.uni_trier.wi2.pki.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
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
     * @return The newNode node of the decision tree
     */
    public static DecisionTreeNode createTree(List<CSVAttribute[]> examples, int labelIndex) {
        return treeUtils(examples, labelIndex, 0, new boolean[examples.get(0).length]);
    }

    public static DecisionTreeNode treeUtils(List<CSVAttribute[]> examples, int labelIndex, int p, boolean visited[]) {
        List<Double> entropyList = EntropyUtils.calcInformationGain(examples, labelIndex);  // calculate gain for all attributes and find best gain
        int bestIndex = -1;                      


        for (int i = 0; i < entropyList.size(); i++) {
            if (visited[i] || i == labelIndex) continue;
            if (bestIndex == -1) bestIndex = i;
            else if (entropyList.get(bestIndex) < entropyList.get(i))      
                bestIndex = i;                                                        
        }
 


        visited[bestIndex] = true;


        DecisionTreeNode newNode = new DecisionTreeNode(bestIndex);     // Create new node, that has a reference to a position
        List<String> keys = getDistinct(examples, labelIndex);          // Get all the diffrent unique values on position ~labelIndex

        if (keys.size() == 1 || !moreOptions(visited, labelIndex) || entropyList.get(bestIndex) == 0.0) {                                                 // Prune branche if the rest of the branche is the same.
            newNode.getSplits().put(getMajority(examples,labelIndex), null);    // The branche is turned into a leef and gets a refrence to its key
            return newNode;                                                     // Retrun leef node
        }




        for (Map.Entry<String, List<CSVAttribute[]>> entry : splitData(examples, bestIndex).entrySet()) {
            DecisionTreeNode child = treeUtils(entry.getValue(), labelIndex, p+1, visited.clone());

            newNode.getSplits().put(entry.getKey(), child);
            child.setParent(newNode);
        }

        return newNode;
    }

////////////////////// Helper Functions //////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Returns all unique values in a list at a given index.
    public  static List<String> getDistinct(List<CSVAttribute[]> examples, int index) {
        return examples.stream().filter(x -> x[index] != null).map(x -> x[index].getCategory().toString())
            .distinct().collect(Collectors.toList());
    }

    public static String getMajority(List<CSVAttribute[]> data, int keyIndex) {
        Map<String, Integer> map = new HashMap<>();

        data.stream().forEach(x -> {
            String key = x[keyIndex].getCategory().toString();
            if (map.get(key) == null) map.put(key, 1);
            else map.put(key, map.get(key)+1);
        });

        return Collections.max(map.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }


    public static Map<String, List<CSVAttribute[]>> splitData(List<CSVAttribute[]> data, int index) {
        List<String> buckets = getDistinct(data, index);
        
        Map<String, List<CSVAttribute[]>> map = new HashMap<>();
        for (String bucket : buckets) map.put(bucket, new LinkedList<>());

        data.stream().forEach(array -> map.get(array[index]
            .getCategory().toString()).add(array));
        return map;
    }

    public static boolean moreOptions(boolean array[], int index) {
        for (int i = 0; i < array.length; i++)
            if (i == index) continue;
            else if (array[i] == false) return true; 
        return false;
    }

    public static void printTree(DecisionTreeNode treeRoot) {
        // create and configure outputSteam
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.getFormat().setExpandEmptyElements(true);
        xmlOutputter.getFormat().setIndent("      ");
//        xmlOutputter.getFormat().setEscapeStrategy(new EscapeStrategy() {
//            @Override
//            public boolean shouldEscape(char c) {
//                return Character.isDigit(c);
//            }
//        });

        // create document and content root
        Element root = new Element("DecisionTree");
        Document doc = new Document(root);

        Element treeRootElement = new Element("Node_With_AttributeID"+treeRoot.getAttributeIndex());
        root.addContent(treeRootElement);
        addXmlChildren(treeRoot, treeRootElement);

        // output the xml file
        try{
//            FileOutputStream fileOutputStream = new FileOutputStream("D:\\Dokumente\\test.xml");
//            xmlOutputter.output(doc, fileOutputStream);
            xmlOutputter.output(doc, System.out);

        }catch (IOException e){
            System.out.println(e.getStackTrace());
        }
    }

    public static void addXmlChildren(DecisionTreeNode root, Element jdomRoot){
        for (Map.Entry<String, DecisionTreeNode> entry : root.getSplits().entrySet()) {
            if (entry.getValue() != null){
                Element element = new Element("Node_With_AttributeID_" + String.valueOf(entry.getValue().getAttributeIndex()));
                Element ifElement;
                try{
                    ifElement = new Element("If_Value_In_Interval_"+String.valueOf(Double.valueOf(entry.getKey())*intervalSizes[root.getAttributeIndex()])+"_to_"+String.valueOf((1+Double.valueOf(entry.getKey()))*intervalSizes[root.getAttributeIndex()]));
                } catch (NumberFormatException e){
                    ifElement = new Element("If_Value_In_Interval_"+entry.getKey());
                }
                jdomRoot.addContent(ifElement);
                ifElement.addContent(element);
                addXmlChildren(entry.getValue(), ifElement);
            } else {
                Element element = new Element("Leaf_Node_Class_" + entry.getKey());
                jdomRoot.removeContent();
                jdomRoot.addContent(element);
            }

        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////
}

