package com.huxley.generic;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.lang.StringBuffer;
public class SimpleRunner {

	public static void main(String[] args) {
			
		String s1 = "01:13 AM";
		String s2 = "12:33 AM";
		String s3 = "02:14 PM";
		String s4 = "12:30 PM";
		boolean isFutureTime = false;
		isFutureTime = SimpleRunner.isTimeFuture(s1);
		System.out.print("Test time is: " + s1 + " and this time is ");
		if (isFutureTime == true)
		{
			System.out.print("ahead of now.");
		}
		else
		{
			System.out.print("before now.");
		}
		System.out.println(" ");
		isFutureTime = false; //just reset it
		
		isFutureTime = SimpleRunner.isTimeFuture(s2);
		System.out.print("Test time is: " + s2 + " and this time is ");
		if (isFutureTime == true)
		{
			System.out.print("ahead of now.");
		}
		else
		{
			System.out.print("before now.");
		}
		System.out.println(" ");
		isFutureTime = false; //just reset it
		
		isFutureTime = SimpleRunner.isTimeFuture(s3);
		System.out.print("Test time is: " + s3 + " and this time is ");
		if (isFutureTime == true)
		{
			System.out.print("ahead of now.");
		}
		else
		{
			System.out.print("before now.");
		}
		System.out.println(" ");
		isFutureTime = false; //just reset it
		
		isFutureTime = SimpleRunner.isTimeFuture(s4);
		System.out.print("Test time is: " + s4 + " and this time is ");
		if (isFutureTime == true)
		{
			System.out.print("ahead of now.");
		}
		else
		{
			System.out.print("before now.");
		}
		System.out.println(" ");
		isFutureTime = false; //just reset it
		
	}
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
}
