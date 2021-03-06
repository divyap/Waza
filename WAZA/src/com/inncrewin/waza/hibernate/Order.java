package com.inncrewin.waza.hibernate;

import java.io.Serializable;


public class Order implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5154914050724830410L;
	private Long id;
	private String date;
	private String status;
	private Long itemId;
	private Long locationId;
	private String price;
	private String quantity;
	private User consumer;
	
	public Order(){
		
	}
	
	public Order(String date, String status, Long itemId, Long locationId, User consumer, String price, String quantity){
		this.date = date;
		this.status = status;
		this.itemId = itemId;
		this.locationId = locationId;
		this.consumer = consumer;
		this.price = price;
		this.quantity = quantity;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public User getConsumer() {
		return consumer;
	}

	public void setConsumer(User consumer) {
		this.consumer = consumer;
	}

}
