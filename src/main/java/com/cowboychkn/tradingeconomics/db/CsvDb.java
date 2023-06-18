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

// Add logging of exceptions etc.

public class CsvDb {

    private String fileLocation=new File("").getAbsolutePath() + "\\EconomicCalendarEvents.csv";

    private List<String> recentlyAddedEvents = new ArrayList<String>(); 

    public CsvDb (){};
    
    public CsvDb (String file){
        fileLocation = file;
    };

    public List<String> getRecentlyAddedEvents () {
        return recentlyAddedEvents;
    }

    public List<String> readCsv() {
        List<String> content = new ArrayList<String>();
        try ( CSVReader reader = new CSVReader( new FileReader(fileLocation))) {
            while (reader.peek() != null){
                String temp = String.join(",", reader.readNext());
                content.add(temp);   
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        catch (CsvException f){
            f.printStackTrace();
        }
        catch (NullPointerException g){
            g.printStackTrace();
        }
        return content;
    }

    public int writeToCsv(List<String> input){
        try ( CSVWriter writer = new CSVWriter( new FileWriter(fileLocation,true))) {
            for(String element:input) writer.writeNext( element.split("[,]"));
            return 1;
        } catch (IOException e){}
        return 0;
    }

    public void updateCsv(List<String> records){
        List<String> newRecords = new ArrayList<String>();
        List<String> existingRecords = readCsv();

        for(String record : records) {
            if( !existingRecords.contains(record) ) newRecords.add(record);
        }
            recentlyAddedEvents = newRecords;
            writeToCsv(newRecords);
    }
}



