package com.huxley.application;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.springframework.http.ResponseEntity;

import com.huxley.db.DatabaseUtil;
import com.huxley.service.ShowtimeService;
import com.huxley.generic.Utility;
import com.huxley.model.Movie;
import com.huxley.model.Theater;
public class ShowtimeClient {

	private static HashMap<Integer, Theater> cinemas;
	public static void main(String[] args) {
		
		boolean loaded = false;
		try {
		loaded = DatabaseUtil.loadTheaterData();
		if (loaded == true)
		{
			cinemas = DatabaseUtil.getTheaters();
		}
		String sDate1="07/02/2019";
		Date sDate = Utility.getFormattedDate(sDate1);
		ShowtimeService show = new ShowtimeService(8, sDate, "85014");
		Scanner sc = new Scanner(System.in);
		int menu_selection = -1;
		List<Movie> movies;
		int theater_id = -1;
		int radius = 8;
		String zipCode = "85014";
		String futureOnly = "";
		ResponseEntity<String> res = show.invokeGetShowtimes();
		movies = show.loadMovieData(res.getBody());
		HashMap<Integer, Movie> movieSelect = new HashMap<Integer, Movie>();
		int movieIndex = 1;
		int movie_id = -1;
		do {
		System.out.println("Please make a selection to view which showtime data you would like to see");
		System.out.println(" ");
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("     1. Display all movie showtimes from local theaters  (radius" + radius + ", zip=" + zipCode + " - default");
		System.out.println("     2. Display movie showtimes for a specific theater by id");
		System.out.println("     3. Display showtimes and theaters for a specific movie");
		System.out.println("     4. Customize search #1 by choosing radius and zipcode");
		System.out.println("     5. Quit");
		 menu_selection = sc.nextInt();
		 switch (menu_selection) {
		 
		 case 1: 
			 Utility.displayLocalShowtimeInfo(movies);
			 break;
			 
		 case 2:
			//display a list of theaters and ID's to them and have them select an ID
			 System.out.println("Select the ID of the theater you would like to have showtimes displayed");
			 Utility.displayTheaterList();
			 theater_id = sc.nextInt();
			 Utility.displayShowtimesByTheater(movies, theater_id);
			 break;
		 
		 case 3:
			 System.out.println("Select the number corresponding to the movie to display showtime and theater information");
			 movieIndex = 1;
			 for (Movie m : movies)
			 {
				 movieSelect.put(new Integer(movieIndex), m);
				 movieIndex++;
			 }
			 Utility.displayMovieList(movieSelect);
			 movie_id = sc.nextInt();
			 System.out.println("Would you like to see only future showings and remove previous showtimes? Y/N:");
			 futureOnly = sc.next();
			 Movie mov = movieSelect.get(new Integer(movie_id));
			 if (futureOnly.equalsIgnoreCase("Y"))
			 {
				 Utility.displayShowtimesByMovie(mov, true);
			 }
			 else
			 {
				 Utility.displayShowtimesByMovie(mov, false);
			 }
			 break;
			 
		 case 4:
			 System.out.println("Enter radius and zip code to display a custom search of local theater showtimes");
			 System.out.print("Radius: ");
			 radius = sc.nextInt();
			 
			// System.out.println(" ");
			 System.out.print("Zipcode: ");
			 zipCode = sc.next();
			 res = show.invokeCustomShowtimes(zipCode, radius);
			 movies = show.loadMovieData(res.getBody());
			 Utility.displayLocalShowtimeInfo(movies);
			 break;
			 
		 case 5:
			 System.out.println("Goodbye!");
			 break;
			 
			 default:
				 System.out.println("Please enter a value from the menu so I can process the request");
		 }
		}while (menu_selection != 5);
		}catch(Exception e)
		{
			System.out.println("Exception caught inside of the ShowtimeClient class, while running the main() method...");
			e.printStackTrace();
		}
	}

}
