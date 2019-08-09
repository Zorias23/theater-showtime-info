package com.huxley.generic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;

import com.huxley.db.DatabaseUtil;
import com.huxley.model.Movie;
import com.huxley.model.Theater;
import com.huxley.service.ShowtimeService;
public class Utility {
	
	//going to convert a runtime String from something like PT01H40M to 1hr and 40mins
	public static String runTimeString(String time)
	{
		String readable_time = "";
		String[] hourSplit;
		String[] minuteSplit;
		String finalHour;
		String finalMinute;
		time = time.replaceAll("\"", "");  //remove quotes from beginning and end of string, if present
		String[] firstSplit = time.split("H");
		String hour = firstSplit[0];
		String minutes = firstSplit[1];
		hourSplit = hour.split("PT");
		minuteSplit = minutes.split("M");
		finalHour = hourSplit[1];
		finalMinute = minuteSplit[0];
		int hours = Integer.valueOf(finalHour).intValue();
		switch (hours) { 
		case 1: 
		readable_time = finalHour + " hour and " + finalMinute + " minutes";
		break;
		case 0:
		readable_time = finalMinute + " minutes";
		break;
		default:
		readable_time = finalHour + " hours and " + finalMinute + " minutes";
		break;
		}
		readable_time = finalHour + " hours and " + finalMinute + " minutes";
		return readable_time;
	}
	//takes a dateString in the form of 06/29/2019 or MM/dd/yyyy as a Date object
	public static Date getFormattedDate(String dateStr)
	{
		Date formatted = null;
		try {
			 formatted=new SimpleDateFormat("MM/dd/yyyy").parse(dateStr);
		}catch(ParseException pe)
		{
			
		}
		
		return formatted;
	}
	public static String prettyCut(String longString)
	{
		if (longString.length() < 145)
		{
			return longString;
		}
		String finalString = "";
		String[] parts = new String[5];
		int index = 0;
		int character_count = 0;
		int cuts = longString.length() / 145;
		int sze = longString.length();
		while (index < cuts)
		{
			parts[index] = longString.substring(character_count, (character_count+145));
			index++;
			character_count += 145;
		}
		if (character_count < longString.length())
		{
			parts[index] = longString.substring(character_count, sze);
		}
		index = 0;
		while (index < parts.length)
		{
			if (parts[index] == null || parts[index].length() == 0)
			{
				break;
			}
			else
			{
				finalString += parts[index];
				finalString += "\n";
			}
			index++;
		}
		return finalString;
	}
	//will convert the JSON dateTime being returned into a more readable String.  Ex: 2019-06-29T11:50 --> 11:50 am
	public static String getShowingTime(String JSON_time)
	{
		String showing = "";
		String am_pm = "";
		JSON_time = JSON_time.replaceAll("\"", "");  //replaces quotes from beginning and end of String, if present
		String[] formatSplit = JSON_time.split("T");
		//after this, we want elemment[1] because that's after the "T" in the dateTime String
		String hoursMins = formatSplit[1]; //may give us something like 14:52
		String[] formatSplit2 = hoursMins.split(":");
		String hour = formatSplit2[0];
		String minutes = formatSplit2[1];
		//do arithmetic on hour to figure out am or pm
		int hour_int = Integer.valueOf(hour);
		if (hour_int > 12) //gets rid of army time
		{
			hour_int = hour_int - 12;
			am_pm = "PM";
		}
		else if (hour_int == 12)
		{
			am_pm = "PM";
		}
		else if (hour_int == 0)
		{
			hour_int = 12;
			am_pm = "AM";
		}
		else
		{
			am_pm = "AM";
		}
		hour = String.valueOf(hour_int);
		showing = hour + ":" + minutes + " " + am_pm;
		return showing;
	}
	
