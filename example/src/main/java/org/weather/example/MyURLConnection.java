package org.weather.example;

/**
 * Created by anthonykavanagh on 6/15/15.
 * Test program to parse a JSON string from openweathermap.com
 *
 * NOTES: Look at jackson for parsing the returned json objects
 * NOTES: jackson has two major branches. Jackson 1 and Jackson 2
 *              - these two can co-exist
 *              - 2 is a complete rewrite and has different namespace and is the new, recommended version
 */

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

public class MyURLConnection {

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        MyURLConnection http = new MyURLConnection();

        http.sendGet();
    }

    private void sendGet() throws Exception {

        String url = "http://api.openweathermap.org/data/2.5/weather?zip=10003,us&units=imperial";
        //String url = "http://api.openweathermap.org/data/2.5/weather?zip=94105,us&units=imperial";
        final String DEFAULT = "default";
        String city = DEFAULT;
        String temp = DEFAULT;
        String humidity = DEFAULT;
        String wind = DEFAULT;
        String windDeg = DEFAULT;
        String windDirection = DEFAULT;
        String pressure = DEFAULT;
        String sky = DEFAULT;
        String time = DEFAULT;
        //String date = null;
        final double INCH_MERC_CONV = 0.0296;


        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer strBuff = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            strBuff.append(inputLine);
        }
        in.close();
        String jsonString = strBuff.toString();
        System.out.println("output=" + jsonString);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonFactory factory = mapper.getFactory();
            JsonParser jp = factory.createParser(jsonString);
            JsonNode input = mapper.readTree(jp);

            Iterator<Map.Entry<String, JsonNode>> nodeIterator = input.fields();
            while (nodeIterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = nodeIterator.next();
                //System.out.println("1 key:" + entry.getKey() + ", value:" + entry.getValue());
                if (entry.getKey().equals("name")) {
                    city = entry.getValue().toString().replaceAll("\"", "");
                }
                if (entry.getKey().equals("wind")) {
                    JsonNode myNode = entry.getValue();
                    //System.out.println("2 key:" + entry.getKey() + ", value:" + entry.getValue());
                    //System.out.println("2 myNode=" + myNode.toString());
                    wind = myNode.get("speed").toString();
                    windDeg = myNode.get("deg").toString();
                }
            }

            nodeIterator = input.get("main").fields();
            while (nodeIterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = nodeIterator.next();
                //System.out.println("2 key:" + entry.getKey() + ", value:" + entry.getValue());
                if (entry.getKey().equals("temp")) {
                    temp = entry.getValue().toString();
                }
                if (entry.getKey().equals("humidity")) {
                    humidity = entry.getValue().toString();
                }
                if (entry.getKey().equals("pressure")) {
                    pressure = entry.getValue().toString();
                }
            }

            Iterator<JsonNode> iter = input.get("weather").elements();    // parse an array
            while (iter.hasNext()) {
                JsonNode myNode = iter.next();                  // there is actually only one element in the array
                sky = myNode.get("description").toString().replaceAll("\"", "");
            }

            // format time
            DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
            format.setTimeZone(TimeZone.getTimeZone("America/New_York"));       // this TimeZone will adjust for daylight savings
            time = format.format(new Date());

            // convert and format barometric pressure
            double press = new Double(pressure);
            double inchMer = press * INCH_MERC_CONV;
            String pressStr = String.format("%.2f", inchMer);

            // format tempurature
            String tempFmt = String.format("%.0f", new Double(temp));

            // format wind speed
            String windFmt = String.format("%.0f", new Double(wind));   // rounds down

            // convert wind degree to cardinal direction
            double windDbl = new Double(windDeg);         // 2015-07-03: windDegree removed from API, so comment out (came back next day)
            String windDir = headingToString(windDbl);

            System.out.println("\n4 city=" + city);
            System.out.println("4 temp=" + tempFmt);
            System.out.println("4 humidity=" + humidity);
            System.out.println("4 wind=" + windFmt);
            System.out.println("4 windDeg=" + windDir);
            System.out.println("4 pressure=" + pressStr);
            System.out.println("4 sky=" + sky);
            System.out.println("4 time=" + time);

        } catch (Exception e) {
            System.out.println("ERROR: caught some kind of error");
            e.printStackTrace();

        }
    }

    public static String headingToString(double x) {
        String directions[] = {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"};
        return directions[ (int)Math.round((  (x % 360) / 45)) ];
    }
}