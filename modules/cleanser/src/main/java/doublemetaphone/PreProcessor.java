package doublemetaphone;

import org.apache.commons.codec.EncoderException;

/**
 * Created by pumudu on 11/18/14.
 */
public class PreProcessor {

    public static void main(String[] args) {


        try {
            String metaphoneValueForMicrosoft = MetaphoneUtility.MetaphoneConvert("microsoft");
            String metaphoneValueForMicrosft  = MetaphoneUtility.MetaphoneConvert("microsof");


            System.out.println("------ metaphone encode value ------------");
            System.out.println("metaphone value for microsoft : " + metaphoneValueForMicrosft);
            System.out.println("metaphone value for microsft : " + metaphoneValueForMicrosoft);

            String doubleMetaphoneForMicrosoft = DoubleMetaphoneUtility.DoubleMetaphoneConvert("microsoft");
            String doubleMetaphoneForMicosoft  = DoubleMetaphoneUtility.DoubleMetaphoneConvert("micrsoft");

            System.out.println("------ double metaphone encode value ----");
            System.out.println("double metaphone value for microsoft : " + doubleMetaphoneForMicrosoft);
            System.out.println("double metaphone value for micosoft  : " + doubleMetaphoneForMicosoft);

        } catch (EncoderException e) {
            e.printStackTrace();
        }


    }


}
