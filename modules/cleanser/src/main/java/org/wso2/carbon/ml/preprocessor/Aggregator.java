package org.wso2.carbon.ml.preprocessor;

/**
 * Created by tharik on 11/28/14.
 */
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.ml.classifiers.TitleUtility;
import org.wso2.carbon.ml.model.Customer;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class Aggregator {

    public static final String INDEX_COLUMN_INPUT = "Index";
    public static final String ACTIVITY_COLUMN_NAME = "Link";
    public static final String TITLE_COLUMN_NAME ="Title";
    public static final String COMPANY_COLUMN_NAME = "Company";
    public static final String IS_CUSTOMER_COLUMN_NAME = "Is Customer";
    public static final String JOINED_DATE_COLUMN_NAME = "Joined Date";
    public static final String ACTIVITY_TIME_STAMP_COLUMN_NAME = "Activity date/time";
    public static final String COUNTRY_COLUMN = "Country";
    public static final String DATE_FORMAT_ACTIVITY = "MMM dd yyyy hh:mma";
    public static final String DATE_FORMAT_JOINED_DATE = "MM/DD/YYYY";
    public static final String KEY_WORD_DOWNLOADS = "downloads";
    public static final String KEY_WORD_WHITE_PAPERS = "whitepapers";
    public static final String KEY_WORD_TUTORIALS = "tutorials";
    public static final String KEY_WORD_WORKSHOPS = "workshops";
    public static final String KEY_WORD_CASE_STUDIES = "casestudies";
    public static final String KEY_WORD_PRODUCT_PAGES = "productpages";
    public static final char CSV_SEPERATOR = ',';
    public static final String CSV_CHARACTER_FORMAT = "UTF-8";
    public static final int ACTIVITY_NUMBER = 100;
    public static final boolean SKIP_AFTER_JOIN_ACTIVITIES = false;
    public static final boolean USE_NUMERIC_FOR_BOOLEAN = false;

    public static String csvPath = "/Users/tharik/Desktop/machine learning/Archive/";
    public static String csvAggregate = "Aggregate.csv";
    public static String csvAggregateCustomers = "AggregateCustomers.csv";
    public static String csvAggregateNonCustomers = "AggregateNonCustomers.csv";
    private static String [] headers  = {"Company Index", "Company Name", "Country 1", "Country 2", "Country 3",
                                         "Is Customer", "Joined Date", "downloads", "whitepapers", "tutorials",
                                         "workshops", "casestudies", "productpages", "other", "totalActivities","seniorTitleCount",
                                        "juniorTitleCount","Median between two Activities", "Max between 2 activities", "Time since 100th activity"};


    private static TitleUtility titleUtil = new TitleUtility();
    private static final Log logger = LogFactory.getLog(Cleanser.class);

    public static void main (String[] args) throws IOException {

        long startTime = System.currentTimeMillis();

        transformCsv(Cleanser.csvPath + Cleanser.csvWriteTransfomed);

        long estimatedTime = System.currentTimeMillis() - startTime;
        logger.info("Time taken : "+ estimatedTime/1000 + " seconds");
    }

    /**
     * Aggregate given CSV File
     * @param csvFile
     * @throws IOException
     */
    public static void transformCsv (String csvFile) throws IOException {
        mapToCsv(getCsvMap(csvFile));
    }

    /**
     * Produce a hash map by given csv path
     * @param csvPath input csv
     * @return produced has map
     * @throws IOException
     */
    private static HashMap<String, Customer> getCsvMap (String csvPath) throws IOException {

        HashMap<String, Customer> csvMap = new HashMap<String, Customer>();

        CSVReader reader=new CSVReader(
                new InputStreamReader(new FileInputStream(csvPath), CSV_CHARACTER_FORMAT),
                                           Aggregator.CSV_SEPERATOR, CSVReader.DEFAULT_QUOTE_CHARACTER,
                                           CSVReader.DEFAULT_QUOTE_CHARACTER);

        String[] nextLine = reader.readNext();
        int linkColumnIndex = Arrays.asList(nextLine).indexOf(Aggregator.ACTIVITY_COLUMN_NAME);
        int titleIndex  = Arrays.asList(nextLine).indexOf(Aggregator.TITLE_COLUMN_NAME);
        int companyNameIndex = Arrays.asList(nextLine).indexOf(Aggregator.COMPANY_COLUMN_NAME);
        int companyIndexColumnIndex = Arrays.asList(nextLine).indexOf(Aggregator.INDEX_COLUMN_INPUT);
        int isCustomerIndex = Arrays.asList(nextLine).indexOf(Aggregator.IS_CUSTOMER_COLUMN_NAME);
        int joinedDateIndex = Arrays.asList(nextLine).indexOf(Aggregator.JOINED_DATE_COLUMN_NAME);
        int activityTimeStampIndex = Arrays.asList(nextLine).indexOf(Aggregator.ACTIVITY_TIME_STAMP_COLUMN_NAME);
        int CountryIndex = Arrays.asList(nextLine).indexOf(Aggregator.COUNTRY_COLUMN);

        try {
            while ((nextLine = reader.readNext()) != null) {

                if (nextLine.length > 0) {
                    try {
                        String companyIndex = nextLine[companyIndexColumnIndex].trim();
                        String actionsType;
                        Customer columnValues = csvMap.get(companyIndex);

                        if (columnValues == null) {
                            columnValues = new Customer();
                        }

                        if(nextLine.length > linkColumnIndex && !nextLine[linkColumnIndex].equals("")) {

                            actionsType = nextLine[linkColumnIndex].trim();

                            if (actionsType.equals("")) {
                                columnValues.setOtherActivityCount(columnValues.getOtherActivityCount() + 1);
                            } else {

                                if (actionsType.contains(Aggregator.KEY_WORD_DOWNLOADS)) {
                                    columnValues.setDownloadActivityCount(
                                            columnValues.getDownloadActivityCount() + 1);
                                }
                                else  if (actionsType.contains(Aggregator.KEY_WORD_WHITE_PAPERS)) {
                                    columnValues.setWhitePaperActivityCount(
                                            columnValues.getWhitePaperActivityCount() + 1);
                                }
                                else  if (actionsType.contains(Aggregator.KEY_WORD_TUTORIALS)) {
                                    columnValues.setTutorialActivityCount(
                                            columnValues.getTutorialActivityCount() + 1);
                                }
                                else  if (actionsType.contains(Aggregator.KEY_WORD_WORKSHOPS)) {
                                    columnValues.setWorkshopActivityCount(
                                            columnValues.getWorkshopActivityCount() + 1);
                                }
                                else  if (actionsType.contains(Aggregator.KEY_WORD_CASE_STUDIES)) {
                                    columnValues.setCaseStudiesActivityCount(
                                            columnValues.getCaseStudiesActivityCount() + 1);
                                }
                                else  if (actionsType.contains(Aggregator.KEY_WORD_PRODUCT_PAGES)) {
                                    columnValues.setProductPagesActivityCount(
                                            columnValues.getProductPagesActivityCount() + 1);
                                }
                                else {
                                    columnValues.setOtherActivityCount(columnValues.getOtherActivityCount() + 1);
                                }

                                if (!companyIndex.equals(INDEX_COLUMN_INPUT)) {
                                    csvMap.put(companyIndex, columnValues);
                                }
                            }
                        }
                        else  {
                            columnValues.setOtherActivityCount(columnValues.getOtherActivityCount() + 1);
                        }

                        if( Integer.parseInt(nextLine[titleIndex]) == TitleUtility.Senior) {
                            columnValues.setSeniorTitleCount(columnValues.getSeniorTitleCount() + 1);
                        }
                        else if( Integer.parseInt(nextLine[titleIndex]) == titleUtil.Junior) {
                            columnValues.setJuniorTitleCount(columnValues.getJuniorTitleCount() + 1);
                        }

                        columnValues.setCompanyName(nextLine[companyNameIndex]);
                        columnValues.setIsCustomer(Customer.stringToBoolean(nextLine[isCustomerIndex]));
                        columnValues.setJoinedDate(nextLine[joinedDateIndex]);

                        SimpleDateFormat activityDateFormat = new SimpleDateFormat(DATE_FORMAT_ACTIVITY);
                        SimpleDateFormat joinedDateFormat = new SimpleDateFormat(DATE_FORMAT_JOINED_DATE);

                        try{

                        if (!nextLine[activityTimeStampIndex].equals("")) {

                            Date activityTimeStamp = activityDateFormat.parse(nextLine[activityTimeStampIndex].trim());

                            if (!columnValues.getIsCustomer())
                            {
                                columnValues.addActivityTimeStamp(activityTimeStamp);
                            }
                             //Check weather skipping after join activity enables for existing customers
                             else if (columnValues.getIsCustomer() && !Aggregator.SKIP_AFTER_JOIN_ACTIVITIES) {
                                 columnValues.addActivityTimeStamp(activityTimeStamp);
                             }
                             //If skipping is enabled but join date is after activity date for existing customers
                             else if (columnValues.getIsCustomer() && !nextLine[joinedDateIndex].equals("") &&
                                     joinedDateFormat.parse(nextLine[joinedDateIndex]).after(activityTimeStamp)) {
                                 columnValues.addActivityTimeStamp(activityTimeStamp);
                             }
                        }

                        columnValues.addCountry(nextLine[CountryIndex]);
                        }
                        catch (ParseException ex) {

                        }
                    }
                    catch (Exception ex) {
                        logger.error(ex);
                    }
                }
            }
        }
        catch (Exception ex) {
            logger.error(ex);
        }
        return csvMap;
    }

    /**
     * Create CSV from the given hash map
     * @param csvMap Hash map which needs to be converted to a CSV
     */
    private static void mapToCsv (HashMap<String, Customer> csvMap) {
        try {

            int totalCustomerCount = 0;
            int existingCustomerCount = 0;

            CSVWriter writerAggregate = new CSVWriter(new FileWriter(csvPath + csvAggregate),
                    CSV_SEPERATOR, CSVWriter.NO_QUOTE_CHARACTER);

            CSVWriter writerAggregateCustomers = new CSVWriter(new FileWriter(csvPath + csvAggregateCustomers),
                    CSV_SEPERATOR, CSVWriter.NO_QUOTE_CHARACTER);

            CSVWriter writerAggregateNonCustomers = new CSVWriter(new FileWriter(csvPath + csvAggregateNonCustomers),
                    CSV_SEPERATOR, CSVWriter.NO_QUOTE_CHARACTER);

            //Write headers to CSV
            writerAggregate.writeNext(headers);
            writerAggregateCustomers.writeNext(headers);
            writerAggregateNonCustomers.writeNext(headers);

            for (String company : csvMap.keySet()) {
                Customer columnValues = csvMap.get(company);

                if (!company.equals("")) {

                    String[] outputLine = new String[headers.length];

                    outputLine[0] = company;
                    outputLine[1] = columnValues.getCompanyName();
                    String [] countries = columnValues.getTopCountries(3);

                     for (int i =0; i < countries.length; i++) {
                        outputLine[2 + i] = countries[i];
                     }

                    if(Aggregator.USE_NUMERIC_FOR_BOOLEAN) {
                        outputLine[5] = Customer.booleanToString(columnValues.getIsCustomer());
                    }
                    else
                    {
                        outputLine[5] = String.valueOf(columnValues.getIsCustomer());
                    }

                    outputLine[6] = columnValues.getJoinedDate();
                    outputLine[7] = String.valueOf(columnValues.getDownloadActivityCount());
                    outputLine[8] = String.valueOf(columnValues.getWhitePaperActivityCount());
                    outputLine[9] = String.valueOf(columnValues.getTutorialActivityCount());
                    outputLine[10] = String.valueOf(columnValues.getWorkshopActivityCount());
                    outputLine[11] = String.valueOf(columnValues.getCaseStudiesActivityCount());
                    outputLine[12] = String.valueOf(columnValues.getProductPagesActivityCount());
                    outputLine[13] = String.valueOf(columnValues.getOtherActivityCount());
                    outputLine[14] = String.valueOf(columnValues.getDownloadActivityCount()
                                                    + columnValues.getWhitePaperActivityCount()
                                                    + columnValues.getTutorialActivityCount()
                                                    + columnValues.getWorkshopActivityCount()
                                                    + columnValues.getCaseStudiesActivityCount()
                                                    + columnValues.getProductPagesActivityCount()
                                                    + columnValues.getOtherActivityCount());
                    outputLine[15] = String.valueOf(columnValues.getSeniorTitleCount());
                    outputLine[16] = String.valueOf(columnValues.getJuniorTitleCount());
                    outputLine[17] = String.valueOf(columnValues.getMedianTimeBetweenTwoActivities());
                    outputLine[18] = String.valueOf(columnValues.getMaxTimeBetweenTwoActivities());




                    if (columnValues.getActivityTimeStamps().size() >= Aggregator.ACTIVITY_NUMBER) {
                        Date hundredthActicity = columnValues.getActivityTimeStamps().get(Aggregator.ACTIVITY_NUMBER - 1);
                        Date today = new Date();
                        outputLine[19] = String.valueOf(columnValues.getDateDiff(hundredthActicity, today, Customer.TIME_UNIT));

                    }
                    else {
                        outputLine[19] = "0";
                    }

                    writerAggregate.writeNext(outputLine);

                    if (columnValues.getIsCustomer()) {
                        existingCustomerCount++;
                        writerAggregateCustomers.writeNext(outputLine);
                    }
                    else {
                        writerAggregateNonCustomers.writeNext(outputLine);
                    }
                }
                totalCustomerCount++;
            }
            writerAggregate.close();

           logger.info("Total Customers : " + totalCustomerCount + " . Existing Customers : " + existingCustomerCount);

        }
        catch(Exception ex) {
            logger.error(ex);
        }
    }


}
