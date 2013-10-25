package com.inncrewin.waza.Resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.inncrewin.waza.main.SearchDAO;

@Path("search")
public class SearchResource {
	
	SearchDAO searchDAO = new SearchDAO();
	
	@GET
	@Path("/getCookSchedule")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCookSchedule(@QueryParam("cookUserId") Long cookUserId,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) {
		return searchDAO.getCookSchedule(cookUserId, startDate, endDate);
	}
	
	@GET
	@Path("/getCookLocations")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCookLocations(@QueryParam("cookUserId") Long cookUserId){
		return searchDAO.getCookLocations(cookUserId);
	}
	
	@GET
	@Path("/getCusines")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCusines(@QueryParam("cookUserId") Long cookUserId){
		return searchDAO.getCuisines(cookUserId);
	}
	
	@GET
	@Path("/getFoodItemsByName")
	@Produces(MediaType.TEXT_PLAIN)
	public String getFoodItemsByName(@QueryParam("name") String name){
		return searchDAO.getFoodItemsByName(name);
	}
	
	@GET
	@Path("/getMyOrders")
	@Produces(MediaType.TEXT_PLAIN)
	public String getMyOrders(@QueryParam("userId") Long userId){
		return searchDAO.getMyOrders(userId);
	}
}
