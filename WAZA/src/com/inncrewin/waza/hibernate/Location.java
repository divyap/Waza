package com.inncrewin.waza.hibernate;

public class Location implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 726109554435210339L;
	private Long locationId;
	private Long cookUserId;
	private String locName;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String zip;
	
	public Location(){
		
	}
	
	public Location(Long cookUserId, String locName, String addressLine1,
			String addressLine2, String city, String state, String zip) {
		this.cookUserId = cookUserId;
		this.locName = locName;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Long getCookUserId() {
		return cookUserId;
	}

	public void setCookUserId(Long cookUserId) {
		this.cookUserId = cookUserId;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getLocName() {
		return locName;
	}

	public void setLocName(String locName) {
		this.locName = locName;
	}
}
