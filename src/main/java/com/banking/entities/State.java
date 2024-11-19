package com.banking.entities;

public class State {
	private Integer stateId;
	private Integer countryId;
	private String stateName;
	private Boolean activeStatus;
	
	public State() {
		// TODO Auto-generated constructor stub
	}

	
	
	public State(Integer countryId, String stateName, Boolean activeStatus) {
		super();
		this.countryId = countryId;
		this.stateName = stateName;
		this.activeStatus = activeStatus;
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

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Boolean getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	 @Override
	    public String toString() {
	        return "State{" +
	                "stateId=" + stateId + "\n"+
	                ", countryId=" + countryId + "\n"+
	                ", stateName='" + stateName + '\'' + "\n"+
	                ", activeStatus=" + activeStatus +
	                '}';
	    }

}
