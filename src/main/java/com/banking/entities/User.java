package com.banking.entities;


public class User {

	private Integer userId;

	private String firstName;

	private String lastName;

	private String mobileNumber;

	private String emailId;

	private String password;

	private Boolean activeStatus;


	public User() {
		// TODO Auto-generated constructor stub
	}

	
	
	public User(String firstName, String lastName, String mobileNumber, String emailId, String password,
			Boolean activeStatus) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.emailId = emailId;
		this.password = password;
		this.activeStatus = activeStatus;
	}



	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	

	@Override
    public String toString() {
        return "User{" +
                "userId=" + userId +"\n"+
                ", firstName='" + firstName + '\'' +"\n"+
                ", lastName='" + lastName + '\'' + "\n"+
                ", mobileNumber='" + mobileNumber + '\'' + "\n"+
                ", emailId='" + emailId + '\'' + "\n"+
                ", password='" + password + '\'' + "\n"+
                ", activeStatus=" + activeStatus +
                '}';
    }

}
