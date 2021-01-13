package com.huxley.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.huxley.generic.Utility;
import com.huxley.model.*;
import com.huxley.security.PasswordUtils;
public class DatabaseUtil {
	
	private DatabaseUtil database = null;
	private static String driverClass = "com.mysql.jdbc.Driver";
	private static String databaseURL = "jdbc:mysql://localhost:3306/movies";
	private static String db_username = "root";
	private static String db_password = "colette";
	private static String getAllFromTheaterQuery = "select * from theater";
	private static HashMap<Integer, Theater> theaters = new HashMap<Integer, Theater>();
	private DatabaseUtil()
	{
		System.out.println("only one instance allowed.");
	}
	
	
	public static HashMap<Integer, Theater> getTheaters()
	{
		return theaters;
	}
	
	/**
	 * This method will check to see if a given username already exists in the user table. This is executed when a user is first trying to sign up for an account, we want to avoid duplicate users
	 * @param userName
	 * @return
	 */
	public static boolean userAlreadyExists(String userName)
	{
		boolean exists = false;
		User user = null;
		try {
			Class.forName(driverClass);  
			Connection con=DriverManager.getConnection(  
			databaseURL,db_username,db_password);  
			Statement stmt=con.createStatement();  
			String query = "Select * from User where USERNAME = '" + userName + "'";
			ResultSet rs=stmt.executeQuery(query);  
			if (rs.isBeforeFirst() == false) {    //this means empty result set, no user found
			    System.out.println("No user data found for: " + userName);
			    exists = false;
			} 
			else
			{ 
				exists = true;
			}
		    rs.close();
		    con.close();
		}catch(Exception e)
		{
			exists = false;
			System.out.println("Error found trying to verify if user with username: " + userName + " already exists in the DB...");
			e.printStackTrace();
		}
		return exists;
	}
	
	/**
	 * If the user has provided a username and password that exists, has verified their account through the providied email address,  we will return the User object.  Otherwise we return null.
	 * @param userName
	 * @param plainPassword
	 * @return User object
	 */
	public static User verifyUserExists(String userName, String plainPassword)
	{
		User user = null;
		try {
			Class.forName(driverClass);  
			Connection con=DriverManager.getConnection(  
			databaseURL,db_username,db_password);  
			Statement stmt=con.createStatement();  
			String query = "Select * from User where USERNAME = '" + userName + "'";
			ResultSet rs=stmt.executeQuery(query);  
			if (rs.isBeforeFirst() == false) {    //this means empty result set, no user found
			    System.out.println("No user data found for: " + userName);
			    rs.close();
			    con.close();
			    return null;
			} 
			//we do have a user, start populating the object, if password is correct
			user = new User();
			String securePassword;
			String salt;
			boolean accountVerified = false;
			while (rs.next())
			{
				if (rs.isFirst() == true) //check if the password matches, else we have an invalid user and need to return null, if we're on the first record
				{
					securePassword = rs.getString(3);
					salt = rs.getString(9);
					boolean isValidUser = PasswordUtils.verifyUserPassword(plainPassword, securePassword, salt);
					if (isValidUser == false)
					{
						System.out.println("Password is incorrect for user: " + userName);
						user = null;
						break;
					}
				    accountVerified = rs.getBoolean(10);
					if (accountVerified == false)
					{
						System.out.println("Account is not verified through email for user: " + userName);
						user = null;
						break;
					}
				}
				user.setUserName(rs.getString(2));
				user.setSecurePassword(rs.getString(3));
				user.setPassword(plainPassword);
				user.setPreferredZipCode(rs.getString(4));
				user.setPreferredRadius(rs.getString(5));
				user.setFilterChildrenContent(rs.getBoolean(6));
				user.setFilterFuture(rs.getBoolean(7));
				user.setAdmin(rs.getBoolean(8));
				user.setPasswordSalt(rs.getString(9));
				user.setUserId(rs.getInt(1));
				user.setVerified(accountVerified);
			}
			rs.close();
			con.close();  
		}catch(Exception e)
		{
			user = null;
			e.printStackTrace();
		}
		//grab user from DB based on UserName, which should be unique, load the secure password and salt values based on userName, verify provided plain password
		return user;
	}
	
	/**
	 * This method does a few things to complete the user registration. First we check the confirmation token gathered from the clicked link in the email and see if it matches up to a proper userId.
	 * If it does, we update the user record matching that userId and set the is_verified field to true. The user is then considered to have a complete registration.
	 * @param confirmationToken
	 * @return boolean true if registration is completed successfully, false otherwise
	 */
	public static boolean completeUserRegistration(String confirmationToken)
	{
		boolean completed = false;
		
		try {
			Class.forName(driverClass);  
			Connection con=DriverManager.getConnection(  
			databaseURL,db_username,db_password);  
			Statement stmt=con.createStatement();  
			String verifyTokenQuery = Utility.buildVerifyTokenQuery(confirmationToken);
			int userId = 0;
			ResultSet rs=stmt.executeQuery(verifyTokenQuery); 
			while (rs.next())
			{
				if (rs.isFirst() == true)
				{
					userId = rs.getInt(1);
					break;
				}
			}
			if (userId == 0)
			{
				throw new SQLException("UserId was not found to for confirmation record: " + confirmationToken);
			}
			String enableActiveUserQuery = Utility.buildEnableActiveUserQuery(userId);
			stmt.executeUpdate(enableActiveUserQuery);
			System.out.println("Executed enableActiveUserQuery to add verify user with userId: " + userId);
			completed = true;
			rs.close();
			con.close();  
		
		}catch(SQLException sqe)
		{
			completed = false;
			sqe.printStackTrace();
		}
		catch(Exception e)
		{
			//if we're here, we need to return false so we know data wasn't loaded properly
			completed = false;
			e.printStackTrace();
		}
		return completed;
	}
	
