package com.inncrewin.waza.Resource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.inncrewin.waza.attributes.ElementAttributes;
import com.inncrewin.waza.hibernate.CookedItem;
import com.inncrewin.waza.hibernate.Location;
import com.inncrewin.waza.hibernate.UserFoodSchedule;
import com.inncrewin.waza.main.CookDAO;
import com.inncrewin.waza.util.CommonUtil;
import com.inncrewin.waza.util.JAXBParser;

@Path("cook")
public class CookServiceResource {
	
	CookDAO cookDAO = new CookDAO();
	
	@POST
	@Path("/saveIngredient")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void saveIngredient(
			@FormParam("ingredientName") String ingredientName,
			@FormParam("energy") String energy) {
		cookDAO.saveIngredient(ingredientName, energy);
	}
	
	@POST
	@Path("/saveCookedItem")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String saveCookedItem(@FormParam("cookXml") String cookXml){
		System.out.println("got item to add as " + cookXml);
		String statusMsg = "";
		String status = "";

		CookedItem cookedItemInput = (CookedItem) new JAXBParser()
				.unmarshalXmlString(cookXml, CookedItem.class);
		if (cookedItemInput != null) {
			try {
				cookDAO.saveCookedItem(cookedItemInput);
				status = ElementAttributes.SUCCESS;
				statusMsg = "Cooked Item is saved successfully";
			} catch (Exception e) {
				e.printStackTrace();
				status = ElementAttributes.FAILURE;
				statusMsg = "Cooked Item could not be saved ";
			}
		} else {
			status = ElementAttributes.FAILURE;
			statusMsg = "Cooked Item could not be saved ";
		}
		return CommonUtil.getStatusXMl(statusMsg, status);
	}
	
	@POST
	@Path("/saveUserFoodSchedule")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String saveUserFoodSchedule(@FormParam("userFoodScheduleXml") String userFoodScheduleXml) {
		System.out.println("got schedule to add as " + userFoodScheduleXml);
		
		String statusMsg = "";
		String status = "";

		UserFoodSchedule ufsInput = (UserFoodSchedule) new JAXBParser()
				.unmarshalXmlString(userFoodScheduleXml, UserFoodSchedule.class);

		if (ufsInput != null) {
			try {
				cookDAO.saveUserFoodSchedule(ufsInput);
				status = ElementAttributes.SUCCESS;
				statusMsg = "User Food Schedule is saved successfully";
			} catch (Exception e) {
				e.printStackTrace();
				status = ElementAttributes.FAILURE;
				statusMsg = "User Food Schedule could not be saved ";
			}
		} else {
			status = ElementAttributes.FAILURE;
			statusMsg = "User Food Schedule could not be saved ";
		}
		
		return CommonUtil.getStatusXMl(statusMsg, status);
	}
	
	@POST
	@Path("/saveCookLocation")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String saveCookLocation(@FormParam("locationXml") String locationXml){
		System.out.println("got location to add as " + locationXml);
		
		String statusMsg = "";
		String status = "";

		Location locationInput = (Location) new JAXBParser()
				.unmarshalXmlString(locationXml, Location.class);

		if (locationInput != null) {
			try {
				cookDAO.saveLocation(locationInput);
				status = ElementAttributes.SUCCESS;
				statusMsg = "Location is saved successfully";
			} catch (Exception e) {
				e.printStackTrace();
				status = ElementAttributes.FAILURE;
				statusMsg = "Location could not be saved ";
			}
			
		} else {
			status = ElementAttributes.FAILURE;
			statusMsg = "Location could not be saved ";
		}

		return  CommonUtil.getStatusXMl(statusMsg, status);
	}
	
	
	@GET
	@Path("/getDayScheduleForCook")
	@Produces(MediaType.TEXT_PLAIN)
	public String getDayScheduleForCook(@QueryParam("cookUserId") long cookUserId,
							@QueryParam("DayDate") String dayDate){
		System.out.println("got getDayScheduleForCook query for cook=" + cookUserId + "for day=" +dayDate);
		
		Element rootEle= DocumentFactory.getInstance().createElement("Schedules");
		SimpleDateFormat fmt= new SimpleDateFormat("yyyy-MM-dd");
		Date dayDateObj= new Date();
		try{
			dayDateObj=fmt.parse(dayDate);
		}catch (Exception e){
			e.printStackTrace();
		}
		List<UserFoodSchedule> list= cookDAO.getDayScheduleForCook(cookUserId, dayDateObj);
		Iterator<UserFoodSchedule> iter = list.iterator();
		JAXBParser parser= new JAXBParser();
		while (iter.hasNext()) {
			UserFoodSchedule each = (UserFoodSchedule) iter.next();
			String eachxml= parser.marshal(UserFoodSchedule.class, each);
			Document document = null;
			try{
				document= DocumentHelper.parseText(eachxml);
			}catch (Exception e){
				continue;
			}
			if(document!=null && document.getRootElement()!=null)
				rootEle.add(document.getRootElement());
		}
		return rootEle.asXML();
	}
	
	@GET
	@Path("/getUpcomingScheduleForCook")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUpcomingScheduleForCook(@QueryParam("cookUserId") long cookUserId){
		System.out.println("got getUpcomingScheduleForCook query for cook=" + cookUserId );
		Element rootEle= DocumentFactory.getInstance().createElement("Schedules");
		
		List<UserFoodSchedule> list= cookDAO.getUcomingScheduleForCook(cookUserId);
		Iterator<UserFoodSchedule> iter = list.iterator();
		while (iter.hasNext()) {
			UserFoodSchedule each = (UserFoodSchedule) iter.next();
			Element eachEle= rootEle.addElement("CookCuisineSchedule");
			eachEle.addElement("wazaBucks").addText(each.getWazaBucks());
			eachEle.addElement("price").addText( each.getPrice());
			eachEle.addElement("quantity").addText( each.getQuantity());
			Element foodEle= eachEle.addElement("foodItem");
			foodEle.addElement("itemId").addText( each.getFoodItem().getItemId().toString());
			foodEle.addElement("title").addText( each.getFoodItem().getTitle());
			Element locEle= eachEle.addElement("location");
			locEle.addElement("locationId").addText( each.getLocation().getLocationId().toString());
			locEle.addElement("locName").addText( each.getLocation().getLocName());
		}
		return rootEle.asXML();
	}
	
}
