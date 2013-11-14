package com.inncrewin.waza.hibernate;

import javax.xml.bind.annotation.XmlRootElement;

// Generated Sep 16, 2013 4:18:06 PM by Hibernate Tools 3.4.0.CR1

/**
 * Cook generated by hbm2java
 */
@XmlRootElement
public class Cook extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1969539853732064091L;
	private String type;
	private String cookingLic;
	private String licFilePath;
	private String ssn;

	public Cook() {
	}

	public Cook(String type, String wazaScore, String wazaBucks,
			String cookingLic, String licFilePath, String ssn) {
		this.type = type;
		this.cookingLic = cookingLic;
		this.licFilePath = licFilePath;
		this.ssn = ssn;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getCookingLic() {
		return this.cookingLic;
	}

	public void setCookingLic(String cookingLic) {
		this.cookingLic = cookingLic;
	}

	public String getLicFilePath() {
		return this.licFilePath;
	}

	public void setLicFilePath(String licFilePath) {
		this.licFilePath = licFilePath;
	}
	
	public String getSsn() {
		return this.ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}


}
