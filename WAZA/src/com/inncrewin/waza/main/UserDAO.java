package com.inncrewin.waza.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

import com.inncrewin.waza.attributes.UserType;
import com.inncrewin.waza.dbconnection.DBConnection;
import com.inncrewin.waza.hibernate.Cook;
import com.inncrewin.waza.hibernate.Order;
import com.inncrewin.waza.hibernate.User;
import com.inncrewin.waza.session.SessionManager;

public class UserDAO {
	
	private static Log log = LogFactory.getLog(UserDAO.class);
	
	private SessionManager sm = new SessionManager();
	
	public User doLoginUser(String email, String password) throws SQLException , HibernateException{

		DBConnection dbConn = new DBConnection();
		Connection conn = dbConn.getDBConnection();
		
		String statement = "SELECT USER_ID, USER_TYPE FROM USER WHERE LOGIN_ID=? AND PASSWORD=?";
		long userId=0;
		PreparedStatement ps= null;
		ResultSet rs= null;
		try {
			ps = conn.prepareStatement(statement);
			ps.setString(1, email);
			ps.setString(2,  password);
			rs= ps.executeQuery();

			if (rs.next()) {
				userId = rs.getLong("user_id");
			}
		}finally{
			try{
				rs.close();
				ps.close();
			}catch(Exception e){
				
			}
		}
		System.out.println("User id found as " +userId);
		if(userId>0){
			try{
				SessionManager sm= new SessionManager();
				User u= (User)sm.load(User.class, new Long(userId));
				System.out.println("login id found as " +u.getLoginId());
				if(u.getUserType()==UserType.CONSUMER)
					Hibernate.initialize(u.getOrders());
				return u;
			}catch(Exception e){
				System.out.println("e is" +e);
				e.printStackTrace();
				return null;
			}
		} else return null; 

    }
	
	public User forgotPassword(String email) {
		
		Long userId = getUserId(email);
		if(userId.longValue()<=0){
			log.info("User with this loginId does not exists");
			return null;
		}
		User user = (User)sm.load(User.class, userId);
		if(user!=null){
			// Create a random password using alpha numeric characters
			String randomPassword = RandomStringUtils.randomAlphanumeric(8);

			user.setPassword(randomPassword);
			sm.saveOrUpdate(user);
			return user;
			
		}else
			return null;
	}
	
	public void saveCook(Cook cook){
		sm.save(cook);
	}
	
	public void saveConsumer(User consumer){
		sm.save(consumer);
	}
	
	private Long getUserId(String loginId){
		DBConnection dbConn = new DBConnection();
		Connection conn = dbConn.getDBConnection();
		
		String statement = "select user_id from user where login_id=?";
		Long userId = new Long(-1);
		try {
			PreparedStatement ps = conn.prepareStatement(statement);
			ps.setString(1, loginId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				userId = rs.getLong(1);
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return userId;
	}
	
	public User getUser(Long userId){
		User u= null;
		try{
			u= (User)sm.load(User.class,  userId);
		}catch (Exception e){
			log.error(e.getMessage());
		}
		return u;
	}
	
	
	public boolean placeOrder(Order placedOrder, long userId){
		
		try{
			User u= (User)sm.load(User.class, userId);
			u.getOrders().add(placedOrder);
			placedOrder.setConsumer(u);
			sm.save(placedOrder);
			return true;
		} catch (Exception e){
			log.error(e.getMessage());
			return false;
		}
	}
}
