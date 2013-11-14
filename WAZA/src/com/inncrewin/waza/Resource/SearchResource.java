package com.inncrewin.waza.Resource;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import com.inncrewin.waza.hibernate.Cook;
import com.inncrewin.waza.hibernate.CookedItem;
import com.inncrewin.waza.hibernate.Location;
import com.inncrewin.waza.hibernate.Order;
import com.inncrewin.waza.main.CookDAO;
import com.inncrewin.waza.main.SearchDAO;

@Path("search")
@SuppressWarnings("rawtypes")
public class SearchResource {
	
	SearchDAO searchDAO = new SearchDAO();
	
	CookDAO cookDAO = new CookDAO();
	
	@GET
	@Path("/getCookSchedule")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCookSchedule(@QueryParam("cookUserId") Long cookUserId,
			@QueryParam("month") int month,
			@QueryParam("year") int year) {
		System.out.println("Received query for CookSchedule Month=" + month + " Year=" +year+ " cookid=" + cookUserId);
		Calendar cal= Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);
		Date startDate= cal.getTime();
		
		cal.set(Calendar.MONTH, month+1);
		cal.add(Calendar.DATE, -1);
		Date endDate= cal.getTime();
		
		Element schedule = DocumentFactory.getInstance().createElement("Schedule");

		System.out.println("Processing getCookSchedule from " + startDate + " to " + endDate + " for cook " + cookUserId);
		List dates = searchDAO.getCookSchedule(cookUserId, startDate, endDate);
		Iterator iter = dates.iterator();

		while(iter.hasNext()){
			Date eachDate = (Date)iter.next();
			if(eachDate!=null ){
				cal.setTime(eachDate);
				Element each= schedule.addElement("BusyDay");
				each.addElement("day").setText(""+cal.get(Calendar.DAY_OF_MONTH));
			}
		}

