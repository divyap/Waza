package com.inncrewin.waza.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.inncrewin.waza.dbconnection.DBConnection;
import com.inncrewin.waza.hibernate.CookedItem;
import com.inncrewin.waza.session.SessionManager;
import com.inncrewin.waza.util.CommonUtil;

@SuppressWarnings("rawtypes")
public class SearchDAO {

	private SessionManager sm = new SessionManager();

	private Connection conn = new DBConnection().getDBConnection();

	public List getCookSchedule(Long cookUserId, Date startDate,
			Date endDate) {
		
		String statement = "select date from user_food_schedule where user_id=? and "
				+ "date>=? and date<=?";

		PreparedStatement ps=null;
		ResultSet rs =null;
		List list = new ArrayList();
		try {
			ps = conn.prepareStatement(statement.toUpperCase());
			ps.setLong(1, cookUserId);
			ps.setDate(2, new java.sql.Date(startDate.getTime()));
			ps.setDate(3, new java.sql.Date(endDate.getTime()));

			rs = ps.executeQuery();

			Date eachDate;
			while (rs.next()) {
				eachDate= rs.getDate(1);
				list.add(eachDate);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{
				rs.close();
				ps.close();
			}catch (Exception e){
				
			}
		}
		System.out.println("no of days busy = " + list.size());
		return list;
	}
	
	public List findCookLocations(Long cookUserId){
		return sm.find("from Location loc where cookUserId=?", cookUserId);
	}
	
	public List findCuisines(Long cookUserId){
		return sm.find("from CookedItem where userId=?", cookUserId);
	}
	
	public List findFoodItemsByName(String name){
		return sm.find("from CookedItem where title like", name);
	}
	
	public List findMyOrders(Long userId){
		return sm.find("from Order where cookUserId=", userId);
	}
	
	private Long getNextSearchId(Long searchId, String searchKeyWord){
		try {
			String statement = "SELECT ID FROM FOOD_ITEMS_BY_NAME WHERE ID>"+searchId.longValue()+" limit 1";
			
			PreparedStatement ps = conn.prepareStatement(statement);
			
			ResultSet rs = ps.executeQuery();
			
			Long id = null;
			if(rs.next())
				id = new Long(1);
			
			return id;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} 
		
	}
	
	// Creating a VIEW for the search results
	public List getFoodItemsByName(String name) {
		// Replace 1 in the below query with the next generated search id
		
		String statement = "CREATE VIEW FOOD_ITEMS_BY_NAME AS (SELECT 1 AS SEARCH_ID, C.* FROM COOKED_ITEM C WHERE TITLE LIKE '%"
				+ name + "%')";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(statement);
			
			ps.executeUpdate();
			
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		statement = "SELECT search_id, id, title, description, price, quantity FROM FOOD_ITEMS_BY_NAME WHERE TITLE LIKE '%"
				+ name + "%' LIMIT 50";
		
		List list = new ArrayList();
		try {
			ps = conn.prepareStatement(statement);
			
			rs = ps.executeQuery();
			
			while(rs.next()){
				String title = rs.getString("title");
				String desc = rs.getString("description");
				String price = rs.getString("price");
				String quantity = rs.getString("quantity");
				
				CookedItem cookedItem = new CookedItem();
				cookedItem.setTitle(title);
				cookedItem.setItemDesc(desc);
				cookedItem.setPrice(price);
				cookedItem.setQuantity(quantity);
				
				list.add(cookedItem);
			}
			
			
			ps.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}
