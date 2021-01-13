package com.huxley.service;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonArray;
import javax.json.JsonObject;

import com.huxley.db.DatabaseUtil;
import com.huxley.generic.Utility;
import com.huxley.model.*;
public class ShowtimeService {
	public static String URI = "http://data.tmsapi.com/v1.1/movies/showings?startDate=2019-07-02&zip=85014&radius=8&api_key=xd3mews5eu3nfs5q22y2kzyr";
	public static String URI_Call;
	private String URI_pt1 = "http://data.tmsapi.com/v1.1/movies/showings?startDate=";
	private String URI_pt2 = "&zip=85008&radius=8&api_key=xd3mews5eu3nfs5q22y2kzyr";
	public static String API_KEY = "xd3mews5eu3nfs5q22y2kzyr";
	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
	private int radius;
	private Date startDate;
	private String zip;
	//private final static HashMap<Integer, Theater> movieTheaters;
	public ShowtimeService(int radius, Date startDate, String zip)
	{
		this.radius = radius;
		this.startDate = startDate;
		this.zip = zip;
	}
	
	/**
	 * This method makes the API call and returns the response.  This method varies from invokeGetShowtimes() in that we don't use the default values, we are supplying the radius and zipcode values
	 * as well possibly a new start date
	 * @param zipCode
	 * @param radius
	 * @return String representation of Response
	 */
	public ResponseEntity<String> invokeCustomShowtimes(String zipCode, int radius)
	{
		 RestTemplate restTemplate = new RestTemplate();
			SimpleDateFormat sdp = new SimpleDateFormat("yyyy-MM-dd");
			//Date now = new Date();
			String dateTime = sdp.format(this.startDate); 
			String URI_CUSTOM_CALL = "http://data.tmsapi.com/v1.1/movies/showings?startDate=" + dateTime + "&zip=ZIP_VAR&radius=RADIUS_VAR&api_key=xd3mews5eu3nfs5q22y2kzyr";
			URI_CUSTOM_CALL = URI_CUSTOM_CALL.replaceAll("ZIP_VAR", zipCode);
			URI_CUSTOM_CALL = URI_CUSTOM_CALL.replaceAll("RADIUS_VAR", String.valueOf(radius));
			URI_Call = URI_CUSTOM_CALL;
		 ResponseEntity<String> response
		  = restTemplate.getForEntity(URI_CUSTOM_CALL, String.class);
		 return response;
	}
	
