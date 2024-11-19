package com.banking.entities;

public class Branch {
	
	private Integer branchId;
	
	private String branchName;
	
	private Integer branchAddressId;
	
	private Integer bankId;
	
	private String ifscCode;
	
	
	public Branch() {
		// TODO Auto-generated constructor stub
	}


	public Integer getBranchId() {
		return branchId;
	}


	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}


	public String getBranchName() {
		return branchName;
	}


	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}


	public Integer getBranchAddressId() {
		return branchAddressId;
	}


	public void setBranchAddressId(Integer branchAddressId) {
		this.branchAddressId = branchAddressId;
	}


	public Integer getBankId() {
		return bankId;
	}


	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}


	public String getIfscCode() {
		return ifscCode;
	}


	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	
	
	@Override
    public String toString() {
        return "Branch{" +
                "branchId=" + branchId +
                ", branchName='" + branchName + '\'' +
                ", branchAddressId=" + branchAddressId +
                ", bankId=" + bankId +
                ", ifscCode='" + ifscCode + '\'' +
                '}';
    }

}
