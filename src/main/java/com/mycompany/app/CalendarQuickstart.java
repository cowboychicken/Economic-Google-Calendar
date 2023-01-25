package com.mycompany.app;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* class to demonstarte use of Calendar events list API */
public class CalendarQuickstart {
    /**
    * Application name.
    */
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    /**
    * Global instance of the JSON factory.
    */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
    * Directory to store authorization tokens for this application.
    */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    /**
    * Global instance of the scopes required by this quickstart.
    * If modifying these scopes, delete your previously saved tokens/ folder.
    */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
    * Creates an authorized Credential object.
    *
    * @param HTTP_TRANSPORT The network HTTP Transport.
    * @return An authorized Credential object.
    * @throws IOException If the credentials.json file cannot be found.
    */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
        throws IOException {
        // Load client secrets.
        InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }

    private static Calendar service;
    private static String CalendarID = "";

    public CalendarQuickstart(String calendarIdInput) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service =
            new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        CalendarID = calendarIdInput;
    }

    public void printCalendarIds() throws IOException {
        CalendarList calendars = service.calendarList().list().execute();
        List <CalendarListEntry> listEntries = calendars.getItems();
        if (listEntries.isEmpty()) System.out.println("No ids found.");
        else for (CalendarListEntry entry : listEntries) System.out.println("\t" + entry.getSummary() + "\t" + entry.getId());
    }

    public void addCalendarEvent(String startDateAndTime, String endDateAndTime, String Title) throws IOException{
        // Add new event
        // time format is YYYY-MM-DDThh:mm:00.000-00:00
        // 6 hours ahead of CT ex: "2023-01-07T15:30:00.000-00:00" == 9:30am
        EventDateTime eventtoaddstart = new EventDateTime().setDateTime(new DateTime(startDateAndTime));
        EventDateTime eventtoaddend = new EventDateTime().setDateTime(new DateTime(endDateAndTime));
        Event eventToAdd = new Event().setDescription("").setSummary(Title)
            .setStart(eventtoaddstart).setEnd(eventtoaddend);
        service.events().insert(CalendarID , eventToAdd).execute();
    }

    public void clearAllCalendarEvents(){
        try {
            String pageToken = null;
            do {
                Events events = service.events().list(CalendarID).setPageToken(pageToken).execute();
                List<Event> items = events.getItems();
                for (Event event : items) {
                    service.events().delete(CalendarID, event.getId() ).execute();
                    System.out.println("Deleting...\ttitle:\t" + event.getSummary() + "id:\t" + event.getId());
                }
                pageToken = events.getNextPageToken();
            } while (pageToken != null);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void printListOfEvents (int n) throws IOException{
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list(CalendarID)
            .setMaxResults(n)
            .setTimeMin(now)
            .setOrderBy("startTime")
            .setSingleEvents(true)
            .execute();
        List<Event> listItems = events.getItems();
        if (listItems.isEmpty()) System.out.println("No events found.");
        else {
            for (Event event : listItems) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) start = event.getStart().getDate();
            }
        }
    }
}