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

            long startTime = System.currentTimeMillis();

            CSVReader reader = new CSVReader(new FileReader(csvPath));
            String []header = reader.readNext();


            String [] nextLine;

            int counter = 0;

            while ((nextLine = reader.readNext()) != null) {


                if(nextLine.length > 4 && nextLine[4] != null) {
                    System.out.println(counter++ + " - " + SoundexMatch.Convert(nextLine[4]) + " - " + nextLine[4]);
                }
                else{
                    System.out.println(counter++ + nextLine[0]);
                }
            }

            long estimatedTime = System.currentTimeMillis() - startTime;
            System.out.println("Time taken : "+ estimatedTime/1000 + " seconds");

        }
        catch(Exception ex)
        {
            System.out.println("Error Occured " +ex);
        }
    }
}
