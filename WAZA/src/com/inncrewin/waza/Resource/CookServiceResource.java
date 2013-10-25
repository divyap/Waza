package com.inncrewin.waza.Resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.inncrewin.waza.main.CookDAO;

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
		return cookDAO.saveCookedItem(cookXml);
	}
	
	@POST
	@Path("/saveUserFoodSchedule")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String saveUserFoodSchedule(@QueryParam("userFoodScheduleXml") String userFoodScheduleXml) {
		return cookDAO.saveUserFoodSchedule(userFoodScheduleXml);
	}
	
	@POST
	@Path("/saveCookLocation")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String saveCookLocation(@QueryParam("locationXml") String locationXml){
		return cookDAO.saveLocation(locationXml);
	}
	
}
