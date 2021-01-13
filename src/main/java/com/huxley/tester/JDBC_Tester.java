package com.huxley.tester;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import com.huxley.model.Theater;

public class JDBC_Tester {

	public static void main(String[] args) {
		Theater t = new Theater();
		try {
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/movies","root", "colette");  
			Statement stmt=con.createStatement();  
			
			int api_id = 0;
			String name = "";
			String address = "";
			String phone = "";
			double distance = 0;
			String cross_streets = "";
			api_id = 10397;
			ResultSet rs=stmt.executeQuery("select * from theater where api_id = " + api_id);  
			System.out.println("fetch size is " + rs.getFetchSize());
			System.out.println("going to fetch theater with api_id of " + api_id);
			
			while (rs.next())
			{
				api_id = rs.getInt(1);
				name = rs.getString(2);
				address = rs.getString(3);
				phone = rs.getString(4);
				cross_streets = rs.getString(5);
				distance = rs.getDouble(6);
				t.setApi_ID(api_id);
				t.setName(name);
				t.setAddress(address);
				t.setPhone(phone);
				t.setCrossStreets(cross_streets);
				t.setDistanceHome(distance);
			}
			con.close();  
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Theater object has been created and has the following data:");
		System.out.println("name: " + t.getName() + " cross_streets: " + t.getCrossStreets());
	}

}
