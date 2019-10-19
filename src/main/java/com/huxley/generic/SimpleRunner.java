package com.huxley.generic;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.huxley.model.User;
import com.huxley.security.PasswordUtils;

import java.lang.StringBuffer;
public class SimpleRunner {

	public static void main(String[] args) {
			
	User u = new User();
	u.setUserName("Zorias23");
	u.setPassword("Colette23");
	u.setPreferredRadius("8");
	u.setPreferredZipCode("85014");
	u.setAdmin(true);
	u.setFilterChildrenContent(true);
	u.setFilterFuture(false);
	u.setSecurePassword("YWkgXD0VATh8OivhnfdCNkdpSUcDGE5+w8R8eMt4gy8=");
	u.setPasswordSalt("0eKnJE2ejnjQ8M0vFzDU2chnr9QUUo");
	String test1 = "Colette23";
	String test2 = "Butthead7";
	boolean correct = false;
	correct = Utility.isPasswordValid(test1, u);
	System.out.println("Your first password guess is " + test1 + " and that test for validity returns ");
	System.out.println(correct);
	correct = Utility.isPasswordValid(test2, u);
	System.out.println("Your first password guess is " + test2 + " and that test for validity returns ");
	System.out.println(correct);
	String date = "10/17/2019";
	String newDateStr = SimpleRunner.getAPIFormattedDate(date);
	System.out.println("You started with date format: " + date + " and now we have: " + newDateStr);
	Calendar calendar = Calendar.getInstance();
	if (calendar.get(Calendar.AM_PM) == Calendar.PM)
	{
		System.out.println("Time of day is PM.");
	}
	if (calendar.get(Calendar.AM_PM) == Calendar.AM)
	{
		System.out.println("Time of day is AM.");
	}
	int hour = 0;
	int ampm = 0;
	hour = calendar.get(Calendar.HOUR);
	ampm = calendar.get(Calendar.AM_PM);
	int am = calendar.get(Calendar.AM);
	int pm = calendar.get(Calendar.PM);
	System.out.println("Hour of day is " + hour);
	System.out.println("AM/PM value is " + ampm);
	System.out.println("AM value is " + am);
	System.out.println("PM value is " + pm);
		
	}
	
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
