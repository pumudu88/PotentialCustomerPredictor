package org.wso2.carbon.ml.algorithms;

import java.text.Normalizer;
import java.util.regex.Pattern;
import org.apache.commons.codec.language.Soundex;

/**
 * Created by tharik on 11/17/14.
 */
public class SoundexMatchUtility {

    private  static Soundex soundex = new Soundex();

    public static String Convert(String input) throws Exception
    {
       return soundex.encode(deAccent(input.trim()));
    }

    public static Object Convert(Object input) throws Exception
    {
        return soundex.encode(input);
    }

    public static int Difference(String s1, String s2) throws Exception
    {
        return soundex.difference(s1, s2);
    }

    /**
     * Used to deAccent words to english letters
     */
    private static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}
