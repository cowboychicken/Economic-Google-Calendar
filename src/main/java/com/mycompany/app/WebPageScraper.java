package com.mycompany.app;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;

public class WebPageScraper{

    private static WebClient client;

    public WebPageScraper(){
        // Open Browser
        client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        System.out.println("\n[] Web Client Created");
    }

    public HtmlPage scrapePage (String siteUrl) throws IOException{
        return client.getPage(siteUrl);
    }


}