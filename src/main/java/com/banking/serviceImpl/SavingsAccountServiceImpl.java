package com.banking.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.banking.db.DbManager;
import com.banking.entities.ATMCard;
import com.banking.entities.Address;
import com.banking.entities.Answer;
import com.banking.entities.Customer;
import com.banking.entities.SavingsAccount;
import com.banking.exception.AccountNumberNotExistException;
import com.banking.requestEntities.CustomerAndAddressId;
import com.banking.requestEntities.CustomerWithAddress;
import com.banking.requestEntities.SavingsAccountWithCustomer;
import com.banking.service.AnswerService;
import com.banking.service.SavingsAccountService;
import com.banking.utils.BcryptUtil;

public class SavingsAccountServiceImpl implements SavingsAccountService {

	private final CustomerServiceImpl customerService = CustomerServiceImpl.getInstance();
	private final ATMCardServiceImpl atmCardService = ATMCardServiceImpl.getInstance();
	private final AnswerService answerService = AnswerServiceImpl.getInstance();

	private static final DbManager DB_MANAGER = DbManager.getInstance();

	private final BcryptUtil bcryptUtil = BcryptUtil.getInstance();

	public static SavingsAccountServiceImpl instance = null;

	private SavingsAccountServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	public static SavingsAccountServiceImpl getInstance() {
		if (instance == null) {
			instance = new SavingsAccountServiceImpl();
		}

		return instance;
	}