	public static boolean requestPasswordChange(User u)
	{
		boolean success = true;
		try {
			Class.forName(driverClass);  
			Connection con=DriverManager.getConnection(  
			databaseURL,db_username,db_password);  
			Statement stmt=con.createStatement();  
			String getUserIdQuery = Utility.buildGetVerifiedUserIdQuery(u.getUserName());
			ResultSet rs=stmt.executeQuery(getUserIdQuery); 
			int userId = 0;
			while (rs.next())
			{
				if (rs.isFirst() == true)
				{
					userId = rs.getInt(1);
					break;
				}
			}
			if (userId == 0)
			{
				throw new SQLException("UserId was not found for record");
			}
			u.setUserId(userId);
			String confirmationTokenQuery = Utility.buildAddConfirmationTokenQuery(u, userId);
			stmt.executeUpdate(confirmationTokenQuery);
			System.out.println("Executed getUserIdQuery and confirmationTokenQuery successfully to create new password request for:" + u.getUserName());
			con.close();  
			rs.close();
		}catch(Exception e)
		{
			//if we're here, we need to return false so we know data wasn't loaded properly
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	
	/**
	 * When a user is first created, we're adding a new User record into the User table, but only username and password are being set at this point. Also a confirmation token is created in a seperate table, and the email verification process begins
	 * @param u
	 * @return boolean true if user is created successfully, false otherwise
	 */
	public static boolean createUser(User u)
	{
		boolean success_loaded = true;
		try {
			Class.forName(driverClass);  
			Connection con=DriverManager.getConnection(  
			databaseURL,db_username,db_password);  
			Statement stmt=con.createStatement();  
			Utility.generateSecurePassword(u);
			//call the method I just created in Utility that builds the addUser query, then executeUpdate with that String
			String query = Utility.buildAddUserQuery(u);
			stmt.executeUpdate(query);
			String getUserIdQuery = Utility.buildGetUserIdQuery(u.getPassword());
			ResultSet rs=stmt.executeQuery(getUserIdQuery); 
			int userId = 0;
			while (rs.next())
			{
				if (rs.isFirst() == true)
				{
					userId = rs.getInt(1);
					break;
				}
			}
			if (userId == 0)
			{
				throw new SQLException("UserId was not found for record");
			}
			u.setUserId(userId);
			String confirmationTokenQuery = Utility.buildAddConfirmationTokenQuery(u, userId);
			stmt.executeUpdate(confirmationTokenQuery);
			System.out.println("Executed addUserQuery, getUserIdQuery and confirmationTokenQuery successfully to add new user:" + u.getUserName());
			//System.out.println(query);
			con.close();  
			rs.close();
		}catch(Exception e)
		{
			//if we're here, we need to return false so we know data wasn't loaded properly
			success_loaded = false;
			e.printStackTrace();
		}
		return success_loaded;
	}
	
	public static Connection getDatabaseConnection()
	{
		Context initContext = null;
		Context envContext = null;
		DataSource ds = null;
		Connection conn = null;
		
		try {
			 initContext = new InitialContext();
			 envContext  = (Context)initContext.lookup("java:/comp/env");
			 ds = (DataSource)envContext.lookup("jdbc/MoviesDB");
			 conn = ds.getConnection();
		}catch(Exception e)
		{
			System.out.println("Error getting database connection!  Here's the stacktrace:");
			e.printStackTrace();
		}

		return conn;
	}
	
	/**
	 * To avoid any possibility of a memory leak, the static HashMap can be cleared with this method
	 */
	public static void clearTheaterData()
	{
		HashMap<Integer, Theater> test = getTheaters();
		if (test != null && test.size() > 0)
		{
			test.clear();
		}
	}
	
	/**
	 * This method loads the internal HashMap of theaters with all of the theaters from the Theater table in the DB
	 * @return boolean true is successfully loaded, false if any exception occurs
	 */
	public static boolean loadTheaterData()
	{
		boolean success_loaded = true;
		try {
			Class.forName(driverClass);  
			Connection con=DriverManager.getConnection(  
			databaseURL,db_username,db_password);  
			//Connection con = DatabaseUtil.getDatabaseConnection(); //not working yet
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery(getAllFromTheaterQuery);  
			Theater t;
			int api_ID = 0;
			while (rs.next())
			{
				t = new Theater();
				t.setApi_ID(rs.getInt(1));
				api_ID = rs.getInt(1);
				t.setName(rs.getString(2));
				
				t.setAddress(rs.getString(3));
				
				t.setPhone(rs.getString(4));
			
				t.setCrossStreets(rs.getString(5));
				
				t.setDistanceHome(rs.getDouble(6));
				t.setLatLong(rs.getString(7));
				Utility.setLongLat(t);
				theaters.put(new Integer(api_ID), t);
				
				//System.out.println("API-ID: " + api_id + ", theater has been added to application local data ");
			}
			con.close();  
		}catch(Exception e)
		{
			//if we're here, we need to return false so we know data wasn't loaded properly
			success_loaded = false;
			e.printStackTrace();
		}
		System.out.println("In DatabaseUtil we processed and stored " + theaters.size() + " records from the database.");
		return success_loaded;
	}
	
}
