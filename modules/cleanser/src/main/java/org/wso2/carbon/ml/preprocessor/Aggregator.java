package org.wso2.carbon.ml.preprocessor;

/**
 * Created by tharik on 11/28/14.
 */
import au.com.bytecode.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Aggregator {

    public static String csvPath = "/Users/tharik/Desktop/machine learning/Archive/";
    public static String csvAggregate = "Aggregate.csv";

    public static void main (String[] args) throws IOException {


        System.out.println( (new Aggregator()).transformCsv("/Users/tharik/Desktop/machine learning/Archive/transformedExisting.csv") );
    }

    public String transformCsv (String csvFile) throws IOException {
        return csvMapToString(getCsvMap(csvFile));
    }

    private HashMap<String, String[]> getCsvMap (String csvFile) throws IOException {


        HashMap<String, String[]> csvMap = new HashMap<String, String[]>();
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
                        String[] columnValues = csvMap.get(company);
                        if(csvColumns.length < 7)
                        {
                            if (columnValues == null) {
                                columnValues = new String[7];
                                columnValues[0] = columnValues[1] = columnValues[2] = columnValues[3] = columnValues[4] = columnValues[5] = columnValues[6] = "0";
                            }
                            else
                            {
                                columnValues[6] = String.valueOf(Integer.parseInt(columnValues[6]) + 1);
                            }
                        }
                        else {

                            actionsType = csvColumns[6].trim();
                            if (actionsType.equals("")) {
                                columnValues[6] = columnValues[6] + 1;
                            } else {


                                if (columnValues == null) {
                                    columnValues = new String[7];
                                    columnValues[0] = columnValues[1] = columnValues[2] = columnValues[3] = columnValues[4] = columnValues[5] = columnValues[6] = "0";
                                }

                                columnValues[0] = String.valueOf (Integer.parseInt(columnValues[0]) + (actionsType.contains("downloads") ? 1 : 0));
                                columnValues[1] =  String.valueOf (Integer.parseInt(columnValues[1]) + (actionsType.contains("whitepapers") ? 1 : 0));
                                columnValues[2] =  String.valueOf (Integer.parseInt(columnValues[2]) + (actionsType.contains("tutorials") ? 1 : 0));
                                columnValues[3] =  String.valueOf (Integer.parseInt(columnValues[3]) + (actionsType.contains("workshops") ? 1 : 0));
                                columnValues[4] =  String.valueOf (Integer.parseInt(columnValues[4]) + (actionsType.contains("casestudies") ? 1 : 0));
                                columnValues[5] =  String.valueOf (Integer.parseInt(columnValues[5]) + (actionsType.contains("productpages") ? 1 : 0));


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

    private String csvMapToString (HashMap<String, String[]> csvMap) {

        try {
            CSVWriter writerNotTransformed = new CSVWriter(new FileWriter(csvPath + csvAggregate),
                    ',', CSVWriter.NO_QUOTE_CHARACTER);

            String [] headers  = {"Company", "downloads", "whitepapers", "tutorials", "workshops", "casestudies", "productpages", "other"};

            writerNotTransformed.writeNext(headers);



            StringBuilder newCsvFile = new StringBuilder();

            for (String company : csvMap.keySet()) {
                String[] columnValues = csvMap.get(company);


               // writerNotTransformed.writeNext(headers);

                String [] outputLine = new String[8];

                outputLine[0] = company;
                outputLine[1] = String.valueOf(columnValues[0]);
                outputLine[2] = String.valueOf(columnValues[1]);
                outputLine[3] = String.valueOf(columnValues[2]);
                outputLine[4] = String.valueOf(columnValues[3]);
                outputLine[5] = String.valueOf(columnValues[4]);
                outputLine[6] = String.valueOf(columnValues[5]);
                outputLine[7] = String.valueOf(columnValues[6]);

                writerNotTransformed.writeNext(outputLine);

            }

            writerNotTransformed.close();
            return newCsvFile.toString();
        }
        catch(Exception ex)
        {
            return null;
        }
    }


}
