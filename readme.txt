Website that is scraped:
https://tradingeconomics.com/united-states/calendar

Must download and place OAuth credentials file in 'resource' folder. Also delete tokens file if security scope is ever changed.

Commands to build and run:
mvn clean package -DskipTests

java -cp target/my-app-1.0-SNAPSHOT.jar com.mycompany.app.App

// Pass in "Redo_All" with the above to clear calendar and place all events from current CSV

----

To do:


