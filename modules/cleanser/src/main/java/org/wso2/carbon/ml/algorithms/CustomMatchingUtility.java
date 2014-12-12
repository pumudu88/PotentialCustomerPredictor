package org.wso2.carbon.ml.algorithms;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pumudu on 11/28/14.
 */
public class CustomMatchingUtility {

    private static final Log logger = LogFactory.getLog(CustomMatchingUtility.class);

    public static final int BeiderMorseAlgorithm = 1;
    public static final int DoubleMetaphoneAlgorithm = 2;
    public static final int MatchRatingApproachAlgorithm = 3;
    public static final int MetaphoneAlgorithm = 4;
    public static final int SoundexAlgorithm = 5;

    static List<String> companySuffixList = new ArrayList<String>();
    private ClassLoader classloader = Thread.currentThread().getContextClassLoader();

    public static final int MinimumConvertLength = 6;

    public void setCompanySuffix(String companySuffix) {
        companySuffixList.add(companySuffix);
    }


    public CustomMatchingUtility() {

        loadCompanySuffixFromCsv(classloader.getResource("companySuffix.csv").getPath());

        try {
            DoubleMetaphoneUtility.setMaxCodeLen(20);
        } catch (Exception e) {
            logger.error(e);
        }

    }

    /**
     * Convert given string with predefined phonetic algorithm
     *
     * @param input
     * @param algorithmIndex
     * @return phonetic index for input string
     * @throws EncoderException
     */
    public String Convert(String input, int algorithmIndex) throws Exception {

        String encodedValue = "";

        input = removeCompanyNameSuffix(input.toLowerCase().trim());

        if (input.length() > CustomMatchingUtility.MinimumConvertLength) {

            switch (algorithmIndex) {
                case CustomMatchingUtility.BeiderMorseAlgorithm:
                    encodedValue = BeiderMorseUtility.Convert(input);
                    break;
                case CustomMatchingUtility.DoubleMetaphoneAlgorithm:
                    encodedValue = DoubleMetaphoneUtility.Convert(input);
                    break;
                case CustomMatchingUtility.MatchRatingApproachAlgorithm:
                    encodedValue = MatchRatingApproachUtility.Convert(input);
                    break;
                case CustomMatchingUtility.MetaphoneAlgorithm:
                    encodedValue = MetaphoneUtility.Convert(input);
                    break;
                case CustomMatchingUtility.SoundexAlgorithm:
                    encodedValue = SoundexMatchUtility.Convert(input);
                    break;
            }

        } else {
            encodedValue = input;
        }

        return encodedValue;
    }

    /**
     * Remove company suffix from given input string.
     *
     * @param input
     * @return
     */
    private String removeCompanyNameSuffix(String input) {
        StringBuffer result = new StringBuffer();

        String[] splitedCompanyName = input.split("\\s+");

        for (int i = 0; i < splitedCompanyName.length; i++) {

            for (int j = 0; j < companySuffixList.size(); j++) {
                if (splitedCompanyName[i].toUpperCase().equals(companySuffixList.get(j).toUpperCase())) {
                    splitedCompanyName[i] = "";
                }
            }

            result.append(splitedCompanyName[i]);
            result.append(" ");
        }

        String companyWithOutSuffix = result.toString();

        return companyWithOutSuffix.trim();

    }

    /**
     * Load company names to a list from given csv file path
     *
     * @param csvFilePath
     */
    public void loadCompanySuffixFromCsv(String csvFilePath) {

        String[] nextLine;

        try {

            CSVReader reader = new CSVReader(
                    new InputStreamReader(new FileInputStream(csvFilePath), "UTF-8"), ',',
                    CSVReader.DEFAULT_QUOTE_CHARACTER, CSVReader.DEFAULT_QUOTE_CHARACTER);

            while ((nextLine = reader.readNext()) != null) {
                setCompanySuffix(nextLine[0].toString().toLowerCase());
            }

            reader.close();

        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }

    }

    public List<String> getCompanySuffixList() {
        return companySuffixList;
    }


}