	@Override
	public String saveSavingsAccount(SavingsAccountWithCustomer savingsAccountWithCustomer)
			throws SQLException, ClassNotFoundException {
		System.out.println("Inside savings account method.");
		Connection connection = null;
		Address address = savingsAccountWithCustomer.getAddress();
		Customer customer = savingsAccountWithCustomer.getCustomer();

//		List<Answer> answers = new ArrayList<Answer>();
			
//		if (savingsAccountWithCustomer.isAtmCardNeeded()) {
//			
//			customer.setIsAtrmCardAdded(true);
//			
//		}
//		else {
//			
//			customer.setIsAtrmCardAdded(false);
//			
//		}

		CustomerWithAddress customerWithAddress = new CustomerWithAddress();
		customerWithAddress.setCustomer(customer);
		customerWithAddress.setAddress(address);

		try {
			// Initialize the connection
			connection = DB_MANAGER.createConnection();
			connection.setAutoCommit(false); // Start transaction

			// Save customer
			Customer savedCustomer = customerService.saveCustomer(customerWithAddress);

//			for (Answer answer : answers) {
//				answer.setCustomerId(savedCustomer.getCustomerId());
//				;
//			}

			// Send Secret answer of the user to the db
//			List<Answer> resultAnswer = answerService.saveAnswer(answers);
			// Prepare savings account details
			SavingsAccount savingsAccount = savingsAccountWithCustomer.getSavingsAccount();
			savingsAccount.setCustomerId(savedCustomer.getCustomerId());

			System.out.println("saved customer Id : " + savedCustomer.getCustomerId());

			String insertQuery = "INSERT INTO \"bankSchema\".savings_account (account_number, balance, minimum_balance, interest_rate, customer_id, branch_id, account_status) VALUES (?, ?, ?, ?, ?, ?, ?)";

			try (PreparedStatement pstmt = connection.prepareStatement(insertQuery,
					PreparedStatement.RETURN_GENERATED_KEYS)) {
				String accountNumber = generateAccountNumber();
				savingsAccount.setAccountNumber(accountNumber);
				pstmt.setString(1, accountNumber);
				pstmt.setDouble(2, 2000); // Default balance
				pstmt.setDouble(3, 1000.00);
				pstmt.setDouble(4, 3.5);
				pstmt.setInt(5, savingsAccount.getCustomerId());
				pstmt.setInt(6, savingsAccount.getBranchId());
				pstmt.setBoolean(7, true); // Default account status

				int affectedRows = pstmt.executeUpdate();

				if (affectedRows > 0) {
					try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
						if (generatedKeys.next()) {
							savingsAccount.setAccountId(generatedKeys.getInt("account_id"));
							System.out.println("Account ID : " + savingsAccount.getAccountId());
						}
					}
				}
			}

			connection.commit();
			// Create and save ATM card
//			if (savingsAccountWithCustomer.isAtmCardNeeded()) {
			System.out.println("is AtmCard selected : " + savingsAccountWithCustomer.isAtmCardNeeded());
			connection.setAutoCommit(false);
			ATMCard atmCard = new ATMCard();
			atmCard.setAccountId(savingsAccount.getAccountId());
			atmCard.setCardholderName(savedCustomer.getFirstName() + "" + savedCustomer.getMiddleName() + " "
					+ savedCustomer.getLastName());
			atmCard.setCardLimit(50000.00); // Default card limit
			atmCard.setCardTypeId(1); // Default card type ID
//				atmCard.setPinHash(bcryptUtil.hashPin(savingsAccountWithCustomer.getAtmPin()));
			atmCard.setPinHash(savingsAccountWithCustomer.getAtmPin());

			ATMCard savedAtmCard = atmCardService.saveATMCard(atmCard);

			// Commit transaction

			connection.commit();
			System.out.println(savedAtmCard);
//			}
//			return savingsAccount;

			String userMessage = "Hi! " + customer.getFirstName() + ",\\n" 
				    + "Your bank account has been created successfully.\\n" 
				    + "Your bank account number is *" + savingsAccount.getAccountNumber() + "*.\\n"
				    + "Your temporary ATM PIN is *" + savedAtmCard.getPinHash() + "*.\\n"
				    + "Please change this after you log in.\\n"
				    + "Click here to log in: [Login to your account](http://localhost:8080/Banking_Project/login.html)";
			return userMessage;
		} catch (SQLException e) {
			// Rollback transaction on error
			if (connection != null) {
				try {
					if (!connection.getAutoCommit()) {
						connection.rollback();
					}
				} catch (SQLException rollbackEx) {
					// Log rollback exception if necessary
					rollbackEx.printStackTrace();
				}
			}
			// Re-throw the original exception
			throw e;
		} finally {
			// Restore default auto-commit behavior and close the connection
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
					connection.close();
				} catch (SQLException e) {
					// Log exception during auto-commit restoration
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public SavingsAccount updateSavingsAccount(Integer accountId, SavingsAccountWithCustomer savingsAccountWithCustomer)
			throws ClassNotFoundException, SQLException {

		Connection connection = null;

		if (isAccountExists(accountId)) {
			CustomerAndAddressId customerAndAddressId = getCustomerAndAddressIdByAccountId(accountId);
			Integer customerId = customerAndAddressId.getCustomerId();
			Integer addressId = customerAndAddressId.getAddressId();
			SavingsAccount savingsAccount = savingsAccountWithCustomer.getSavingsAccount();
			savingsAccount.setAccountId(accountId);
			savingsAccount.setCustomerId(customerId);

			Address address = savingsAccountWithCustomer.getAddress();
			address.setAddressId(addressId);

			Customer customer = savingsAccountWithCustomer.getCustomer();
			customer.setCustomerId(customerId);
			CustomerWithAddress customerWithAddress = new CustomerWithAddress();

			customerWithAddress.setCustomer(customer);
			customerWithAddress.setAddress(address);
			Customer updatedCustomer = customerService.updateCustomer(customerId, customerWithAddress);

			String query = "UPDATE \"bankSchema\".savings_account\n"
					+ "	SET minimum_balance=?, interest_rate=?, account_status=?\n" + "	WHERE account_id = ?";

			try {

				connection = DB_MANAGER.createConnection();
				connection.setAutoCommit(false);
				PreparedStatement pstmt = connection.prepareStatement(query);

				pstmt.setDouble(1, savingsAccount.getMinBalance());
				pstmt.setDouble(2, savingsAccount.getInterestRate());
//				pstmt.setInt(3, savingsAccount.getCustomerId());
//				pstmt.setInt(4, savingsAccount.getBranchId());
				pstmt.setBoolean(3, savingsAccount.getAccountStatus());
				pstmt.setInt(4, accountId);

				int affectedRows = pstmt.executeUpdate();

				if (affectedRows > 0) {
					connection.commit(); // Commit transaction if all updates succeed
					return savingsAccount;
				} else {
					throw new SQLException("Updating customer failed, no rows affected.");
				}

			}

			catch (SQLException e) {
				if (connection != null) {
					try {
						connection.rollback(); // Rollback transaction on error
					} catch (SQLException rollbackEx) {
						rollbackEx.printStackTrace();
					}
				}
				throw e; // Re-throw the exception after rollback
			} finally {
				if (connection != null) {
					try {
						connection.setAutoCommit(true); // Reset auto-commit to true
						connection.close(); // Close the connection
					} catch (SQLException closeEx) {
						closeEx.printStackTrace();
					}
				}
			}

		}

		return null;
	}

	@Override
	public List<SavingsAccount> getAllSavingsAccounts() {

		return null;
	}

	@Override
	public SavingsAccount getSavingsAccountByAccountNo(String accountNo) throws AccountNumberNotExistException {

		String query = "SELECT * FROM \"bankSchema\".savings_account WHERE account_number = ?";

		Connection connection = null;

		SavingsAccount savingAccount = null;

		if (doesSavingsAccountExist(accountNo)) {
			try {
				connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setString(1, accountNo);

				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					int accountId = rs.getInt("account_id");
					double balance = rs.getDouble("balance");
					double minBalance = rs.getDouble("minimum_balance");
					boolean accountStatus = rs.getBoolean("account_status");
					int branchId = rs.getInt("branch_id");
					double interestRate = rs.getDouble("interest_rate");
					savingAccount = new SavingsAccount(balance, minBalance, accountStatus, branchId, interestRate);
					savingAccount.setAccountId(accountId);
					savingAccount.setAccountNumber(accountNo);

				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return savingAccount;
	}

	@Override
	public int deleteSavingsAccount(Integer accountId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isAccountExists(Integer accountId) throws ClassNotFoundException, SQLException {

		String query = "SELECT \"bankSchema\".check_bank_account_id_exists(?)";

		Connection connection = null;

		try {
			connection = DB_MANAGER.createConnection();
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, accountId);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getBoolean(1);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		return false;

	}

	public CustomerAndAddressId getCustomerAndAddressIdByAccountId(Integer accountId)
			throws SQLException, ClassNotFoundException {
		String query = "SELECT\n" + "	ac.customer_id As customerId,\n" + "	a.address_id As addressId\n" + "FROM\n"
				+ "	\"bankSchema\".savings_account ac\n" + "JOIN\n"
				+ "	\"bankSchema\".customer cu ON ac.customer_id = cu.customer_id\n" + "JOIN\n"
				+ "	\"bankSchema\".address a ON cu.address_id = a.address_id\n" + "WHERE\n" + "	ac.account_id = ?";

		try (Connection connection = DB_MANAGER.createConnection()) {
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, accountId);

			ResultSet rs = pstmt.executeQuery();

			CustomerAndAddressId customerAndAddressId = new CustomerAndAddressId();
			if (rs.next()) {
				customerAndAddressId.setCustomerId(rs.getInt("customerId"));
				customerAndAddressId.setAddressId(rs.getInt("addressId"));
			}
			return customerAndAddressId;
		}

	}

	public static String generateAccountNumber() throws ClassNotFoundException {
		String accountNumber = null;
		try (Connection conn = DB_MANAGER.createConnection()) {
			// Start a transaction
			conn.setAutoCommit(false);

			// Retrieve the current sequence number
			String sql = "SELECT counter FROM \"bankSchema\".account_number_sequence FOR UPDATE";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				int counter = rs.getInt("counter");
				counter++;

				// Update the sequence number in the database
				sql = "UPDATE \"bankSchema\".account_number_sequence SET counter = ? WHERE id = 1";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, counter);
				pstmt.executeUpdate();

				// Commit the transaction
				conn.commit();

				// Generate the account number with a fixed prefix and the sequence number
//				accountNumber = "ACCT-" + String.format("%08d", counter);
				int number = 100000 + counter;
				accountNumber = String.valueOf(number);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accountNumber;
	}

	public boolean doesSavingsAccountExist(String accountNo) throws AccountNumberNotExistException {

		String query = "SELECT COUNT(*) FROM \"bankSchema\".savings_account WHERE account_number = ?";

		Connection connection = null;
		boolean exists = false;

		try {
			connection = DB_MANAGER.createConnection();
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, accountNo);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					exists = true;
				}
			}

		} catch (Exception e) {
			throw new AccountNumberNotExistException("Account No does not exist.");
		} finally {
			// Close resources
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return exists;
	}

	public Double getBalance(Integer accountId) {
		String query = "SELECT balance FROM \"bankSchema\".savings_account WHERE account_id = ?";

		Connection connection = null;

		try {
			System.out.println("Inside Balance Check methods.");

			connection = DB_MANAGER.createConnection();
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, accountId);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getDouble(1);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

	@Override
	public Double getBalanceByCustomerId(Integer customerId) {
		System.out.println("customerId : " + customerId);
		String query = "SELECT balance FROM \"bankSchema\".savings_account WHERE customer_id = ?";

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			System.out.println("Inside Balance Check methods.");

			connection = DB_MANAGER.createConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, customerId);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				Double balance = rs.getDouble("balance");
				System.out.println("Balance: " + balance);
				return balance;
			} else {
				System.out.println("No record found for customerId: " + customerId);
			}
		} catch (SQLException e) {
			System.err.println("SQL Exception occurred: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception occurred: " + e.getMessage());
			e.printStackTrace();
		}

