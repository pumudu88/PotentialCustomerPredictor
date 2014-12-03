package org.wso2.carbon.ml.preprocessor;

/**
 * Created by tharik on 11/28/14.
 */
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class Aggregator {

    public static final String INDEX_COLUMN_INPUT = "Company";
    public static final String ACTIVITY_COLUMN_NAME = "Link";

    public static final char CSV_SEPERATOR = ',';
    public static final String CSV_CHARACTER_FORMAT = "UTF-8";

    public static String csvPath = "/Users/tharik/Desktop/machine learning/Archive/";
    public static String csvAggregate = "Aggregate.csv";
    private static String [] headers  = {"Company", "downloads", "whitepapers", "tutorials", "workshops", "casestudies", "productpages", "other"};

    private static final Log logger = LogFactory.getLog(Cleanser.class);

    public static void main (String[] args) throws IOException {


        transformCsv(Cleanser.csvPath + Cleanser.csvWriteTransfomed);
    }

    public static void transformCsv (String csvFile) throws IOException {

        mapToCsv(getCsvMap(csvFile));
    }

    private static HashMap<String, String[]> getCsvMap (String csvPath) throws IOException {


        HashMap<String, String[]> csvMap = new HashMap<String, String[]>();

        CSVReader reader=new CSVReader(
                new InputStreamReader(new FileInputStream(csvPath), CSV_CHARACTER_FORMAT),
                CSV_SEPERATOR, CSVReader.DEFAULT_QUOTE_CHARACTER,
                CSVReader.DEFAULT_QUOTE_CHARACTER);


        String[] nextLine = reader.readNext();




        int linkColumnIndex = Arrays.asList(nextLine).indexOf(ACTIVITY_COLUMN_NAME);


        // Create map
        try {
            while ((nextLine = reader.readNext()) != null) {

                if (nextLine.length > 0) {
                    try {
                        String company = nextLine[0].trim();


                        String actionsType;
                        String[] columnValues = csvMap.get(company);

                        if(nextLine.length > linkColumnIndex && !nextLine[linkColumnIndex].equals("")) {

                            actionsType = nextLine[linkColumnIndex].trim();
                            if (actionsType.equals("")) {
                                columnValues[6] = columnValues[6] + 1;
                            } else {


                                if (columnValues == null) {
                                    columnValues = new String[7];
                                    columnValues[0] = columnValues[1] = columnValues[2] = columnValues[3] = columnValues[4] = columnValues[5] = columnValues[6] = "0";
                                }

                                columnValues[0] = String.valueOf(Integer.parseInt(columnValues[0]) + (actionsType.contains("downloads") ? 1 : 0));
                                columnValues[1] = String.valueOf(Integer.parseInt(columnValues[1]) + (actionsType.contains("whitepapers") ? 1 : 0));
                                columnValues[2] = String.valueOf(Integer.parseInt(columnValues[2]) + (actionsType.contains("tutorials") ? 1 : 0));
                                columnValues[3] = String.valueOf(Integer.parseInt(columnValues[3]) + (actionsType.contains("workshops") ? 1 : 0));
                                columnValues[4] = String.valueOf(Integer.parseInt(columnValues[4]) + (actionsType.contains("casestudies") ? 1 : 0));
                                columnValues[5] = String.valueOf(Integer.parseInt(columnValues[5]) + (actionsType.contains("productpages") ? 1 : 0));


                                if (!company.equals(INDEX_COLUMN_INPUT)) {
                                    csvMap.put(company, columnValues);
                                }
                            }
                        }
                        else  {
                            if (columnValues == null) {
                                columnValues = new String[7];
                                columnValues[0] = columnValues[1] = columnValues[2] = columnValues[3] = columnValues[4] = columnValues[5] = columnValues[6] = "0";
                            }
                            else
                            {
                                columnValues[6] = String.valueOf(Integer.parseInt(columnValues[6]) + 1);
                            }
                        }
                    }
                    catch (Exception ex) {
                        logger.error(ex);
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return csvMap;
    }

    private static void mapToCsv (HashMap<String, String[]> csvMap) {

        try {
            CSVWriter writerNotTransformed = new CSVWriter(new FileWriter(csvPath + csvAggregate),
                    ',', CSVWriter.NO_QUOTE_CHARACTER);

            //Write headers to CSV
            writerNotTransformed.writeNext(headers);

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
        }
        catch(Exception ex)
        {
            logger.error(ex);
        }
    }


}
