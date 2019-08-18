package com.huxley.generic;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
