package com.banking.service;

import java.util.List;

import com.banking.entities.State;

public interface StateService {

    State saveState(State state);

    State updateState(Integer stateId, State state);

    List<State> getAllStates();

    State getStateById(Integer stateId);

    int deleteState(Integer stateId);

	List<State> getStatesByCountryId(Integer countryId);
}
