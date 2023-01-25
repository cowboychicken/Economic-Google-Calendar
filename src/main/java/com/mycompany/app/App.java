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

public class App {

  public static void main(String[] args) {

    System.out.println("\n[] Starting...");
    String calendarId = "a2f405442fb6c4687738183931cbe0fa188d41fd0e60d0c021f544f51b639dc9@group.calendar.google.com";
    String siteUrl2 = "https://tradingeconomics.com/united-states/calendar";
    String csvFileLocation = "C:\\Users\\user\\Documents\\testmaven3\\my-app\\src\\main\\resources\\EconomicCalendarEvents.csv";

    try {
      OpenCsv openCsvObj = new OpenCsv(csvFileLocation);
      CalendarQuickstart calobj = new CalendarQuickstart(calendarId);

      if (args.length == 0) {
        // Go to webpage
        WebPageScraper marketWatchCalendarScraper = new WebPageScraper();
        ArrayList<String> scrapedEvents = new HtmlParser().getEvents2(marketWatchCalendarScraper.scrapePage(siteUrl2));

        // Print out events that have been scraped
        System.out.println("\n[] Events Scraped: " + scrapedEvents.size());

        // Compare current CSV with new events
        ArrayList<String> newEvents = new ArrayList<String>(openCsvObj.updateCsv(scrapedEvents));

        if (newEvents.isEmpty()) {
          System.out.println("\n[] No events added");
        } else {
          // Add new events to calendar
          for (String addedEvent : newEvents) {
            String[] addedEventDetails = addedEvent.split("[,]");
            if (addedEventDetails[5].equals("3")) {
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
            }
          }
          openCsvObj.writeToCsv(newEvents);
        }
      }

      // For when Calendar needs to be reset
      else if (args[0].equals("Redo_All")) {
        System.out.println("\n[] Deleting all calendar events...");
        calobj.clearAllCalendarEvents();

        ArrayList<String> allEvents = openCsvObj.readCsv();
        System.out.println("\n[] Adding back all calendar events...");
        for (String addedEvent : allEvents) {
          String[] addedEventDetails = addedEvent.split("[,]");
          if (addedEventDetails[5].equals("3")) {
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
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (GeneralSecurityException f) {
      f.printStackTrace();
    }
    ;
    System.out.println("\n[] Ending...\n");
  }
}