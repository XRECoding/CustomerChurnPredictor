package de.uni_trier.wi2.pki.io;

import de.uni_trier.wi2.pki.tree.DecisionTreeNode;
import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.xml.transform.URIResolver;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import static de.uni_trier.wi2.pki.Main.intervalSizes;

/**
 * Serializes the decision tree in form of an XML structure.
 */
public class XMLWriter {

    /**
     * Serialize decision tree to specified path.
     *
     * @param path         the path to write to.
     * @param decisionTree the tree to serialize.
     * @throws IOException if something goes wrong.
     */
    public static void writeXML(String path, DecisionTreeNode decisionTree) throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.getFormat().setIndent("\t  ");       // increasing the default indent


        // create document and content root
        Element root = new Element("DecisionTree");
        Document doc = new Document(root);


        Element treeRootElement = new Element("Node");
        treeRootElement.setAttribute(new Attribute("attributeID", String.valueOf(decisionTree.getAttributeIndex())));
        root.addContent(treeRootElement);
        addXmlChildren(decisionTree, treeRootElement);


        // output the xml file
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        xmlOutputter.output(doc, fileOutputStream);
//        xmlOutputter.output(doc, System.out);
    }

    /**
     * Serialize decision tree to specified path.
     *
     * @param root         the subtree to serialize.
     * @param jdomRoot     the subtree equivalent jdom Element.
     */
    public static void addXmlChildren(DecisionTreeNode root, Element jdomRoot){
        for (Map.Entry<String, DecisionTreeNode> entry : root.getSplits().entrySet()) {             // for every child of the DecisionTreeNode
            if (entry.getValue() != null){                                                          // check if the child is a leaf
                // <------------------- create a jdom Element with corresponding if-Element ------------------->
                Element element = new Element("Node");
                element.setAttribute(new Attribute("attributeID", String.valueOf(entry.getValue().getAttributeIndex())));
                Element ifElement = new Element("If");
                try{        // setting attribute for continuous nodes
                    ifElement.setAttribute(new Attribute("value", "greater than or equal "+
                            String.valueOf(Double.valueOf(entry.getKey())*intervalSizes[root.getAttributeIndex()]) +
                            " and lesser than " + String.valueOf((1+Double.valueOf(entry.getKey()))*intervalSizes[root.getAttributeIndex()])));
                } catch (NumberFormatException e){      // setting attribute for categoric nodes
                    ifElement.setAttribute(new Attribute("value", entry.getKey()));
                }
                jdomRoot.addContent(ifElement);     // adding the if-Element to the parent
                ifElement.addContent(element);      // adding the new node to the if-Element
                addXmlChildren(entry.getValue(), element);
            } else {
                // <------------------- transform jdom Element into LeafElement ------------------->
                Element element = new Element("LeafNode");
                element.setAttribute(new Attribute("class", entry.getKey()));
                jdomRoot = jdomRoot.getParentElement();
                jdomRoot.removeContent();           // remove the jdom Element
                jdomRoot.addContent(element);       // add the LeafElement
            }

        }
    }

}
