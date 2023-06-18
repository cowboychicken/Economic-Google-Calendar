package com.cowboychkn.tradingeconomics.webscrape;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/*
 *  Think we should save as raw as we can 
 *  so keep original time. make time conversion a function either at DB or Calendar
 *      Same with holiday and Month conversion
 *  Should add date added as column, get rid of log 
 *  
 */

public class TradingEconomicsCalendarScraper{

    private String siteUrl = "https://tradingeconomics.com/united-states/calendar";

    public List<String> scrapePage () throws IOException{
        
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
       
        HtmlPage page = client.getPage(siteUrl);

        HtmlTable table = page.getHtmlElementById("calendar");
      
        // Events come grouped by Date (header), in seperate tables
        // Each row is a single event
        List<HtmlTableBody> tableBodies = table.getBodies();
        List<HtmlTableHeaderCell> tableHeaders = table.getByXPath("//thead//tr//th[1]");
        
        List <String>  events = new ArrayList<String>();

        for (int i = 0; i < tableBodies.size(); i++) {
            // Each date has two headers (one fixed for scrolling)
            String date = tableHeaders.get(i*2).asNormalizedText();

            List<HtmlTableRow> tableRows = tableBodies.get(i).getRows();

            for (HtmlTableRow tableRow : tableRows) {

                String time = tableRow.getCell(0).asNormalizedText();
                String details = tableRow.getCell(2).asNormalizedText();

                // level is nested in 'class' of time cell
                String level = tableRow.getCell(0).asXml();
                level = level.substring(level.indexOf("calendar-date-") + 14, level.indexOf("calendar-date-") + 15);

                if (time.indexOf(":") < 0 || details.contains("holiday")) {
                  time = "08:00 PM";
                  level = "1"; // level is stored in class attribute of time. so no time, no level
                }
                String convertedTime = convertTime(time);
     // System.out.println("[scraper-detailofevent]  "  + details);
                events.add (
                    date + "," +
                    convertedTime + "," + 
                    details + "," +
                    level + "," + 
                    time
                 );

                 

                
                //String month = enummonths(date[1].toUpperCase());
                //String day = date[2];
                //String year = date[3];

/*
                events.add(
                    year + "," +
                        month + "," +
                        day + "," +
                        time + "," +
                        details + "," +
                        level + "," + 
                        originaltime

                );
                */
                
            }
        }

      client.close();
      return events;



    }



    
  private String convertTime(String input) {

    int outputHours = 0;
    int outputMins = 0;
    // millitary time
    if (input.toLowerCase().contains("pm") ){
      if (!input.contains("12:")){
        outputHours += 12;
      }
    }

    // cut off am/pm
    input = input.substring(0, input.indexOf(" "));

    if (input.indexOf(':') > 0) {
      outputHours += Integer.parseInt(input.substring(0, input.indexOf(":")));
      outputMins += Integer.parseInt(input.substring(input.indexOf(":") + 1, input.length()));
    } else
      outputHours += Integer.parseInt(input);

    return String.format("%02d:%02d", outputHours , outputMins);

  }
/*
  private String enummonths(String abrv) {

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
*/

}