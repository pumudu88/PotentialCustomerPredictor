/**
 * Created by tharik on 11/17/14.
 */
public class Cleanser {

    public static void main(String[] args)
    {
        try {
            System.out.println(Soundex.Convert("Tharik"));
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
