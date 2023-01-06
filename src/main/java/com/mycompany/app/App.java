package com.mycompany.app;

import com.mycompany.app.WebPageScraper;
import com.mycompany.app.OpenCsv;
import com.mycompany.app.CalendarQuickstart;

import java.security.GeneralSecurityException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlBold;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.time.Year;

/*
    To Do
    Fix time adjustment

  - figure out how to capture 'year' 
      might not be issue... only for week before new year, but would just create entries for previou january until new year rolls around
  - Make it so, just incase writing to CSV fails or Calendar fails, we don't have to edit text files again. 
    Maybe, do one at a time. (add to calendar, if it works, then write CSV )
*/

public class App {

  public static void main(String[] args) {
    System.out.println("\nStarting...");

    String calendarId = "a2f405442fb6c4687738183931cbe0fa188d41fd0e60d0c021f544f51b639dc9@group.calendar.google.com";
    String siteUrl = "https://www.marketwatch.com/economy-politics/calendar";
    String siteUrl2 = "https://tradingeconomics.com/united-states/calendar";
    String csvFileLocation = "C:\\Users\\user\\Documents\\testmaven3\\my-app\\src\\main\\resources\\EconomicCalendarEvents.csv";

    // Go to page
    try {
    
      WebPageScraper marketWatchCalendarScraper = new WebPageScraper();
      //ArrayList<String> scrapedEvents = new HtmlParser().getEvents(marketWatchCalendarScraper.scrapePage(siteUrl));
      ArrayList<String> scrapedEvents = new HtmlParser().getEvents2(marketWatchCalendarScraper.scrapePage(siteUrl2));
      
        // Print out events that have been scraped
        System.out.println("\n\nEvents Scraped:\n\n" + scrapedEvents.size());
        for (String scrapeditem : scrapedEvents){
          System.out.println (scrapeditem);
        }

        // Update CSV with new events
        OpenCsv openCsvObj = new OpenCsv();
        openCsvObj.fileLocation = csvFileLocation;
        ArrayList<String> addedEvents = new ArrayList<String>(openCsvObj.updateCsv(scrapedEvents));

        if (addedEvents.isEmpty()){
          System.out.println("No events added");
        }
        else {
          CalendarQuickstart calobj = new CalendarQuickstart(calendarId);
         // calobj.printListOfEvents(10);
          calobj.clearAllCalendarEvents();
 
          for (String addedEvent : addedEvents){
            String[] addedEventDetails = addedEvent.split("[,]");
            if (addedEventDetails[5].equals("3"))
              calobj.addCalendarEvent(
                addedEventDetails[0] + "-" +  // year
                addedEventDetails[1] + "-" +  // month
                addedEventDetails[2] + "T" +  // day
                addedEventDetails[3] + ":00.000-00:00", // time
                addedEventDetails[0] + "-" +  
                addedEventDetails[1] + "-" + 
                addedEventDetails[2] + "T" + 
                addedEventDetails[3] + ":05.000-00:00",
                addedEventDetails[4]   // details
                );
          }

         
          calobj.printListOfEvents(10);
        }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (GeneralSecurityException f) {
      f.printStackTrace();
    };
    System.out.println("\nEnding...\n");
  }
}