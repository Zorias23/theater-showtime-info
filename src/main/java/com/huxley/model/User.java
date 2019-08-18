package com.huxley.model;
import  org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/* Users:
 * SN:Zorias23
 * PW:***** 
 * 
 *  SN:Sabrina26
 *  PW:Damned93*/

public class User {
	
	public User() {
		//default no-arg contructor
	}
	public User(String userName, String passWord, String zipCode, String radius, boolean filterChildContent, boolean filterFuture, boolean isAdmin)
	{
		this.userName = userName;
		this.password = passWord;
		this.preferredZipCode = zipCode;
		this.preferredRadius = radius;
		this.filterChildrenContent = filterChildContent;
		this.filterFuture = filterFuture;
		this.isAdmin = isAdmin;
	}

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
	public boolean isFilterFuture() {
		return filterFuture;
	}
	public void setFilterFuture(boolean filterFuture) {
		this.filterFuture = filterFuture;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	private boolean filterFuture;
	private boolean isAdmin;
	private String securePassword;
	private String passwordSalt;
	
	public String getPasswordSalt() {
		return passwordSalt;
	}
	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}
	public String getSecurePassword() {
		return securePassword;
	}
	public void setSecurePassword(String securePassword) {
		this.securePassword = securePassword;
	}
	@Override
	public String toString()
	{
		this.userName = (String) ObjectUtils.defaultIfNull(this.userName, "NOT_SET");
		this.password = (String) ObjectUtils.defaultIfNull(this.password, "NOT_SET");
		this.securePassword = (String) ObjectUtils.defaultIfNull(this.securePassword, "NOT_SET");
		this.preferredZipCode = (String) ObjectUtils.defaultIfNull(this.preferredZipCode, "NOT_SET");
		this.preferredRadius = (String) ObjectUtils.defaultIfNull(this.preferredRadius, "NOT_SET");
		this.filterChildrenContent = (boolean) ObjectUtils.defaultIfNull(this.filterChildrenContent, false);
		this.filterFuture = (boolean) ObjectUtils.defaultIfNull(this.filterFuture, false);
		this.isAdmin = (boolean) ObjectUtils.defaultIfNull(this.isAdmin, false);
		  return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
			       append("UserName", this.userName).
			       append("PassWord", "************"). //for security reasons, let's not output the password
			       append("Secure_Password", this.securePassword).
			       append("ZipCode", this.preferredZipCode).
			       append("Radius", this.preferredRadius).
			       append("Filter_Children_content", this.filterChildrenContent).
			       append("Filter_future_showtimes", this.filterFuture).
			       append("Is_Admin", this.isAdmin).
			       toString();
	}
}