	/**
	 * This method makes the API call and returns the response.  Default values for radius (8) and zipcode (85014) are used and the start date will either be today or tomorrow (if after 11pm) by default
	 * @return String representation of Response
	 */
	public ResponseEntity<String> invokeGetShowtimes()
	{
		 RestTemplate restTemplate = new RestTemplate();
			SimpleDateFormat sdp = new SimpleDateFormat("yyyy-MM-dd");
			//Date now = new Date();
			String dateTime = sdp.format(this.startDate);
			URI_Call = URI_pt1 + dateTime + URI_pt2;
			//System.out.println("about to try to call this URI: " + URI_Call);
		 ResponseEntity<String> response
		  = restTemplate.getForEntity(URI_Call, String.class);
		 return response;
	}
	
	
	/**
	 * This method takes the JSON response String from the movie showtime API call and parses the JSON data.  A list of Movie objects is populated, each Movie object also having
	 * a HashMap of Theater objects associated with it, and each Theater object having a String array representing the showtimes for that particular movie.
	 * @param JSONData
	 * @return List of Movie objects
	 */
	public List<Movie> loadMovieData(String JSONData)
	{
		List<Movie> movies = new ArrayList<Movie>();
		 JsonReader reader = Json.createReader(new StringReader(JSONData));
		 JsonStructure top = reader.read();
		 JsonArray outerMostArray = (JsonArray) top;
		final HashMap<Integer, Theater> movieTheaters = DatabaseUtil.getTheaters();
		 int array_size = outerMostArray.size();
		 int records_processed = 0;
		 
		 try {
		 for (JsonValue outerObject : outerMostArray) //33 elements of JsonValue inside of outerMostArray
		 {
			 JsonObject jo = (JsonObject) outerObject;
			 String title = jo.getJsonString("title").getString();
			 Movie movie = new Movie();
			 String long_description;
			 String short_description;
			 String releaseDate;
			 String runtime;
				String audience; //will be null sometimes
				if (jo.getJsonString("audience") != null && jo.getJsonString("audience").toString().length() > 0)
				{
					audience = jo.getJsonString("audience").toString();
					String testAudience = audience.replaceAll("\"", "");
					if (testAudience.equals("Children"))
					{
						continue;  //we don't want any children's movies returned in our list!
					}
					movie.setAudience(audience);
				}
				else
				{
					movie.setAudience("N/A");
				}
			 if (jo.getJsonString("longDescription") != null)
			 {
				 long_description = jo.getJsonString("longDescription").getString();
				 movie.setLongDescription(long_description);
			 }
			  if (jo.getJsonString("shortDescription") != null)
			  {
				  short_description = jo.getJsonString("shortDescription").getString();
				  movie.setShortDescription(short_description);
			  }
			  if (jo.getJsonString("releaseDate") != null)
			  {
				  releaseDate = jo.getJsonString("releaseDate").getString();
				  movie.setReleaseDate(releaseDate);
			  }
			if (jo.getJsonString("runTime") != null)
			{
				 runtime = jo.getJsonString("runTime").getString();
				 movie.setRuntime(Utility.runTimeString(runtime));
			}
			 movie.setTitle(title);
			 
			 String[] mainActors;
			 int index = 0;
				JsonArray actorArray = jo.getJsonArray("topCast");
			 if (actorArray != null && actorArray.size() > 0)
			    {
					mainActors = new String[actorArray.size()];
					
					for(JsonValue actor : actorArray)
					{
						mainActors[index] = actor.toString();
						index++;
					}
					movie.setTopCast(mainActors);
				}

			String[] genres;
			JsonArray genreArray = jo.getJsonArray("genres");
			 if (genreArray != null && genreArray.size() > 0)
			 {
					genres = new String[genreArray.size()];
					index = 0;
					for(JsonValue genre : genreArray)
					{
						genres[index] = genre.toString();
						index++;
					}
					movie.setGenres(genres);
			 }

			String[] directors;
			JsonArray directorArray = jo.getJsonArray("directors");
			 if (directorArray != null && directorArray.size() > 0)
			 {
					directors = new String[directorArray.size()];
					index = 0;
					for(JsonValue director : directorArray)
					{
						directors[index] = director.toString();
						index++;
					}
					movie.setDirectors(directors);
			 }


			//movie.setCinemas(cinemas);
			JsonArray showtimesArray = jo.getJsonArray("showtimes");
			 int theater_id = 0;
			 Integer theater_key= new Integer(theater_id);
			 movie.initializeTheaterMap(); //initialize the HashMap that will store our current list of showtimes for this movie at a particular theater
			HashMap<Integer, Theater> currentTheaters =  movie.getCurrentTheaters();
			for(JsonValue innerObject : showtimesArray)
			{
				 JsonObject ko = (JsonObject) innerObject;
				 //get the theater object
				 JsonObject thr = ko.getJsonObject("theatre");
				 String id_as_string;
				 id_as_string = thr.getJsonString("id").toString();
				 id_as_string = id_as_string.replaceAll("\"", ""); //for some reason the string returned has "" quotes at the beginning and end, stripping them out
				 int debug = 1;
				 theater_key = Integer.valueOf(id_as_string);
				 if (id_as_string.equals("9437") && movie.getTitle().equalsIgnoreCase("Midsommar"))
				 {
					 debug = 50;
				 }
				 //theater_key = theater_id;  //Integer object wrapper for theater_id
				 String showing = ko.getJsonString("dateTime").toString();
				 //let's convert the showing String to something we want to see
				 showing = Utility.getShowingTime(showing);  //should be something like 11:30 am now
				 //we have to decide if we're grabbing the theater object from the database hashmap, or the current movie object hashmap. 
				 //if we are coming across this id for the first time in this array, then we grab it from the database hashmap. otherwise we grab it from the movie
				 //object hashmap, and keep appending the next showtime we are reading in
				 // t = movieTheaters.get(new Integer(theater_id));
				 if (currentTheaters.containsKey(theater_key))
				 {
					 Theater currentTheater = (Theater) currentTheaters.get(theater_key);
					 currentTheater.addShowing(showing);
					 currentTheaters.put(theater_key, currentTheater);
				 }
				 else  //we must grab it from the database HashMap, add the showing, then put that in our movie HashMap for the next iteration
				 {
					 Theater currentTheater = (Theater) movieTheaters.get(theater_key);  //we just want the basic theater info from the database, should be empty showtime data
					 Theater newTheater = new Theater();
					 newTheater.copyData(currentTheater);
					 newTheater.addShowing(showing);
					 currentTheaters.put(theater_key, newTheater);  //now adding it to the movie HashMap for the first time
				 } 
			} //after the showtimes array has looped through and processed all values, we are done with this movie object and ready to move onto the next one
			movie.setCurrentTheaters(currentTheaters);
			movies.add(movie);
		records_processed++; 
		if ((records_processed % 10) == 0)
		{
			//System.out.println("we've processed " + records_processed + " records. Let's get our last movie object just to test...");
			Movie test = movies.get((records_processed -2));
			//System.out.println("Movie title is: " + test.getTitle());
		}
		 }//outermost For loop
		 }catch(Exception e)
		 {
			 System.out.println("exception caught in loadMovieData in the ShowtimeService class. We processed " + records_processed + " before getting an error.");
			 e.printStackTrace();
		 }
		// System.out.println("we got through this many records that were processed without an error: " + records_processed);
		 return movies;
	}
}
