package com.inncrewin.waza.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.inncrewin.waza.attributes.ElementAttributes;
import com.inncrewin.waza.hibernate.CreditCardInfo;
import com.inncrewin.waza.hibernate.Order;
import com.inncrewin.waza.main.ConsumerDAO;
import com.inncrewin.waza.main.UserDAO;
import com.inncrewin.waza.util.CommonUtil;
import com.inncrewin.waza.util.JAXBParser;

@Path("consumer")
public class ConsumerServiceResource {

	ConsumerDAO  consumerDAO = new ConsumerDAO();
	@POST
	@Path("/saveCreditCardInfo")
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public String saveCreditCardInfo(@QueryParam("ccXml") String ccXml) {
		String statusMsg = "";
		String status = "";

		CreditCardInfo ccInput = (CreditCardInfo) new JAXBParser()
				.unmarshalXmlString(ccXml, CreditCardInfo.class);

		if (ccInput != null) {
			try {
				consumerDAO.saveCreditCardInfo(ccInput);
				status = ElementAttributes.SUCCESS;
				statusMsg = "Credit Card Info is saved successfully";
			} catch (Exception e) {
				e.printStackTrace();
				status = ElementAttributes.FAILURE;
				statusMsg = "Credit Card Info could not be saved ";
			}
 
		} else {
			status = ElementAttributes.FAILURE;
			statusMsg = "Credit Card Info could not be saved ";
		}
		return CommonUtil.getStatusXMl(statusMsg, status);
	}
	
	@POST
	@Path("/placeOrder")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String placeOrder(@FormParam("orderXML") String orderXML){
		String result= null;
		try{
			UserDAO dao= new UserDAO();
			Order placedOrder= (Order)new JAXBParser().unmarshalXmlString(orderXML, Order.class);
			dao.placeOrder(placedOrder, placedOrder.getConsumer().getUserId());
			result= CommonUtil.getStatusXMl("Placed Order", ElementAttributes.SUCCESS);
		}catch (Exception e){
			e.printStackTrace();
			result= CommonUtil.getErrorXMl(e.getMessage());
		}
		
		return result;
		
	}
	
	
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(final MimeMultipart file) {
 
		String uploadedFileLocation = "jboss.server.data.dir" + "/waza";
 
		// save it
		writeToFile(file, uploadedFileLocation);
 
		String output = "File uploaded to : " + uploadedFileLocation;
 
		return Response.status(200).entity(output).build();
 
	}
 
	// save uploaded file to new location
	private void writeToFile(MimeMultipart file,
		String uploadedFileLocation) {
 
		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
 
			out = new FileOutputStream(new File(uploadedFileLocation));
			
			try {
				for (int i = 0; i < file.getCount(); i++) {
					InputStream is = file.getBodyPart(i).getInputStream();
					while ((read = is.read(bytes)) != -1) {
						out.write(bytes, 0, read);
					}
				 }
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.flush();
			out.close();
		} catch (IOException e) {
 
			e.printStackTrace();
		}
 
	}
}
