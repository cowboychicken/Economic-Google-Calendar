package com.mycompany.app;

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

public class HtmlParser{

    public HtmlParser(){};

    public ArrayList<String> getEvents (HtmlPage inputPage ){
        
        List<HtmlElement> elementsByTag = inputPage.getByXPath("//tr");
        ArrayList<String> events = new ArrayList<String>();

        if (elementsByTag.isEmpty()) {
            System.out.println("No elements found!");
        } 
        else {}
            String dateToAdd = "";  
    
            int dateCount = 0;
            int eventCount = 0;

            for (HtmlElement element : elementsByTag) {

                System.out.println(element.asXml());
          
                // Check if DATE containing row
                HtmlBold dateBold = (HtmlBold) element.getFirstByXPath(".//b");

                try {
                    
                    dateToAdd = dateBold.getTextContent();
                    dateCount ++;
                    System.out.println("not null");

                } catch (NullPointerException e) {

                    // If there is no date, then treat as an event 
                    System.out.println("----- null");
                    HtmlTableCell eventTime = (HtmlTableCell) element.getFirstByXPath(".//td");
                    System.out.println("----- null1");
                    HtmlTableCell eventTitle = (HtmlTableCell) element.getFirstByXPath(".//td[2]");
                    System.out.println("----- null2");

                    String eventTimeStr = "";
                    String eventTitleStr = "";
                    try {
                          eventTimeStr = eventTime.getTextContent();
                          eventTitleStr = eventTitle.getTextContent(); 
                          System.out.println("date to add   " + dateToAdd );
                          //If event exist
                      
                          // Testing
                          //  System.out.println(eventTime.getTextContent());
                          System.out.println("----- null3");
                          //System.out.println(eventTitle.getTextContent());
      
      
                      //    String[] addedEventDetails = addedEvent.split("[,]");
                          
                          String year = Integer.toString(Year.now().getValue());
      //                    String month = enummonths(addedEventDetails[1].substring(0,addedEventDetails[1].indexOf(".")).trim());
                          String month = enummonths(dateToAdd.substring(dateToAdd.indexOf(",")+1,dateToAdd.indexOf(".")).trim());
                        //  System.out.println("month value befor passing : "+addedEventDetails[1].substring(0,addedEventDetails[1].indexOf(".")).trim() );
                         // String day = String.format("%02d",  Integer.parseInt(addedEventDetails[1].substring(addedEventDetails[1].indexOf('.')+1,addedEventDetails[1].length()).trim()));
                          
                          String day = String.format("%02d", Integer.parseInt(dateToAdd.substring(dateToAdd.indexOf(".")+1,dateToAdd.length()).trim())  );
      
                          String details = eventTitleStr;
                         // System.out.println("\tdetails  " + details+ " time  : " +addedEventDetails[2]);
                          
                          String startTime = "";
                          
                          if (eventTimeStr.indexOf(" ")<0 || details.contains("holiday") ) startTime ="8 am";
                          else startTime = eventTimeStr;
                          startTime = convertTime(startTime);
                          
              
                       //   System.out.println("_" + addedEventDetails[1].substring(0,addedEventDetails[1].indexOf(".")) + "_");
                       //   System.out.println("_" + addedEventDetails[1] + "_");
                          System.out.println("\tYear " + year + "\tMonth " + month + "\tday" + day + "\tstarttime" + startTime + "\tdetails " + details);
                          
                       // eventCount ++;

                       events.add(
                        year + "," +
                        month + "," +
                        day + "," +
                        startTime + "," +
                        details 
                      
                    );
                    eventCount ++;

                    } catch (NullPointerException f) {
                        f.printStackTrace();
                    }
                }
            }
            return events;
        }


        
  private static String convertTime (String input){
    System.out.println("input convertime =  " + input);

    int outputHours = 0;
    int outputMins = 0;

    if (input.contains("pm")) outputHours += 12;

    input = input.substring(0,input.indexOf(" "));

    if (input.indexOf(':') > 0){
      outputHours += Integer.parseInt(input.substring(0,input.indexOf(":")));
      outputMins += Integer.parseInt(input.substring(input.indexOf(":")+1,input.length()));
    } 
    else outputHours += Integer.parseInt(input);

    return String.format("%02d:%02d", outputHours+6-1, outputMins);

  }

  private static String enummonths(String abrv){

    if (abrv.equals( "JAN")) return "01";
    else if (abrv.equals("FEB")) return "02";
    else if (abrv.equals("MAR")) return "03";
    else if (abrv.equals("APR")) return "04";
    else if (abrv.equals("MAY")) return "05";
    else if (abrv.equals("JUN")) return "06";
    else if (abrv.equals("JUL")) return "07";
    else if (abrv.equals("AUG")) return "08";
    else if (abrv.equals("SEP")) return "09";
    else if (abrv.equals("OCT")) return "10";
    else if (abrv.equals("NOV")) return "11";
    else if (abrv.equals("DEC")) return "12";
    else return "00";



  }
  



    }







