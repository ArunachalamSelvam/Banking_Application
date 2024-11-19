package com.banking.serviceImpl;

import com.banking.db.DbManager;
import com.banking.entities.ATMCard;
import com.banking.exception.AccountNumberNotExistException;
import com.banking.exception.BalanceInsufficientExeception;
import com.banking.service.ATMCardService;
import com.banking.utils.BcryptUtil;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class ATMCardServiceImpl implements ATMCardService {

	private static final DbManager DB_MANAGER = DbManager.getInstance();
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();
	private final BcryptUtil bcryptUtil = BcryptUtil.getInstance();

	private static TransactionServiceImpl transactionService = TransactionServiceImpl.getInstance();

	private static ATMCardServiceImpl instance = null;

	private ATMCardServiceImpl() {
		// Private constructor for singleton
	}

	public static ATMCardServiceImpl getInstance() {
		if (instance == null) {
			instance = new ATMCardServiceImpl();
		}
		return instance;
	}

	@Override
	public ATMCard saveATMCard(ATMCard atmCard) throws SQLException, ClassNotFoundException {
		String cardNumber;
		do {
			cardNumber = generateCardNumber();
		} while (!isCardNumberUnique(cardNumber));

		atmCard.setCardNumber(cardNumber);
		atmCard.setCvv(generateCVV());
		atmCard.setExpirationDate(generateExpirationDate());
		atmCard.setIssueDate(getIssueDate());
//		String pinHash = bcryptUtil.hashPin(generatePIN());
		atmCard.setPinHash(generatePIN());
		atmCard.setStatus(true);
		atmCard.setFreezeStatus(true);
		atmCard.setCardLimit(50000.00);

		String insertQuery = "INSERT INTO \"bankSchema\".atm_card (card_number, card_holder_name, expiration_date, cvv, pin, account_id, card_type_id, issue_date, status, card_limit, freeze_status, sleep_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection connection = null;
		try {
			connection = DB_MANAGER.createConnection();
			PreparedStatement pstmt = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, atmCard.getCardNumber());
			pstmt.setString(2, atmCard.getCardholderName());
			pstmt.setString(3, atmCard.getExpirationDate());
			pstmt.setString(4, atmCard.getCvv());
			pstmt.setString(5, atmCard.getPinHash());
			pstmt.setInt(6, atmCard.getAccountId());
			pstmt.setInt(7, atmCard.getCardTypeId());
			pstmt.setString(8, atmCard.getIssueDate());
			pstmt.setBoolean(9, atmCard.getStatus());
			pstmt.setDouble(10, atmCard.getCardLimit());
			pstmt.setBoolean(11, atmCard.getFreezeStatus());
			pstmt.setBoolean(12, false);

			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						atmCard.setCardId(generatedKeys.getInt(1));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			connection.close();
		}
		return atmCard;
	}

	private String generateCVV() {
		StringBuilder cvv = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			cvv.append(SECURE_RANDOM.nextInt(10)); // Generate 15 random digits
		}

		return cvv.toString();
	}

	private String generatePIN() {
		StringBuilder pin = new StringBuilder();
		for (int i = 0; i <= 3; i++) {
			pin.append(SECURE_RANDOM.nextInt(10));
		}

		System.out.println("Atm pin : " + pin.toString());
		return pin.toString();
	}

	private String generateExpirationDate() {
		// Get current date
		Calendar calendar = Calendar.getInstance();

		// Add 5 years to the current date
		calendar.add(Calendar.YEAR, 5);

		// Format the expiration date as MM/yy
		SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
		return sdf.format(calendar.getTime());
	}

	private String getIssueDate() {

		Calendar calendar = Calendar.getInstance();

//		calendar.add(Calendar.YEAR, 5);

		// Format the expiration date as MM/yy
		SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
		return sdf.format(calendar.getTime());

	}

	private String generateCardNumber() {
		StringBuilder cardNumber = new StringBuilder();
		for (int i = 0; i < 15; i++) {
			cardNumber.append(SECURE_RANDOM.nextInt(10));
		}
		String numberWithoutCheckDigit = cardNumber.toString();
		int checkDigit = calculateLuhnCheckDigit(numberWithoutCheckDigit);
		cardNumber.append(checkDigit);
		return cardNumber.toString();
	}

	private int calculateLuhnCheckDigit(String number) {
		int sum = 0;
		boolean alternate = false;
		for (int i = number.length() - 1; i >= 0; i--) {
			int n = Character.getNumericValue(number.charAt(i));
			if (alternate) {
				n *= 2;
				if (n > 9) {
					n -= 9;
				}
			}
			sum += n;
			alternate = !alternate;
		}
		int mod = sum % 10;
		return (mod == 0) ? 0 : 10 - mod;
	}

	private boolean isCardNumberUnique(String cardNumber) throws SQLException, ClassNotFoundException {
		String query = "SELECT COUNT(*) FROM \"bankSchema\".atm_card WHERE card_number = ?";
		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {

			pstmt.setString(1, cardNumber);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) == 0; // Return true if count is zero
			}
		}
		return false;
	}

	@Override
	public boolean isAtmcardExists(Integer customerId) throws SQLException, ClassNotFoundException {
		String query = "SELECT COUNT(*) FROM \"bankSchema\".atm_card atm "
				+ "JOIN \"bankSchema\".savings_account ac ON ac.account_id = atm.account_id "
				+ "JOIN \"bankSchema\".customer c ON ac.customer_id = c.customer_id " + "WHERE c.customer_id = ?";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, customerId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		}

		return false;
	}

	@Override
	public boolean verifyPin(Integer customerId, String pin) throws SQLException, ClassNotFoundException {
		String query = "SELECT COUNT(*) FROM \"bankSchema\".atm_card atm "
				+ "JOIN \"bankSchema\".savings_account ac ON ac.account_id = atm.account_id "
				+ "JOIN \"bankSchema\".customer c ON ac.customer_id = c.customer_id "
				+ "WHERE c.customer_id = ? AND atm.pin = ?";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, customerId);
			pstmt.setString(2, pin);

			// Debugging log
			System.out.println("Query: " + query);
			System.out.println("Customer ID: " + customerId);
			System.out.println("Pin: " + pin);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {

					int count = rs.getInt(1);
					System.out.println("Count: " + count); // Debugging log

					return count > 0;
				}
			}
		}

		return false;
	}

