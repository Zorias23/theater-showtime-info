package com.huxley.tester;



import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.internal.HttpUrlConnector;


public class Service_Tester {

	public static void main(String[] args) {
		  Client client = ClientBuilder.newClient();
		  Invocation inv1 = client.target("http://data.tmsapi.com/v1.1/movies/showings")
			       .queryParam("radius", "10").queryParam("zip", "85017").queryParam("startDate", "2019-06-26").queryParam("api_key", "8d2rfg9axf8xccjtcf4d8azp")
			       .request("text/plain").buildGet();
		  Response res = inv1.invoke();
		 String json_parsed_string =  res.readEntity(String.class);
		 int json_string_size = json_parsed_string.length();
		 
		 int status_code = res.getStatus();
		 MediaType mt =  res.getMediaType();
		 System.out.println("Our json string size is: " + json_string_size);
		 System.out.println("Our media type for our response is: " + mt.toString() +" and our status code is: " + status_code);
		  System.out.println("main method has completed and service call completed without exception");

	}

}
