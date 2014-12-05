package org.wso2.carbon.ml.preprocessor;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.ml.algorithms.CustomMatchingUtility;
import org.wso2.carbon.ml.algorithms.DoubleMetaphoneUtility;
import org.wso2.carbon.ml.classifiers.TitleUtility;
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
    public static final String JOINED_DATE_COLUMN_NAME = "Joined Date";

    public static final String COUNTRY_COLUMN_NAME = "Country";
    public static final String IP_COLUMN_NAME ="IpAddress";
    public static final String TITLE_COLUMN_NAME ="Title";

    public static final String MIN_INDEX_VAL = "-1";
    public static final int DOUBLE_META_PHONE_THRESHOLD = 10;

    public static final char CSV_SEPERATOR = ',';
    public static final String CSV_CHARACTER_FORMAT = "UTF-8";

    public static String csvPath = "/Users/tharik/Desktop/machine learning/Archive/";
    public static String csvReadFile = "ContactWebsiteBehaviour_tier1_20141014.csv";
    public static String csvReadCustomerFile = "customerBase.csv";
    public static String csvWriteCustomerFile = "customersIndexed.csv";
    public static String csvCompanySuffixFile =  "company_suffix.csv";

    public static String csvWriteTransfomed = "transformed.csv";
    public static String csvWriteNotTransformedFile = "notTransformed.csv";
    public static String [] columnsIncluded = { "Title", "Company", "Country", "IpAddress",
                                                "Activity date/time", "Link"};

    private static boolean enableIpValidate = false;


    private static CustomMatchingUtility customMatching = new CustomMatchingUtility();
    private static TitleUtility titleUtil = new TitleUtility();


    public static void main(String[] args) {
        try {

            Map<String,String[]> currentCustomers;



            DoubleMetaphoneUtility.setMaxCodeLen(DOUBLE_META_PHONE_THRESHOLD);
            customMatching.loadCompanySuffixFromCsv(csvPath + csvCompanySuffixFile);

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
                    Cleanser.COUNTRY_COLUMN_NAME, Cleanser.IP_COLUMN_NAME, Cleanser.TITLE_COLUMN_NAME,
                    Cleanser.JOINED_DATE_COLUMN_NAME, currentCustomers, columnsIncluded,
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
    private static String isCustomer(Map<String,String[]> currentCustomers, String currentValue) {


        String [] rowVal = currentCustomers.get(currentValue);


       if (rowVal == null)
       {
           return "";
       }
       else
       {
           return rowVal[1];
       }


    }

    /**
     * Load all the current customers from specified CSV read file and convert to indexes into a string array
     * @param readerCustomers input CSV file of existing customers
     * @param indexAlgorithm indexing algorithm
     * @param indexColumn indexing column index
     * @return
     * @throws Exception
     */
    private static  Map<String,String[]> loadCurrentCustomers(CSVReader readerCustomers, CSVWriter writerCustomers,
                                                   int indexAlgorithm, int indexColumn) throws  Exception {

        Map<String,String[]> Customers = new HashMap<String,String[]>();
        String [] nextLine;


        nextLine = readerCustomers.readNext();


        while ((nextLine = readerCustomers.readNext()) != null) {
            try {

                String [] rowVal = new String[2];

                rowVal[0] = nextLine[indexColumn];
                rowVal[1] = nextLine[1];

                //Set  algorithm Index for first column
                switch (indexAlgorithm) {
                    case Cleanser.INDEX_ALGO_DOUBLE_META_PHONE:
                        Customers.put(customMatching.Convert(nextLine[indexColumn],
                                CustomMatchingUtility.DoubleMetaphoneAlgorithm), rowVal);
                        break;
                    case Cleanser.INDEX_ALGO_META_PHONE:
                        Customers.put(customMatching.Convert(nextLine[indexColumn],
                                      CustomMatchingUtility.MetaphoneAlgorithm), rowVal);
                        break;
                    default:
                        Customers.put(customMatching.Convert(nextLine[indexColumn],
                                      CustomMatchingUtility.SoundexAlgorithm), rowVal);
                        break;
                }
            }
            catch (IllegalArgumentException ex) {
                //handles algorithm encode exceptions
                logger.error(ex);
            }

        }
        return Customers;
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
                                String ipColumnName, String titleColumnName, String joinedDateColumnName,
                                Map<String,String[]> currentCustomer, String [] columnsIncluded,
                                int indexAlgorithm ) throws Exception {
        int totalCounter = 0;
        int transformedCounter = 0;
        int currentCustomerActionCounter = 0;


        int columnIndex ;
        int countryColumnIndex;
        int ipColumnIndex;
        int titleColumnIndex;
        int generatedColumnCount;



        int [] columnIncludedIndexes = new int[columnsIncluded.length];

        ValidationUtility validator = new ValidationUtility();

        //Writing Header row for output files
        String [] nextLine = reader.readNext();
        writerNotTransformed.writeNext(nextLine);

        //Add one more column for algorithm index to specified column array and write as header
        List<String> list = new LinkedList<String>(Arrays.asList(columnsIncluded));

        list.add(0, indexOutputColumnName);
        list.add(1, isCutomerColumnName);
        list.add(2, isValidCountryColumnName);
        list.add(3, joinedDateColumnName);


        generatedColumnCount = list.size() - columnIncludedIndexes.length;

        writerTransformed.writeNext(list.toArray(new String[indexColumnName.length()+1]));

        //Get the column index of given column name
        columnIndex = Arrays.asList(nextLine).indexOf(indexColumnName);
        countryColumnIndex = Arrays.asList(nextLine).indexOf(countryColumnName);
        ipColumnIndex = Arrays.asList(nextLine).indexOf(ipColumnName);
        titleColumnIndex = Arrays.asList(nextLine).indexOf(titleColumnName);

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
                    String[] outputLine = new String[columnIncludedIndexes.length + 4];

                    try {



                        //Set  algorithm Index for first column
                        switch (indexAlgorithm) {
                                    case Cleanser.INDEX_ALGO_DOUBLE_META_PHONE:
                                        outputLine[0] = customMatching.Convert(nextLine[columnIndex],
                                                                        CustomMatchingUtility.DoubleMetaphoneAlgorithm);
                                        break;
                                    case Cleanser.INDEX_ALGO_META_PHONE:
                                        outputLine[0] = customMatching.Convert(nextLine[columnIndex],
                                                                        CustomMatchingUtility.MetaphoneAlgorithm);
                                        break;
                                    default:
                                        outputLine[0] = customMatching.Convert(nextLine[columnIndex],
                                                                        CustomMatchingUtility.SoundexAlgorithm);
                                        break;
                        }


                        if (outputLine[0] != null) {

                            boolean isExistingCustomer;
                            String  isCustomerDate = isCustomer(currentCustomer, outputLine[0]);
                            boolean isValidIp = false;

                            if (isCustomerDate.equals(""))
                            {
                                isExistingCustomer = false;
                            }
                            else
                            {
                                isExistingCustomer = true;
                            }

                            outputLine[1] = String.valueOf(isExistingCustomer);

                            if(enableIpValidate){
                                isValidIp = validator.countryByIpAddressValidation(nextLine[ipColumnIndex],
                                        nextLine[countryColumnIndex]);
                            }

                            outputLine[2] = String.valueOf(isValidIp);
                            outputLine[3] = isCustomerDate;

                            //Set specified columns for output
                            for (int i = generatedColumnCount;
                                 i < columnIncludedIndexes.length + generatedColumnCount; i++) {
                                //Check include index is available on readLine
                                if (nextLine.length > columnIncludedIndexes[i - generatedColumnCount]) {

                                   if (titleColumnIndex == columnIncludedIndexes[i - generatedColumnCount])
                                   {
                                       outputLine[i] = titleUtil.titleClassifier(
                                               nextLine[columnIncludedIndexes[i - generatedColumnCount]]).toString();
                                   }
                                    else
                                   {
                                       outputLine[i] = nextLine[columnIncludedIndexes[i - generatedColumnCount]];
                                   }
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
