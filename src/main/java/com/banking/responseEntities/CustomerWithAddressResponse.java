package com.banking.responseEntities;

public class CustomerWithAddressResponse {
	
	private Integer customerId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String fatherName;
	private String motherName;

	private String gender;
	private String mobileNumber;
	private String emailId;
	private String adharNumber;
	private String panNumber;
	
	private Integer addressId;
	
	private String address;
	private Integer stateId;
	private Integer countryId;
	
	private String countryName;
	private String stateName;
	
	
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getAdharNumber() {
		return adharNumber;
	}
	public void setAdharNumber(String adharNumber) {
		this.adharNumber = adharNumber;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
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
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	 @Override
	    public String toString() {
	        return "CustomerWithAddressResponse{" +
	                "customerId=" + customerId +
	                ", firstName='" + firstName + '\'' +
	                ", middleName='" + middleName + '\'' +
	                ", lastName='" + lastName + '\'' +
	                ", fatherName='" + fatherName + '\'' +
	                ", motherName='" + motherName + '\'' +
	                ", gender='" + gender + '\'' +
	                ", mobileNumber='" + mobileNumber + '\'' +
	                ", emailId='" + emailId + '\'' +
	                ", adharNumber='" + adharNumber + '\'' +
	                ", panNumber='" + panNumber + '\'' +
	                ", addressId=" + addressId +
	                ", address='" + address + '\'' +
	                ", stateId=" + stateId +
	                ", countryId=" + countryId +
	                ", countryName='" + countryName + '\'' +
	                ", stateName='" + stateName + '\'' +
	                '}';
	    }


}
