package com.inncrewin.waza.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.inncrewin.waza.attributes.ElementAttributes;
import com.inncrewin.waza.attributes.UserType;
import com.inncrewin.waza.dbconnection.DBConnection;
import com.inncrewin.waza.hibernate.Cook;
import com.inncrewin.waza.hibernate.User;
import com.inncrewin.waza.main.UserDAO;
import com.inncrewin.waza.util.CommonUtil;
import com.inncrewin.waza.util.EmailUtil;
import com.inncrewin.waza.util.ImageUtil;
import com.inncrewin.waza.util.JAXBParser;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.mail.util.BASE64DecoderStream;


@Path("user")
public class UserServiceResource {
	
	UserDAO userDAO = new UserDAO();
	@POST
	@Path("/doLoginUser")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String doLoginUser(@FormParam("loginId") String email,
                               @FormParam("password") String password) {
		System.out.println("User Login " + email);
		
		try{
			User u= new UserDAO().doLoginUser(email, password);
			if(u==null)
				return CommonUtil.getErrorXMl("Invalid User");
			
			String  retxml= new JAXBParser().marshal(User.class, u) ;
			System.out.println("returning userxml as " + retxml);
			return retxml;
			
		}catch (Exception e){
			e.printStackTrace();
			return CommonUtil.getErrorXMl(e.getMessage());
		}	
	}
	
	@GET
	@Path("/doLoginUserTest")
    @Produces(MediaType.TEXT_PLAIN)
	public String doLoginUserTest(@QueryParam("loginId") String email,
			@QueryParam("password") String password) {
		System.out.println("User Login " + email);
		
		try{
			User u= new UserDAO().doLoginUser(email, password);
			if(u==null)
				return CommonUtil.getErrorXMl("Invalid User");
			
			return new JAXBParser().marshal(User.class, u) ;
			
		}catch (Exception e){
			e.printStackTrace();
			return CommonUtil.getErrorXMl(e.getMessage());
		}	
	}
	
	@POST
	@Path("/forgotPassword")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String forgotPassword(@FormParam("loginId") String email) {
		User user = new UserDAO().forgotPassword(email);
		if (user == null)
			return CommonUtil.getErrorXMl("Invalid User");

		return new EmailUtil().doSendEmail(email, user.getPassword());
	}
	
	/*@POST
	@Path("/forgotPassword")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String forgotPassword(@FormParam("loginId") String email) {
		return new UserDAO().forgotPassword(email);
	}*/
	
	/*@POST
	@Path("/doRegisterCook")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public String doRegisterCook(@QueryParam("cookXml") String cookXml,
			@FormDataParam("photo") InputStream photo){
		return new UserDAO().doRegisterCook(cookXml, photo);
	}*/
	
	@POST
	@Path("/doRegisterCook")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public String doRegisterCook(@FormDataParam("cookXml") String cookXml, @FormDataParam("imageStream") InputStream imageStream){
		
		System.out.print("received doRegisterCook with " +cookXml);
		String statusMsg = "";
		String status = "";
		byte[] imageData= null;
		
		try{
			imageData= IOUtils.toByteArray(imageStream);
		}catch(Exception e){
			e.printStackTrace();
		}

		Cook cookInput = (Cook)new JAXBParser().unmarshalXmlString(cookXml, Cook.class); 
		if(cookInput==null || cookInput.getLoginId()==null) {
			return CommonUtil.getErrorXMl("Please enter a Login Id");
		}
		DBConnection dbConn = new DBConnection();
		Connection conn = dbConn.getDBConnection();

		String statement = "SELECT USER_ID FROM USER WHERE LOGIN_ID=?";
		try {
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, cookInput.getLoginId());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				status = ElementAttributes.FAILURE;
				statusMsg = "Cook already exists!! Cook is not registered";
			} else {
				
				cookInput.setType(UserType.COOK);
				cookInput.setUserType(UserType.COOK);
				cookInput.setWazaScore(new Long(0));
								
				try{
					if(imageStream!=null){
						String filePath= new ImageUtil().doSaveImage(imageData, cookInput.getLoginId()+".jpg");
						cookInput.setPhotoPath(filePath);
		   			}
				}catch (Exception e){
					e.printStackTrace();
					statusMsg = "Could not save image";
				}
				try {
					userDAO.saveCook(cookInput);
					status = ElementAttributes.SUCCESS;
					statusMsg = "Cook is registered successfully"; 
				} catch (Exception e) {
					e.printStackTrace();
					status = ElementAttributes.FAILURE;
					statusMsg = "Cook registration failed";
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
			status = ElementAttributes.FAILURE;
			statusMsg = "Cook registration failed";
		}
		
		return CommonUtil.getStatusXMl(statusMsg, status);
	}
	
	@POST
	@Path("/doRegisterConsumer")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String doRegisterConsumer(@FormParam("consumerXml") String consumerXml){
		System.out.print("received doRegisterCosumer with " +consumerXml);
		String statusMsg = "";
		String status = "";
		
		User consumerInput = (User)new JAXBParser().unmarshalXmlString(consumerXml, User.class); 
		if(consumerInput==null || consumerInput.getLoginId()==null) {
			return CommonUtil.getErrorXMl("Please enter a Login Id");
		}
		DBConnection dbConn = new DBConnection();
		Connection conn = dbConn.getDBConnection();

		PreparedStatement ps=null;
		ResultSet rs=null;
		String statement = "SELECT USER_ID FROM USER WHERE LOGIN_ID=?";
		try {
			ps = conn.prepareStatement(statement);
			ps.setString(1, consumerInput.getLoginId());
			rs = ps.executeQuery();

			if (rs.next()) {
				status = ElementAttributes.FAILURE;
				statusMsg = "This email is already registered!";
			} else {
				consumerInput.setUserType(UserType.CONSUMER);
				consumerInput.setWazaScore(new Long(100));
				
				try {
					userDAO.saveConsumer(consumerInput);
					status = ElementAttributes.SUCCESS;
					statusMsg = "Consumer is registered successfully";
				} catch (Exception e) {
					e.printStackTrace();
					status = ElementAttributes.FAILURE;
					statusMsg = "Consumer registration failed! " + e.getMessage();
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			status = ElementAttributes.FAILURE;
			statusMsg = "Consumer registration failed! " + e.getMessage();
		}finally{
			try{
				rs.close();
				ps.close();
			}catch (Exception e){}
		}
	
		return CommonUtil.getStatusXMl(statusMsg, status);
	}

	@GET
	@Path("/getProfile")
	@Produces(MediaType.TEXT_PLAIN)
	public String getProfile(@QueryParam("userId") Long userId){
		User user= userDAO.getUser(userId);
		if(user!=null){
			if(user instanceof Cook)
				return new JAXBParser().marshal(User.class, user) ;
			else
				return new JAXBParser().marshal(Cook.class, user) ;
			
		}
		else return CommonUtil.getErrorXMl("Could not Fetch user details");
	}
}
