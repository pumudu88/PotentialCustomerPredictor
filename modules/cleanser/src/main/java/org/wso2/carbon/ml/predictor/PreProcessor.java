package org.wso2.carbon.ml.predictor;

import org.wso2.carbon.ml.algorithms.MetaphoneUtility;
import org.apache.commons.codec.EncoderException;
import org.wso2.carbon.ml.algorithms.DoubleMetaphoneUtility;

/**
 * Created by pumudu on 11/18/14.
 */
public class PreProcessor {

    public static void main(String[] args) {


        try {
            String metaphoneValueForMicrosoft = MetaphoneUtility.Convert("microsoft");
            String metaphoneValueForMicrosft  = MetaphoneUtility.Convert("microsof");


            String metaphoneForMicrosoft      = MetaphoneUtility.StringConvert("microsoft");
            int codeLengthMetaphone           = MetaphoneUtility.getMaxCodeLen();


            System.out.println("------ metaphone encode value ------------");
            System.out.println("metaphone value for microsoft : " + metaphoneValueForMicrosft);
            System.out.println("metaphone value for microsft : " + metaphoneValueForMicrosoft);
            System.out.println("code length metaphone : " + codeLengthMetaphone );

            MetaphoneUtility.setMaxCodeLen(5);
            codeLengthMetaphone           = MetaphoneUtility.getMaxCodeLen();
            System.out.println("code length metaphone : " + codeLengthMetaphone );
            metaphoneValueForMicrosft  = MetaphoneUtility.Convert("microsof");
            System.out.println("metaphone value for microsoft : " + metaphoneValueForMicrosft);




            String doubleMetaphoneForMicrosoft = DoubleMetaphoneUtility.Convert("microsoft");
            String doubleMetaphoneForMicosoft  = DoubleMetaphoneUtility.Convert("micrsoft");

            System.out.println("------ double metaphone encode value ----");
            System.out.println("double metaphone value for microsoft : " + doubleMetaphoneForMicrosoft);
            System.out.println("double metaphone value for micosoft  : " + doubleMetaphoneForMicosoft);

        } catch (EncoderException e) {
            e.printStackTrace();
        }


    }


}