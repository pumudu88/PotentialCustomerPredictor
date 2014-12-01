package org.wso2.carbon.ml.preprocessor;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.ml.algorithms.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pumudu on 11/18/14.
 */
public class PreProcessor {

    private static final Log logger = LogFactory.getLog(Cleanser.class);

    public static void main(String[] args) {

        String csvCompanySuffixFile = "/Users/pumudu/Documents/Machine Lerning/available data/company_suffix.csv";

        long startTime = System.currentTimeMillis();

        List<String> nameList = new ArrayList<String>();
        nameList.add("Hutchcraft");
        nameList.add("Hadcroft");
        nameList.add("Hatchard");
        nameList.add("Hatcher");
        nameList.add("Hatzar");
        nameList.add("Hedger");
        nameList.add("Hitscher");
        nameList.add("Hodcroft");
        nameList.add("Hadgraft");

        nameList.add("Braz");
        nameList.add("Broz");

        nameList.add("ENIS");
        nameList.add("Emoxa");

        nameList.add("Hilti Befestigungstechnik AG");
        nameList.add("Hewlett-Packard Co");

        nameList.add("Mathew");
        nameList.add("Meth");

        nameList.add("Intel Corp");
        nameList.add("Intel");

        nameList.add("Corp");
        nameList.add("Inc");

        try {

            // set metaphone settings
            System.out.println("------------metaphone and double metaphone settings-----------------");
            MetaphoneUtility.setMaxCodeLen(10);
            DoubleMetaphoneUtility.setMaxCodeLen(10);

            System.out.println("code length metaphone : " + MetaphoneUtility.getMaxCodeLen());
            System.out.println("code length double metaphone : " + DoubleMetaphoneUtility.getMaxCodeLen());

            //set beiderMorse settings
            System.out.println("-------------------beider morse settings----------------------------");

            System.out.println("----------old settings-----------");
            System.out.println("concat status :" + BeiderMorseUtility.isConcat());
            System.out.println("name type     :" + BeiderMorseUtility.getNameType().toString());
            System.out.println("rule type     :" + BeiderMorseUtility.getRuleType().toString());

            System.out.println("---------new settings-----------");
            BeiderMorseUtility.setConcat(false);
            BeiderMorseUtility.setMaxPhonemes(2);
            BeiderMorseUtility.setRuleType(BeiderMorseUtility.ExactRuleType);

            System.out.println("concat status :" + BeiderMorseUtility.isConcat());
            System.out.println("name type     :" + BeiderMorseUtility.getNameType().toString());
            System.out.println("rule type     :" + BeiderMorseUtility.getRuleType().toString());

            System.out.println("----custom matching settings--------");
            CustomMatchingUtility.LoadCompanySuffixFromCsv(csvCompanySuffixFile);

            System.out.println("company suffix csv file path :" + csvCompanySuffixFile);


            System.out.println("-------------test names-------------");

            for (int i = 0; i < nameList.size(); i++) {

                System.out.println("----" + nameList.get(i) + "---");
                System.out.println("soundex          : " + SoundexMatchUtility.Convert(nameList.get(i)));
                System.out.println("metaphone        : " + MetaphoneUtility.Convert(nameList.get(i)));
                System.out.println("double metaphone : " + DoubleMetaphoneUtility.Convert(nameList.get(i)));
                System.out.println("MRA              : " + MatchRatingApproachUtility.Convert(nameList.get(i)));
                System.out.println("BeiderMorse      : " + BeiderMorseUtility.Convert(nameList.get(i)));
                System.out.println("custom utility   : " + CustomMatchingUtility.Convert(nameList.get(i), CustomMatchingUtility.MatchRatingApproachAlgorithm));
            }

            long estimatedTime = System.currentTimeMillis() - startTime;
            logger.info("Time taken : " + estimatedTime / 1000 + " seconds");


        } catch (EncoderException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
