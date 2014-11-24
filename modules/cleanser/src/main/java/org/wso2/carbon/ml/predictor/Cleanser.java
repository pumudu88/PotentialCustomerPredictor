package org.wso2.carbon.ml.predictor;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.ml.algorithms.DoubleMetaphoneUtility;
import org.wso2.carbon.ml.algorithms.MetaphoneUtility;
import org.wso2.carbon.ml.algorithms.SoundexMatchUtility;


import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tharik on 11/17/14.
 */
public class Cleanser {

    private static final Log logger = LogFactory.getLog(Cleanser.class);

    public static final int INDEX_ALGO_SOUNDEX = 1;
    public static final int INDEX_ALGO_META_PHONE = 2;
    public static final int INDEX_ALGO_DOUBLE_META_PHONE = 3;
    public static final String INDEX_COLUMN_NAME = "Index";
    public static final String INDEX_COLUMN_INPUT = "Company";
    public static final String IS_CUSTOMER_COLUMN_NAME = "Is Customer";




    public static void main(String[] args)
    {
        try {
            String csvPath = "/Users/tharik/Desktop/machine learning/Archive/";
            String csvReadFile = "Activity behaviou_Tier2_20141015.csv";
            String csvReadCustomerFile = "customers.csv";
            String csvWriteTransfomedFile = "transformed.csv";
            String csvWriteNotTransformedFile = "notTransformed.csv";
            String [] columnsIncluded = {"Title","Company","Country","Activity Type","Activity","Activity date/time","IpAddress"};
            String [] currentCustomers;

            long startTime = System.currentTimeMillis();

            CSVReader reader=new CSVReader(
                    new InputStreamReader(new FileInputStream(csvPath + csvReadFile), "UTF-8"), ',',CSVReader.DEFAULT_QUOTE_CHARACTER,CSVReader.DEFAULT_QUOTE_CHARACTER);

            CSVReader readerCustomers=new CSVReader(
                    new InputStreamReader(new FileInputStream(csvPath + csvReadCustomerFile), "UTF-8"), ',',CSVReader.DEFAULT_QUOTE_CHARACTER,CSVReader.DEFAULT_QUOTE_CHARACTER);


            CSVWriter writerTransformed = new CSVWriter(new FileWriter(csvPath + csvWriteTransfomedFile), ',', CSVWriter.NO_QUOTE_CHARACTER);
            CSVWriter writerNotTransformed = new CSVWriter(new FileWriter(csvPath + csvWriteNotTransformedFile), ',', CSVWriter.NO_QUOTE_CHARACTER);


            currentCustomers = LoadCurrentCustomers(readerCustomers, Cleanser.INDEX_ALGO_SOUNDEX, 0);

            Cleanse(reader, writerTransformed, writerNotTransformed, INDEX_COLUMN_INPUT, Cleanser.INDEX_COLUMN_NAME, Cleanser.IS_CUSTOMER_COLUMN_NAME, currentCustomers, columnsIncluded, Cleanser.INDEX_ALGO_SOUNDEX);

            long estimatedTime = System.currentTimeMillis() - startTime;
            logger.info("Time taken : "+ estimatedTime/1000 + " seconds");

            writerTransformed.close();
            writerNotTransformed.close();
            reader.close();



        }
        catch(Exception ex)
        {
            logger.error(ex);
        }
    }

    private static boolean isCustomer(String [] currentCustomers, String currentValue)
    {
        for (int i = 0; i < currentCustomers.length; i++)
        {
            if ( currentValue.equals(currentCustomers[i]))
            {
                return  true;
            }
        }

        return false;

    }

    private static String[] LoadCurrentCustomers(CSVReader readerCustomers, int indexAlgorithm, int indexColumn) throws  Exception
    {
        ArrayList<String> Customers = new ArrayList<String>();
        String [] nextLine;


        while ((nextLine = readerCustomers.readNext()) != null) {

            //Set  algorithm Index for first column
            switch (indexAlgorithm) {
                case Cleanser.INDEX_ALGO_DOUBLE_META_PHONE:
                    Customers.add(Customers.size(), DoubleMetaphoneUtility.Convert(nextLine[indexColumn]));
                    break;
                case Cleanser.INDEX_ALGO_META_PHONE:
                    Customers.add(Customers.size(), MetaphoneUtility.Convert(nextLine[indexColumn]));
                    break;
                default:
                    Customers.add(Customers.size(), SoundexMatchUtility.Convert(nextLine[indexColumn]));
                    break;
            }

        }



        return Customers.toArray(new String[Customers.size()]);
    }