	public static void displayTheaterList()
	{
		HashMap<Integer, Theater> theaters = DatabaseUtil.getTheaters();
		for (Map.Entry<Integer, Theater> entry : theaters.entrySet()) {
		    //System.out.println("ID: " + entry.getKey() + ", T " + entry.getValue());
			System.out.println("(" + entry.getKey() + ")      " + entry.getValue().getName() + "      " + entry.getValue().getCrossStreets());
		}
		System.out.println(" ");
	}
	
	public static void displayMovieList(HashMap<Integer, Movie> movieList)
	{
		for (Map.Entry<Integer, Movie> entry : movieList.entrySet()) {
		    //System.out.println("ID: " + entry.getKey() + ", T " + entry.getValue());
			System.out.println("(" + entry.getKey() + ")      " + entry.getValue().getTitle() + "   Description: " + entry.getValue().getShortDescription());
		}
		System.out.println(" ");
	}
	
	public static void displayShowtimesByTheater(List<Movie> movies, int theaterId)
	{
		int movie_display_count = 1;
		System.out.println("Here is your theater showtime data:");
		Integer theater_key = new Integer(theaterId);
		
		for(Movie mov : movies)
		{
			HashMap<Integer, Theater> theaters = mov.getCurrentTheaters();
			if (theaters.containsKey(theater_key))
			{
				Theater selected_theater = theaters.get(theater_key);
				System.out.println(movie_display_count + ")");
				System.out.println("Movie: " + mov.getTitle());
				int index = 0;
				String[] directors;
				String[] actors;
				String[] genres;
				if (mov.getDirectors() != null)
				{
					directors = mov.getDirectors();
					System.out.print("Director: ");
					while (index < directors.length)
					{
						System.out.println(directors[index]);
						index++;
					}
				}
				index = 0;
				if (mov.getTopCast() != null)
				{
					actors = mov.getTopCast();
					System.out.print("Top Cast: ");
					while (index < actors.length)
					{
						if ((index+1) == actors.length)
						{
							System.out.print(actors[index]);
						}
						else
						{
							System.out.print(actors[index] +", ");
						}
						index++;
					}
					
				}
				System.out.println(" ");
				index = 0;
				if (mov.getGenres() != null)
				{
					genres = mov.getGenres();
					System.out.print("Genres: ");
					while (index < genres.length)
					{
						if ((index+1) == genres.length)
						{
							System.out.print(genres[index]);
						}
						else
						{
							System.out.print(genres[index] +", ");
						}
						index++;
					}
				}
				System.out.println(" ");
				if (mov.getReleaseDate() != null)
				{
					System.out.println("Release Date: " + mov.getReleaseDate());
				}
				if (mov.getRuntime() != null)
				{
					System.out.println("Running Time: " + mov.getRuntime());
				}
				if (mov.getLongDescription() != null)
				{
					String original = mov.getLongDescription();
					String shorter = Utility.prettyCut(original);
					System.out.println("Description: " + shorter);
				}
				System.out.println("Here is your Theater and showtime information:");
			    System.out.println("     Theater: " + selected_theater.getName() + "     (" + selected_theater.getApi_ID() + ")");
			    System.out.println("     " + selected_theater.getAddress());
			    System.out.println("     Cross Streets: " + selected_theater.getCrossStreets());
			    System.out.println("     " + selected_theater.getDistanceHome() + " from home.");
			    System.out.print("     Showtimes: ");
			    int count = 0;
			    List<String> showtimes = selected_theater.getShowtimes();
			    for (String st : showtimes)
			    {
			    	if ( (count+1) == showtimes.size())
			    	{
			    		System.out.print(st);
			    	}
			    	else
			    	{
			    		System.out.print(st + ", ");
			    	}	
			    	count++;
			    }
			    selected_theater.setShowtimes(showtimes);
			    System.out.println(" ");
			    System.out.println(" ");
				System.out.println("-----------------------------------------------------------------------------------------------------------------------");
				movie_display_count++;
			} //if contains theater
		} //outermost movie for loop
	}
	
