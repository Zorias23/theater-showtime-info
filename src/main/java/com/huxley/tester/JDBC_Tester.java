package com.huxley.tester;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBC_Tester {

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/movies","root","colette");  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from theater");  
			int api_id = 0;
			String name = "";
			String address = "";
			String phone = "";
			String distance = "";
			String cross_streets = "";
			while (rs.next())
			{
				api_id = rs.getInt(2);
				name = rs.getString(3);
				address = rs.getString(4);
				phone = rs.getString(5);
				cross_streets = rs.getString(6);
				distance = rs.getString(7);
				System.out.println("API-ID: " + api_id + ", Name: " + name + ", Address: " + address + ", Phone: " + phone + ", Cross_streets: " + cross_streets 
						+ ", Distance: " + distance);
			}
			con.close();  
		}catch(Exception e)
		{
			
		}

	}

}
