package org.wso2.carbon.ml.preprocessor;

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
import java.util.*;

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
    public static final int MIN_NAME_LENGTH = 2;
    public static final String MIN_INDEX_VAL = "-1";
    public static final int DOUBLE_META_PHONE_THRESHOLD = 10;




    public static void main(String[] args)
    {
        try {
            String csvPath = "/Users/tharik/Desktop/machine learning/Archive/";
            String csvReadFile = "Activity behaviou_Tier2_20141015.csv";
            String csvReadCustomerFile = "customers.csv";
            String csvWriteCustomerFile = "customersIndexed.csv";

            String csvWriteTransfomedNewFile = "transformedNew.csv";
            String csvWriteTransfomedExistingFile = "transformedExisting.csv";
            String csvWriteNotTransformedFile = "notTransformed.csv";
            String [] columnsIncluded = {"Title","Company","Country","Activity Type",
                                        "Activity","Activity date/time","IpAddress"};
            String [][] currentCustomers;

            DoubleMetaphoneUtility.setMaxCodeLen(DOUBLE_META_PHONE_THRESHOLD);

            long startTime = System.currentTimeMillis();

            CSVReader reader=new CSVReader(
                    new InputStreamReader(new FileInputStream(csvPath + csvReadFile), "UTF-8"), ',',
                                          CSVReader.DEFAULT_QUOTE_CHARACTER,CSVReader.DEFAULT_QUOTE_CHARACTER);

            CSVReader readerCustomers=new CSVReader(
                    new InputStreamReader(new FileInputStream(csvPath + csvReadCustomerFile), "UTF-8"), ',',
                                          CSVReader.DEFAULT_QUOTE_CHARACTER,CSVReader.DEFAULT_QUOTE_CHARACTER);


            CSVWriter writerCustomers = new CSVWriter(new FileWriter(csvPath + csvWriteCustomerFile), ',',
                                                      CSVWriter.NO_QUOTE_CHARACTER);
            CSVWriter writerTransformedNew = new CSVWriter(new FileWriter(csvPath + csvWriteTransfomedNewFile), ',',
                                                           CSVWriter.NO_QUOTE_CHARACTER);
            CSVWriter writerTransformedExisting = new CSVWriter(new FileWriter(csvPath
                                                                            + csvWriteTransfomedExistingFile),
                                                                ',', CSVWriter.NO_QUOTE_CHARACTER);
            CSVWriter writerNotTransformed = new CSVWriter(new FileWriter(csvPath + csvWriteNotTransformedFile),
                                                            ',', CSVWriter.NO_QUOTE_CHARACTER);


            currentCustomers = LoadCurrentCustomers(readerCustomers, writerCustomers,
                    Cleanser.INDEX_ALGO_DOUBLE_META_PHONE, 0);

            Cleanse(reader, writerTransformedNew, writerTransformedExisting, writerNotTransformed, INDEX_COLUMN_INPUT,
                    Cleanser.INDEX_COLUMN_NAME, Cleanser.IS_CUSTOMER_COLUMN_NAME, currentCustomers, columnsIncluded,
                    Cleanser.INDEX_ALGO_DOUBLE_META_PHONE);

            long estimatedTime = System.currentTimeMillis() - startTime;
            logger.info("Time taken : "+ estimatedTime/1000 + " seconds");

            writerTransformedNew.close();
            writerTransformedExisting.close();
            writerNotTransformed.close();
            writerCustomers.close();
            reader.close();



        }
        catch(Exception ex)
        {
            logger.error(ex);
        }
    }

    /**
     * Specifies weather current value is an existing customer
     * @param currentCustomers string array of customer indexes
     * @param currentValue current index value
     * @return
     */
    private static boolean isCustomer(String [][] currentCustomers, String currentValue)
    {
        for (int i = 0; i < currentCustomers.length; i++)
        {
            String matchingVal;

            if(currentValue.length() > Cleanser.MIN_NAME_LENGTH)
            {
                matchingVal = currentCustomers[i][0];
            }
            else
            {
                matchingVal = currentCustomers[i][1];
            }

            if (currentValue.equals(matchingVal))
            {
                return  true;
            }
        }

        return false;

    }

    /**
     * Load all the current customers from specified CSV read file and convert to indexes into a string array
     * @param readerCustomers input CSV file of existing customers
     * @param indexAlgorithm indexing algorithm
     * @param indexColumn indexing column index
     * @return
     * @throws Exception
     */
    private static String[][] LoadCurrentCustomers(CSVReader readerCustomers, CSVWriter writerCustomers,
                                                   int indexAlgorithm, int indexColumn) throws  Exception
    {

        Map<String,String> Customers = new HashMap<String,String>();
        String [] nextLine;


        while ((nextLine = readerCustomers.readNext()) != null) {
            try {
                //Set  algorithm Index for first column
                switch (indexAlgorithm) {
                    case Cleanser.INDEX_ALGO_DOUBLE_META_PHONE:
                        Customers.put(DoubleMetaphoneUtility.Convert(nextLine[indexColumn]), nextLine[indexColumn]);
                        break;
                    case Cleanser.INDEX_ALGO_META_PHONE:
                        Customers.put(MetaphoneUtility.Convert(nextLine[indexColumn]), nextLine[indexColumn]);
                        break;
                    default:
                        Customers.put(SoundexMatchUtility.Convert(nextLine[indexColumn]), nextLine[indexColumn]);
                        break;
                }
            }
            catch (IllegalArgumentException ex)
            {
                //handles algorithm encode exceptions
                logger.error(ex);
            }

        }

        String [][] CustomersArray =  new String[Customers.size()][2];
        final Iterator<?> iterator = Customers.entrySet().iterator();

        int counter = 0;
        while(iterator.hasNext()){
            final Map.Entry<?, ?> mapping = (Map.Entry<?, ?>) iterator.next();

            CustomersArray[counter][0] = mapping.getKey().toString();
            CustomersArray[counter][1] = mapping.getValue().toString();

            String [] customers = {CustomersArray[counter][0], CustomersArray[counter][1]};
            writerCustomers.writeNext(customers);
            counter++;
        }

        return CustomersArray;
    }

    /**
     *
     * Data Cleansing will be done on specified input csv and transform into specified csv files
     *
     * @param reader input csv file
     * @param writerTransformedNew  output transform csv file of new customer activities
     * @param writerTransformedExisting  output transform csv file of new customer activities
     * @param writerNotTransformed output not transformed/ignored csv file
     * @param indexColumnName indexing column name by algorithm
     * @param columnsIncluded Including column names for transformation
     * @param indexAlgorithm Specified algorithm for indexing
     * @throws IOException
     */
    private static void Cleanse(CSVReader reader, CSVWriter writerTransformedNew,CSVWriter writerTransformedExisting,
                                CSVWriter writerNotTransformed, String indexColumnName, String indexOutputColumnName,
                                String isCutomerColumnName, String[][] currentCustomer, String [] columnsIncluded,
                                int indexAlgorithm ) throws Exception
    {
        int totalCounter = 0;
        int transformedCounter = 0;
        int currentCustomerActionCounter = 0;


        int columnIndex ;
        int []columnIncludedIndexes = new int[columnsIncluded.length];

        //Writing Header row for output files
        String [] nextLine = reader.readNext();
        writerNotTransformed.writeNext(nextLine);

        //Add one more column for algorithm index to specified column array and write as header
        List<String> list = new LinkedList<String>(Arrays.asList(columnsIncluded));
        list.add(0, indexOutputColumnName);

        writerTransformedNew.writeNext(list.toArray(new String[indexColumnName.length()+1]));
        writerTransformedExisting.writeNext(list.toArray(new String[indexColumnName.length()+1]));

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


                    //If column value length is greater than specified minimum length then index it
                    if (nextLine[columnIndex].length() > Cleanser.MIN_NAME_LENGTH) {
                        //Set  algorithm Index for first column
                        switch (indexAlgorithm) {
                            case Cleanser.INDEX_ALGO_DOUBLE_META_PHONE:
                                outputLine[0] = DoubleMetaphoneUtility.Convert(nextLine[columnIndex]);
                                break;
                            case Cleanser.INDEX_ALGO_META_PHONE:
                                outputLine[0] = MetaphoneUtility.Convert(nextLine[columnIndex]);
                                break;
                            default:
                                outputLine[0] = SoundexMatchUtility.Convert(nextLine[columnIndex]);
                                break;
                        }
                    }
                    else
                    {
                        //Else Assign default index value
                        outputLine[0] = Cleanser.MIN_INDEX_VAL;
                    }

                    boolean isExistingCustomer = isCustomer(currentCustomer, outputLine[0]);



                    outputLine[1]  = String.valueOf(isExistingCustomer);

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

                    if(isExistingCustomer)
                    {
                        currentCustomerActionCounter++;
                        writerTransformedExisting.writeNext(outputLine);
                    }
                    else
                    {
                        writerTransformedNew.writeNext(outputLine);
                    }


                }
                catch (IllegalArgumentException ex)
                {
                    //handles algorithm encode exceptions
                    writerNotTransformed.writeNext(nextLine);
                }

            }
            else{
                writerNotTransformed.writeNext(nextLine);
            }
            totalCounter++;
        }

        logger.info(totalCounter + " rows processed.  "
                            + transformedCounter + " rows transformed. "
                            + (totalCounter - transformedCounter)
                            + " rows not transformed. Total current customer actions "
                            + currentCustomerActionCounter+ ", Other actions "
                            + (transformedCounter - currentCustomerActionCounter));
    }
}
