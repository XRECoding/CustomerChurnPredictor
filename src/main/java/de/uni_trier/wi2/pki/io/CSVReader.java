package de.uni_trier.wi2.pki.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Is used to read in CSV files.
 */
public class CSVReader {

    /**
     * Read a CSV file and return a list of string arrays.
     *
     * @param relativePath the path where the CSV file is located (has to be relative path!)
     * @param delimiter    the delimiter symbol which is used in the CSV
     * @param ignoreHeader A boolean that indicates whether to ignore the header line or not, i.e., whether to include the first line into the list or not
     * @return A list that contains string arrays. Each string array stands for one parsed line of the CSV file
     * @throws IOException if something goes wrong. Exception should be handled at the calling function.
     */
    
    public static List<String[]> readCsvToArray(String relativePath, String delimiter, boolean ignoreHeader) throws IOException {
        List<String[]> output = new LinkedList<String[]>();
        FileReader file = new FileReader("src/main/resources/" + relativePath); // subject to change; specification of relativePath needed

        try (BufferedReader bufferedReader = new BufferedReader(file)) {
            String currentLine;

            while ((currentLine = bufferedReader.readLine()) != null)
                output.add(currentLine.split(delimiter));
        }
        
        return (ignoreHeader)? output.subList(1, output.size()) : output;
    }
}
