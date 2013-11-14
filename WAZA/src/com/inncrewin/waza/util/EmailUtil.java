package com.inncrewin.waza.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inncrewin.waza.attributes.ElementAttributes;

public class EmailUtil {
	
	private static Log log = LogFactory.getLog(EmailUtil.class);

	private static String host = "pod51009.outlook.com";
	
	private static String port = "587"; 

	private static String from = "divya@inncrewin.com";
	
	public Session getSession(){
		Properties properties = System.getProperties();

		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.port", port);
		properties.setProperty("mail.smtp.from", from);
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.auth", "true");
		
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties, new Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, "Sanjay_19");
			}
		});

		return session;
	}

	public String doSendEmail(String toRecipient, String newPassword) {
		String status = ElementAttributes.FAILURE;
		String statusMsg = "";
		Session session = getSession();
		
		try {
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					toRecipient));

			// Set Subject: header field
			message.setSubject("Password Reset");

			// Now set the actual message
			message.setText("Your password has been reset to " + newPassword);
			
			// Send message
			Transport.send(message);

			status = ElementAttributes.SUCCESS;
			statusMsg = "Email sent successfully....";
		} catch (MessagingException mex) {
			status = ElementAttributes.FAILURE;
			statusMsg = "Email could not be sent";
			log.error(mex.getMessage());
		}
		return CommonUtil.getStatusXMl(statusMsg, status);
	}
	
	public static void main(String[] args){
		String randomPassword = RandomStringUtils.randomAlphanumeric(8);

		new EmailUtil().doSendEmail("divya3007@gmail.com", randomPassword);
	}
}
