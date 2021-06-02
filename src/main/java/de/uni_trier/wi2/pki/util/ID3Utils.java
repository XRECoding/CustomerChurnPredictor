package de.uni_trier.wi2.pki.util;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.EscapeStrategy;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
        if (examples.size() == 0) return null;



        List<Double> entropyList = EntropyUtils.calcInformationGain(examples, labelIndex);  // calculate gain for all attributes and find best gain

//        entropyList.forEach(System.out::println);

        int bestIndex = 0;

        for (int i = 1; i < entropyList.size(); i++) {                  // Iterate thru the entropy set
            if (entropyList.get(bestIndex) < entropyList.get(i))        // Find the best entropy
                bestIndex = i;                                          // Set a refrence to the new best entropy
        }



        DecisionTreeNode newNode = new DecisionTreeNode(bestIndex);     // Create new node, that has a reference to a position
        List<String> keys = getDistinct(examples, labelIndex);          // Get all the diffrent unique values on position ~labelIndex

        if (keys.size() == 1) {                                         // Prune branche if the rest of the branche is the same.
            newNode.getSplits().put((keys.iterator().next()), null);    // The branche is turned into a leef and gets a refrence to its key
            return newNode;                                             // Retrun leef node
        }



        
        List<String> buckets = getDistinct(examples, bestIndex);        // Get all the diffrent unique values on position ~bestIndex

        for (String bucket : buckets) {                                             // Generate rest of tree for each bucket
            List<CSVAttribute[]> clone = getClone(examples, bucket, bestIndex);     // Get a clone that ony has the entrys for the given bucket
            DecisionTreeNode child = createTree(clone, labelIndex);                 // Build rest of the tree

            newNode.getSplits().put(bucket, child);                     // Give the parent a refrence to its children
            if (child == null) continue;
            child.setParent(newNode);                                   // Give the children a refrence to its parent
        }
        return newNode;
    }

////////////////////// Helper Functions //////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Returns all unique values in a list at a given index.
    public  static List<String> getDistinct(List<CSVAttribute[]> examples, int index) {
        return examples.stream().map(x -> x[index].getCategory().toString())
                       .distinct().collect(Collectors.toList());
    }

    // Retruns a set of entrys that is part of the bucket. 
    public static List<CSVAttribute[]> getClone(List<CSVAttribute[]> examples, String bucket, int index) {
        return examples.stream().filter(x -> !x[index].getCategory().toString().equals(bucket))
                       .collect(Collectors.toList());
    }

    public static void printTree(DecisionTreeNode treeRoot) throws ParserConfigurationException {
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
        addChildren(treeRoot, treeRootElement);

        // output the xml file
        try{
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\Dokumente\\test.xml");
            xmlOutputter.output(doc, System.out);
//            xmlOutputter.output(doc, fileOutputStream);
        }catch (IOException e){
            System.out.println(e.getStackTrace());
        }
    }

    public static void addChildren(DecisionTreeNode root, Element jdomRoot){
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
                addChildren(entry.getValue(), ifElement);
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

