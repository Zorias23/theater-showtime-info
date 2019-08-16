package com.huxley.model;

public class User {

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPreferredZipCode() {
		return preferredZipCode;
	}
	public void setPreferredZipCode(String preferredZipCode) {
		this.preferredZipCode = preferredZipCode;
	}
	public String getPreferredRadius() {
		return preferredRadius;
	}
	public void setPreferredRadius(String preferredRadius) {
		this.preferredRadius = preferredRadius;
	}
	public boolean isFilterChildrenContent() {
		return filterChildrenContent;
	}
	public void setFilterChildrenContent(boolean filterChildrenContent) {
		this.filterChildrenContent = filterChildrenContent;
	}
	private String userName;
	private String password;
	private String preferredZipCode;
	private String preferredRadius;
	private boolean filterChildrenContent;
}
