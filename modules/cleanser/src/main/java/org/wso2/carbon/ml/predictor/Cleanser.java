package org.wso2.carbon.ml.predictor;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.wso2.carbon.ml.algorithms.SoundexMatch;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
            String [] columnsIncluded = {"Title","Company","Country","Activity Type","Activity","Activity date/time","IpAddress"};

            long startTime = System.currentTimeMillis();

            CSVReader reader=new CSVReader(
                    new InputStreamReader(new FileInputStream(csvPath + csvReadFile), "UTF-8"), ',',CSVReader.DEFAULT_QUOTE_CHARACTER,CSVReader.DEFAULT_QUOTE_CHARACTER);


            CSVWriter writerTransformed = new CSVWriter(new FileWriter(csvPath + csvWriteTransfomedFile), ',', CSVWriter.NO_QUOTE_CHARACTER);
            CSVWriter writerNotTransformed = new CSVWriter(new FileWriter(csvPath + csvWriteNotTransformedFile), ',', CSVWriter.NO_QUOTE_CHARACTER);

            System.out.println("Processing started....");

            Cleanse(reader, writerTransformed, writerNotTransformed, "Company", columnsIncluded);

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

    private static void Cleanse(CSVReader reader, CSVWriter writerTransformed,  CSVWriter writerNotTransformed, String indexColumnName, String [] columnsIncluded ) throws IOException
    {
        int totalCounter = 0;
        int transformedCounter = 0;
        int columnIndex ;
        int []columnIncludedIndexes = new int[columnsIncluded.length];

        //Writing Header row for output files
        String [] nextLine = reader.readNext();
        writerNotTransformed.writeNext(nextLine);

        //Add one more column for algorithm index to specified column array and write as header
        List<String> list = new LinkedList<String>(Arrays.asList(columnsIncluded));
        list.add(0, "Index");
        writerTransformed.writeNext(list.toArray(new String[indexColumnName.length()+1]));

        //Get the column index of given column name
        columnIndex = Arrays.asList(nextLine).indexOf(indexColumnName);

        for(int i =0; i < nextLine.length; i++)
        {
            nextLine[i] = nextLine[i].trim();
        }

        for(int i = 0; i < columnsIncluded.length; i++)
        {
            columnIncludedIndexes[i] = Arrays.asList(nextLine).indexOf(columnsIncluded[i]);
        }

        while ((nextLine = reader.readNext()) != null) {


            //Check read line is number of required columns and indexing column value is not empty
            if(nextLine.length >= columnIncludedIndexes.length && !(nextLine[columnIndex].equals(""))) {

                //Initialize output with specified columns plus one more column for algorithm index
                String [] outputLine = new String[columnIncludedIndexes.length + 1];

                //Set  algorithm Index for first column
                outputLine[0]  = SoundexMatch.Convert(nextLine[columnIndex]);

                //Set specified columns for rest
                for (int i =1; i < columnIncludedIndexes.length; i++)
                {
                    //Check include index is available on readLine
                    if (nextLine.length > columnIncludedIndexes[i-1])
                    {
                        outputLine[i] = nextLine[columnIncludedIndexes[i-1]];
                    }
                    else
                    {
                        outputLine[i] = "";
                    }
                }

                transformedCounter++;
                writerTransformed.writeNext(outputLine);
            }
            else{
                writerNotTransformed.writeNext(nextLine);
            }
            totalCounter++;
        }

        System.out.println(totalCounter + " rows processed.  " + transformedCounter + " rows transformed. " + (totalCounter - transformedCounter) + " rows not transformed");
    }
}
