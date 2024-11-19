package com.banking.entities;

public class Customer {
	
	private Integer customerId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String fatherName;
	private String motherName;
	private String password;
	private String gender;
	private String mobileNumber;
	private String emailId;
	private String adharNumber;
	private String panNumber;
	
	
	private Integer addressId;
	
	private Integer roleId;
	
//	private Boolean isAtrmCardAdded;
	
	public Customer() {
		// TODO Auto-generated constructor stub
	}
	
	

	public Customer(String firstName, String middleName, String lastName, String fatherName, String motherName,
			String gender, String mobileNumber, String emailId, String adharNumber, String panNumber,
			Integer addressId, Integer roleId) {
		super();
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.fatherName = fatherName;
		this.motherName = motherName;
		this.gender = gender;
		this.mobileNumber = mobileNumber;
		this.emailId = emailId;
		this.adharNumber = adharNumber;
		this.panNumber = panNumber;
		this.addressId = addressId;
		this.roleId = roleId;
	}



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

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public Integer getRoleId() {
		return roleId;
	}



	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}



//	public Boolean getIsAtrmCardAdded() {
//		return isAtrmCardAdded;
//	}
//
//
//
//	public void setIsAtrmCardAdded(Boolean isAtrmCardAdded) {
//		this.isAtrmCardAdded = isAtrmCardAdded;
//	}
	
	@Override
	public String toString() {
	    return "Customer{" +
	            "customerId=" + customerId +
	            ", firstName='" + firstName + '\'' +
	            ", middleName='" + middleName + '\'' +
	            ", lastName='" + lastName + '\'' +
	            ", fatherName='" + fatherName + '\'' +
	            ", motherName='" + motherName + '\'' +
	            ", password='" + password + '\'' +
	            ", gender='" + gender + '\'' +
	            ", mobileNumber='" + mobileNumber + '\'' +
	            ", emailId='" + emailId + '\'' +
	            ", adharNumber='" + adharNumber + '\'' +
	            ", panNumber='" + panNumber + '\'' +
	            ", addressId=" + addressId +
	            ", roleId=" + roleId +
	            '}';
	}


}
