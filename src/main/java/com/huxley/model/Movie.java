package com.huxley.model;


import java.util.HashMap;

public class Movie {
	private HashMap<Integer, Theater> currentTheaters; //holds a running list of the theaters currently associated with the movie, while we are appending/building the list of showtimes

	public HashMap<Integer, Theater> getCurrentTheaters() {
		return currentTheaters;
	}
	public void setCurrentTheaters(HashMap<Integer, Theater> currentTheaters) {
		this.currentTheaters = currentTheaters;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String[] getGenres() {
		return genres;
	}
	public void setGenres(String[] genres) {
		this.genres = genres;
	}
	public String getAudience() {
		return audience;
	}
	public void setAudience(String audience) {
		this.audience = audience;
	}
	public String getLongDescription() {
		return longDescription;
	}
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String[] getTopCast() {
		return topCast;
	}
	public void setTopCast(String[] topCast) {
		this.topCast = topCast;
	}
	public String[] getDirectors() {
		return directors;
	}
	public void setDirectors(String[] directors) {
		this.directors = directors;
	}
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public Theater[] getCinemas() {
		return cinemas;
	}
	public void setCinemas(Theater[] cinemas) {
		this.cinemas = cinemas;
	}
	
	public void initializeTheaterMap()
	{
		currentTheaters = new HashMap<Integer, Theater>();
	}
	private String title;
	private String releaseDate;
	private String[] genres;
	//audience will only be populated sometimes
	private String audience;
	private String longDescription;
	private String shortDescription;
	//top actors
	private String[] topCast;
	private String[] directors;
	//create utility method to convert this runtime string to a more readable runtime string
	private String runtime;
	Theater[] cinemas;
	
}
