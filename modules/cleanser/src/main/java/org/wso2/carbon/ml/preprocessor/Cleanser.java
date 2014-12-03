package org.wso2.carbon.ml.preprocessor;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.ml.algorithms.CustomMatchingUtility;
import org.wso2.carbon.ml.algorithms.DoubleMetaphoneUtility;
import org.wso2.carbon.ml.validations.ValidationUtility;

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
    public static final String IS_VALID_COUNTRY_COLUMN_NAME = "Is valid Country";

    public static final String COUNTRY_COLUMN_NAME = "Country";
    public static final String IP_COLUMN_NAME ="IpAddress";

    public static final int MIN_NAME_LENGTH = 2;
    public static final String MIN_INDEX_VAL = "-1";
    public static final int DOUBLE_META_PHONE_THRESHOLD = 10;

    public static final char CSV_SEPERATOR = ',';
    public static final String CSV_CHARACTER_FORMAT = "UTF-8";

    public static String csvPath = "/Users/tharik/Desktop/machine learning/Archive/";
    public static String csvReadFile = "ContactWebsiteBehaviour_tier1_20141014.csv";
    public static String csvReadCustomerFile = "customers.csv";
    public static String csvWriteCustomerFile = "customersIndexed.csv";
    public static String csvCompanySuffixFile =  "company_suffix.csv";

    public static String csvWriteTransfomed = "transformed.csv";
    public static String csvWriteNotTransformedFile = "notTransformed.csv";
    public static String [] columnsIncluded = { "Title", "Company", "Country", "IpAddress",
                                                "Activity date/time", "Link"};

    private static boolean enableIpValidate = false;




    public static void main(String[] args) {
        try {

            String [][] currentCustomers;

            DoubleMetaphoneUtility.setMaxCodeLen(DOUBLE_META_PHONE_THRESHOLD);
            CustomMatchingUtility.LoadCompanySuffixFromCsv(csvPath + csvCompanySuffixFile);

            long startTime = System.currentTimeMillis();

            CSVReader reader=new CSVReader(
                    new InputStreamReader(new FileInputStream(csvPath + csvReadFile), CSV_CHARACTER_FORMAT),
                                          CSV_SEPERATOR, CSVReader.DEFAULT_QUOTE_CHARACTER,
                                          CSVReader.DEFAULT_QUOTE_CHARACTER);

            CSVReader readerCustomers=new CSVReader(
                    new InputStreamReader(new FileInputStream(csvPath + csvReadCustomerFile), CSV_CHARACTER_FORMAT),
                                          CSV_SEPERATOR, CSVReader.DEFAULT_QUOTE_CHARACTER,
                                          CSVReader.DEFAULT_QUOTE_CHARACTER);


            CSVWriter writerCustomers = new CSVWriter(new FileWriter(csvPath + csvWriteCustomerFile), CSV_SEPERATOR,
                                                      CSVWriter.NO_QUOTE_CHARACTER);

            CSVWriter writerTransformed= new CSVWriter(new FileWriter(csvPath + csvWriteTransfomed),
                                                                ',', CSVWriter.NO_QUOTE_CHARACTER);

            CSVWriter writerNotTransformed = new CSVWriter(new FileWriter(csvPath + csvWriteNotTransformedFile),
                                                            ',', CSVWriter.NO_QUOTE_CHARACTER);


            currentCustomers = loadCurrentCustomers(readerCustomers, writerCustomers,
                    Cleanser.INDEX_ALGO_DOUBLE_META_PHONE, 0);

            cleanse(reader, writerTransformed, writerNotTransformed, INDEX_COLUMN_INPUT,
                    Cleanser.INDEX_COLUMN_NAME, Cleanser.IS_CUSTOMER_COLUMN_NAME, Cleanser.IS_VALID_COUNTRY_COLUMN_NAME,
                    Cleanser.COUNTRY_COLUMN_NAME, Cleanser.IP_COLUMN_NAME, currentCustomers, columnsIncluded,
                    Cleanser.INDEX_ALGO_DOUBLE_META_PHONE);

            long estimatedTime = System.currentTimeMillis() - startTime;
            logger.info("Time taken : "+ estimatedTime/1000 + " seconds");

            writerTransformed.close();
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
    private static boolean isCustomer(String [][] currentCustomers, String currentValue) {
        for (int i = 0; i < currentCustomers.length; i++) {
            String matchingVal;

            if(currentValue.length() > Cleanser.MIN_NAME_LENGTH) {
                matchingVal = currentCustomers[i][0];
            }
            else {
                matchingVal = currentCustomers[i][1];
            }

            if (currentValue.equals(matchingVal)){
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
    private static String[][] loadCurrentCustomers(CSVReader readerCustomers, CSVWriter writerCustomers,
                                                   int indexAlgorithm, int indexColumn) throws  Exception {

        Map<String,String> Customers = new HashMap<String,String>();
        String [] nextLine;


        while ((nextLine = readerCustomers.readNext()) != null) {
            try {
                //Set  algorithm Index for first column
                switch (indexAlgorithm) {
                    case Cleanser.INDEX_ALGO_DOUBLE_META_PHONE:
                        Customers.put(CustomMatchingUtility.Convert(nextLine[indexColumn],
                                      CustomMatchingUtility.DoubleMetaphoneAlgorithm), nextLine[indexColumn]);
                        break;
                    case Cleanser.INDEX_ALGO_META_PHONE:
                        Customers.put(CustomMatchingUtility.Convert(nextLine[indexColumn],
                                      CustomMatchingUtility.MetaphoneAlgorithm), nextLine[indexColumn]);
                        break;
                    default:
                        Customers.put(CustomMatchingUtility.Convert(nextLine[indexColumn],
                                      CustomMatchingUtility.SoundexAlgorithm), nextLine[indexColumn]);
                        break;
                }
            }
            catch (IllegalArgumentException ex) {
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
     * @param writerTransformed  output transform csv file of new customer activities
     * @param writerNotTransformed output not transformed/ignored csv file
     * @param indexColumnName indexing column name by algorithm
     * @param columnsIncluded Including column names for transformation
     * @param indexAlgorithm Specified algorithm for indexing
     * @throws IOException
     */
    private static void cleanse(CSVReader reader,CSVWriter writerTransformed,
                                CSVWriter writerNotTransformed, String indexColumnName, String indexOutputColumnName,
                                String isCutomerColumnName, String isValidCountryColumnName, String countryColumnName,
                                String ipColumnName,  String[][] currentCustomer, String [] columnsIncluded,
                                int indexAlgorithm ) throws Exception {
        int totalCounter = 0;
        int transformedCounter = 0;
        int currentCustomerActionCounter = 0;


        int columnIndex ;
        int countryColumnIndex;
        int ipColumnIndex;
        int generatedColumnCount;


        int []columnIncludedIndexes = new int[columnsIncluded.length];

        ValidationUtility validator = new ValidationUtility();

        //Writing Header row for output files
        String [] nextLine = reader.readNext();
        writerNotTransformed.writeNext(nextLine);

        //Add one more column for algorithm index to specified column array and write as header
        List<String> list = new LinkedList<String>(Arrays.asList(columnsIncluded));

        list.add(0, indexOutputColumnName);
        list.add(1, isCutomerColumnName);
        list.add(2, isValidCountryColumnName);


        generatedColumnCount = list.size() - columnIncludedIndexes.length;

        writerTransformed.writeNext(list.toArray(new String[indexColumnName.length()+1]));

        //Get the column index of given column name
        columnIndex = Arrays.asList(nextLine).indexOf(indexColumnName);
        countryColumnIndex = Arrays.asList(nextLine).indexOf(countryColumnName);
        ipColumnIndex = Arrays.asList(nextLine).indexOf(ipColumnName);

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
                if (nextLine.length >= columnIncludedIndexes.length && !(nextLine[columnIndex].equals(""))) {

                    //Initialize output with specified columns plus one more column for algorithm index
                    String[] outputLine = new String[columnIncludedIndexes.length + 3];

                    try {


                        //If column value length is greater than specified minimum length then index it
                        if (nextLine[columnIndex].length() > Cleanser.MIN_NAME_LENGTH) {
                            //Set  algorithm Index for first column
                                switch (indexAlgorithm) {
                                    case Cleanser.INDEX_ALGO_DOUBLE_META_PHONE:
                                        outputLine[0] = CustomMatchingUtility.Convert(nextLine[columnIndex],
                                                                        CustomMatchingUtility.DoubleMetaphoneAlgorithm);
                                        break;
                                    case Cleanser.INDEX_ALGO_META_PHONE:
                                        outputLine[0] = CustomMatchingUtility.Convert(nextLine[columnIndex],
                                                                        CustomMatchingUtility.MetaphoneAlgorithm);
                                        break;
                                    default:
                                        outputLine[0] = CustomMatchingUtility.Convert(nextLine[columnIndex],
                                                                        CustomMatchingUtility.SoundexAlgorithm);
                                        break;
                                }
                        } else {
                            //Else Assign default index value
                            outputLine[0] = Cleanser.MIN_INDEX_VAL;
                        }

                        if (outputLine[0] != null) {

                            boolean isExistingCustomer = isCustomer(currentCustomer, outputLine[0]);
                            boolean isValidIp = false;

                            outputLine[1] = String.valueOf(isExistingCustomer);

                            if(enableIpValidate){
                                isValidIp = validator.countryByIpAddressValidation(nextLine[ipColumnIndex],
                                        nextLine[countryColumnIndex]);
                            }

                            outputLine[2] = String.valueOf(isValidIp);

                            //Set specified columns for rest
                            for (int i = generatedColumnCount;
                                 i < columnIncludedIndexes.length + generatedColumnCount; i++) {
                                //Check include index is available on readLine
                                if (nextLine.length > columnIncludedIndexes[i - generatedColumnCount]) {
                                    outputLine[i] = nextLine[columnIncludedIndexes[i - generatedColumnCount]];
                                } else {
                                    outputLine[i] = "";
                                }
                            }

                            transformedCounter++;

                            if (isExistingCustomer) {
                                currentCustomerActionCounter++;
                            }

                            writerTransformed.writeNext(outputLine);
                        }
                        else
                        {
                            writerNotTransformed.writeNext(nextLine);
                        }



                    } catch (IllegalArgumentException ex) {
                        //handles algorithm encode exceptions
                        writerNotTransformed.writeNext(nextLine);
                    }

                } else {
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