	//**have to take care of trailing zeros which I think are lost with double conversion. ex: 11:30 am becomes 11.3, so when I convert back I get 11:3 am which is wrong
	public static List<String> sortShowTimes(List<String> showtimes)
	{
		List<String> sorted = new ArrayList<String>();
		TreeSet<Double> sortedShows = new TreeSet<Double>();
		double val = 0;
		for (String st : showtimes)
		{
			if (st.endsWith("AM"))
			{
				String[] split = st.split("AM");
				String time = split[0];
				time = time.trim();
				time = time.replaceAll(":", ".");
				Double doubleTime = Double.valueOf(time);
				sortedShows.add(doubleTime);
			}
			if (st.endsWith("PM"))
			{
				String[] split = st.split("PM");
				String time = split[0];
				time = time.trim();
				time = time.replaceAll(":", ".");
				Double doubleTime = Double.valueOf(time);
				if (doubleTime >= 1.0)
				{
					doubleTime += 12; //account for pm and army time but values of 12.00 to 12.99 should not have 12 added to it
				}
				
				sortedShows.add(doubleTime);
			}
		}
		for (Double d : sortedShows)
		{
			double rounder = 0;
			String trailingZero;
			String[] test;
			String minutes;
			if (d.doubleValue() < 12.0)
			{
				rounder = d.doubleValue();
				rounder = Math.ceil((rounder*1000)) / 1000;
				String stringVal = String.valueOf(rounder);
				String escaped = "\\.";
				stringVal = stringVal.replaceAll(escaped, ":");
				trailingZero = stringVal;
				test = trailingZero.split(":");
				//this is for cases such as a string being 11:3 when it should be 11:30, if the string returned in element 1 is just a size of 1, we need to add a trailing 0
				if (test[1] != null && test[1].length() > 0)
				{
					
					if (test[1].length() == 1)
					{
						//we need to add a trailing zero.  the only time we'll have a length of one, is if we're incorrectly doing 11:3 instead of 11:30
						minutes = test[1];
						minutes += "0";
						trailingZero = test[0] + ":" + minutes;
						
					}
					if (test[1].length() > 2)  //for cases like 11:301
					{
						minutes = test[1];
						minutes = minutes.substring(0, 2);
						trailingZero = test[0] + ":" + minutes;
					}
					
				}
				stringVal = trailingZero; //may or may not have been modified to add a trailing zero or remove extra digits at the end of the string
				stringVal += " AM";
				sorted.add(stringVal);
			}
			else if (d.doubleValue() < 13.0)
			{
				val = d.doubleValue();
				rounder = d.doubleValue();
				rounder = Math.ceil((rounder*1000)) / 1000;
				String stringVal = String.valueOf(rounder);
				String escaped = "\\.";
				stringVal = stringVal.replaceAll(escaped, ":");
				trailingZero = stringVal;
				test = trailingZero.split(":");
				if (test[1] != null && test[1].length() > 0)
				{
					
					if (test[1].length() == 1)
					{
						//we need to add a trailing zero.  the only time we'll have a length of one, is if we're incorrectly doing 11:3 instead of 11:30
						minutes = test[1];
						minutes += "0";
						trailingZero = test[0] + ":" + minutes;
						
					}
					if (test[1].length() > 2)  //for cases like 11:301
					{
						minutes = test[1];
						minutes = minutes.substring(0, 2);
						trailingZero = test[0] + ":" + minutes;
					}
					
				}
				stringVal = trailingZero;
				stringVal += " PM";
				sorted.add(stringVal);
			}
			else
			{
				val = d.doubleValue();
				val -= 12;
				rounder = val;
				rounder = Math.ceil((rounder*1000)) / 1000;	
				String stringVal = String.valueOf(rounder);
				String escaped = "\\.";
				stringVal = stringVal.replaceAll(escaped, ":");
				trailingZero = stringVal;
				test = trailingZero.split(":");
				if (test[1] != null && test[1].length() > 0)
				{
					
					if (test[1].length() == 1)
					{
						//we need to add a trailing zero.  the only time we'll have a length of one, is if we're incorrectly doing 11:3 instead of 11:30
						minutes = test[1];
						minutes += "0";
						trailingZero = test[0] + ":" + minutes;
						
					}
					if (test[1].length() > 2)  //for cases like 11:301
					{
						minutes = test[1];
						minutes = minutes.substring(0, 2);
						trailingZero = test[0] + ":" + minutes;
					}
					
				}
				stringVal = trailingZero;
				stringVal += " PM";
				sorted.add(stringVal);
			}
		}
		return sorted;
	}
	
