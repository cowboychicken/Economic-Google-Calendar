Website that is scraped:
https://tradingeconomics.com/united-states/calendar

CSV file currently used as DB (saved in root):
EconomicCalendarEvents.csv

Must download and place OAuth credentials file in 'resource' folder. Also delete tokens file if security scope is ever changed or if expired.

Commands to build and run:
mvn clean package -DskipTests

java -cp target/my-app-1.0-SNAPSHOT.jar com.cowboychkn.tradingeconomics.RunUpdate;

