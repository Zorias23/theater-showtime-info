package com.huxley.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import com.huxley.model.*;
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
	
	public DatabaseUtil getInstance()
	{
		if (database == null)
		{
			database = new DatabaseUtil();
			return database;
		}
		else
		{
			return database;
		}
	}
	public static HashMap<Integer, Theater> getTheaters()
	{
		return theaters;
	}
	public static boolean loadTheaterData()
	{
		boolean success_loaded = true;
		try {
			Class.forName(driverClass);  
			Connection con=DriverManager.getConnection(  
			databaseURL,db_username,db_password);  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery(getAllFromTheaterQuery);  
			Theater t;
			int api_ID = 0;
			while (rs.next())
			{
				t = new Theater();
				t.setApi_ID(rs.getInt(2));
				api_ID = rs.getInt(2);
				t.setName(rs.getString(3));
				
				t.setAddress(rs.getString(4));
				
				t.setPhone(rs.getString(5));
			
				t.setCrossStreets(rs.getString(6));
				
				t.setDistanceHome(rs.getString(7));
				theaters.put(new Integer(api_ID), t);
				
				//System.out.println("API-ID: " + api_id + ", theater has been added to application local data ");
			}
			con.close();  
		}catch(Exception e)
		{
			//if we're here, we need to return false so we know data wasn't loaded properly
			success_loaded = false;
		}
		//System.out.println("In DatabaseUtil we processed and stored " + theaters.size() + " records from the database.");
		return success_loaded;
	}
	
}