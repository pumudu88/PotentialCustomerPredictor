package org.wso2.carbon.ml.preprocessor;

/**
 * Created by tharik on 11/28/14.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

public class CSVMapper {

    public String transformCsv (String csvFile) throws IOException {
        return csvMapToString(getCsvMap(csvFile));
    }

    private HashMap<String, Integer[]> getCsvMap (String csvFile) throws IOException {
        // <K,V> := <Company, [Downloaded, Watched, Subscribed]>
        HashMap<String, Integer[]> csvMap = new HashMap<String, Integer[]>();
        BufferedReader reader = new BufferedReader(new FileReader(csvFile));
        String csvLine;

        // Create map
        try {
            while ((csvLine = reader.readLine()) != null) {
                String[] csvColumns = csvLine.split(",");
                if (csvColumns.length > 0) {
                    try {
                        String company = csvColumns[0].trim();


                        String actionsType;
                        Integer[] columnValues = csvMap.get(company);
                        if(csvColumns.length < 7)
                        {
                            if (columnValues == null) {
                                columnValues = new Integer[7];
                                columnValues[0] = columnValues[1] = columnValues[2] = columnValues[3] = columnValues[4] = columnValues[5] = columnValues[6] = 0;
                            }
                            else
                            {
                                columnValues[6] = columnValues[6] + 1;
                            }
                        }
                        else {

                            actionsType = csvColumns[6].trim();





                            if (actionsType.equals("")) {
                                columnValues[6] = columnValues[6] + 1;
                            } else {


                                if (columnValues == null) {
                                    columnValues = new Integer[7];
                                    columnValues[0] = columnValues[1] = columnValues[2] = columnValues[3] = columnValues[4] = columnValues[5] = columnValues[6] = 0;
                                }
                                columnValues[0] = columnValues[0] + (actionsType.contains("downloads") ? 1 : 0);
                                columnValues[1] = columnValues[1] + (actionsType.contains("whitepapers") ? 1 : 0);
                                columnValues[2] = columnValues[2] + (actionsType.contains("tutorials") ? 1 : 0);
                                columnValues[3] = columnValues[3] + (actionsType.contains("workshops") ? 1 : 0);
                                columnValues[4] = columnValues[4] + (actionsType.contains("casestudies") ? 1 : 0);
                                columnValues[5] = columnValues[5] + (actionsType.contains("productpages") ? 1 : 0);


                                if (!company.equals("Company"))
                                    csvMap.put(company, columnValues);
                            }
                        }
                    }
                    catch (Exception nfe) {
                        //TODO: handle NumberFormatException
                        System.out.println(nfe);
                    }
                }
            }
        }
        catch (Exception e) {
            //TODO: handle IOException
            System.out.println(e);
        }
        return csvMap;
    }

    private String csvMapToString (HashMap<String, Integer[]> csvMap) {
        StringBuilder newCsvFile = new StringBuilder();
        newCsvFile.append("Company, downloads, whitepapers, tutorials, workshops, casestudies, productpages, other \n");
        for (String company : csvMap.keySet()) {
            Integer[] columnValues = csvMap.get(company);
            newCsvFile.append(company +
                    ", " + Integer.toString(columnValues[0]) +
                    ", " + Integer.toString(columnValues[1]) +
                    ", " + Integer.toString(columnValues[2]) +
                    ", " + Integer.toString(columnValues[3]) +
                    ", " + Integer.toString(columnValues[4]) +
                    ", " + Integer.toString(columnValues[5]) +
                    ", " + Integer.toString(columnValues[6]) + "\n");
        }
        return newCsvFile.toString();
    }

    public static void main (String[] args) throws IOException {


        System.out.println( (new CSVMapper()).transformCsv("/Users/tharik/Desktop/machine learning/Archive/transformedExisting.csv") );
    }
}
