package com.inncrewin.waza.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;

import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import com.inncrewin.waza.main.ConsumerDAO;
import com.inncrewin.waza.main.CookDAO;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

public class TestPost {
	
	public static void main(String[] args){
		
		try {
			Client client = Client.create();
		    WebResource resource = client
		            .resource("http://localhost:8080/WAZA/rest/user/doRegisterCook");
		    String conString = "This is the content";

		    FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
		    formDataMultiPart.field("cookXml", "<user></user>");

		    FormDataBodyPart bodyPart = new FormDataBodyPart("imageData",
		            new ByteArrayInputStream(conString.getBytes()),
		            MediaType.APPLICATION_OCTET_STREAM_TYPE);
		    formDataMultiPart.bodyPart(bodyPart);

		    String reString = resource.type(MediaType.MULTIPART_FORM_DATA)
		            .accept(MediaType.TEXT_PLAIN)
		            .post(String.class, formDataMultiPart);
		    System.out.println(reString);
		
			/*
			URL url = new URL("http://localhost:8080/WAZA/rest/cook/saveCookedItem");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", MediaType.TEXT_PLAIN);
			
			String input = getCookedItemXml();
			
			//Write input to the server
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}
	 
			//Read output from the server
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
	 
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
	 
			conn.disconnect();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		}catch (Exception e){
			e.printStackTrace();
		}
		
			/*ClientConfig config = new DefaultClientConfig();
	        Client client = Client.create(config);
	        WebResource resource = client.resource("http://localhost:8080/WAZA/rest/cook/saveCookedItem");
	        ClientResponse response = resource.accept(MediaType.TEXT_XML).post(ClientResponse.class);
	        String en = response.getEntity(String.class);
	        System.out.println(en);*/
			
	}
	
	private static String getCookedItemXml(){
		CookDAO cookDAO = new CookDAO();
		
		Element cookedItem = DocumentFactory.getInstance().createElement("CookedItem");
		cookedItem.addElement("userId").setText("2");
		cookedItem.addElement("title").setText("Sweet Corn Veg Soup");
		cookedItem.addElement("description").setText("Vegetable corn soup with a blend of cream");
		cookedItem.addElement("categoryId").setText("Soups");
		cookedItem.addElement("subCategoryId").setText("Vegetrarian Soups");
		cookedItem.addElement("imagePath").setText("");
		cookedItem.addElement("price").setText("75");
		cookedItem.addElement("wazaBucks").setText("75");
		cookedItem.addElement("ingredients").setText("Corn, Vegetables, Cream");
		cookedItem.addElement("quantity").setText("15");
		
		return cookedItem.asXML();
	}
}
