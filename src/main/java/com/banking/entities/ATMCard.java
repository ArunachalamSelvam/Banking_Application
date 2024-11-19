package com.banking.entities;

import java.util.Date;

public class ATMCard {

    private Integer cardId; 
    private String cardNumber; 
    private String cardholderName; 
    private String expirationDate; 
    private String cvv; 
    private String pinHash; 
    private Integer accountId;
    private Integer cardTypeId; 
    private String issueDate; 
    private Boolean status; 
    private Boolean freezeStatus;
    
    private Double cardLimit; 

    // Getters and setters
    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getPinHash() {
        return pinHash;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(Integer cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Double getCardLimit() {
        return cardLimit;
    }

    public void setCardLimit(Double cardLimit) {
        this.cardLimit = cardLimit;
    }
    
    @Override
    public String toString() {
        return "ATMCard{" +
                "cardId=" + cardId +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardholderName='" + cardholderName + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", cvv='" + cvv + '\'' +
                ", pinHash='" + pinHash + '\'' +
                ", accountId=" + accountId +
                ", cardTypeId=" + cardTypeId +
                ", issueDate='" + issueDate + '\'' +
                ", status='" + status + '\'' +
                ", cardLimit=" + cardLimit +
                '}';
    }

	public Boolean getFreezeStatus() {
		return freezeStatus;
	}

	public void setFreezeStatus(Boolean freezeStatus) {
		this.freezeStatus = freezeStatus;
	}

}
