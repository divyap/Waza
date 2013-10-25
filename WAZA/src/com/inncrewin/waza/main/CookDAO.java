package com.inncrewin.waza.main;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import com.inncrewin.waza.attributes.ElementAttributes;
import com.inncrewin.waza.hibernate.CookedItem;
import com.inncrewin.waza.hibernate.Ingredient;
import com.inncrewin.waza.hibernate.Location;
import com.inncrewin.waza.hibernate.UserFoodSchedule;
import com.inncrewin.waza.session.SessionManager;
import com.inncrewin.waza.util.JAXBParser;

public class CookDAO {
	
	private static Log log = LogFactory.getLog(CookedItem.class);
	
	private SessionManager sm = new SessionManager();
	
	public void saveIngredient(String ingredientName,
			String energy) {
		Ingredient ingredient = new Ingredient(ingredientName, energy);
		sm.save(ingredient);
	}
	
	
	public String saveCookedItem(String cookedItemXml) {
		Element ele = DocumentFactory.getInstance().createElement("CookedItem");
		String statusMsg = "";
		String status = "";

		System.out.println("add cooked item xml received as " + cookedItemXml);
		CookedItem cookedItemInput = (CookedItem) new JAXBParser()
				.unmarshalXmlString(cookedItemXml, CookedItem.class);
		if (cookedItemInput != null) {
			CookedItem cookedItem = new CookedItem(cookedItemInput.getUserId(),
					cookedItemInput.getTitle(),
					cookedItemInput.getDescription(),
					cookedItemInput.getCategoryId(),
					cookedItemInput.getSubCategoryId(),
					cookedItemInput.getImagePath(), cookedItemInput.getPrice(),
					cookedItemInput.getUpvote(), cookedItemInput.getDownvote(),
					cookedItemInput.getWazaBucks(),
					cookedItemInput.getIngredients(),
					cookedItemInput.getQuantity());
			sm.save(cookedItem);

			status = ElementAttributes.SUCCESS;
			statusMsg = "Cooked Item is saved successfully";
		} else {
	status = ElementAttributes.FAILURE;
			statusMsg = "Cooked Item could not be saved ";
		}
		ele.addAttribute(ElementAttributes.STATUS_MESSAGE, statusMsg);
		ele.addAttribute(ElementAttributes.STATUS, status);

		return ele.asXML();
	}
	
	public String saveUserFoodSchedule(String userFoodScheduleXml) {
		Element ele = DocumentFactory.getInstance().createElement("CookedItem");
		String statusMsg = "";
		String status = "";

		UserFoodSchedule ufsInput = (UserFoodSchedule) new JAXBParser()
				.unmarshalXmlString(userFoodScheduleXml, UserFoodSchedule.class);

		if (ufsInput != null) {
			UserFoodSchedule ufs = new UserFoodSchedule(
					ufsInput.getCookedItemId(), ufsInput.getUserId(),
					ufsInput.getDate(), ufsInput.getQuantity(),
					ufsInput.getLocationId(), ufsInput.getPrice(),
					ufsInput.getWazaBucks());
			sm.save(ufs);

			status = ElementAttributes.SUCCESS;
			statusMsg = "User Food Schedule is saved successfully";
		} else {
			status = ElementAttributes.FAILURE;
			statusMsg = "User Food Schedule could not be saved ";
		}
		ele.addAttribute(ElementAttributes.STATUS_MESSAGE, statusMsg);
		ele.addAttribute(ElementAttributes.STATUS, status);

		return ele.asXML();
	}
	
	
	public String saveLocation(String newLocationXml) {
		Element ele = DocumentFactory.getInstance().createElement("Location");
		String statusMsg = "";
		String status = "";

		Location locationInput = (Location) new JAXBParser()
				.unmarshalXmlString(newLocationXml, Location.class);

		if (locationInput != null) {
			Location location = new Location(locationInput.getCookUserId(),
					locationInput.getLocName(),
					locationInput.getAddressLine1(),
					locationInput.getAddressLine2(), locationInput.getCity(),
					locationInput.getState(), locationInput.getZip());
			sm.save(location);

			status = ElementAttributes.SUCCESS;
			statusMsg = "Location is saved successfully";
		} else {
			status = ElementAttributes.FAILURE;
			statusMsg = "Location could not be saved ";
		}

		ele.addAttribute(ElementAttributes.STATUS_MESSAGE, statusMsg);
		ele.addAttribute(ElementAttributes.STATUS, status);

		return ele.asXML();
	}
	
	
	// Remove all the methods below this line later once above methods are tested
	@POST
	@Path("/saveCookedItem")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void saveCookedItem(@FormParam("userId") Long userId,
			@FormParam("title") String title,
			@FormParam("description") String description,
			@FormParam("categoryId") String categoryId,
			@FormParam("subCategoryId") String subCategoryId,
			@FormParam("imagePath") String imagePath,
			@FormParam("price") String price,
			@FormParam("upVote") String upVote,
			@FormParam("downVote") String downVote) {
		/*CookedItem cookedItem = new CookedItem(userId, title, description,
				categoryId, subCategoryId, imagePath, price, upVote, downVote);
		sm.save(cookedItem);*/
	}
	
	@POST
	@Path("/saveUserFoodSchedule")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void saveUserFoodSchedule(
			@FormParam("cookedItemId") Long cookedItemId,
			@FormParam("userId") Long userId, @FormParam("date") Date date,
			@FormParam("quantity") String quantity) {
		/*UserFoodSchedule ufs = new UserFoodSchedule(cookedItemId, userId, date,
				quantity);
		sm.save(ufs);*/
	}

}