		return null;

	}

	@Override
	public Double getMinBalanceByCustomerId(Integer customerId) {
		String query = "SELECT minimum_balance FROM \"bankSchema\".savings_account WHERE customer_id = ?";

		Connection connection = null;

		try {
			System.out.println("Inside Balance Check methods.");

			connection = DB_MANAGER.createConnection();
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, customerId);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getDouble(1);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

	public Double getMinBalanceByAccountId(Integer accountId) {
		String query = "SELECT minimum_balance FROM \"bankSchema\".savings_account WHERE account_id = ?";

		Connection connection = null;

		try {
			System.out.println("Inside Balance Check methods.");

			connection = DB_MANAGER.createConnection();
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, accountId);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getDouble(1);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

	public Integer getAccountIdByCustomerId(Integer customerId) {
		String query = "SELECT account_id FROM \"bankSchema\".savings_account WHERE customer_id = ?";

		Connection connection = null;

		try {
			System.out.println("Inside Balance Check methods.");

			connection = DB_MANAGER.createConnection();
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, customerId);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

	public Integer getAccountIdByAccountNo(String accountNo)
			throws SQLException, ClassNotFoundException, AccountNumberNotExistException {
		if (doesSavingsAccountExist(accountNo)) {
			String query = "SELECT account_id FROM \"bankSchema\".savings_account WHERE account_number=?";
			System.out.println("Inside Account No Check methods.");

			try (Connection conn = DB_MANAGER.createConnection();
					PreparedStatement pstmt = conn.prepareStatement(query)) {
				pstmt.setString(1, accountNo);
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					return rs.getInt(1);
				}

			}
		}

		return null;
	}

	public boolean isValidUser(Integer customerId, String password) throws ClassNotFoundException, SQLException {
		String query = "SELECT COUNT(*) FROM \"bankSchema\".savings_account WHERE customer_id =? AND password=?";
		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {

			pstmt.setInt(1, customerId);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					return true;
				}
			}

		}

		return false;

	}

}
