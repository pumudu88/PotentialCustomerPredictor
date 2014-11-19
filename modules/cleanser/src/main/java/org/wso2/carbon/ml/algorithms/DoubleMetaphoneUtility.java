package org.wso2.carbon.ml.algorithms;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.DoubleMetaphone;


/**
 * Created by pumudu on 11/18/14.
 */
public class DoubleMetaphoneUtility {



    public static String DoubleMetaphoneConvert(String input) throws EncoderException
    {
        DoubleMetaphone doubleMetaphone = new DoubleMetaphone();
        return doubleMetaphone.encode(input);
    }


}
