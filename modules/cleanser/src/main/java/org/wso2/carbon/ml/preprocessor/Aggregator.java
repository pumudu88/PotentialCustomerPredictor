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
    private static String [] keyWords = {"downloads", "whitepapers", "tutorials", "workshops", "casestudies", "productpages"};

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

                        if (columnValues == null) {
                            columnValues = new String[keyWords.length + 1];
                            columnValues[0] = columnValues[1] = columnValues[2] = columnValues[3] = columnValues[4] = columnValues[5] = columnValues[6] = "0";
                        }

                        if(nextLine.length > linkColumnIndex && !nextLine[linkColumnIndex].equals("")) {

                            actionsType = nextLine[linkColumnIndex].trim();

                            if (actionsType.equals("")) {
                                    columnValues[6] = String.valueOf(Integer.parseInt(columnValues[6]) + 1);
                            } else {

                                for (int i = 0; i < columnValues.length - 1 ; i++)
                                {
                                    columnValues[i] = String.valueOf(Integer.parseInt(columnValues[i]) + (actionsType.contains(keyWords[i]) ? 1 : 0));
                                }

                                if (!company.equals(INDEX_COLUMN_INPUT)) {
                                    csvMap.put(company, columnValues);
                                }
                            }
                        }
                        else  {
                                columnValues[6] = String.valueOf(Integer.parseInt(columnValues[6]) + 1);
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
                    CSV_SEPERATOR, CSVWriter.NO_QUOTE_CHARACTER);

            //Write headers to CSV
            writerNotTransformed.writeNext(headers);

            for (String company : csvMap.keySet()) {
                String[] columnValues = csvMap.get(company);

                if (!company.equals("")) {

                    String[] outputLine = new String[8];
                    outputLine[0] = company;
                    int preColumnCount = 1;

                    for (int i = preColumnCount; i < columnValues.length + preColumnCount; i++) {
                        outputLine[i] = String.valueOf(columnValues[i - preColumnCount]);
                    }
                    writerNotTransformed.writeNext(outputLine);
                }
            }
            writerNotTransformed.close();
        }
        catch(Exception ex)
        {
            logger.error(ex);
        }
    }


}
