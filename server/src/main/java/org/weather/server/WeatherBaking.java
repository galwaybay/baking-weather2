package org.weather.server;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by anthonykavanagh on 6/21/15.
 */
public class WeatherBaking {


    private final String GET = "GET";
    private final String AGENT = "User-Agent";
    private final String USER_AGENT = "Mozilla/5.0";
    private final String URL = "http://api.openweathermap.org/data/2.5/weather?zip=10003,us&units=imperial";
    //private final String URL = "http://api.openweathermap.org/data/2.5/weather?zip=94941,us&units=imperial";
    private final String EAST_COAST_TIME_ZONE = "America/New_York";     // todo: let user choose east or west zone
    private final String WEST_COAST_TIME_ZONE = "America/Los_Angeles";
    private final String YORK = "York";
    private final String CITY = " City";
    private String city = "default";
    private String cityTmp = null;
    private String temperatureTmp = "default";
    private String temperature = "default";
    private String humidity = "default";
    private String windTmp = "default";
    private String wind = "default";
    private String windDegTmp = null;
    private String windDir = "default";
    private String pressureTmp = null;
    private String pressure = "default";
    private String sky = "default";
    private String time = "default";
    private String date = "default";
    private final double INCH_MERC_CONV = 0.0296;   // const for hectopascal to inches mercury


    public void processUrl() {
        try {

            JsonNode rootNode = getJsonRootNode(getJsonFromURL());  // todo: pass url in this method (to support multi-urls)
            getValuesFromJsonNodes(rootNode);
            setAndFormatValues();

        } catch (Exception e) {
            System.out.println("ERROR: caught error in processUrl()");
            e.printStackTrace();
        }
    }

    private JsonNode getJsonRootNode(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();               // condense
            JsonFactory factory = mapper.getFactory();
            JsonParser jp = factory.createParser(jsonString);
            return mapper.readTree(jp);
        } catch (Exception e) {
            System.out.println("ERROR: caught exception in getJsonRootNode()");
            e.printStackTrace();
        }
        return null;
    }

    private String getJsonFromURL() {
        try {
            java.net.URL obj = new URL(URL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(GET);
            con.setRequestProperty(AGENT, USER_AGENT);

            int responseCode = con.getResponseCode();                   // can remove?
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer strBuff = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                strBuff.append(inputLine);
            }
            in.close();
            return strBuff.toString();
        } catch (Exception e) {
            System.out.println("ERROR: caught exception in getJsonFromURL()");
            e.printStackTrace();
        }
        return null;
    }

    private void getValuesFromJsonNodes(JsonNode rootNode) {
        getTopLevelNodes(rootNode);
        getMainLevelNodes(rootNode);
        getWeatherLevelNodes(rootNode);
    }

    private void setAndFormatValues() {
        setTimeValues();
        formatSetCityName();
        formatSetBarometric();
        setTemperature(String.format("%.0f", new Double(temperatureTmp)));
        setWind(String.format("%.0f", new Double(windTmp)));                // rounds down
        convertSetWindDir();
    }

    private void getTopLevelNodes(JsonNode rootNode) {

        Iterator<Map.Entry<String, JsonNode>> nodeIterator = rootNode.fields();
        while (nodeIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodeIterator.next();
            if (entry.getKey().equals("name")) {
                cityTmp = entry.getValue().toString().replaceAll("\"", "");  // get rid of extra quotes
            }
            if (entry.getKey().equals("wind")) {
                JsonNode myNode = entry.getValue();
                windTmp = myNode.get("speed").toString();
                windDegTmp = myNode.get("deg").toString();   // 2015-07-03: wind degree removed from the API
            }
        }
    }

    private void getMainLevelNodes(JsonNode rootNode) {

        Iterator<Map.Entry<String, JsonNode>> nodeIterator = rootNode.get("main").fields();
        while (nodeIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodeIterator.next();
            if (entry.getKey().equals("temp")) {
                temperatureTmp = entry.getValue().toString();
            }
            if (entry.getKey().equals("humidity")) {
                setHumidity(entry.getValue().toString());
            }
            if (entry.getKey().equals("pressure")) {
                pressureTmp = entry.getValue().toString();
            }
        }
    }

    private void getWeatherLevelNodes(JsonNode rootNode) {

        Iterator<JsonNode> iter = rootNode.get("weather").elements();
        while (iter.hasNext()) {
            JsonNode myNode = iter.next();
            setSky(myNode.get("description").toString().replaceAll("\"", ""));  // get rid of extra quotes
        }
    }

    private void setTimeValues() {
        DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);   // ie 4:41 PM
        format.setTimeZone(TimeZone.getTimeZone(EAST_COAST_TIME_ZONE));
        setTime(format.format(new Date()));

        format = DateFormat.getDateInstance(DateFormat.FULL);               // ie Saturday, August 1, 2015
        format.setTimeZone(TimeZone.getTimeZone(EAST_COAST_TIME_ZONE));
        setDate(format.format(new Date()));
    }

    private void formatSetCityName() {
        if (cityTmp.contains(YORK)) {
            setCity(cityTmp + CITY);
        } else {
            setCity(cityTmp);
        }

    }

    private void formatSetBarometric() {
        double press = new Double(pressureTmp);
        double inchMer = press * INCH_MERC_CONV;        // convert hectopascals to inches mercury
        setPressure(String.format("%.2f", inchMer));
    }


    private void convertSetWindDir() {               // 20150703: wind def was removed from the API (then returned July 4th)
        if (windDegTmp != null) {
            double windDbl = new Double(windDegTmp);
            setWindDir(headingToString(windDbl));       // convert wind degree to cardinal direction
        }
    }

    private String headingToString(double x) {
        String directions[] = {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"};
        return directions[ (int)Math.round((  (x % 360) / 45)) ];
    }

    // Getters and Setters
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSky() {
        return sky;
    }

    public void setSky(String sky) {
        this.sky = sky;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
