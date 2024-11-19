package com.banking.entities;

public class Address {
	private Integer addressId;
	private String address;
	private Integer stateId;
	private Integer countryId;
	private Boolean activeStatus;
	
	public Address() {
		// TODO Auto-generated constructor stub
	}
	
	

	public Address(String address, Integer stateId, Integer countryId, Boolean activeStatus) {
		super();
		this.address = address;
		this.stateId = stateId;
		this.countryId = countryId;
		this.activeStatus = activeStatus;
	}



	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Boolean getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	@Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", address='" + address + '\'' +
                ", stateId=" + stateId +
                ", countryId=" + countryId +
                ", activeStatus=" + activeStatus +
                '}';
    }
	
}
