package com.banking.entities;

public class Country {
	private Integer countryId;
	private String countryName;
	private Boolean activeStatus;
	
	
	public Country() {
		// TODO Auto-generated constructor stub
	}


	public Integer getCountryId() {
		return countryId;
	}


	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}


	public String getCountryName() {
		return countryName;
	}


	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}


	public Boolean getActiveStatus() {
		return activeStatus;
	}


	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	

}
