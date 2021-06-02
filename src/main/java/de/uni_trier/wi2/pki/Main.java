package de.uni_trier.wi2.pki;

import de.uni_trier.wi2.pki.io.CSVReader;
import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.preprocess.BinningDiscretizer;
import de.uni_trier.wi2.pki.preprocess.Categorizer;
import de.uni_trier.wi2.pki.tree.DecisionTreeNode;
import de.uni_trier.wi2.pki.util.EntropyUtils;
import de.uni_trier.wi2.pki.util.ID3Utils;
import de.uni_trier.wi2.pki.util.newTest;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("rawtypes")

public class Main {
    public static double[] intervalSizes;

    public static void main(String[] args) {
        // TODO: this should be the main executable method for your project

        try {
            // reading input data
            List<String[]> linkedList = CSVReader.readCsvToArray("churn_data.csv", ";", true);

            // initializing intervalSize array
            intervalSizes = new double[linkedList.size()];
            
            // categorizing input data into CSVAttributes
            
            List<CSVAttribute[]> newList = Categorizer.categorize(linkedList);
            //EntropyUtils.calcInformationGain(newList, 10).forEach(System.out::println);
            System.out.println();
            //newTest.calcInformationGain(newList, 10).forEach(System.out::println);
            DecisionTreeNode root =  ID3Utils.createTree(newList, 10);
//            DFS(root);


            DecisionTreeNode test = new DecisionTreeNode(999);

            DecisionTreeNode test1 = new DecisionTreeNode(100);
            DecisionTreeNode test2 = new DecisionTreeNode(200);
            DecisionTreeNode test3 = new DecisionTreeNode(300);

            test.getSplits().put("1", test1);
            test.getSplits().put("2", test2);
            test.getSplits().put("3", test3);

            DecisionTreeNode test4 = new DecisionTreeNode(101);
            DecisionTreeNode test5 = new DecisionTreeNode(102);
            DecisionTreeNode test6 = new DecisionTreeNode(103);

            test1.getSplits().put("1", test4);
            test1.getSplits().put("2", test5);
            test1.getSplits().put("3", test6);

            DecisionTreeNode test7 = new DecisionTreeNode(201);
            DecisionTreeNode test8 = new DecisionTreeNode(202);
            DecisionTreeNode test9 = new DecisionTreeNode(203);

            test2.getSplits().put("1", test7);
            test2.getSplits().put("2", test8);
            test2.getSplits().put("3", test9);

            test3.getSplits().put("plus", null);




            try{
                ID3Utils.printTree(root);
            } catch (ParserConfigurationException e){
                System.out.println(e.getStackTrace());
            }



        }catch (IOException e){
            System.out.println(e.getStackTrace());
        }



    }

    public static void DFS(DecisionTreeNode node) {
        if (node == null) return;

        System.out.println(node.getAttributeIndex());
        DFS(node.splits.entrySet().iterator().next().getValue());
    }
}
