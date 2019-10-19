package com.huxley.model;

import java.util.Date;
import java.util.UUID;

public class ConfirmationToken {

	private User user;
	private Date createDate;
	private String confirmationToken;
	
	//when first initializing this object, we're attaching a User object and then creating a random confirmationToken string to be associated
	//with this new user
	public ConfirmationToken(User u)
	{
		this.user = u;
		this.createDate = new Date();
		this.confirmationToken = UUID.randomUUID().toString();
	}
	
	//use this constructor when a user has been created but not validated yet, and we want to attach the confirmationToken
	//that was already created for the user and stored in the DB
	public ConfirmationToken(User u, String confirmationToken, Date createDate)
	{
		this.user = u;
		this.confirmationToken = confirmationToken;
		this.createDate = createDate;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getConfirmationToken() {
		return confirmationToken;
	}
	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

}