	public static void displayShowtimesByMovie(Movie mov, boolean showFutureOnly)
	{
		System.out.println("Here is your movie data:");
			System.out.println("Movie: " + mov.getTitle());
			int index = 0;
			String[] directors;
			String[] actors;
			String[] genres;
			if (mov.getDirectors() != null)
			{
				directors = mov.getDirectors();
				System.out.print("Director: ");
				while (index < directors.length)
				{
					System.out.println(directors[index]);
					index++;
				}
			}
			index = 0;
			if (mov.getTopCast() != null)
			{
				actors = mov.getTopCast();
				System.out.print("Top Cast: ");
				while (index < actors.length)
				{
					if ((index+1) == actors.length)
					{
						System.out.print(actors[index]);
					}
					else
					{
						System.out.print(actors[index] +", ");
					}
					index++;
				}
				
			}
			System.out.println(" ");
			index = 0;
			if (mov.getGenres() != null)
			{
				genres = mov.getGenres();
				System.out.print("Genres: ");
				while (index < genres.length)
				{
					if ((index+1) == genres.length)
					{
						System.out.print(genres[index]);
					}
					else
					{
						System.out.print(genres[index] +", ");
					}
					index++;
				}
			}
			System.out.println(" ");
			if (mov.getReleaseDate() != null)
			{
				System.out.println("Release Date: " + mov.getReleaseDate());
			}
			if (mov.getRuntime() != null)
			{
				System.out.println("Running Time: " + mov.getRuntime());
			}
			if (mov.getLongDescription() != null)
			{
				String original = mov.getLongDescription();
				String shorter = Utility.prettyCut(original);
				System.out.println("Description: " + shorter);
			}
			System.out.println("This movie is showing at the following theaters:");
			HashMap<Integer, Theater> theaters = mov.getCurrentTheaters();
			for (Theater theater : theaters.values()) {
			    System.out.println("     Theater: " + theater.getName() + "     (" + theater.getApi_ID() + ")");
			    System.out.println("     " + theater.getAddress());
			    System.out.println("     Cross Streets: " + theater.getCrossStreets());
			    System.out.println("     " + theater.getDistanceHome() + " from home.");
			    System.out.print("     All showtimes: ");
			    int count = 0;
			    List<String> showtimes = theater.getShowtimes();
			    for (String st : showtimes)
			    {
			    	if (showFutureOnly == true)
			    	{
			    		if (Utility.isTimeFuture(st) == false)
			    		{
			    			continue;
			    		}
			    	}
			    	if ((count+1) == showtimes.size())
			    	{
			    		System.out.print(st);
			    	}
			    	else
			    	{
			    		System.out.print(st + ", ");
			    	}	
			    	count++;
			    }
			    System.out.println(" ");
			    System.out.println(" ");
			}
			System.out.println("-----------------------------------------------------------------------------------------------------------------------");
		    System.out.println(" ");
		    System.out.println(" ");
	}
	
	
	/**
	 * This method will take a showtime string, which will be something like 04:50 pm or 11:22 am, and convert this to a date and see if it's a time in the future,
	 * essentially we want to look for only upcoming showtimes, ignoring those that have already been shown
	 * @param showtime
	 * @return
	 */
	public static boolean isTimeFuture(String showtime)
	{
		boolean isFuture = false;
		Calendar before = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		String amPm = "";
		if (showtime.endsWith("PM"))
		{
			amPm = "PM";
			before.set(Calendar.AM_PM, Calendar.PM);
		}
		else
		{
			amPm = "AM";
			before.set(Calendar.AM_PM, Calendar.AM);
		}
		String[] parsed;
		String hoursMins = showtime.substring(0, 5);
		hoursMins = hoursMins.trim();
		String hours;
		String minutes;
		parsed = hoursMins.split(":");
		hours = parsed[0];
		minutes = parsed[1];
		if (hours.equals("12"))
		{
			hours = "0";
			//this is a special case with the Calendar class, if we're using the int field 'HOUR', noon and midnight, aka '12', are represented as '0'
		}
		before.set(Calendar.HOUR, Integer.valueOf(hours).intValue());
		before.set(Calendar.MINUTE, Integer.valueOf(minutes).intValue());
		Date prev = before.getTime();
		Date present = now.getTime();
		if (prev.before(present))
		{
			isFuture = false;
		}
		if (prev.after(present))
		{
			isFuture = true;
		}
		return isFuture;
	}
	
