package com.banking.service;

import com.banking.entities.ATMCard;

import java.sql.SQLException;
import java.util.List;

public interface ATMCardService {

    ATMCard saveATMCard(ATMCard atmCard) throws SQLException, ClassNotFoundException;

    ATMCard updateATMCard(Integer cardId, ATMCard atmCard) throws SQLException, ClassNotFoundException;

    ATMCard getATMCardById(Integer cardId) throws SQLException, ClassNotFoundException;

    List<ATMCard> getAllATMCard() throws SQLException, ClassNotFoundException;

    int deleteATMCard(Integer cardId) throws SQLException, ClassNotFoundException;

//	int blockTheATMCard(Integer cardId) throws SQLException, ClassNotFoundException;

	String getCVV(Integer customerId) throws SQLException, ClassNotFoundException;

	boolean isAtmcardExists(Integer customerId) throws SQLException, ClassNotFoundException;


	boolean verifyPin(Integer customerId, String pin) throws SQLException, ClassNotFoundException;

	boolean resetPin(Integer customerId, String oldPin, String newPin) throws ClassNotFoundException, SQLException;

	ATMCard getAtmCardWithCustomerNameAndAccountIdByCustomerId(int customerId)
			throws ClassNotFoundException, SQLException;

	int changeFreezeStatusOfAtmCard(int customerId, boolean status) throws SQLException, ClassNotFoundException;

	int changeBlockStatusOfTheAtmCard(int customerId, boolean status) throws SQLException, ClassNotFoundException;

	boolean getAtmCardFreezeStatus(int customerId) throws ClassNotFoundException, SQLException;

	int changeSleepStatusOfAtmCard(int customerId, boolean status) throws SQLException, ClassNotFoundException;

	boolean getAtmCardSleepStatus(int customerId) throws ClassNotFoundException, SQLException;

	boolean getAtmCardBlockStatus(int customerId) throws ClassNotFoundException, SQLException;
}
