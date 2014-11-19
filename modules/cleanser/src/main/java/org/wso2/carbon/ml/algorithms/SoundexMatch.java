package org.wso2.carbon.ml.algorithms;

import java.text.Normalizer;
import java.util.regex.Pattern;
import org.apache.commons.codec.language.Soundex;

/**
 * Created by tharik on 11/17/14.
 */
public class SoundexMatch {

    public static String Convert(String input)
    {
        try {
            Soundex soundex = new Soundex();
            return soundex.encode(deAccent(input.trim()));
        }
        catch (Exception ex)
        {
            return "";
        }

    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}