	public static void displayLocalShowtimeInfo(List<Movie> movies)
	{
		System.out.println("***** API URI Call used to load data: " + ShowtimeService.URI_Call + " ***********");
		int movie_display_count = 1;
		System.out.println("Here is your movie data:");
		for(Movie mov : movies)
		{
			System.out.println(movie_display_count + ")");
			System.out.println("Movie: " + mov.getTitle());
			int index = 0;
			String[] directors;
			String[] actors;
			String[] genres;
			if (mov.getDirectors() != null)
			{
				directors = mov.getDirectors();
				System.out.print("Director: ");
				while (index < directors.length)
				{
					System.out.println(directors[index]);
					index++;
				}
			}
			index = 0;
			if (mov.getTopCast() != null)
			{
				actors = mov.getTopCast();
				System.out.print("Top Cast: ");
				while (index < actors.length)
				{
					if ((index+1) == actors.length)
					{
						System.out.print(actors[index]);
					}
					else
					{
						System.out.print(actors[index] +", ");
					}
					index++;
				}
				
			}
			System.out.println(" ");
			index = 0;
			if (mov.getGenres() != null)
			{
				genres = mov.getGenres();
				System.out.print("Genres: ");
				while (index < genres.length)
				{
					if ((index+1) == genres.length)
					{
						System.out.print(genres[index]);
					}
					else
					{
						System.out.print(genres[index] +", ");
					}
					index++;
				}
			}
			System.out.println(" ");
			if (mov.getReleaseDate() != null)
			{
				System.out.println("Release Date: " + mov.getReleaseDate());
			}
			if (mov.getRuntime() != null)
			{
				System.out.println("Running Time: " + mov.getRuntime());
			}
			if (mov.getLongDescription() != null)
			{
				String original = mov.getLongDescription();
				String shorter = Utility.prettyCut(original);
				System.out.println("Description: " + shorter);
			}
			System.out.println("This movie is showing at the following theaters:");
			HashMap<Integer, Theater> theaters = mov.getCurrentTheaters();
			for (Theater theater : theaters.values()) {
			    System.out.println("     Theater: " + theater.getName() + "     (" + theater.getApi_ID() + ")");
			    System.out.println("     " + theater.getAddress());
			    System.out.println("     Cross Streets: " + theater.getCrossStreets());
			    System.out.println("     " + theater.getDistanceHome() + " from home.");
			    System.out.print("     All showtimes: ");
			    int count = 0;
			    List<String> showtimes = theater.getShowtimes();
			    for (String st : showtimes)
			    {
			    	if ((count+1) == showtimes.size())
			    	{
			    		System.out.print(st);
			    	}
			    	else
			    	{
			    		System.out.print(st + ", ");
			    	}	
			    	count++;
			    }
			    System.out.println(" ");
			    System.out.println(" ");
			}
			System.out.println("-----------------------------------------------------------------------------------------------------------------------");
			movie_display_count++;
		}//outermost for loop
	}
}
