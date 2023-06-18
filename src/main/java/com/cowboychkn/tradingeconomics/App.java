package com.cowboychkn.tradingeconomics;

import com.cowboychkn.tradingeconomics.db.OpenCsv;
import com.cowboychkn.tradingeconomics.webscrape.HtmlParser;
import com.cowboychkn.tradingeconomics.webscrape.WebPageScraper;

import java.security.GeneralSecurityException;

import java.io.*;
import java.util.ArrayList;
import java.time.LocalDateTime;

/*
 *  Figure out how to relative reference csv file 
 *  Move parameters to a config file 
 *  
 */

public class App {

  public static void main(String[] args) {

    System.out.println("\n[] Starting...");
    String calendarId = "a2f405442fb6c4687738183931cbe0fa188d41fd0e60d0c021f544f51b639dc9@group.calendar.google.com";
    String siteUrl2 = "https://tradingeconomics.com/united-states/calendar";
    //String csvFileLocation = "C:\\Users\\user\\Documents\\testmaven3\\my-app\\src\\main\\resources\\EconomicCalendarEvents.csv";
    String csvFileLocation = new File("").getAbsolutePath() + "\\EconomicCalendarEvents.csv";
    try {
      OpenCsv openCsvObj = new OpenCsv(csvFileLocation);
      GoogleCalendar calobj = new GoogleCalendar(calendarId);

        // Log info
        ArrayList<String> debugInfo = new ArrayList<String>();
        debugInfo.add("[log]\tTask ran:\t" + LocalDateTime.now().toString());
        debugInfo.add("------------");
        openCsvObj.writeToCsv(debugInfo);

        // Go to webpage
        WebPageScraper marketWatchCalendarScraper = new WebPageScraper();
        ArrayList<String> scrapedEvents = new HtmlParser().getEvents2(marketWatchCalendarScraper.scrapePage(siteUrl2));

        // Print out events that have been scraped
        System.out.println("\n[] Events Scraped: " + scrapedEvents.size());

        // Compare current CSV with new events
        ArrayList<String> newEvents = new ArrayList<String>(openCsvObj.updateCsv(scrapedEvents));
        int addedEventCount=0;

        ArrayList<String> eventsToAdd = new ArrayList<String>();
        if (args.length == 0) eventsToAdd = newEvents;
        else if (args[0].equals("Redo_All")){
          System.out.println("\n[] Deleting all calendar events...");
          calobj.clearAllCalendarEvents();
          eventsToAdd = openCsvObj.readCsv();
        }

        if (eventsToAdd.isEmpty()) {
          System.out.println("\n[] No events added");
        } else {
          // Add new events to calendar
          for (String addedEvent : eventsToAdd) {
            String[] addedEventDetails = addedEvent.split("[,]");
            if (addedEventDetails[5].equals("3")  // All level 3 events
            || (addedEventDetails[5].equals("2") && addedEventDetails[4].contains("Initial jobless claims")) // Level 2 Filters for specific events... 
            || (addedEventDetails[5].equals("2") && addedEventDetails[4].contains("GDP Growth Rate"))
            || (addedEventDetails[5].equals("2") && addedEventDetails[4].contains("CPI"))
            || (addedEventDetails[5].equals("2") && addedEventDetails[4].contains("Core PCE Price Index MoM"))
            || (addedEventDetails[5].equals("2") && addedEventDetails[4].contains("New Home Sales MoM"))
            ) {
              calobj.addCalendarEvent(
                  addedEventDetails[0] + "-" + // year
                      addedEventDetails[1] + "-" + // month
                      addedEventDetails[2] + "T" + // day
                      addedEventDetails[3] + ":00.000-00:00", // time
                  addedEventDetails[0] + "-" +
                      addedEventDetails[1] + "-" +
                      addedEventDetails[2] + "T" +
                      addedEventDetails[3] + ":05.000-00:00",
                  addedEventDetails[4] // details
              );
              addedEventCount++;
            }
          }
          System.out.println("\n[] Events added to calendar: " + addedEventCount);
          if (args.length == 0) {

            newEvents.add("Date Added:\t" + LocalDateTime.now().toString());
            newEvents.add("------------------------------------------");
            openCsvObj.writeToCsv(newEvents);
          }
        }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (GeneralSecurityException f) {
      f.printStackTrace();
    }
    System.out.println("\n[] Ending...\n");
  }
}