//	@Override
//	public boolean verifyPin(Integer customerId, String pin) throws SQLException, ClassNotFoundException {
//	    String query = "SELECT atm.pin FROM \"bankSchema\".atm_card atm "
//	                 + "JOIN \"bankSchema\".savings_account ac ON ac.account_id = atm.account_id "
//	                 + "JOIN \"bankSchema\".customer c ON ac.customer_id = c.customer_id "
//	                 + "WHERE c.customer_id = ?";
//
//	    try (Connection connection = DB_MANAGER.createConnection();
//	         PreparedStatement pstmt = connection.prepareStatement(query)) {
//	        pstmt.setInt(1, customerId);
//
//	        
//	        try (ResultSet rs = pstmt.executeQuery()) {
//	            if (rs.next()) {
//	                String hashedPin = rs.getString("pin");
//	                
//	                // Compare the provided pin with the stored hashed pin
//	                return bcryptUtil.checkPassword(pin, hashedPin); // Assuming `checkpw` method is present in `bcryptUtil`
//	            }
//	        }
//	    }
//	    
//	    return false; 
//	}

	@Override
	public boolean resetPin(Integer customerId, String oldPin, String newPin)
			throws ClassNotFoundException, SQLException {
		if (verifyPin(customerId, oldPin)) {
			String updateQuery = "UPDATE \"bankSchema\".atm_card atm\n" + "SET pin = ?\n"
					+ "WHERE atm.account_id IN (\n" + "    SELECT ac.account_id\n"
					+ "    FROM \"bankSchema\".savings_account ac\n"
					+ "    JOIN \"bankSchema\".customer c ON ac.customer_id = c.customer_id\n"
					+ "    WHERE c.customer_id = ?\n" + ")\n" + "";

			try (Connection connection = DB_MANAGER.createConnection();
					PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {

//				pstmt.setString(1,  bcryptUtil.hashPin(newPin));

				pstmt.setString(1, newPin);

				pstmt.setInt(2, customerId);

				int affectedRows = pstmt.executeUpdate();

				if (affectedRows > 0) {
					return true;
				}
			}

		}

		return false;
	}

	@Override
	public ATMCard updateATMCard(Integer cardId, ATMCard atmCard) throws SQLException, ClassNotFoundException {
		String updateQuery = "UPDATE \"bankSchema\".atm_card SET card_number = ?, card_holder_name = ?, expiration_date = ?, cvv = ?, pin = ?, account_id = ?, card_type_id = ?, issue_date = ?, card_limit = ? WHERE card_id = ?";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {

			pstmt.setString(1, atmCard.getCardNumber());
			pstmt.setString(2, atmCard.getCardholderName());
			pstmt.setString(3, atmCard.getExpirationDate());
			pstmt.setString(4, atmCard.getCvv());
			pstmt.setString(5, atmCard.getPinHash());
			pstmt.setInt(6, atmCard.getAccountId());
			pstmt.setInt(7, atmCard.getCardTypeId());
			pstmt.setString(8, atmCard.getIssueDate());
			pstmt.setDouble(9, atmCard.getCardLimit());
			pstmt.setInt(10, cardId);

			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				return atmCard;
			} else {
				throw new SQLException("Updating ATMCard failed, no rows affected.");
			}
		}
	}

	@Override
	public ATMCard getATMCardById(Integer customerId) throws SQLException, ClassNotFoundException {
		String selectQuery = "SELECT atm.* FROM \"bankSchema\".atm_card atm\n"
				+ "JOIN \"bankSchema\".savings_account ac ON atm.account_id = ac.account_id\n"
				+ "JOIN \"bankSchema\".customer cu ON ac.customer_id = cu.customer_id	\n"
				+ "WHERE cu.customer_id = ?";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {

			pstmt.setInt(1, customerId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					ATMCard atmCard = new ATMCard();
//					atmCard.setCardId(rs.getInt("card_id"));
					atmCard.setCardNumber(rs.getString("card_number"));
					atmCard.setCardholderName(rs.getString("card_holder_name"));
					atmCard.setExpirationDate(rs.getString("expiration_date"));
					atmCard.setCvv(rs.getString("cvv"));
//					atmCard.setPinHash(rs.getString("pin"));
//					atmCard.setAccountId(rs.getInt("account_id"));
//					atmCard.setCardTypeId(rs.getInt("card_type_id"));
//					atmCard.setIssueDate(rs.getString("issue_date"));
//					atmCard.setStatus(rs.getString("status"));
					atmCard.setCardLimit(rs.getDouble("card_limit"));
					return atmCard;
				} else {
					return null; // No ATMCard found
				}
			}
		}
	}

	public Integer getCardIdBycustomerId(Integer customerId) throws SQLException, ClassNotFoundException {
		String selectQuery = "SELECT atm.card_id FROM \"bankSchema\".atm_card atm\n"
				+ "JOIN \"bankSchema\".savings_account ac ON atm.account_id = ac.account_id\n"
				+ "JOIN \"bankSchema\".customer cu ON ac.customer_id = cu.customer_id	\n"
				+ "WHERE cu.customer_id = ?";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {

			pstmt.setInt(1, customerId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("card_id");
				} else {
					return null; // No ATMCard found
				}
			}
		}

	}

	@Override
	public String getCVV(Integer customerId) throws SQLException, ClassNotFoundException {
		String selectQuery = "SELECT atm.cvv FROM \"bankSchema\".atm_card atm\n"
				+ "JOIN \"bankSchema\".savings_account ac ON atm.account_id = ac.account_id\n"
				+ "JOIN \"bankSchema\".customer cu ON ac.customer_id = cu.customer_id	\n"
				+ "WHERE cu.customer_id = ?";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {

			pstmt.setInt(1, customerId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {

					return rs.getString("cvv");
				} else {
					return null; // No ATMCard found
				}
			}
		}
	}

	@Override
	public List<ATMCard> getAllATMCard() throws SQLException, ClassNotFoundException {
		String selectQuery = "SELECT * FROM \"bankSchema\".atm_card";

		List<ATMCard> atmCards = new ArrayList<>();

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(selectQuery);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				ATMCard atmCard = new ATMCard();
				atmCard.setCardId(rs.getInt("card_id"));
				atmCard.setCardNumber(rs.getString("card_number"));
				atmCard.setCardholderName(rs.getString("card_holder_name"));
				atmCard.setExpirationDate(rs.getString("expiration_date"));
				atmCard.setCvv(rs.getString("cvv"));
				atmCard.setPinHash(rs.getString("pin"));
				atmCard.setAccountId(rs.getInt("account_id"));
				atmCard.setCardTypeId(rs.getInt("card_type_id"));
				atmCard.setIssueDate(rs.getString("issue_date"));
				atmCard.setStatus(rs.getBoolean("status"));
				atmCard.setFreezeStatus(rs.getBoolean("freeze"));
				atmCard.setCardLimit(rs.getDouble("card_limit"));
				atmCards.add(atmCard);
			}
		}
		return atmCards;
	}

	@Override
	public int deleteATMCard(Integer cardId) throws SQLException, ClassNotFoundException {
		String deleteQuery = "DELETE FROM \"bankSchema\".atm_card WHERE card_id = ?";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {

			pstmt.setInt(1, cardId);
			return pstmt.executeUpdate();
		}
	}

	@Override
	public int changeBlockStatusOfTheAtmCard(int customerId, boolean status) throws SQLException, ClassNotFoundException {
		String query = "UPDATE \"bankSchema\".atm_card ac\n" + "SET status = ?\n" + "WHERE ac.account_id IN (\n"
				+ "    SELECT sa.account_id\n" + "    FROM \"bankSchema\".savings_account sa\n"
				+ "    JOIN \"bankSchema\".customer cu ON cu.customer_id = sa.customer_id\n"
				+ "    WHERE cu.customer_id = ?\n" + ")";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setBoolean(1, status);
			pstmt.setInt(2, customerId);

			return pstmt.executeUpdate();
		}
	}

	@Override
	public int changeFreezeStatusOfAtmCard(int customerId, boolean status) throws SQLException, ClassNotFoundException {
		String query = "UPDATE \"bankSchema\".atm_card ac\n" + "SET freeze_status = ?\n" + "WHERE ac.account_id IN (\n"
				+ "    SELECT sa.account_id\n" + "    FROM \"bankSchema\".savings_account sa\n"
				+ "    JOIN \"bankSchema\".customer cu ON cu.customer_id = sa.customer_id\n"
				+ "    WHERE cu.customer_id = ?\n" + ")";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setBoolean(1, status);
			pstmt.setInt(2, customerId);

			return pstmt.executeUpdate();
		}
	}

	@Override
	public int changeSleepStatusOfAtmCard(int customerId, boolean status) throws SQLException, ClassNotFoundException {
		String query = "UPDATE \"bankSchema\".atm_card ac\n" + "SET sleep_status = ?\n" + "WHERE ac.account_id IN (\n"
				+ "    SELECT sa.account_id\n" + "    FROM \"bankSchema\".savings_account sa\n"
				+ "    JOIN \"bankSchema\".customer cu ON cu.customer_id = sa.customer_id\n"
				+ "    WHERE cu.customer_id = ?\n" + ")";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setBoolean(1, status);
			pstmt.setInt(2, customerId);

			return pstmt.executeUpdate();
		}
	}

	@Override
	public boolean getAtmCardBlockStatus(int customerId) throws ClassNotFoundException, SQLException {
		String query = "SELECT atm.status \n" + "FROM \"bankSchema\".atm_card atm\n"
				+ "JOIN \"bankSchema\".savings_account sa ON atm.account_id = sa.account_id\n"
				+ "JOIN \"bankSchema\".customer cu ON cu.customer_id = sa.customer_id\n" + "WHERE cu.customer_id = ?";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, customerId);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getBoolean("status");
			}
		}
		return false;

	}
	
	
	@Override
	public boolean getAtmCardFreezeStatus(int customerId) throws ClassNotFoundException, SQLException {
		String query = "SELECT atm.freeze_status \n" + "FROM \"bankSchema\".atm_card atm\n"
				+ "JOIN \"bankSchema\".savings_account sa ON atm.account_id = sa.account_id\n"
				+ "JOIN \"bankSchema\".customer cu ON cu.customer_id = sa.customer_id\n" + "WHERE cu.customer_id = ?";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, customerId);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getBoolean("freeze_status");
			}
		}
		return false;

	}
	
	@Override
	public boolean getAtmCardSleepStatus(int customerId) throws ClassNotFoundException, SQLException {
		String query = "SELECT atm.sleep_status \n" + "FROM \"bankSchema\".atm_card atm\n"
				+ "JOIN \"bankSchema\".savings_account sa ON atm.account_id = sa.account_id\n"
				+ "JOIN \"bankSchema\".customer cu ON cu.customer_id = sa.customer_id\n" + "WHERE cu.customer_id = ?";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, customerId);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getBoolean("sleep_status");
			}
		}
		return false;

	}

	public boolean depositToTheOwnAccountByAtmCard(String cardNo, String cvv, String expiryDate, String cardHolderName,
			double amount, String pin) {
		try {
			if (isAtmCardExists(cardNo, cvv, expiryDate, cardHolderName, pin)) {
				Integer accountId = getAccountIdByAtmCardNo(cardNo);
				return transactionService.depositByAtmCard(accountId, amount);
			} else {
				System.out.println("ATM Card not found.");

			}
		} catch (ClassNotFoundException | SQLException | AccountNumberNotExistException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return false;
	}

	public boolean withdrawFromTheOwnAccountByAtmCard(String cardNo, String cvv, String expiryDate,
			String cardHolderName, double amount, String pin) {
		try {
			if (isAtmCardExists(cardNo, cvv, expiryDate, cardHolderName, pin)) {
				Integer accountId = getAccountIdByAtmCardNo(cardNo);
				return transactionService.withdrawByAtmCard(accountId, amount);
			} else {
				System.out.println("ATM Card not found.");
			}
		} catch (ClassNotFoundException | SQLException | AccountNumberNotExistException
				| BalanceInsufficientExeception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public boolean isAtmCardExists(String cardNo, String cvv, String expiryDate, String cardHolderName, String pin)
			throws ClassNotFoundException, SQLException {
		String query = "SELECT EXISTS (\n" + "    SELECT 1 \n" + "    FROM \"bankSchema\".atm_card \n"
				+ "    WHERE card_number = ? \n" + "    AND cvv = ?\n" + "    AND expiration_date = ?\n"
				+ "    AND card_holder_name = ?\n" + "		AND pin = ?" + ")";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {

			pstmt.setString(1, cardNo);
			pstmt.setString(2, cvv);
			pstmt.setString(3, expiryDate);
			pstmt.setString(4, cardHolderName);
			pstmt.setString(5, pin);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getBoolean(1);
			}

		}

		return false;

	}

	public Integer getAccountIdByAtmCardNo(String cardNo) {
		String query = "    SELECT account_id \n" + "    FROM \"bankSchema\".atm_card \n" + "    WHERE card_number = ?";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {

			pstmt.setString(1, cardNo);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ATMCard getAtmCardWithCustomerNameAndAccountIdByCustomerId(int customerId)
			throws ClassNotFoundException, SQLException {
		String query = "SELECT c.first_name AS firstName,\n" + "		c.middle_name AS middleName,\n"
				+ "		c.last_name AS lastName,\n" + "		ac.account_id AS accountId\n" + "\n"
				+ "FROM \"bankSchema\".customer c\n"
				+ "JOIN \"bankSchema\".savings_account ac ON c.customer_id = ac.customer_id\n"
				+ "Where c.customer_id = ?";
		ATMCard atmCard = null;
		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, customerId);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				atmCard.setAccountId(rs.getInt("accountId"));
				atmCard.setCardholderName(rs.getString("firstName") + rs.getString("lastName"));
			}
		}

		return atmCard;
	}
}
