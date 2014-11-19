package org.wso2.carbon.ml.algorithms;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Metaphone;

/**
 * Created by pumudu on 11/18/14.
 */
public class MetaphoneUtility {


    public static String MetaphoneConvert(String input) throws EncoderException
    {
        Metaphone metaphone = new Metaphone();
        return metaphone.encode(input);

    }



}
