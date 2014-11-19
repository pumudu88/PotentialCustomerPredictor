package org.wso2.carbon.ml.predictor;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.wso2.carbon.ml.algorithms.SoundexMatch;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
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
            String csvReadFile = "Activity behaviou_Tier2_20141015.csv";
            String csvWriteTransfomedFile = "transformed.csv";
            String csvWriteNotTransformedFile = "notTransformed.csv";

            long startTime = System.currentTimeMillis();

            //CSVReader reader = new CSVReader(new FileReader(csvPath + csvReadFile), ',');

            CSVReader reader=new CSVReader(
                    new InputStreamReader(new FileInputStream(csvPath + csvReadFile), "UTF-8"), ',',CSVReader.DEFAULT_QUOTE_CHARACTER,CSVReader.DEFAULT_QUOTE_CHARACTER);



            CSVWriter writerTransformed = new CSVWriter(new FileWriter(csvPath + csvWriteTransfomedFile), ',', CSVWriter.NO_QUOTE_CHARACTER);
            CSVWriter writerNotTransformed = new CSVWriter(new FileWriter(csvPath + csvWriteNotTransformedFile), ',', CSVWriter.NO_QUOTE_CHARACTER);

            System.out.println("Processing started....");

            Cleanse(reader, writerTransformed, writerNotTransformed, "Company");

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

    private static void Cleanse(CSVReader reader, CSVWriter writerTransformed,  CSVWriter writerNotTransformed, String columnName ) throws IOException
    {
        int columnIndex;

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
                writerTransformed.writeNext(nextLine);
            }
            else{

                writerNotTransformed.writeNext(nextLine);
            }
        }


    }
}
