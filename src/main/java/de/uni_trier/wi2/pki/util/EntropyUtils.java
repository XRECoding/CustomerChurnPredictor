package de.uni_trier.wi2.pki.util;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;

import java.util.*;

/**
 * Contains methods that help with computing the entropy.
 */
public class EntropyUtils {

    /**
     * Calculates the information gain for all attributes
     *
     * @param matrix     Matrix of the training data (example data), e.g. ArrayList<String[]>
     * @param labelIndex the index of the attribute that contains the class. If the dataset is [Temperature,Weather,PlayFootball] and you want to predict playing
     *                   football, than labelIndex is 2
     * @return the information gain for each attribute
     */
    public static List<Double> calcInformationGain(LinkedList<CSVAttribute[]> matrix, int labelIndex) {     // changed Collection to LinkedList

        CSVAttribute[] exitedArray = matrix.get(labelIndex);

        double positive = 0;

        for (int i = 0; i < exitedArray.length; i++) {
            if(Integer.parseInt(exitedArray[i].getValue().toString()) == 1){positive++;}        // only accounts for boolean classes
        }

        double negative = exitedArray.length - positive;

        double entropy = (-positive/(positive + negative) * log2(positive/(positive + negative))) -(negative/(positive + negative) * log2(negative/(positive + negative))); // H(E)

//        System.out.println(entropy);

        // calculating entropy per attribute
        for (int i = 0; i < matrix.size(); i++) {
            if(i != labelIndex){
                HashMap<String, double[]> hashMap = new HashMap<>();
                CSVAttribute[] csvAttributes = matrix.get(i);
                // counting positive and negative per interval
                for (int j = 0; j < csvAttributes.length; j++) {
                    try{
                        double[] counterArray = hashMap.get(csvAttributes[j].getCategory().toString());        // array counts class = 0 in position 0 and class = 1 in position 1; generic implementation requires hashmap
                        if (exitedArray[j].getValue().toString().equals("0")){
                            counterArray[0]++;
                        }else{
                            counterArray[1]++;
                        }
                        hashMap.put(csvAttributes[j].getCategory().toString(), counterArray);

                    }catch (NullPointerException e){
                        if (exitedArray[j].getValue().toString().equals("0")){
                            hashMap.put(csvAttributes[j].getCategory().toString(), new double[] {1, 0});
                        }else{
                            hashMap.put(csvAttributes[j].getCategory().toString(), new double[] {0, 1});
                        }
                    }
                }

                // calculating entropy per interval
                double restEntropy = 0;
                for (Map.Entry<String, double[]> entry: hashMap.entrySet()){
                    double[] array = entry.getValue();
                    double intervalEntropy = (-array[1]/(array[1] + array[0]) * log2(array[1]/(array[1] + array[0]))) -(array[0]/(array[1] + array[0]) * log2(array[0]/(array[1] + array[0]))); // H(E i)
                    // calculating rest entropy R(A)
                    restEntropy = restEntropy + (((array[1] + array[0])/ csvAttributes.length) * intervalEntropy);

                }

                // calculating gain
                double gain = entropy - restEntropy;
                System.out.println("Gain for Attribute is: " + gain);

//                System.out.println(hashMap.size());       // prints # of categories per attribute

//                hashMap.forEach((k, v) ->{
//
//                    System.out.println(k + " " + v[0] +" "+ v[1]);
//                });
//                System.out.println("______________");
            }
        }

        return null;
    }

    public static double log2(double value){
        return (Math.log(value) / Math.log(2));
    }
}
