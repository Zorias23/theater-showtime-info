package com.huxley.tester;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonArray;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class Spring_service_tester {

	public static void main(String[] args) {
		String URI = "http://data.tmsapi.com/v1.1/movies/showings?startDate=2019-06-29&zip=85014&radius=8&api_key=8d2rfg9axf8xccjtcf4d8azp";
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("radius", "8");
		 params.put("zip", "85014");
		 params.put("startDate", "2019-06-26");
		 params.put("api_key", "8d2rfg9axf8xccjtcf4d8azp");
		 RestTemplate restTemplate = new RestTemplate();
		 ResponseEntity<String> response
		  = restTemplate.getForEntity(URI, String.class);
		 int status_code = response.getStatusCodeValue();
		 String content_type = response.getHeaders().getContentType().getSubtype();
		 System.out.println("Our status code is: " + status_code + " and our content type for body is: " + content_type);
		 String json_output = response.getBody();
		 JsonReader reader = Json.createReader(new StringReader(json_output));
		 JsonStructure top = reader.read();
		 if (top.getValueType() == top.getValueType().ARRAY)
		 {
			 System.out.println("our top type is an array.");
		 }
		 if (top.getValueType() == top.getValueType().OBJECT)
		 {
			 System.out.println("our top type is an object.");
		 }
		 JsonArray arr = (JsonArray) top;
		int array_size = arr.size();
		System.out.println("so our array size is: " + array_size);
		 reader.close();
	}

}
