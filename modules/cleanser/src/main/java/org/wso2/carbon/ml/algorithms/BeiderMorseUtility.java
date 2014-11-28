package org.wso2.carbon.ml.algorithms;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.bm.BeiderMorseEncoder;
import org.apache.commons.codec.language.bm.NameType;
import org.apache.commons.codec.language.bm.RuleType;

/**
 * Created by pumudu on 11/27/14.
 */
public class BeiderMorseUtility {

    private static BeiderMorseEncoder beiderMorse = new BeiderMorseEncoder();


    public static final RuleType ExactRuleType = RuleType.EXACT;

    public static final RuleType ApproxRuleType = RuleType.APPROX;

    public static String Convert(String input) throws EncoderException {

        return beiderMorse.encode(input);
    }


    public static NameType getNameType() throws EncoderException {
        return beiderMorse.getNameType();
    }

    public static RuleType getRuleType() throws EncoderException {
        return beiderMorse.getRuleType();
    }

    public static boolean isConcat() throws EncoderException {
        return beiderMorse.isConcat();
    }

    public static void setConcat(boolean concat) throws EncoderException {
        beiderMorse.setConcat(concat);
    }

    public static void setNameType(NameType nameType) throws EncoderException {

        beiderMorse.setNameType(nameType);
    }

    public static void setRuleType(RuleType ruleType) throws EncoderException {
        beiderMorse.setRuleType(ruleType);
    }

    public static void setMaxPhonemes(int maxPhonemes) throws EncoderException {
        beiderMorse.setMaxPhonemes(maxPhonemes);
    }


}
