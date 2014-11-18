import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;

/**
 * Created by tharik on 11/17/14.
 */
public class Cleanser {

    public static void main(String[] args)
    {
        try {

            String csvPath = "/Users/tharik/Desktop/machine learning/Archive/Activity behaviou_Tier2_20141015.csv";
            String columnName = "Company";


            CSVReader reader = new CSVReader(new FileReader(csvPath));
            String [] nextLine;

            int counter = 0;

            while ((nextLine = reader.readNext()) != null) {
                System.out.println(counter++  + " - " +Soundex.Convert(nextLine[4]) + " - " +nextLine[4] );
            }

        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