    /**
     *
     * Data Cleansing will be done on specified input csv and transform into specified csv files
     *
     * @param reader input csv file
     * @param writerTransformed output transform csv file
     * @param writerNotTransformed output not transformed/ignored csv file
     * @param indexColumnName indexing column name by algorithm
     * @param columnsIncluded Including column names for transformation
     * @param indexAlgorithm Specified algorithm for indexing
     * @throws IOException
     */
    private static void Cleanse(CSVReader reader, CSVWriter writerTransformed,  CSVWriter writerNotTransformed, String indexColumnName, String indexOutputColumnName, String isCutomerColumnName, String[] currentCustomer, String [] columnsIncluded, int indexAlgorithm ) throws Exception
    {
        int totalCounter = 0;
        int transformedCounter = 0;
        int columnIndex ;
        int []columnIncludedIndexes = new int[columnsIncluded.length];

        //Writing Header row for output files
        String [] nextLine = reader.readNext();
        writerNotTransformed.writeNext(nextLine);

        //Add one more column for algorithm index to specified column array and write as header
        List<String> list = new LinkedList<String>(Arrays.asList(columnsIncluded));
        list.add(0, indexOutputColumnName);
        list.add(1, isCutomerColumnName);

        writerTransformed.writeNext(list.toArray(new String[indexColumnName.length()+1]));

        //Get the column index of given column name
        columnIndex = Arrays.asList(nextLine).indexOf(indexColumnName);

        for(int i =0; i < nextLine.length; i++)
        {
            nextLine[i] = nextLine[i].trim();
        }

        for(int i = 0; i < columnsIncluded.length; i++)
        {
            columnIncludedIndexes[i] = Arrays.asList(nextLine).indexOf(columnsIncluded[i]);
        }

        while ((nextLine = reader.readNext()) != null) {


            //Check read line is number of required columns and indexing column value is not empty
            if(nextLine.length >= columnIncludedIndexes.length && !(nextLine[columnIndex].equals(""))) {

                //Initialize output with specified columns plus one more column for algorithm index
                String [] outputLine = new String[columnIncludedIndexes.length + 1];


                try {

                    //Set  algorithm Index for first column
                    switch (indexAlgorithm) {
                        case Cleanser.INDEX_ALGO_DOUBLE_META_PHONE:
                            outputLine[0]  = DoubleMetaphoneUtility.Convert(nextLine[columnIndex]);
                            break;
                        case Cleanser.INDEX_ALGO_META_PHONE:
                            outputLine[0]  = MetaphoneUtility.Convert(nextLine[columnIndex]);
                            break;
                        default:
                            outputLine[0]  = SoundexMatchUtility.Convert(nextLine[columnIndex]);
                            break;
                    }

                    outputLine[0]  = SoundexMatchUtility.Convert(nextLine[columnIndex]);
                    outputLine[1]  = String.valueOf(isCustomer(currentCustomer, outputLine[0]));

                    //Set specified columns for rest
                    for (int i = 1; i < columnIncludedIndexes.length; i++) {
                        //Check include index is available on readLine
                        if (nextLine.length > columnIncludedIndexes[i - 1]) {
                            outputLine[i] = nextLine[columnIncludedIndexes[i - 1]];
                        } else {
                            outputLine[i] = "";
                        }
                    }

                    transformedCounter++;
                    writerTransformed.writeNext(outputLine);
                }
                catch (IllegalArgumentException ex)
                {
                    //handles algorithm encode exceptions and return empty index
                    writerNotTransformed.writeNext(nextLine);
                }

            }
            else{
                writerNotTransformed.writeNext(nextLine);
            }
            totalCounter++;
        }

        logger.info(totalCounter + " rows processed.  " + transformedCounter + " rows transformed. " + (totalCounter - transformedCounter) + " rows not transformed");
    }
}
