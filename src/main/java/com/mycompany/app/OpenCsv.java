package com.mycompany.app;

import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Arrays;
import java.util.Collections;

public class OpenCsv {

    public static String fileLocation;

    public static ArrayList<String> updateCsv(ArrayList<String> input){
        ArrayList<String> newEvents = new ArrayList<String>();
        System.out.println("\n\n\tupdateCsv DEBUG");
        File f = new File(fileLocation);
        System.out.println("Yoo2");
        if(!f.exists()) writeToCsv(newEvents);
        else{
            System.out.println("Yoo3");
            ArrayList<String> currentContents = readCsv();
            System.out.println("Current contents debug = " + currentContents.size());
            if (!currentContents.isEmpty()) {
                for(String entry : input) {
                    if(!currentContents.contains(entry)) newEvents.add(entry);
                }
            }
            else{
                System.out.println("Yoo4");
                for(String entry1 : input) newEvents.add(entry1);
            }
            System.out.println("\n\t" + newEvents.size() + " new events\n");
            if(!newEvents.isEmpty()) writeToCsv(newEvents);
        }
        
        return newEvents;
    }


    public static int writeToCsv(ArrayList<String> input){
        System.out.println("\n\n\tWritetoCSV DEBUG");
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileLocation,true))) {
            for(String element:input) writer.writeNext( element.split("[,]"));
            
            System.out.println("Records written to file....");

        } catch (IOException e){
            System.out.println("No records written to file....");
            return 0;
        }
        return 1;
    }



    public static ArrayList<String> readCsv(){
        System.out.println("\n\n\treadCsv DEBUG");
        
        try (CSVReader reader = new CSVReader(new FileReader(fileLocation))) {
            List<String[]> tmpList = reader.readAll();
            System.out.println(tmpList);

            ArrayList<String> Content = List2ArrayList(tmpList);
            System.out.println("\n\n\treadcsv - after list2array");
            for (String str : Content) System.out.println("readcsv each str from list 2 array" + str);
            System.out.println("\n\t" + Content.size() + " events in current file\n");

            return Content;
        } catch (IOException e){
            System.out.println("no read");
            e.printStackTrace();
            return new ArrayList();
        }
        catch (CsvException f){
            System.out.println("no read");
            f.printStackTrace();
            return new ArrayList();
        }
        catch (NullPointerException g){
            System.out.println("no read");
            g.printStackTrace();
            return new ArrayList();
        }
    }

    private static ArrayList<String> List2ArrayList(List<String[]> input){
        ArrayList<String> output = new ArrayList<String>();

        System.out.println("\n\n\tList2Array DEBUG");
        for (String[] array : input){
            String tmpstr = "";
            System.out.println("yo1");
            for ( String str : array){
                tmpstr+="," + str;
                System.out.println(str);
            }
            System.out.println("List2ArrayList" + tmpstr.substring(1,tmpstr.length()));
            output.add(tmpstr.substring(1,tmpstr.length()));
            System.out.println("List2ArrayList end of loop");
        }
        return output;
    }



}