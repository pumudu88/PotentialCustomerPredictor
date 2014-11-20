package org.wso2.carbon.ml.algorithms;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.DoubleMetaphone;


/**
 * Created by pumudu on 11/18/14.
 */
public class DoubleMetaphoneUtility {

    private static DoubleMetaphone doubleMetaphone = new DoubleMetaphone();

    public static String Convert(String input) throws EncoderException
    {

        return doubleMetaphone.encode(input);
    }

    public static String StringConvert(String input) throws EncoderException
    {
        return doubleMetaphone.doubleMetaphone(input);
    }

    public static int getMaxCodeLen() throws EncoderException
    {
        return doubleMetaphone.getMaxCodeLen();
    }

    public static void setMaxCodeLen(int length) throws EncoderException
    {
        doubleMetaphone.setMaxCodeLen(length);
    }


}
