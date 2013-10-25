package com.inncrewin.waza.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inncrewin.waza.dbconnection.DBConnection;
import com.inncrewin.waza.hibernate.Cook;
import com.inncrewin.waza.hibernate.CookedItem;
import com.inncrewin.waza.hibernate.Location;
import com.inncrewin.waza.hibernate.Order;
import com.inncrewin.waza.session.SessionManager;

@SuppressWarnings("rawtypes")
public class SearchDAO {

	private static Log log = LogFactory.getLog(SearchDAO.class);

	SessionManager sm = new SessionManager();

	public String getCookSchedule(Long cookUserId, String startDate,
			String endDate) {
		StringBuffer resultXml = new StringBuffer();
		resultXml.append("<Schedule>");

		DBConnection dbConn = new DBConnection();
		Connection conn = dbConn.getDBConnection();

		String statement = "select date from user_food_schedule where user_id=? and"
				+ "date>=? and date<=?";

		try {
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setLong(1, cookUserId);
			ps.setString(2, startDate);
			ps.setString(3, endDate);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				resultXml.append("<Date>");
				resultXml.append(rs.getString(1));
				resultXml.append("</Date>");
			}

		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		resultXml.append("</Schedule>");
		return resultXml.toString();
	}

	public String getCookLocations(Long cookUserId) {
		StringBuffer resultXml = new StringBuffer();
		resultXml.append("<Locations>");

		List list = sm.find("from Location loc where cookUserId=", cookUserId);
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Location loc = (Location) iter.next();

			resultXml.append("<Location>");
			resultXml.append("<Id>");
			resultXml.append(loc.getId());
			resultXml.append("</Id>");
			resultXml.append("<LocName>");
			resultXml.append(loc.getLocName());
			resultXml.append("</LocName>");
			resultXml.append("<AddrLine1>");
			resultXml.append(loc.getAddressLine1());
			resultXml.append("</AddrLine1>");
			resultXml.append("<AddrLine2>");
			resultXml.append(loc.getAddressLine2());
			resultXml.append("</AddrLine2>");
			resultXml.append("<City>");
			resultXml.append(loc.getCity());
			resultXml.append("</City>");
			resultXml.append("<State>");
			resultXml.append(loc.getState());
			resultXml.append("</State>");
			resultXml.append("<Zip>");
			resultXml.append(loc.getZip());
			resultXml.append("</Zip>");
			resultXml.append("</Location>");
		}

		resultXml.append("</Locations>");
		return resultXml.toString();
	}

	public String getCuisines(Long cookUserId) {
		StringBuffer resultXml = new StringBuffer();
		resultXml.append("<Cuisines>");

		List list = sm.find("from CookedItem where userId=", cookUserId);
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			CookedItem item = (CookedItem) iter.next();

			resultXml.append("<Cuisine>");
			resultXml.append("<Id>");
			resultXml.append(item.getItemId());
			resultXml.append("</Id>");
			resultXml.append("<Name>");
			resultXml.append(item.getTitle());
			resultXml.append("</Name>");
			resultXml.append("<Category>");
			resultXml.append(item.getCategoryId());
			resultXml.append("</Category>");
			resultXml.append("<Description>");
			resultXml.append(item.getDescription());
			resultXml.append("</Description>");
			resultXml.append("<Ingredients>");
			resultXml.append(item.getIngredients());
			resultXml.append("</Ingredients>");
			resultXml.append("<Price>");
			resultXml.append(item.getPrice());
			resultXml.append("</Price>");
			resultXml.append("<WazaBucks>");
			resultXml.append(item.getWazaBucks());
			resultXml.append("</WazaBucks>");
			resultXml.append("<Quantity>");
			resultXml.append(item.getQuantity());
			resultXml.append("</Quantity>");
			resultXml.append("</Cuisine>");
		}

		resultXml.append("</Cuisines>");
		return resultXml.toString();
	}
	
	public String getFoodItemsByName(String name){
		StringBuffer resultXml = new StringBuffer();
		resultXml.append("<FoodItems>");
		
		List list = sm.find("from CookedItem where title like", name);
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			CookedItem item = (CookedItem) iter.next();

			resultXml.append("<FoodItem>");
			resultXml.append("<Id>");
			resultXml.append(item.getItemId());
			resultXml.append("</Id>");
			resultXml.append("<Name>");
			resultXml.append(item.getTitle());
			resultXml.append("</Name>");
			resultXml.append("<ImageURL>");
			resultXml.append(item.getImagePath());
			resultXml.append("</ImageURL>");
			resultXml.append("<QtyAvailable>");
			resultXml.append(item.getQuantity());
			resultXml.append("</QtyAvailable>");
			
			resultXml.append("<Distance>");
			resultXml.append("</Distance>");
			
			Cook cook = (Cook)sm.load(Cook.class, item.getUserId());
			if(cook!=null){
				resultXml.append("<CookUserId>");
				resultXml.append(cook.getUserId());
				resultXml.append("</CookUserId>");
				resultXml.append("<CookName>");
				resultXml.append(cook.getUserId());
				resultXml.append("</CookName>");
			}else{
				resultXml.append("<CookUserId>");
				resultXml.append("</CookUserId>");
				resultXml.append("<CookName>");
				resultXml.append("</CookName>");
			}
			
			String locations = this.getCookLocations(item.getUserId());
			if(locations!=null){
				resultXml.append(locations);
			}
			
			//Analytics
			
			resultXml.append("</FoodItem>");
			
		}

		resultXml.append("</FoodItems>");
		return resultXml.toString();
	}
	
	public String getMyOrders(Long userId){
		StringBuffer resultXml = new StringBuffer();
		resultXml.append("<Orders>");
		
		//This should be a consumer orders
		List list = sm.find("from Order where cookUserId=", userId);
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Order order = (Order)iter.next();
			resultXml.append("<Order>");
			
			resultXml.append("<Id>");
			resultXml.append(order.getId());
			resultXml.append("</Id>");
			resultXml.append("<Date>");
			resultXml.append(order.getDate());
			resultXml.append("</Date>");
			resultXml.append("<Status>");
			resultXml.append(order.getStatus());
			resultXml.append("</Status>");
			
			resultXml.append("<ItemId>");
			resultXml.append(order.getItemId());
			resultXml.append("</ItemId>");
			
			CookedItem item = (CookedItem)sm.load(CookedItem.class, order.getItemId());
			resultXml.append("<ItemName>");
			resultXml.append(item.getTitle());
			resultXml.append("</ItemName>");
			
			resultXml.append("<LocationId>");
			resultXml.append(order.getLocationId());
			resultXml.append("</LocationId>");
			
			Location loc = (Location)sm.load(Location.class, order.getLocationId());
			resultXml.append("<LocationName>");
			resultXml.append(loc.getLocName());
			resultXml.append("</LocationName>");
			
			resultXml.append("<CookUserId>");
			resultXml.append(item.getUserId());
			resultXml.append("</CookUserId>");
			
			Cook cook = (Cook)sm.load(Cook.class, item.getUserId());
			resultXml.append("<CookUserName>");
			resultXml.append(cook.getUserName());
			resultXml.append("</CookUserName>");
			
			resultXml.append("</Order>");
		}

		resultXml.append("</Orders>");
		return resultXml.toString();
	}

}
