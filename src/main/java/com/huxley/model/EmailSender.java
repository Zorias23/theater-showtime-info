package com.huxley.model;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class EmailSender {
	
	public static final String HOST_LABEL = "mail.smtp.host";
	public static final String HOST = "smtp.gmail.com";
	public static final String AUTH_LABEL = "mail.smtp.auth";
	public static final String START_TLS_LABEL = "mail.smtp.starttls.enable";
	public static final String PORT_LABEL = "mail.smtp.port";
	public static final String PORT = "587";
	public static final String FROM_FIELD = "MovineMailInfo@gmail.com";
	public static final String ACCOUNT_USERNAME = "MovineMailInfo";
	public static final String ACCOUNT_PASSWORD = "Colette23";
	Properties props;
	
	
	public EmailSender()
	{
	      props = new Properties();
	      props.put(EmailSender.AUTH_LABEL, "true");
	      props.put(EmailSender.START_TLS_LABEL, "true");
	      props.put(EmailSender.HOST_LABEL, EmailSender.HOST);
	      props.put(EmailSender.PORT_LABEL, EmailSender.PORT);
	}
	
	public boolean sendEmail(String toAddress, String message)
	{
		boolean sent = false;
		
	      Session session = Session.getInstance(this.props,
	      new javax.mail.Authenticator() {
	         protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(ACCOUNT_USERNAME, ACCOUNT_PASSWORD);
	         }
	      });
	      
	      try {
	          // Create a default MimeMessage object.
	          Message msg = new MimeMessage(session);

	          // Set From: header field of the header.
	          msg.setFrom(new InternetAddress(FROM_FIELD));

	          // Set To: header field of the header.
	          msg.setRecipients(Message.RecipientType.TO,
	          InternetAddress.parse(toAddress));

	          // Set Subject: header field
	          msg.setSubject("Notice from Movine App");

	          // Now set the actual message
	          msg.setText(message);

	          // Send message
	          Transport.send(msg);

	          System.out.println("Email notification message sent successfully to " + toAddress + "...");
	          sent = true;

	       } catch (MessagingException e) {
	    	   System.out.println("We've encountered an exception while trying to send an email notification to " +toAddress);
	    	   e.printStackTrace();
	       }
		
		return sent;
	}
}
