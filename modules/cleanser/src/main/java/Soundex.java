import org.apache.commons.codec.EncoderException;

/**
 * Created by tharik on 11/17/14.
 */
public class Soundex {

    public static String Convert(String input) throws EncoderException
    {
        org.apache.commons.codec.language.Soundex soundex = new org.apache.commons.codec.language.Soundex();
        return soundex.encode(input);

    }
}
