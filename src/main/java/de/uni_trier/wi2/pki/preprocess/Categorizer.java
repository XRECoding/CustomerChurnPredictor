package de.uni_trier.wi2.pki.preprocess;

import de.uni_trier.wi2.pki.io.attr.CSVAttribute;
import de.uni_trier.wi2.pki.io.attr.Categoric;
import de.uni_trier.wi2.pki.io.attr.Continuously;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Categorizer {

    public static LinkedList<CSVAttribute[]> categorize(List<String[]> linkedList){

        LinkedList<CSVAttribute[]> output = new LinkedList<>();

        Continuously[] creditScore = new Continuously[linkedList.size()];
        Categoric[] geography = new Categoric[linkedList.size()];
        Categoric[] gender = new Categoric[linkedList.size()];
        Continuously[] age = new Continuously[linkedList.size()];
        Continuously[] tenure = new Continuously[linkedList.size()];
        Continuously[] balance = new Continuously[linkedList.size()];
        Continuously[] numOfProducts = new Continuously[linkedList.size()];
        Categoric[] hasCrCard = new Categoric[linkedList.size()];
        Categoric[] isActiveMember = new Categoric[linkedList.size()];
        Continuously[] estimatedSalary = new Continuously[linkedList.size()];
        Categoric[] exited = new Categoric[linkedList.size()];

        output.add(creditScore);
        output.add(geography);
        output.add(gender);
        output.add(age);
        output.add(tenure);
        output.add(balance);
        output.add(numOfProducts);
        output.add(hasCrCard);
        output.add(isActiveMember);
        output.add(estimatedSalary);
        output.add(exited);

        int counter = 0;
        for (String[] variable : linkedList) {
            creditScore[counter] = new Continuously(variable[0]);
            geography[counter] = new Categoric(variable[1]);
            gender[counter] = new Categoric(variable[2]);
            age[counter] = new Continuously(variable[3]);
            tenure[counter] = new Continuously(variable[4]);
            balance[counter] = new Continuously(variable[5]);
            numOfProducts[counter] = new Continuously(variable[6]);
            hasCrCard[counter] = new Categoric(variable[7]);
            isActiveMember[counter] = new Categoric(variable[8]);
            estimatedSalary[counter] = new Continuously(variable[9]);
            exited[counter] = new Categoric(variable[10]);
            counter++;
        }


        Map<String,Integer>[] map = new HashMap[linkedList.get(0).length];
        for (int n = 0; n < map.length; n++) {
            map[n] = new HashMap<String,Integer>();
        }

        for (int j = 0; j < linkedList.size(); j++) {
            String[] r = linkedList.get(j);
            for (int i = 0; i < r.length; i++) {
                if(map[i].get(r[i]) == null)
                    map[i].put(r[i], 1);
                else
                    map[i].put(r[i], map[i].get(r[i])+1);
            }
        }


        // for check Method
        Map<String,String>[] map2 = new HashMap[13];
        for (int n = 0; n < map.length; n++) {
            map2[n] = new HashMap<String,String>();
        }
        for (int i = 0; i < linkedList.size(); i++) {
            String[] r = linkedList.get(i);
            for (int j = 0; j < r.length; j++) {
                map2[j].put(r[j], r[j]);
            }
        }

        for (int i = 0; i < 11; i++)
            System.out.println("Attribut " + (i + 1) + ": " + (check(map2[i])?"kategorisch":"kontinuierlich"));
//            System.out.println("Attribut " + (i + 1) + ": " + (hello(map[i])?"kategorisch":"kontinuierlich"));

        return output;
    }

    public static Boolean hello (Map<String, Integer> map) {
        try {
            Double value = map.entrySet().stream().filter(x -> (Integer) x.getValue() / Double.valueOf(9763) < 0.02)
                    .mapToDouble(x -> (Integer) x.getValue() / Double.valueOf(9763)).average().getAsDouble();
            return value < 0.02;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean check(Map<String, String> map) {
        try {
            for (Map.Entry<String, String> element : map.entrySet()) {
                Double.parseDouble(element.getValue().toString());
                break;
            }
        } catch (Exception e) {
            return true;
        }
        return map.size() == 2;
    }
}
