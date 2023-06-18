package com.cowboychkn.tradingeconomics;

import com.cowboychkn.tradingeconomics.db.CsvDb;
import com.cowboychkn.tradingeconomics.webscrape.TradingEconomicsCalendarScraper;

import java.io.*;
import java.util.List;

public class RunUpdate {
  public static void main(String[] args) {

    System.out.println("\n[] Starting...");
    
    String calendarId = "a2f405442fb6c4687738183931cbe0fa188d41fd0e60d0c021f544f51b639dc9@group.calendar.google.com";

    try ( GoogleCalendar calobj = new GoogleCalendar(calendarId) ){
      System.out.println("\n[] Calendar Opened...");

      CsvDb openCsvObj = new CsvDb();
      openCsvObj.updateCsv(new TradingEconomicsCalendarScraper().scrapePage());   // Scrape website and update csv/db
      List<String> newEvents = openCsvObj.getRecentlyAddedEvents();
      
      int count = 0;
      if (!newEvents.isEmpty()) { // Add new events to Google Calendar
        for (String event : newEvents) {

          String[] eventDetails = event.split("[,]");
          String[] DateDetails = eventDetails[0].split(" ");
          String month = enummonths(DateDetails[1]);

          if ( // Filter Level 3 + Specific events to add
              eventDetails[3].equals("3")  // All level 3 events
              || (eventDetails[3].equals("2") && eventDetails[2].contains("Initial jobless claims")) // Level 2 Filters for specific events... 
              || (eventDetails[3].equals("2") && eventDetails[2].contains("GDP Growth Rate"))
              || (eventDetails[3].equals("2") && eventDetails[2].contains("CPI"))
              || (eventDetails[3].equals("2") && eventDetails[2].contains("Core PCE Price Index MoM"))
              || (eventDetails[3].equals("2") && eventDetails[2].contains("New Home Sales MoM"))
          ) 
          {
            calobj.addCalendarEvent(
              DateDetails[3] + "-" + // year
              month + "-" + // month
              DateDetails[2] + "T" + // day
              eventDetails[1] + ":00.000-00:00", // time
              DateDetails[3] + "-" + // year
              month + "-" + // month
              DateDetails[2] + "T" + // day
              eventDetails[1] + ":05.000-00:00", // time
              eventDetails[2] // details
            );
            count++;
          }
        }
      }
      System.out.println("\n[] " + newEvents.size() + " events added to db. " + count + " events added to calendar.");
    } catch (IOException e) {
      e.printStackTrace();
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("\n[] Ending...\n");
  }

  private static String enummonths(String abrv) {
    abrv = abrv.toUpperCase();
    if (abrv.equals("JAN"))
      return "01";
    else if (abrv.equals("FEB"))
      return "02";
    else if (abrv.equals("MAR"))
      return "03";
    else if (abrv.equals("APR"))
      return "04";
    else if (abrv.equals("MAY"))
      return "05";
    else if (abrv.equals("JUN"))
      return "06";
    else if (abrv.equals("JUL"))
      return "07";
    else if (abrv.equals("AUG"))
      return "08";
    else if (abrv.equals("SEP"))
      return "09";
    else if (abrv.equals("OCT"))
      return "10";
    else if (abrv.equals("NOV"))
      return "11";
    else if (abrv.equals("DEC"))
      return "12";
    else if (abrv.equals("JANUARY"))
      return "01";
    else if (abrv.equals("FEBRUARY"))
      return "02";
    else if (abrv.equals("MARCH"))
      return "03";
    else if (abrv.equals("APRIL"))
      return "04";
    else if (abrv.equals("MAY"))
      return "05";
    else if (abrv.equals("JUNE"))
      return "06";
    else if (abrv.equals("JULY"))
      return "07";
    else if (abrv.equals("AUGUST"))
      return "08";
    else if (abrv.equals("SEPTEMBER"))
      return "09";
    else if (abrv.equals("OCTOBER"))
      return "10";
    else if (abrv.equals("NOVEMBER"))
      return "11";
    else if (abrv.equals("DECEMBER"))
      return "12";
    else
      return "00";
  }
}