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
import com.huxley.model.User;
import com.huxley.security.PasswordUtils;
import com.huxley.service.ShowtimeService;
public class Utility {
	
	public static final String SUCCESS_SIGNUP_MESSAGE = "Contgratluations! You have successfully signed up. You must check your email and verify your account by clicking on the link in your email. Then you can successfully login to the application.";
	
	/**
	 * This method converts a runtime String from something like PT01H40M to 1hr and 40mins
	 * @param time
	 * @return String
	 */
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
	
	/**
	 * This methods builds the email message sent to the newly created user and instructs them to verify their account with a specific link
	 * @param u
	 * @return String
	 */
	public static String verifyEmailAccountMessage(User u)
	{
		String message = "";
		message += "Hello, you have recently created an account with the Movine app with the following username: " + u.getUserName() + ".";
		message+= " In order to continue to use the app, you must verify your email address. Please click on the provided link and then you're account will be fully created! ";
		message += "To confirm your account, please click here : "
	            +"http://localhost:8080/movie_showtimes/confirm-account.html?token="+u.getConfirmationToken();
		return message;
	}
	
	/**
	 * This method builds the email message sent to the user if they have requested a password change. It creates a link for them to click on to send them to the password reset page.
	 * The link contains a unique token that will allow them to reset their password.
	 * @param u
	 * @return
	 */
	public static String requestPasswordChangeMessage(User u)
	{
		String message = "";
		message += "Hello, you have requested a password change for your account in the Movine app, for the following username: " + u.getUserName() + ".";
		message+= " In order to change your password, please click on the provided link and you will be forwarded to the password reset page! ";
		message += "To change your password, please click here : "
	            +"http://localhost:8080/movie_showtimes/change-password.html?token="+u.getConfirmationToken();
		return message;
	}
	
	/**
	 * This method builds the query for inserting a new confirmation token record right after a new user is created a new user record is inserted into the user table
	 * @param u
	 * @param userId
	 * @return String
	 */
	public static String buildAddConfirmationTokenQuery(User u, int userId)
	{
		String query = "";
		query += "INSERT INTO confirmation_token (USER_ID, CONFIRMATION_TOKEN, CREATE_DATE) "
				+ " values (";
		query += userId + ", ";
		query += "'" + u.getConfirmationToken() + "', ";
		query += "CURRENT_TIMESTAMP)"; 
		return query;
	}
	
	/**
	 * This method builds the query to retrieve the userId associated with a newly created user in the user table. This is primarily used for inserting a new record into the confirmation_token table, since it needs the userId as a foreign key
	 * @param u
	 * @return String
	 */
	public static String buildGetUserIdQuery(String userName)
	{
		String query = "";
		query = "Select USER_ID from User where USERNAME = '" + userName + "'";
		return query;
	}
	
	/**
	 * This query gets a UserID for a user, based on the username, but only returns the record if the user is also verified, is_verified = true
	 * @param userName
	 * @return
	 */
	public static String buildGetVerifiedUserIdQuery(String userName)
	{
		String query = "";
		query = "Select USER_ID from User where USERNAME = '" + userName + "' AND is_verified = true";
		return query;
	}
	
	public static String buildEnableActiveUserQuery(int userId)
	{
		String query = "";
		query += "UPDATE user SET IS_VERIFIED = true ";
		query += "WHERE USER_ID = " + userId;
		return query;
	}
	
	public static String buildVerifyTokenQuery(String confirmationToken)
	{
		String query = "";
		query = "Select USER_ID from confirmation_token where confirmation_token = '" + confirmationToken + "'";
		return query;
	}
	
	/**
	 * This method returns a String SQL statement that is used for inserting a new user into the user table, only the values username, password and password salt is set at this point
	 * @param u
	 * @return String
	 */
	public static String buildAddUserQuery(User u)
	{
		String query = "";
		query += "INSERT INTO user (USERNAME, USER_PASSWORD, PASSWORD_SALT) "
				+ " values ('";
		query += u.getUserName() + "', ";
		query += "'" + u.getSecurePassword() + "', ";		
		query += "'" + u.getPasswordSalt() + "' ) ";		
/*INSERT INTO user (USERNAME, USER_PASSWORD)
values ('Zorias23', 'Colette23');
*/
		return query;
	}
	
	
	/**
	 * This method takes a Theater object, grabs it's latLong attribute and then parses it into 2 seperate Strings for lattitude and longitude
	 * @param t
	 */
	public static void setLongLat(Theater t)
	{
		if (t == null || t.getLatLong() == null || t.getLatLong().length() == 0)
		{
			return;
		}
		String latlong = t.getLatLong();
		String[] nums = latlong.split(",");
		t.setLattitude(nums[0]);
		t.setLongitude(nums[1]);
	}
	
	/**
	 * Generates a secure password and unique password_salt value based on the plaintext password entered by the user at signup
	 * @param u
	 */
	public static void generateSecurePassword(User u)
	{
		if (u == null || u.getPassword() == null || u.getPassword().length() == 0)
		{
			return;
		}
		String secure = "";
		String salt = PasswordUtils.getSalt(30);
		String password = u.getPassword(); //the plaintext password entered on the form
		secure = PasswordUtils.generateSecurePassword(password, salt);
		u.setSecurePassword(secure);
		u.setPasswordSalt(salt);
	}
	
