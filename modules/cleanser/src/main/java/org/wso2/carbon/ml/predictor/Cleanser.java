package org.wso2.carbon.ml.predictor;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.wso2.carbon.ml.algorithms.SoundexMatch;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by tharik on 11/17/14.
 */
public class Cleanser {

    public static void main(String[] args)
    {
        try {
            String csvPath = "/Users/tharik/Desktop/machine learning/Archive/";
            String csvReadFile = "backup/Activity behaviou_Tier2_20141015.csv";
            String csvWriteTransfomedFile = "transformed.csv";
            String csvWriteNotTransformedFile = "notTransformed.csv";
            String columnName = "Company";
            int columnIndex;
            long startTime = System.currentTimeMillis();

            //CSVReader reader = new CSVReader(new FileReader(csvPath + csvReadFile), ',');

            CSVReader reader=new CSVReader(
                    new InputStreamReader(new FileInputStream(csvPath + csvReadFile), "UTF-8"), ',');


            CSVWriter writerTransformed = new CSVWriter(new FileWriter(csvPath + csvWriteTransfomedFile), ',');
            CSVWriter writerNotTransformed = new CSVWriter(new FileWriter(csvPath + csvWriteNotTransformedFile), ',');

            System.out.println("Processing started....");

            //Writing Header row for output files
            String [] nextLine = reader.readNext();
            writerTransformed.writeNext(nextLine);
            writerNotTransformed.writeNext(nextLine);


            int counter = 0;

            //Get the column index of given column name
            columnIndex = Arrays.asList(nextLine).indexOf(columnName);

            while ((nextLine = reader.readNext()) != null) {


                if(nextLine.length > columnIndex) {

                    String [] outputLine = {String.valueOf(counter++), SoundexMatch.Convert(nextLine[columnIndex]), nextLine[columnIndex] };
                    writerTransformed.writeNext(outputLine);
                }
                else{

                    if (nextLine[0].equals("Scuthheft")) {

                        String cont = "";

                        for (int i = 0; i < nextLine.length; i++) {
                            cont += nextLine[i] + " ";
                        }

                        System.out.println(cont);
                    }


                    writerNotTransformed.writeNext(nextLine);
                }
            }

            long estimatedTime = System.currentTimeMillis() - startTime;
            System.out.println("Time taken : "+ estimatedTime/1000 + " seconds");

            writerTransformed.close();
            writerNotTransformed.close();
            reader.close();

        }
        catch(Exception ex)
        {
            System.out.println("Error Occured " +ex);
        }
    }
}
