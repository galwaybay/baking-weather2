<!DOCTYPE html>
  <!-- ================================================================================  --> 
  <!-- Redirect to the baking weather app                                                -->
  <!--                                                                                   --> 
  <!-- Put this index.jsp file in the <tomcat/webapps/ROOT folder                        --> 
  <!-- This will allow the user to go to both of these URLS (if you want to enable this) -->
  <!--   <HOSTNAME>                                                                      --> 
  <!--   <HOSTNAME>/BakingWeather                                                        --> 
  <!-- For instance:                                                                     --> 
  <!--    http://my-host                                                                 --> 
  <!--    http://my-host/BakingWeather                                                   --> 
  <!--                                                                                   --> 
  <!-- (This assumes that tomcat is running on port 80)                                  --> 
  <!-- (If not then the URLs might look like this:)                                      --> 
  <!--    http://my-host:8080                                                            --> 
  <!--    http://my-host/:8080/BakingWeather                                             --> 
  <!--                                                                                   --> 
  <!-- Final Tomcat file structure:                                                      --> 
  <!--   tomcat/webapps/ROOT/index.jsp                                                   --> 
  <!--   tomcat/webapps/BakingWeather.war                                                --> 
  <!--                                                                                   --> 
  <!-- ================================================================================  --> 
<html lang="en">
<head>
</head>
<body>
  
  <%
  response.sendRedirect("/BakingWeather")
  %>

</body>
</html>
