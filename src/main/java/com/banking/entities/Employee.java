package com.banking.entities;

public class Employee {

	private Integer empoyeeId;

	private String firstName;

	private String middleName;

	private String lastName;
	
	private String gender;

	private String fatherName;

	private String motherName;

	private String mobileNumber;

	private String emailId;

	private String password;

	private Integer employeeNo;

	private Boolean activeStatus;
	
	private Integer roleId;

	private Integer branchId;

	private Integer addressId;

	public Employee() {

	}
	
	
	

	public Employee(String firstName, String middleName, String lastName, String gender, String fatherName,
			String motherName, String mobileNumber, String emailId, String password,
			Boolean activeStatus,Integer roleId, Integer branchId, Integer addressId) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.gender = gender;
		this.fatherName = fatherName;
		this.motherName = motherName;
		this.mobileNumber = mobileNumber;
		this.emailId = emailId;
		this.password = password;
		this.activeStatus = activeStatus;
		this.setRoleId(roleId);
		this.branchId = branchId;
		this.addressId = addressId;
	}



	public Integer getEmpoyeeId() {
		return empoyeeId;
	}

	public void setEmpoyeeId(Integer empoyeeId) {
		this.empoyeeId = empoyeeId;
	}

	

	public Integer getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(Integer employeeNo) {
		this.employeeNo = employeeNo;
	}

	public Boolean getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
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

	
	@Override
	public String toString() {
	    return "Employee{" +
	            "employeeId=" + empoyeeId +
	            ", firstName='" + firstName + '\'' +
	            ", middleName='" + middleName + '\'' +
	            ", lastName='" + lastName + '\'' +
	            ", gender='" + gender + '\'' +
	            ", fatherName='" + fatherName + '\'' +
	            ", motherName='" + motherName + '\'' +
	            ", mobileNumber='" + mobileNumber + '\'' +
	            ", emailId='" + emailId + '\'' +
	            ", password='" + password + '\'' +
	            ", employeeNo=" + employeeNo +
	            ", activeStatus=" + activeStatus +
	            ", branchId=" + branchId +
	            ", addressId=" + addressId +
	            '}';
	}




	public Integer getRoleId() {
		return roleId;
	}




	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	
}
