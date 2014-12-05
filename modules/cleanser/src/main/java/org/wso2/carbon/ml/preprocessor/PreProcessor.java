package org.wso2.carbon.ml.preprocessor;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.ml.algorithms.*;
import org.wso2.carbon.ml.classifiers.TitleUtility;
import org.wso2.carbon.ml.validations.ValidationUtility;

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

        nameList.add("BBC");
        nameList.add("bbc");
        nameList.add("ACT");

        nameList.add("to");
        nameList.add("hp");
        nameList.add("nsi");


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

            System.out.println("------country name validation test-------------");
            ValidationUtility validation = new ValidationUtility();
            System.out.println(validation.countryByIpAddressValidation("66.248.180.14", "Virgin Islands").toString());

            System.out.println(validation.countryByIpAddressValidation("202.43.124.98", "Pakistan"));
            System.out.println(validation.countryByIpAddressValidation("208.77.165.40", "United States"));
            System.out.println(validation.countryByIpAddressValidation("203.100.58.40", "Australia"));
//            for(int i = 1; i < 1200000; i++) {
//                validation.countryByIpAddressValidation("115.84.146.69", "Maldives");
//                validation.countryByIpAddressValidation("34534g4","4353453df");
//                if( i == 100000){
//                    System.out.println(i);
//                }
//                if( i == 200000){
//                    System.out.println(i);
//                }
//                if( i == 300000){
//                    System.out.println(i);
//                }
//                if( i == 500000){
//                    System.out.println(i);
//                }
//                if( i == 700000){
//                    System.out.println(i);
//                }
//                if( i == 1000000){
//                    System.out.println(i);
//                }
//
//            }

            System.out.println("-----------------Title classifier-------------------");
            System.out.println("title integer :" + TitleUtility.tilteClassifier("senior sdfsdfs"));

            long estimatedTime = System.currentTimeMillis() - startTime;
            logger.info("Time taken : " + estimatedTime / 1000 + " seconds");


        } catch (EncoderException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
