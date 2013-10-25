package com.inncrewin.waza.Resource;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import com.inncrewin.waza.main.UserDAO;
import com.sun.jersey.multipart.FormDataParam;

@Path("user")
public class UserServiceResource {

	@POST
	@Path("/doLoginUser")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String doLoginUser(@FormParam("loginId") String email,
                               @FormParam("password") String password) {
		return new UserDAO().doLoginUser(email, password);
	}
	
	@POST
	@Path("/forgotPassword")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String forgotPassword(@FormParam("loginId") String email) {
		return new UserDAO().forgotPassword(email);
	}
	
	/*@POST
	@Path("/doRegisterCook")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public String doRegisterCook(@QueryParam("cookXml") String cookXml,
			@FormDataParam("photo") InputStream photo){
		return new UserDAO().doRegisterCook(cookXml, photo);
	}*/
	
	@GET
	@Path("/doRegisterCook")
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public String doRegisterCook(@QueryParam("cookXml") String cookXml){
		return new UserDAO().doRegisterCook(cookXml, null);
	}
	
	@POST
	@Path("/doRegisterConsumer")
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public String doRegisterConsumer(@QueryParam("consumerXml") String consumerXml){
		return new UserDAO().doRegisterConsumer(consumerXml);
	}
	
	
}
