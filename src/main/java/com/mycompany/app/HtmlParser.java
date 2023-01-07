package com.mycompany.app;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlBold;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeader;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.time.Year;

public class HtmlParser{

    public HtmlParser(){};

        public ArrayList<String> getEvents2 (HtmlPage page1 ){
            ArrayList<String> events = new ArrayList<String>();

            HtmlTable table = page1.getHtmlElementById("calendar");

            HtmlTableHeader tHeader = table.getHeader();
    
            // Gets dates with duplicates within TH
            List <HtmlTableBody> tableBodies = table.getBodies();
            List<HtmlTableHeaderCell> tableHeaders = table.getByXPath("//thead//tr//th[1]");
      
            int j = 0;
            for (int i = 0; i < tableBodies.size(); i++) {
    
                String[] date = tableHeaders.get(j).asNormalizedText().split(" ");

                List<HtmlTableRow>  tableRows = tableBodies.get(i).getRows();
    
                for (HtmlTableRow tableRow : tableRows ){
    
                    String level = tableRow.getCell(0).asXml();
                    level = level.substring(level.indexOf("calendar-date-")+14, level.indexOf("calendar-date-")+15);
                    String time = tableRow.getCell(0).asNormalizedText();
                    String details = tableRow.getCell(2).asNormalizedText();

                    String year = date[3];
                    String month = enummonths(date[1].toUpperCase());
                    String day = date[2];
                    String startTime="";

                    if (time.indexOf(":")<0 || details.contains("holiday") ) {
                        time ="08:00 PM";
                        level = "1";    // level is stored in class attribute of time. so no time, no level
                    }
                    time = convertTime(time);

                    events.add(
                        year + "," +
                        month + "," +
                        day + "," +
                        time + "," +
                        details + "," +
                        level
                      
                    );
                }
                j = j + 2;
            }
            return events;
        }
        
  private static String convertTime (String input ){

    int outputHours = 0;
    int outputMins = 0;

    // millitary time
    if (input.contains("pm")) outputHours += 12;

    // cut off am/pm
    input = input.substring(0,input.indexOf(" "));

    if (input.indexOf(':') > 0){
      outputHours += Integer.parseInt(input.substring(0,input.indexOf(":")));
      outputMins += Integer.parseInt(input.substring(input.indexOf(":")+1,input.length()));
    } 
    else outputHours += Integer.parseInt(input);

    return String.format("%02d:%02d", outputHours + 12, outputMins);

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
    else if (abrv.equals("JANUARY")) return "01";
    else if (abrv.equals("FEBRUARY")) return "02";
    else if (abrv.equals("MARCH")) return "03";
    else if (abrv.equals("APRIL")) return "04";
    else if (abrv.equals("MAY")) return "05";
    else if (abrv.equals("JUNE")) return "06";
    else if (abrv.equals("JULY")) return "07";
    else if (abrv.equals("AUGUST")) return "08";
    else if (abrv.equals("SEPTEMBER")) return "09";
    else if (abrv.equals("OCTOBER")) return "10";
    else if (abrv.equals("NOVEMBER")) return "11";
    else if (abrv.equals("DECEMBER")) return "12";
    else return "00";
  }
    }







