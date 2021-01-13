package com.huxley.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
public class Theater {
	public Theater()
	{
		
	}
	public Theater(int id)
	{
		this.api_ID = id;
	}
	public int getApi_ID() {
		return api_ID;
	}
	private boolean sortedShowtimes = false;
	public boolean isSortedShowtimes() {
		return sortedShowtimes;
	}
	public void setSortedShowtimesFlag(boolean sortedShowtimes) {
		this.sortedShowtimes = sortedShowtimes;
	}
	public void setApi_ID(int api_ID) {
		this.api_ID = api_ID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCrossStreets() {
		return crossStreets;
	}
	public void setCrossStreets(String crossStreets) {
		this.crossStreets = crossStreets;
	}
	public double getDistanceHome() {
		return distanceHome;
	}
	public void setDistanceHome(double distanceHome) {
		this.distanceHome = distanceHome;
	}
	public List<String> getShowtimes() {
		return showtimes;
	}
	public void setShowtimes(List<String> showtimes) {
		this.showtimes = showtimes;
	}
	
	@Override
	public String toString()
	{
		String number_of_showtimes;
		if (showtimes == null)
		{
			number_of_showtimes = "N/A";
		}
		else
		{
			number_of_showtimes = String.valueOf(showtimes.size());
		}
		  return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
			       append("api_ID", api_ID).
			       append("name", name).
			       append("address", address).
			       append("phone", phone).
			       append("crossStreets", crossStreets).
			       append("distanceHome", distanceHome).
			       append("number_of_showtimes", number_of_showtimes).
			       append("LatLong", latLong).
			       toString();
	}
	public void addShowing(String showtime)
	{
		if (showtimes == null)
		{
			showtimes = new ArrayList<String>();
		}
		showtimes.add(showtime);
	}
	/**
	 * Copies all data from another Theater object into this object, but intentionally leaves out the showtime list. 
	 * we want to just grab the basic data we got from the database concerning this theater object, not the showtime information from the JSON call
	 * @param copy
	 */
	public void copyData(Theater copy)
	{
		if (copy == null)
		{
			return;
		}
		this.setApi_ID(copy.getApi_ID());
		this.setName(copy.getName());
		this.setAddress(copy.getAddress());
		this.setPhone(copy.getPhone());
		this.setCrossStreets(copy.getCrossStreets());
		this.setDistanceHome(copy.getDistanceHome());
		
	}
	private int api_ID;
	private String name;
	private String address;
	private String phone;
	private String crossStreets;
	private double distanceHome;
	private List<String> showtimes;
	private String latLong; //lattitude and longitutde cooridnates for display on a GUI map, the location of the theater. will be read from the DB as 'lat#,long#'
	public String getLattitude() {
		return lattitude;
	}
	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	private String lattitude;
	private String longitude;
	public String getLatLong() {
		return latLong;
	}
	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}
}
