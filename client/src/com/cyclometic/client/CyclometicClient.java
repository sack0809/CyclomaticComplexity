package com.cyclometic.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
@Path("/CyclometicClient") 
public class CyclometicClient {
	@GET
    @Path("/")
	public static void main(String[] args) {
		    HttpURLConnection connection = null;
		    BufferedReader reader = null;
		    String json = null;
		    try {
		      URL resetEndpoint = new URL("http://localhost:8080/ServerRESTAPI/rest/CyclometicServer/main");
		      connection = (HttpURLConnection) resetEndpoint.openConnection();
		      // Set request method to GET as required from the API
		      connection.setRequestMethod("GET");
		      connection.setRequestProperty("Accept", "application/json");
		      
		      if (connection.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ connection.getResponseCode());
				}


		      // Read the response
		      BufferedReader br = new BufferedReader(new InputStreamReader(
		  			(connection.getInputStream())));
               
		      String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}
		      
		      /*StringBuilder jsonSb = new StringBuilder();
		      String line = null;
		      while ((line = reader.readLine()) != null) {
		        jsonSb.append(line);
		      }
		      json = jsonSb.toString();
               System.out.println(json);*/
		      // Converts JSON string to Java object
				connection.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			  }

			}
		  }


