package org.wso2.carbon.ml.algorithms;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.MatchRatingApproachEncoder;

/**
 * Created by pumudu on 11/26/14.
 */
public class MatchRatingApproachUtility {

    private static MatchRatingApproachEncoder matchRatingApproach = new MatchRatingApproachEncoder();

    public static String Convert(String input) throws EncoderException {
        return matchRatingApproach.encode(input);
    }

    public static boolean isStringsEqual(String name1, String name2) {

        return matchRatingApproach.isEncodeEquals(name1, name2);
    }

}
