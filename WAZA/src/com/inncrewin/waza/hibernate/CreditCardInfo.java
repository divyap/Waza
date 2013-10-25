package com.inncrewin.waza.hibernate;

// Generated Sep 16, 2013 4:18:06 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * CreditCardInfo generated by hbm2java
 */
public class CreditCardInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5465682738094756593L;
	private Long userId;
	private String creditCardNo;
	private Date expiryDate;
	private String cvv;
	private String type;
	private String needAuth;

	public CreditCardInfo() {
	}

	public CreditCardInfo(Long userId, String creditCardNo, Date expiryDate,
			String cvv, String type, String needAuth) {
		this.userId = userId;
		this.creditCardNo = creditCardNo;
		this.expiryDate = expiryDate;
		this.cvv = cvv;
		this.type = type;
		this.needAuth = needAuth;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCreditCardNo() {
		return this.creditCardNo;
	}

	public void setCreditCardNo(String creditCardNo) {
		this.creditCardNo = creditCardNo;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getCvv() {
		return this.cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNeedAuth() {
		return this.needAuth;
	}

	public void setNeedAuth(String needAuth) {
		this.needAuth = needAuth;
	}

}
