<!DOCTYPE html>
<%@ page import="org.weather.server.*" %>
<html lang="en">
<head>

  <!-- Basic Page Needs
  –––––––––––––––––––––––––––––––––––––––––––––––––– -->
  <meta charset="utf-8">
  <title>Baking Weather</title>
  <meta name="description" content="Baking Weather">
  <meta name="author" content="Tony Kavanagh">

  <!-- Mobile Specific Metas
  –––––––––––––––––––––––––––––––––––––––––––––––––– -->
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <!-- FONT
  –––––––––––––––––––––––––––––––––––––––––––––––––– -->
  <link href="//fonts.googleapis.com/css?family=Raleway:400,300,600" rel="stylesheet" type="text/css">

  <!-- CSS
  –––––––––––––––––––––––––––––––––––––––––––––––––– -->
  <!-- <link rel="stylesheet" href="css/normalize.css"> -->
  <!-- <link rel="stylesheet" href="css/skeleton.css"> --> 
  <link rel="stylesheet" href="css/normalize.css">
  <link rel="stylesheet" href="css/skeleton.css">

  <!-- Favicon
  –––––––––––––––––––––––––––––––––––––––––––––––––– -->
  <link rel="icon" type="image/png" href="images/favicon.png">

</head>
<body bgcolor="#E6E6FA">

<%
    WeatherBaking baking = new WeatherBaking();
    baking.processUrl();
%>

  <!-- Primary Page Layout
  –––––––––––––––––––––––––––––––––––––––––––––––––– -->
  <div class="container">
    <div class="row">
      <table border="0">
        <tr>
          <td><img src="images/spacer.gif" height="1" width="5"></td>
          <td><img src="images/spacer.gif" height="1" width="5"></td>
        </tr>
        <tr>
          <td><img src="images/spacer.gif" height="1" width="5"></td>
          <td><b>Baking in <%= baking.getCity() %></b></td>
        </tr>
      </table>
    </div>
    <div class="row">
        <table border="0">
          <tr>
            <td class="small">Temperature:</td>
            <td class="large"><%= baking.getTemperature() %></td>
          </tr>
          <tr>
            <td class="small">Humidity:</td>
            <td class="large"><%= baking.getHumidity() %></td>
          </tr>
          <tr>
            <td class="small">Wind Speed:</td>
            <td class="large"><%= baking.getWind() %></td>
          </tr>
          <tr>
            <td class="small">Wind Direction:</td>
            <td class="large"><%= baking.getWindDir() %></td>
          </tr>
          <tr>
            <td class="small">Barometer:</td>
            <td class="large"><%= baking.getPressure() %></td>
          </tr>
          <tr>
            <td class="small">Conditions:</td>
            <td class="large"><%= baking.getSky() %></td>
          </tr>
          <tr>
            <td class="small">Current Time:</td>
            <td class="large"><%= baking.getTime() %></td>
          </tr>
          <tr>
            <td class="small">Current Date:</td>
            <td class="large"><%= baking.getDate() %></td>
          </tr>
          <tr>
            <td class="small"><img src="images/spacer.gif" height="1" width="1"></td>
            <td class="large"><img src="images/spacer.gif" height="1" width="1"></td> 
          </tr>
          <tr>
            <td class="small">Dough:</td>
            <td class="large">May need extra flour</td> 
          </tr>
          <tr>
            <td class="small">Baking:</td>
            <td class="large">Tough day to bake</td> 
          </tr>
        </table>
    </div>
    <!-- START BOTTOM IMAGE --> 
    <div class="row">
      <table border="0">
        <tr>
          <td><img src="images/spacer.gif" height="1" width="3"></td> 
          <td><img src="images/feather-60x60.png" alt="Feather" height="60" width="60"></td> 
        </tr>
      </table>
    </div>
  </div>
  <!-- END BOTTOM IMAGE --> 

<!-- End Document
  –––––––––––––––––––––––––––––––––––––––––––––––––– -->
</body>
</html>
