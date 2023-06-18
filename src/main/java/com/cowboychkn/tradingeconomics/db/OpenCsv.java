package com.cowboychkn.tradingeconomics.db;

import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OpenCsv {

    private String fileLocation;

    public OpenCsv (String filePath){
        fileLocation = filePath;
    }

    public ArrayList<String> updateCsv(ArrayList<String> input){
        ArrayList<String> newEvents = new ArrayList<String>();
        File f = new File(fileLocation);
        if(!f.exists()) writeToCsv(newEvents);
        else{
            ArrayList<String> currentContents = readCsv();
            if (!currentContents.isEmpty()) {
                for(String entry : input) {
                    if(!currentContents.contains(entry)) newEvents.add(entry);
                }
            }
            else{
                for(String entry1 : input) newEvents.add(entry1);
            }
            System.out.println("\n[] # of events not already in file: " + newEvents.size());
           // if(!newEvents.isEmpty()) writeToCsv(newEvents);
        }
        
        return newEvents;
    }


    public int writeToCsv(ArrayList<String> input){
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileLocation,true))) {
            for(String element:input) writer.writeNext( element.split("[,]"));
            
            System.out.println("\n[] Records have been written to file...");

        } catch (IOException e){
            System.out.println("\n[] No records have been written to file...");
            return 0;
        }
        return 1;
    }

    public ArrayList<String> readCsv(){
        
        try (CSVReader reader = new CSVReader(new FileReader(fileLocation))) {
            List<String[]> tmpList = reader.readAll();
            ArrayList<String> Content = List2ArrayList(tmpList);
            System.out.println("\n[] # of events already in file: " + Content.size());
            return Content;
        } catch (IOException e){
            System.out.println("\n[] File not read");
            e.printStackTrace();
            return new ArrayList();
        }
        catch (CsvException f){
            System.out.println("\n[] File not read");
            f.printStackTrace();
            return new ArrayList();
        }
        catch (NullPointerException g){
            System.out.println("\n[] File not read");
            g.printStackTrace();
            return new ArrayList();
        }
    }

    private ArrayList<String> List2ArrayList(List<String[]> input){
        ArrayList<String> output = new ArrayList<String>();
        for (String[] array : input){
            String tmpstr = "";
            for ( String str : array){
                tmpstr+="," + str;
                //System.out.println(str);
            }
            output.add(tmpstr.substring(1,tmpstr.length()));
        }
        return output;
    }

}