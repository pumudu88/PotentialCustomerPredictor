package org.wso2.carbon.ml.algorithms;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Metaphone;

/**
 * Created by pumudu on 11/18/14.
 */
public class MetaphoneUtility {

   private static Metaphone metaphone = new Metaphone();

    public static String Convert(String input) throws EncoderException
    {
        return metaphone.encode(input);
    }

    public static String StringConvert(String input) throws EncoderException
    {
        return metaphone.metaphone(input);
    }

    public static int getMaxCodeLen() throws EncoderException
    {
        return metaphone.getMaxCodeLen();
    }

    public static void setMaxCodeLen(int length) throws EncoderException
    {
        metaphone.setMaxCodeLen(length);
    }





}