		return schedule.asXML();
		
	}
	
	@GET
	@Path("/getCookLocations")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCookLocations(@QueryParam("cookUserId") Long cookUserId){
		System.out.print("getCookLocations request");
		
		Element resultXml = DocumentFactory.getInstance().createElement("Locations");
		System.out.println("Processing getCusines cook id= " + cookUserId);
		
		List list = searchDAO.findCookLocations(cookUserId);
		
		System.out.println("Found " + list.size() + " no of items");
		
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Location loc = (Location) iter.next();

			if(loc!=null){
				Element locEle= resultXml.addElement("Location");
				locEle.addElement("locationId").setText(loc.getLocationId().toString());
				locEle.addElement("locName").setText(loc.getLocName());
				locEle.addElement("addressLine1").setText(loc.getAddressLine1());
				locEle.addElement("addressLine2").setText(loc.getAddressLine2());
				locEle.addElement("city").setText(loc.getCity());
				locEle.addElement("state").setText(loc.getState());
				locEle.addElement("zip").setText(loc.getZip());
			}
		}
		System.out.println(resultXml);

		return resultXml.asXML();
	}
	
	@GET
	@Path("/getCuisines")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCusines(@QueryParam("cookUserId") Long cookUserId) {
		System.out.print("getCusines request");
		System.out.println("Processing getCusines cook id= " + cookUserId);

		List list = searchDAO.findCuisines(cookUserId);

		System.out.println("Found " + list.size() + " no of items");
		Iterator iter = list.iterator();
		Element resultXml = DocumentFactory.getInstance().createElement(
				"Cuisines");
		while (iter.hasNext()) {
			CookedItem item = (CookedItem) iter.next();

			Element cuisineEle = resultXml.addElement("FoodItem");
			cuisineEle.addElement("itemId")
					.setText(item.getItemId().toString());
			cuisineEle.addElement("title").setText(item.getTitle());
			cuisineEle.addElement("category").setText(item.getCategoryId());
			cuisineEle.addElement("itemDesc").setText(item.getItemDesc());
			cuisineEle.addElement("ingredients").setText(item.getIngredients());
			cuisineEle.addElement("price").setText(item.getPrice());
			cuisineEle.addElement("wazaBucks").setText(item.getWazaBucks());
			cuisineEle.addElement("quantity").setText(item.getQuantity());
		}
		return resultXml.asXML();
	}

	@GET
	@Path("/getFoodItemsByName")
	@Produces(MediaType.TEXT_PLAIN)
	public String getFoodItemsByName(@QueryParam("name") String name){
		Element resultXml = DocumentFactory.getInstance().createElement("FoodItems");
		
		List list = searchDAO.findFoodItemsByName(name);
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			CookedItem item = (CookedItem) iter.next();

			resultXml.addElement("FoodItem");
			resultXml.addElement("Id").setText(item.getItemId().toString());
			resultXml.addElement("Name").setText(item.getTitle());
			resultXml.addElement("ImageURL").setText(item.getImagePath());
			resultXml.addElement("QtyAvailable").setText(item.getQuantity());
			resultXml.addElement("Distance").setText("");
			
			Cook cook = cookDAO.loadCook(item.getUserId());
			if(cook!=null){
				resultXml.addElement("CookUserId").setText(cook.getUserId().toString());
				resultXml.addElement("CookName").setText(cook.getUserName());
			}else{
				resultXml.addElement("CookUserId").setText("");
				resultXml.addElement("CookName").setText("");
			}
			
			
			List cookLocations = searchDAO.findCookLocations(item.getUserId());
			
			System.out.println("Found " + list.size() + " no of items");
			
			Iterator locIter = cookLocations.iterator();
			while (iter.hasNext()) {
				Location loc = (Location) locIter.next();

				if(loc!=null){
					Element locEle= resultXml.addElement("Location");
					locEle.addElement("locationId").setText(loc.getLocationId().toString());
					locEle.addElement("locName").setText(loc.getLocName());
					locEle.addElement("addressLine1").setText(loc.getAddressLine1());
					locEle.addElement("addressLine2").setText(loc.getAddressLine2());
					locEle.addElement("city").setText(loc.getCity());
					locEle.addElement("state").setText(loc.getState());
					locEle.addElement("zip").setText(loc.getZip());
				}
			}
			//Analytics
			
		}

		return resultXml.toString();
	}
	
	@GET
	@Path("/getMyOrders")
	@Produces(MediaType.TEXT_PLAIN)
	public String getMyOrders(@QueryParam("userId") Long userId){
		Element resultXml = DocumentFactory.getInstance().createElement("Orders");
		
		//This should be consumer orders
		List list = searchDAO.findMyOrders(userId);
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Order order = (Order)iter.next();
			
			if(order!=null){
				resultXml.addElement("Order");
				
				resultXml.addElement("Id").setText(order.getId().toString());
				resultXml.addElement("Date").setText(order.getDate());
				resultXml.addElement("Status").setText(order.getStatus());
				resultXml.addElement("ItemId").setText(order.getItemId().toString());
				
				
				CookedItem item = cookDAO.loadCookedItem(order.getItemId());
				if(item!=null){
					resultXml.addElement("ItemName").setText(item.getTitle());
					resultXml.addElement("LocationId").setText(order.getLocationId().toString());
				}
				
				Location loc = cookDAO.loadLocation(order.getLocationId());
				if(loc!=null){
					resultXml.addElement("LocationName").setText(loc.getLocName());
					resultXml.addElement("CookUserId").setText(item.getUserId().toString());
				}
				
				Cook cook = cookDAO.loadCook(item.getUserId());
				if(cook!=null)
					resultXml.addElement("CookUserName").setText(cook.getUserName());
			}
		}

		return resultXml.toString();
	}
	
	
	@GET
	@Path("/getFoodItemsByNameViewTest")
	@Produces(MediaType.TEXT_PLAIN)
	public String getFoodItemsByNameViewTest(@QueryParam("name") String name){
		Element resultXml = DocumentFactory.getInstance().createElement("FoodItems");
		List list = searchDAO.getFoodItemsByName(name);
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			CookedItem item = (CookedItem) iter.next();

			resultXml.addElement("FoodItem");
			resultXml.addElement("Name").setText(item.getTitle());
			resultXml.addElement("Description").setText(item.getPrice());
			resultXml.addElement("Price").setText(item.getPrice());
			resultXml.addElement("QtyAvailable").setText(item.getQuantity());
		}

		return resultXml.toString();
	}
		
}
