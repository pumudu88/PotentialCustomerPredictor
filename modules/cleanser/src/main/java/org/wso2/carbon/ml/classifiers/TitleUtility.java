package org.wso2.carbon.ml.classifiers;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pumudu on 12/3/14.
 */
public class TitleUtility {

    private static final Log logger = LogFactory.getLog(TitleUtility.class);

    public static final int NotRelevant = 0;
    public static final int Junior = 1;
    public static final int Senior = 2;

    static List<String> seniorTitleList = new ArrayList<String>();
    static List<String> juniorTitleList = new ArrayList<String>();
    static List<String> notReleventTitleList = new ArrayList<String>();

    private ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    private String titleClassifierFileName = "titleClassifier.csv";

    public TitleUtility() {
        loadCompanySuffixFromCsv(classloader.getResource(titleClassifierFileName).getPath());

    }

    public Integer titleClassifier(String title) {

        int returnValue = TitleUtility.Junior;

        if (seniorTitleList.contains(title.toLowerCase())) {
            returnValue = TitleUtility.Senior;
        }

        if (juniorTitleList.contains(title.toLowerCase())) {
            returnValue = TitleUtility.Junior;
        }

        if (notReleventTitleList.contains(title.toLowerCase())) {
            returnValue = TitleUtility.NotRelevant;
        }


        return returnValue;

    }

    public void loadCompanySuffixFromCsv(String csvFilePath) {

        String[] nextLine;

        try {

            CSVReader reader = new CSVReader(
                    new InputStreamReader(new FileInputStream(csvFilePath), "UTF-8"), ',',
                    CSVReader.DEFAULT_QUOTE_CHARACTER, CSVReader.DEFAULT_QUOTE_CHARACTER);

            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                seniorTitleList.add(nextLine[0].toString().toLowerCase());
                juniorTitleList.add(nextLine[1].toString().toLowerCase());
                notReleventTitleList.add(nextLine[2].toString().toLowerCase());
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

}
