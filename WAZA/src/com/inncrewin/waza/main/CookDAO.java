package com.inncrewin.waza.main;

import java.util.Date;
import java.util.List;

import com.inncrewin.waza.hibernate.Cook;
import com.inncrewin.waza.hibernate.CookedItem;
import com.inncrewin.waza.hibernate.Ingredient;
import com.inncrewin.waza.hibernate.Location;
import com.inncrewin.waza.hibernate.UserFoodSchedule;
import com.inncrewin.waza.session.SessionManager;

public class CookDAO {
	
	private SessionManager sm = new SessionManager();
	
	public void saveIngredient(String ingredientName,
			String energy) {
		Ingredient ingredient = new Ingredient(ingredientName, energy);
		sm.save(ingredient);
	}
	
	
	public void saveCookedItem(CookedItem cookedItemInput) {
		sm.saveOrUpdate(cookedItemInput);
	}
	
	public void saveUserFoodSchedule(UserFoodSchedule ufsInput) {
		sm.saveOrUpdate(ufsInput);
	}
	
	public void saveLocation(Location location) {
		sm.save(location);
	}
	
	public Cook loadCook(Long cookUserId){
		return (Cook)sm.load(Cook.class, cookUserId);
	}
	
	public CookedItem loadCookedItem(Long cookUserId){
		return (CookedItem)sm.load(CookedItem.class, cookUserId);
	}
	
	public Location loadLocation(Long locId){
		return (Location)sm.load(Location.class, locId);
	}
	
	public List<UserFoodSchedule> getDayScheduleForCook(long cookUserId, Date dayDate){
		System.out.println("Processing getDayScheduleForCook cook id= " + cookUserId + "& daydate=" + dayDate );
		List<UserFoodSchedule> resultList =(List<UserFoodSchedule>) sm.find("from UserFoodSchedule sched WHERE userId=" + cookUserId + " and date=?", dayDate);
		//for (UserFoodSchedule sched : resultList) {
		  //  Hibernate.initialize(sched.getCookedItem());
		   // Hibernate.initialize(sched.getLocation());
		//}
		System.out.println(" " + resultList.size() + " no of items");
		return resultList;
		
	}
	
	public List<UserFoodSchedule> getUcomingScheduleForCook(long cookUserId){
		System.out.println("Processing getUcomingScheduleForCook cook id= " + cookUserId );
		Date today= new Date();
		List<UserFoodSchedule> resultList =(List<UserFoodSchedule>) sm.find("from UserFoodSchedule sched WHERE userId=" + cookUserId + " and date>=", today);
		//for (UserFoodSchedule sched : resultList) {
		  //  Hibernate.initialize(sched.getCookedItem());
		   // Hibernate.initialize(sched.getLocation());
		//}
		System.out.println(" " + resultList.size() + " no of items");
		return resultList;
		
	}
	
}
