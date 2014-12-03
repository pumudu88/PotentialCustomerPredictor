package org.wso2.carbon.ml.classifiers;

/**
 * Created by pumudu on 12/3/14.
 */
public class TitleUtility {

    public static final int Senior = 1;
    public static final int Jounior = 2;

    public static Integer tilteClassifier(String title) {

        int returnValue;

        if(title.contains("senior")) {
            returnValue = TitleUtility.Senior;
        } else {
            returnValue = TitleUtility.Jounior;
        }


        return returnValue;

    }

}
