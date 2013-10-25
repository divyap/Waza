package com.inncrewin.waza.main;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import com.inncrewin.waza.attributes.ElementAttributes;
import com.inncrewin.waza.attributes.UserType;
import com.inncrewin.waza.dbconnection.DBConnection;
import com.inncrewin.waza.hibernate.Consumer;
import com.inncrewin.waza.hibernate.Cook;
import com.inncrewin.waza.hibernate.User;
import com.inncrewin.waza.session.SessionManager;
import com.inncrewin.waza.session.SessionManager;
import com.inncrewin.waza.util.EmailUtil;
import com.inncrewin.waza.util.ImageUtil;
import com.inncrewin.waza.util.JAXBParser;
import com.sun.jersey.multipart.FormDataParam;

public class UserDAO {
	
	private static Log log = LogFactory.getLog(UserDAO.class);
	
	private SessionManager sm = new SessionManager();
	
	@GET
	@Path("/user")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@QueryParam("userid") String userid){
		DBConnection dbConn = new DBConnection();
		Connection conn = dbConn.getDBConnection();
		
		String statement = "select user_id from user where user_id=?";
		try {
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, userid);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return "1";
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return "0";
	}
	
	public String doLoginUser(String email, String password) {

		DBConnection dbConn = new DBConnection();
		Connection conn = dbConn.getDBConnection();
		
		StringBuffer xml = new StringBuffer();
		xml = xml.append("<User>");
		String statusMsg = "";
		String status = "";
		String statement = "select user_id, user_type from user where login_id=? and password=?";
		try {
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, email);
			ps.setString(2,  password);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				status = ElementAttributes.SUCCESS ;
				statusMsg =  "User logged in successfully";
				String userId = rs.getString("user_id");
				String userType = rs.getString("user_type");
				xml = xml.append("<UserId>" + userId + "</UserId>");
				xml = xml.append("<UserType>" + userType + "</UserType>");
			}
		} catch (SQLException e) {
			status = ElementAttributes.FAILURE;
			statusMsg = "User cannot be logged in";
			log.error(e.getMessage());
		}
		xml = xml.append("<Status>" + status + "</Status>");
		xml = xml.append("<StatusMessage>" + statusMsg + "</StatusMessage>");
		xml = xml.append("</User>");
		return xml.toString();

    }
	
	public String forgotPassword(String email) {
		Element ele = DocumentFactory.getInstance().createElement("ForgotPassword");
		int result = 0;
		String statusMsg = "";
		
		// Create a random password using alpha numeric characters
		String randomPassword = RandomStringUtils.randomAlphanumeric(8);

		EmailUtil utils = new EmailUtil();
		Long userId = getUserId(email);
		if(userId.longValue()<=0){
			statusMsg = "User with this loginId does not exists";
		}
		ele.addAttribute(ElementAttributes.STATUS_MESSAGE, statusMsg);
		ele.addAttribute(ElementAttributes.STATUS, String.valueOf(result));
		
		User user = (User)sm.load(User.class, userId);
		if(user!=null){
			user.setPassword(randomPassword);
			sm.saveOrUpdate(user);
			utils.doSendEmail(email, randomPassword, ele);
		}
		return ele.toString();
	}
	
	public String doRegisterCook(String cookXml, InputStream photo){
		Element ele = DocumentFactory.getInstance().createElement("RegisterCook");
		String statusMsg = "";
		String status = "";
		
		Cook cookInput = (Cook)new JAXBParser().unmarshalXmlString(cookXml, Cook.class); 
		if(cookInput==null || cookInput.getLoginId()==null) {
			status = ElementAttributes.FAILURE;
			statusMsg = "No loginId found|| Cook is not registered";
			ele.addAttribute(ElementAttributes.STATUS_MESSAGE, statusMsg);
			ele.addAttribute(ElementAttributes.STATUS, status);
			return ele.asXML();
		}
		DBConnection dbConn = new DBConnection();
		Connection conn = dbConn.getDBConnection();

		String statement = "select user_id from user where login_id=?";
		try {
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, cookInput.getLoginId());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				status = ElementAttributes.FAILURE;
				statusMsg = "Cook already exists!! Cook is not registered";
			} else {
				Cook cook = new Cook();
				cook.setSsn(cookInput.getSsn());
				cook.setAddressLine1(cookInput.getAddressLine1());
				cook.setAddressLine2(cookInput.getAddressLine2());
				cook.setCity(cookInput.getCity());
				cook.setCookingLic(cookInput.getCookingLic());
				cook.setCountry(cookInput.getCountry());
				cook.setLoginId(cookInput.getLoginId());
				cook.setMobile(cookInput.getMobile());
				cook.setPassword(cookInput.getPassword());
				cook.setPhone(cookInput.getPhone());
				cook.setState(cookInput.getState());
				cook.setType(UserType.COOK);
				cook.setUserType(UserType.COOK);
				cook.setZip(cookInput.getZip());
				
				if(photo!=null){
					new ImageUtil().doSaveImage(photo);
				}
				
				sm.save(cook);

				status = ElementAttributes.SUCCESS;
				statusMsg = "Cook is registered successfully";
			}
		} catch (SQLException e) {
			status = ElementAttributes.FAILURE;
			statusMsg = "Cook registration failed";
			log.error(e.getMessage());
		}
		ele.addAttribute(ElementAttributes.STATUS_MESSAGE, statusMsg);
		ele.addAttribute(ElementAttributes.STATUS, status);
	
		return ele.asXML();
	}
	
	public String doRegisterConsumer(String consumerXml){
		Element ele = DocumentFactory.getInstance().createElement("RegisterConsumer");
		String statusMsg = "";
		String status = "";
		
		Consumer consumerInput = (Consumer)new JAXBParser().unmarshalXmlString(consumerXml, Cook.class); 
		if(consumerInput==null || consumerInput.getLoginId()==null) {
			status = ElementAttributes.FAILURE;
			statusMsg = "No loginId found|| Consumer is not registered";
			ele.addAttribute(ElementAttributes.STATUS_MESSAGE, statusMsg);
			ele.addAttribute(ElementAttributes.STATUS, status);
			return ele.asXML();
		}
		DBConnection dbConn = new DBConnection();
		Connection conn = dbConn.getDBConnection();

		String statement = "select user_id from user where login_id=?";
		try {
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, consumerInput.getLoginId());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				status = ElementAttributes.FAILURE;
				statusMsg = "Consumer already exists!! Consumer is not registered";
			} else {
				Consumer consumer = new Consumer();
				consumer.setAddressLine1(consumerInput.getAddressLine1());
				consumer.setAddressLine2(consumerInput.getAddressLine2());
				consumer.setCity(consumerInput.getCity());
				consumer.setCountry(consumerInput.getCountry());
				consumer.setLoginId(consumerInput.getLoginId());
				consumer.setMobile(consumerInput.getMobile());
				consumer.setPassword(consumerInput.getPassword());
				consumer.setPhone(consumerInput.getPhone());
				consumer.setState(consumerInput.getState());
				consumer.setUserType(UserType.COOK);
				consumer.setZip(consumerInput.getZip());
				
				sm.save(consumer);

				status = ElementAttributes.SUCCESS;
				statusMsg = "Consumer is registered successfully";
			}
		} catch (SQLException e) {
			status = ElementAttributes.FAILURE;
			statusMsg = "Consumer registration failed";
			log.error(e.getMessage());
		}
		ele.addAttribute(ElementAttributes.STATUS_MESSAGE, statusMsg);
		ele.addAttribute(ElementAttributes.STATUS, status);
	
		return ele.asXML();
	}
	
	
	@POST
	@Path("/doRegisterCook")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public String doRegisterUser(@FormDataParam("loginId") String email,
			@FormDataParam("password") String password,
			@FormDataParam("name") String name,
			@FormDataParam("addressLine1") String addressLine1,
			@FormDataParam("addressLine1") String addressLine2,
			@FormDataParam("city") String city,
			@FormDataParam("state") String state,
			@FormDataParam("zip") String zip,
			@FormDataParam("country") String country,
			@FormDataParam("phone") String phone,
			@FormDataParam("mobile") String mobile,
			@FormDataParam("ssn") String ssn,
			@FormDataParam("photo") InputStream photo) {

		Element ele = DocumentFactory.getInstance().createElement("RegisterCook");
		
		String statusMsg = "";
		String status = "";
		DBConnection dbConn = new DBConnection();
		Connection conn = dbConn.getDBConnection();

		String statement = "select user_id from user where login_id=?";
		try {
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				status = ElementAttributes.FAILURE;
				statusMsg = "Cook already exists!! Cook is not registered";
			} else {
				Cook cook = new Cook();
				cook.setSsn(ssn);
				cook.setAddressLine1(addressLine1);
				cook.setAddressLine2(addressLine2);
				cook.setCity(city);
				cook.setCountry(country);
				cook.setLoginId(email);
				cook.setMobile(mobile);
				cook.setPassword(password);
				cook.setPhone(phone);
				cook.setState(state);
				cook.setType(UserType.COOK);
				cook.setUserType(UserType.COOK);
				cook.setUserName(name);
				cook.setZip(zip);
				
				if(photo!=null){
					new ImageUtil().doSaveImage(photo);
				}
				
				sm.save(cook);

				status = ElementAttributes.SUCCESS;
				statusMsg = "Cook is registered successfully";
			}
		} catch (SQLException e) {
			status = ElementAttributes.FAILURE;
			statusMsg = "Cook registration failed";
			log.error(e.getMessage());
		}
		ele.addAttribute(ElementAttributes.STATUS_MESSAGE, statusMsg);
		ele.addAttribute(ElementAttributes.STATUS, status);
	
		return ele.asXML();
	}
	
	@POST
	@Path("/doRegisterConsumer")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String doRegisterConsumer(@FormParam("loginId") String email,
			@FormParam("password") String password,
			@FormParam("name") String name,
			@FormParam("addressLine1") String addressLine1,
			@FormParam("addressLine1") String addressLine2,
			@FormParam("city") String city, @FormParam("state") String state,
			@FormParam("zip") String zip, @FormParam("country") String country,
			@FormParam("phone") String phone,
			@FormParam("mobile") String mobile) {

		Element ele = DocumentFactory.getInstance().createElement("RegisterCook");
		String statusMsg = "";
		String status = "";
		
		DBConnection dbConn = new DBConnection();
		Connection conn = dbConn.getDBConnection();

		String statement = "select user_id from user where login_id=?";
		try {
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				status = ElementAttributes.FAILURE;
				statusMsg = "Consumer already exists!! Consumer is not registered";
			} else {
				Consumer consumer = new Consumer();
				consumer.setAddressLine1(addressLine1);
				consumer.setAddressLine2(addressLine2);
				consumer.setCity(city);
				consumer.setCountry(country);
				consumer.setLoginId(email);
				consumer.setMobile(mobile);
				consumer.setPassword(password);
				consumer.setPhone(phone);
				consumer.setState(state);
				consumer.setUserType(UserType.CONSUMER);
				consumer.setUserName(name);
				consumer.setZip(zip);
				consumer.setWazaBucks("0");
				sm.save(consumer);
				
				status = ElementAttributes.SUCCESS;
				statusMsg = "Consumer is registered successfully";
			}
		} catch (SQLException e) {
			status = ElementAttributes.FAILURE;
			statusMsg = "Consumer registration failed";
			log.error(e.getMessage());
		}
		ele.addAttribute(ElementAttributes.STATUS_MESSAGE, statusMsg);
		ele.addAttribute(ElementAttributes.STATUS, status);
	
		return ele.asXML();
	}
	
	private Long getUserId(String loginId){
		DBConnection dbConn = new DBConnection();
		Connection conn = dbConn.getDBConnection();
		
		String statement = "select user_id from user where login_id=?";
		Long userId = new Long(-1);
		try {
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, loginId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				userId = rs.getLong(1);
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return userId;
	}
}