	/**
	 * This method is just to simplify checking if a String is either null or has a length of 0
	 * @param str
	 * @return boolean
	 */
	public static boolean emptyString(String str)
	{
		boolean empty = false;
		if (str == null || str.length() == 0)
		{
			empty = true;
		}
		return empty;
	}
	
	/**
	 * The secure password and password salt are going to be stored in the database, both of those fields will be read to see if the provided user password matches and is valid
	 * This method assumes we already have a loaded User object. At the login screen we need to use the method which only has UserName and provided password
	 * @param providedPass
	 * @param u
	 * @return boolean
	 */
	public static boolean isPasswordValid(String providedPass, User u)
	{
		boolean isValid = false;
		if (providedPass == null || u == null || Utility.emptyString(u.getSecurePassword()) || Utility.emptyString(u.getPasswordSalt()))
		{
			return false;
		}
		isValid = PasswordUtils.verifyUserPassword(providedPass, u.getSecurePassword(), u.getPasswordSalt());
		return isValid;
	}
	
	/**
	 * This method will either return today represented as a Date object, or tomorrow if it's after 11pm
	 * @return Date object
	 */
	public static Date getDefaultStartDate()
	{
		Date dat = null;
		boolean afterHours = Utility.isAfterHours();
		if (afterHours == true)
		{
			dat = Utility.getTomorrowAsDate();
		}
		else
		{
			dat = new Date();//defaults to today
		}
		return dat;
	}
	
	/**
	 * This method checks to see if it is currently 11pm or later. If so, we're going to want to default to showing showtimes
	 * for tomorrow, not today
	 * @return boolean
	 */
	public static boolean isAfterHours()
	{
		boolean afterHours = false;
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR);
		if (calendar.get(Calendar.AM_PM) == Calendar.PM && hour == 11)
		{
			//it's 11pm or later
			afterHours = true;
		}
		return afterHours;
	}
	
	/**
	 * This method will return whatever tomorrow is represented as a Date object
	 * @return Date object
	 */
	public static Date getTomorrowAsDate()
	{
		Date tomorrow = null;
		 Calendar calendar = Calendar.getInstance();
		 calendar.add(Calendar.DAY_OF_YEAR, 1);
		 tomorrow = calendar.getTime();
		return tomorrow;
	}
	
	/**
	 * This method will take a string version of a date in the format a user would like, such as MM/dd/yyyy, or 10/17/2019, and converts it to the formatted date string that the
	 * API wants it to be in: yyyy-MM-dd
	 * @param dateStr
	 * @return String
	 */
	public static String getAPIFormattedDate(String dateStr)
	{
		String dateString = "";
		Date dat = null;
		try {
			 dat=new SimpleDateFormat("MM/dd/yyyy").parse(dateStr);
			 SimpleDateFormat api = new SimpleDateFormat("yyyy-MM-dd");
			 dateString = api.format(dat);
		}catch(ParseException pe)
		{
			
		}
		return dateString;
	}
	
	/**
	 * takes a dateString in the form of 06/29/2019 or MM/dd/yyyy and returns the Date object representing that value
	 * @param dateStr
	 * @return Date object
	 */
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
	
	/**
	 * This method is just trying to make the data written to System.out a little prettier and easier to read.  It's goal is to have a line break after about every 145 characters or so
	 * @return String
	 */
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
	
	
	/**
	 * This method will convert the JSON dateTime being returned in the API response into a more readable String.  Ex: 2019-06-29T11:50 --> 11:50 am
	 * the JSON response returns the hour as 24 hour army time, we convert it to standard 12 hour time and append either am or pm to the string returned
	 * @param JSON_time
	 * @return String
	 */
	public static String getShowingTime(String JSON_time)
	{
		if (Utility.emptyString(JSON_time))
		{
			return null;
		}
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
	
	/**
	 * This method grabs the HashMap of Theater objects stored in the DatabaseUtil class and displays in System.out the Theater ID, name and cross streets for each theater
	 */
	public static void displayTheaterList()
	{
		HashMap<Integer, Theater> theaters = DatabaseUtil.getTheaters();
		if (theaters == null || theaters.size() == 0)
		{
			System.out.println("Theater list in DatabaseUtil is null or empty.");
			return;
		}
		for (Map.Entry<Integer, Theater> entry : theaters.entrySet()) {
		    //System.out.println("ID: " + entry.getKey() + ", T " + entry.getValue());
			System.out.println("(" + entry.getKey() + ")      " + entry.getValue().getName() + "      " + entry.getValue().getCrossStreets());
		}
		System.out.println(" ");
	}
	
	
	/**
	 * This method iterates through the given HashMap of Movie objects and writes to System.out the movie ID, title and description
	 * @param movieList
	 */
	public static void displayMovieList(HashMap<Integer, Movie> movieList)
	{
		if (movieList == null || movieList.size() == 0)
		{
			System.out.println("The given movie list is null or empty.");
			return;
		}
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
			    System.out.println("     " + selected_theater.getDistanceHome() + " miles from home.");
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
			    System.out.println("     " + theater.getDistanceHome() + " miles from home.");
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
	 * @return boolean
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
			    System.out.println("     " + theater.getDistanceHome() + " miles from home.");
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
