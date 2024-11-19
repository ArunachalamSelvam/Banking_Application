package com.banking.serviceImpl;


import com.banking.db.DbManager;
import com.banking.entities.Transaction;
import com.banking.exception.AccountNumberNotExistException;
import com.banking.exception.BalanceInsufficientExeception;
import com.banking.exception.InvalidDepositAmountException;
import com.banking.service.TransactionService;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionServiceImpl implements TransactionService {
	
	public static TransactionServiceImpl instance = null;
	
	public final SavingsAccountServiceImpl savingsAccountService = SavingsAccountServiceImpl.getInstance();
	
	public final ATMCardServiceImpl atmCardService = ATMCardServiceImpl.getInstance();
	
	private TransactionServiceImpl() {
		
	}
	
	public static TransactionServiceImpl getInstance() {
		if(instance == null) {
			instance = new TransactionServiceImpl();
		}
		
		return instance;
	}

    private final DbManager DB_MANAGER = DbManager.getInstance();

    @Override
    public List<Transaction> getTransactionHistory(Integer accountId) {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DB_MANAGER.createConnection()) {
            String query = "SELECT * FROM \"bankSchema\".transactions WHERE account_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setSourceAccountId(rs.getInt("source_account_id"));
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setTransactionDate(rs.getDate("transaction_date"));
                transaction.setTransactionTypeId(rs.getInt("transaction_type_id"));
                transaction.setDestinationAccountId(rs.getInt("receiver_account_id"));
                transaction.setDescription(rs.getString("description"));
                transaction.setBalance(rs.getDouble("available_balance"));
                transactions.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    @Override
    public List<Transaction> getTransactionHistoryByCustomerId(Integer customerId, String startDate, String endDate) {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DB_MANAGER.createConnection()) {
        	String query = "SELECT tr.transaction_id AS transactionId,\n"
                    + "    tr.source_account_id AS sourceAccountId,\n"
                    + "    tr.receiver_account_id AS receiverAccountId,\n"
                    + "    tr.amount AS amount,\n"
                    + "    tr.transaction_type_id AS transactionTypeId,\n"
                    + "    tr.description AS description,\n"
                    + "    tr.transaction_date AS transactionDate,\n"
                    + "    tr.available_balance AS availableBalance\n"
                    + "FROM \"bankSchema\".transactions tr\n"
                    + "LEFT JOIN \"bankSchema\".savings_account ac_src \n"
                    + "    ON tr.source_account_id = ac_src.account_id\n"
                    + "LEFT JOIN \"bankSchema\".savings_account ac_rec \n"
                    + "    ON tr.receiver_account_id = ac_rec.account_id\n"
                    + "WHERE (ac_src.customer_id = ?) \n"
                    + "  AND (tr.transaction_date >= ?::timestamp AND tr.transaction_date <= ?::timestamp)\n"
                    + "ORDER BY tr.transaction_date DESC";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, customerId);
            pstmt.setString(2, startDate);
            pstmt.setString(3, endDate);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	 Transaction transaction = new Transaction();
                 transaction.setTransactionId(rs.getInt("transactionId"));
                 transaction.setSourceAccountId(rs.getInt("sourceAccountId"));
                 transaction.setAmount(rs.getDouble("amount"));
                 transaction.setTransactionDate(rs.getTimestamp("transactionDate"));
                 transaction.setTransactionTypeId(rs.getInt("transactionTypeId"));
                 transaction.setDestinationAccountId(rs.getInt("receiverAccountId"));
                 transaction.setDescription(rs.getString("description"));
                 transaction.setBalance(rs.getDouble("availableBalance"));
                 transactions.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    @Override
    public List<Transaction> getTransactionHistoryByCustomerId(Integer customerId) {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DB_MANAGER.createConnection()) {
        	String query = "SELECT tr.transaction_id AS transactionId,\n"
                    + "    tr.source_account_id AS sourceAccountId,\n"
                    + "    tr.receiver_account_id AS receiverAccountId,\n"
                    + "    tr.amount AS amount,\n"
                    + "    tr.transaction_type_id AS transactionTypeId,\n"
                    + "    tr.description AS description,\n"
                    + "    tr.transaction_date AS transactionDate,\n"
                    + "    tr.available_balance AS availableBalance\n"
                    + "FROM \"bankSchema\".transactions tr\n"
                    + "LEFT JOIN \"bankSchema\".savings_account ac_src \n"
                    + "    ON tr.source_account_id = ac_src.account_id\n"
                    + "LEFT JOIN \"bankSchema\".savings_account ac_rec \n"
                    + "    ON tr.receiver_account_id = ac_rec.account_id\n"
                    + "WHERE (ac_src.customer_id = ? OR ac_rec.customer_id = ?) \n"
                    + "ORDER BY tr.transaction_date ASC";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, customerId);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	 Transaction transaction = new Transaction();
                 transaction.setTransactionId(rs.getInt("transactionId"));
                 transaction.setSourceAccountId(rs.getInt("sourceAccountId"));
                 transaction.setAmount(rs.getDouble("amount"));
                 transaction.setTransactionDate(rs.getTimestamp("transactionDate"));
                 transaction.setTransactionTypeId(rs.getInt("transactionTypeId"));
                 transaction.setDestinationAccountId(rs.getInt("receiverAccountId"));
                 transaction.setDescription(rs.getString("description"));
                 transaction.setBalance(rs.getDouble("availableBalance"));
                 transactions.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    
    public List<Transaction> getTransactionHistoryByCustomerId(Integer customerId, Integer lastNDays) {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DB_MANAGER.createConnection()) {
        	String query = "SELECT tr.transaction_id AS transactionId,\n"
                    + "    tr.source_account_id AS sourceAccountId,\n"
                    + "    tr.receiver_account_id AS receiverAccountId,\n"
                    + "    tr.amount AS amount,\n"
                    + "    tr.transaction_type_id AS transactionTypeId,\n"
                    + "    tr.description AS description,\n"
                    + "    tr.transaction_date AS transactionDate,\n"
                    + "    tr.available_balance AS availableBalance\n"
                    + "FROM \"bankSchema\".transactions tr\n"
                    + "LEFT JOIN \"bankSchema\".savings_account ac_src \n"
                    + "    ON tr.source_account_id = ac_src.account_id\n"
                    + "LEFT JOIN \"bankSchema\".savings_account ac_rec \n"
                    + "    ON tr.receiver_account_id = ac_rec.account_id\n"
                    + "WHERE (ac_src.customer_id = ?) \n"
                    + "  AND tr.transaction_date >= CURRENT_DATE - INTERVAL ? * '1 day'::INTERVAL\n"
                    + "ORDER BY tr.transaction_date ASC";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, customerId);
            pstmt.setInt(3, lastNDays);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	 Transaction transaction = new Transaction();
                 transaction.setTransactionId(rs.getInt("transactionId"));
                 transaction.setSourceAccountId(rs.getInt("sourceAccountId"));
                 transaction.setAmount(rs.getDouble("amount"));
                 transaction.setTransactionDate(rs.getTimestamp("transactionDate"));
                 transaction.setTransactionTypeId(rs.getInt("transactionTypeId"));
                 transaction.setDestinationAccountId(rs.getInt("receiverAccountId"));
                 transaction.setDescription(rs.getString("description"));
                 transaction.setBalance(rs.getDouble("availableBalance"));
                 transactions.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }


  
//    public boolean addTransaction(Transaction transaction) {
//        boolean isAdded = false;
//        try (Connection conn = DB_MANAGER.createConnection()) {
//            String query = "INSERT INTO \"bankSchema\".transactions (source_account_id, amount, transaction_date, transaction_type_id, receiver_account_id) VALUES (?, ?, ?, ?,?)";
//            PreparedStatement pstmt = conn.prepareStatement(query);
//            pstmt.setInt(1, transaction.getSourceAccountId());
//            pstmt.setDouble(2, transaction.getAmount());
//            pstmt.setDate(3, new java.sql.Date(transaction.getTransactionDate().getTime()));
//            pstmt.setInt(4, transaction.getTransactionTypeId());
//            pstmt.setInt(4, transaction.getDestinationAccountId());
//            int rowsAffected = pstmt.executeUpdate();
//            isAdded = rowsAffected > 0;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return isAdded;
//    }
    
    @Override
    public boolean deposit(String accountNo, Double amount) throws SQLException, ClassNotFoundException, AccountNumberNotExistException, InvalidDepositAmountException {
    	Integer accountId = savingsAccountService.getAccountIdByAccountNo(accountNo);
    	
    	if(accountId!=null && amount>0) {
    		String description = amount+" deposited.";
    		return updateBalanceAndRecordTransaction(accountId ,amount, 1, description);
    	}else {
    		throw new InvalidDepositAmountException("Deposit amount should be greater than zero.");
//    		return false;
    	}
    	
    	
    }

    public boolean depositByAtmCard(Integer accountId, Double amount) throws SQLException, ClassNotFoundException, AccountNumberNotExistException {
    	
    	if(accountId!=null) {
    		String description = amount+" deposited by atm Card.";
    		return updateBalanceAndRecordTransaction(accountId ,amount, 1, description);
    	}
    	
    	return false;
    }
    
    public boolean depositByAtmCard(String cardNo, String cvv, String expiryDate, String cardHolderName, double amount, String pin) {
    	System.out.println(cardNo+","+ cvv+","+ expiryDate+","+cardHolderName+","+amount);
    	try {
			if(atmCardService.isAtmCardExists(cardNo, cvv, expiryDate, cardHolderName,pin)) {
				System.out.println("Card available");
				Integer accountId = atmCardService.getAccountIdByAtmCardNo(cardNo);
				if(accountId!=null) {
					String description = amount+" deposited by atm Card.";
					try {
						return updateBalanceAndRecordTransaction(accountId ,amount, 1, description);
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false; 
    }
    
    
    public boolean withdrawByAtmCard(Integer accountId, Double amount) throws SQLException, ClassNotFoundException, AccountNumberNotExistException, BalanceInsufficientExeception {

    	Double balance = savingsAccountService.getBalance(accountId);
    	Double minBalance = savingsAccountService.getMinBalanceByAccountId(accountId);
    	

    	
    	if(accountId!=null) { 
    		if( balance>minBalance ) {
    			
    			String description = amount+" withdrawn by atm.";
        		return updateBalanceAndRecordTransaction(accountId, -amount, 2, description);
        		
    		}
    		else {
        		throw new BalanceInsufficientExeception("Balance Insufficient.");
        	}
    		
    	}
    	return false;
    }
    
    
    @Override
    public boolean withdraw(String accountNo, Double amount) throws SQLException, ClassNotFoundException, AccountNumberNotExistException {

    	Integer accountId = savingsAccountService.getAccountIdByAccountNo(accountNo);
    	
    	if(accountId!=null) {
    		String description = amount+" withdrawn.";
    		return updateBalanceAndRecordTransaction(accountId, -amount, 2, description);
    	}
    	return false;
    }
    
    @Override
    public boolean deposit(Integer customerId, Double amount) throws SQLException, ClassNotFoundException, AccountNumberNotExistException {
    	Integer accountId = savingsAccountService.getAccountIdByCustomerId(customerId);
    	
    	if(accountId!=null) {
    		String description = amount+" deposited.";
    		return updateBalanceAndRecordTransaction(accountId ,amount, 1, description);
    	}
    	
    	return false;
    }

    @Override
    public boolean withdraw(Integer customerId, Double amount) throws SQLException, ClassNotFoundException, AccountNumberNotExistException, BalanceInsufficientExeception {

    	Integer accountId = savingsAccountService.getAccountIdByCustomerId(customerId);
    	Double balance = savingsAccountService.getBalance(accountId);
    	Double minBalance = savingsAccountService.getMinBalanceByCustomerId(customerId);
    	

    	
    	if(accountId!=null) { 
    		if( balance>minBalance ) {
    			
    			String description = amount+" withdrawn.";
        		return updateBalanceAndRecordTransaction(accountId, -amount, 2, description);
        		
    		}
    		else {
        		throw new BalanceInsufficientExeception("Balance Insufficient.");
        	}
    		
    	}
    	return false;
    }
    
    
    public boolean transactToReceiverMobileNumber(Integer customerId, String receiverMobileNo, Double amount) throws SQLException, ClassNotFoundException, AccountNumberNotExistException, BalanceInsufficientExeception {
    	

    	Double balance = savingsAccountService.getBalanceByCustomerId(customerId);
    	Double minBalance = savingsAccountService.getMinBalanceByCustomerId(customerId);
    	
		if( balance>minBalance ) {

    	
    	String sourceQuery = "SELECT ac.account_number AS accountNo\n"
    			+ "FROM\n"
    			+ "	\"bankSchema\".customer c\n"
    			+ "JOIN \n"
    			+ "	\"bankSchema\".savings_account ac ON ac.customer_id = c.customer_id\n"
    			+ "WHERE\n"
    			+ "	c.customer_id = ?";
    	
    	String receiverQuery = "SELECT ac.account_number AS accountNo\n"
    			+ "FROM\n"
    			+ "	\"bankSchema\".customer c\n"
    			+ "JOIN \n"
    			+ "	\"bankSchema\".savings_account ac ON ac.customer_id = c.customer_id\n"
    			+ "WHERE\n"
    			+ "	c.mobile_number = ?";
    	
        try (Connection connection = DB_MANAGER.createConnection();
        		PreparedStatement sourcePstmt = connection.prepareStatement(sourceQuery);
        		PreparedStatement receiverPstmt = connection.prepareStatement(receiverQuery)) {
        	
        	sourcePstmt.setInt(1,customerId);
        	
        	ResultSet rs = sourcePstmt.executeQuery();
        	
        	receiverPstmt.setString(1,receiverMobileNo);
        	
        	ResultSet receiverRs = sourcePstmt.executeQuery();
        	
        	
        	
        	if(rs.next() && receiverRs.next()) {
        		
        		String sourceAccountNo = rs.getString("accountNo");
        		String receiverAccountNo = receiverRs.getString("accountNo");
        		return transaction(sourceAccountNo, receiverAccountNo, amount);
        	}
        	
        }	
        
		}else {
    		throw new BalanceInsufficientExeception("Balance Insufficient.");

		}
        
        return false;
    	
    }
    @Override
    public boolean transactionToReceiverAccountNoByCustomerId(Integer customerId, String receiverAccountNo, Double amount) throws SQLException, ClassNotFoundException, AccountNumberNotExistException, BalanceInsufficientExeception {
    	
    	Double balance = savingsAccountService.getBalanceByCustomerId(customerId);
    	Double minBalance = savingsAccountService.getMinBalanceByCustomerId(customerId);
    	
    	
		if( amount< balance &&balance-amount > minBalance && balance>minBalance  ) {
    	
    	String query = "SELECT ac.account_number AS accountNo\n"
    			+ "FROM\n"
    			+ "	\"bankSchema\".customer c\n"
    			+ "JOIN \n"
    			+ "	\"bankSchema\".savings_account ac ON ac.customer_id = c.customer_id\n"
    			+ "WHERE\n"
    			+ "	c.customer_id = ?";
    	
        try (Connection connection = DB_MANAGER.createConnection();
        		PreparedStatement pstmt = connection.prepareStatement(query)) {
        	
        	pstmt.setInt(1,customerId);
        	
        	ResultSet rs = pstmt.executeQuery();
        	
        	if(rs.next()) {
        		String sourceAccountNo = rs.getString("accountNo");
        		return transaction(sourceAccountNo, receiverAccountNo, amount);
        	}
        	
        }
        
		}
        else {
    		throw new BalanceInsufficientExeception("Balance Insufficient.");

		}
		
        
        return false;
    	
    }
    
    
    @Override
    public boolean transaction(String sourceAccountNo, String receiverAccountNo, Double amount) throws SQLException, ClassNotFoundException, AccountNumberNotExistException {
    	System.out.println("Inside transaction method");
        Integer sourceAccountId = savingsAccountService.getAccountIdByAccountNo(sourceAccountNo);
        Integer receiverAccountId = savingsAccountService.getAccountIdByAccountNo(receiverAccountNo);

        String updateBalanceQuery = "UPDATE \"bankSchema\".savings_account SET balance = balance - ? WHERE account_id = ?";
        String updateDestinationAcBalanceQuery = "UPDATE \"bankSchema\".savings_account SET balance = balance + ? WHERE account_id = ?";

        try (Connection connection = DB_MANAGER.createConnection()) {
            connection.setAutoCommit(false);
            
            // Generate a unique transaction_group_id
            int transactionGroupId = generateUniqueTransactionGroupId();


            try (PreparedStatement updateBalanceStmt = connection.prepareStatement(updateBalanceQuery);
                 PreparedStatement updateDestinationAcBalanceStmt = connection.prepareStatement(updateDestinationAcBalanceQuery)) {

                // Update sender's balance
                updateBalanceStmt.setDouble(1, amount);
                updateBalanceStmt.setInt(2, sourceAccountId);
                int balanceUpdated = updateBalanceStmt.executeUpdate();

                // Update receiver's balance
                updateDestinationAcBalanceStmt.setDouble(1, amount);
                updateDestinationAcBalanceStmt.setInt(2, receiverAccountId);
                int destinationAcBalanceUpdated = updateDestinationAcBalanceStmt.executeUpdate();
                
                

                if (balanceUpdated > 0 && destinationAcBalanceUpdated > 0) {
                    // Record transactions for sender and receiver
                    boolean senderTransaction = addTransaction(connection, sourceAccountId, receiverAccountId, -amount, "Transfer to Account " + receiverAccountNo, transactionGroupId);
                    boolean receiverTransaction = addTransaction(connection, receiverAccountId, sourceAccountId, amount, "Received from Account " + sourceAccountNo, transactionGroupId);

                    if (senderTransaction && receiverTransaction) {
                        connection.commit();
                        return true;
                    }
                }

                connection.rollback(); // Rollback if any operation fails
            } catch (SQLException e) {
                connection.rollback(); // Rollback on exception
                throw e;
            }
        }

        return false;
    }
   
    private boolean addTransaction(Connection connection, Integer sourceAccountId, Integer receiverAccountId, Double amount, String description, int transactionGroupId) throws SQLException {
        String insertTransactionQuery = "INSERT INTO \"bankSchema\".transactions(" +
                                        "source_account_id, amount, transaction_date, transaction_type_id, " +
                                        "receiver_account_id, description, available_balance, transaction_group_id) " +
                                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertTransactionStmt = connection.prepareStatement(insertTransactionQuery)) {
            Double balance = savingsAccountService.getBalance(sourceAccountId);

            // Determine transaction type (1 for Debit, 2 for Credit)
            int transactionTypeId = amount < 0 ? 1 : 2;

            // Record transaction
            insertTransactionStmt.setInt(1, sourceAccountId);
            insertTransactionStmt.setDouble(2, amount);
            insertTransactionStmt.setTimestamp(3, new Timestamp(new Date().getTime()));
            insertTransactionStmt.setInt(4, transactionTypeId);
            insertTransactionStmt.setInt(5, receiverAccountId);
            insertTransactionStmt.setString(6, description);
            insertTransactionStmt.setDouble(7, balance + amount); // Adjust balance after transaction
            insertTransactionStmt.setInt(8, transactionGroupId); // Set the transaction group ID


            int transactionRecorded = insertTransactionStmt.executeUpdate();
            return transactionRecorded > 0;
        }
    }

    
//    @Override
//    public boolean transaction(String sourceAccountNo,String receiverAccountNo, Double amount) throws SQLException, ClassNotFoundException, AccountNumberNotExistException {
//    	
//    	Integer sourceAccountId = savingsAccountService.getAccountIdByAccountNo(sourceAccountNo);
////    	Integer transactionTypeId = 3;
//    	Integer receiverAccountId = savingsAccountService.getAccountIdByAccountNo(receiverAccountNo);
//    	
//        String updateBalanceQuery = "UPDATE \"bankSchema\".savings_account SET balance = balance - ? WHERE account_id = ?";
//        
//        String updateDestinationAcBalanceQuery = "UPDATE \"bankSchema\".savings_account SET balance = balance + ? WHERE account_id = ?";
//
//       
//    	
////    	String insertTransactionQuery = "INSERT INTO \"bankSchema\".transactions(\n"
////    			+ "	source_account_id, amount, transaction_date,transaction_type_id, receiver_account_id, available_balance)\n"
////    			+ "	VALUES (?, ?, ?, ?, ?, ?)";
//    	
//    	
//    	 try (Connection connection = DB_MANAGER.createConnection()) {
//             connection.setAutoCommit(false);
//
//             try (PreparedStatement updateBalanceStmt = connection.prepareStatement(updateBalanceQuery);
//            	  PreparedStatement updateDestinationAcBalanceStmt = connection.prepareStatement(updateDestinationAcBalanceQuery)){
////                  PreparedStatement insertTransactionStmt = connection.prepareStatement(insertTransactionQuery)) {
////            	 
////            	 Double balance = savingsAccountService.getBalance(accountId);
//
//
//                 // Update balance
//                 updateBalanceStmt.setDouble(1, amount);
//                 updateBalanceStmt.setInt(2, sourceAccountId);
//                 int balanceUpdated = updateBalanceStmt.executeUpdate();
//                 
//                 updateDestinationAcBalanceStmt.setDouble(1, amount);
//                 updateDestinationAcBalanceStmt.setInt(2, receiverAccountId);
//                 int destinationAcBalanceUpdated = updateDestinationAcBalanceStmt.executeUpdate();
//
//                 if (balanceUpdated > 0 && destinationAcBalanceUpdated>0) {
//                	 
//                 	
////                     // Record transaction
////                     insertTransactionStmt.setInt(1, accountId);
////                     insertTransactionStmt.setDouble(2, amount);
////                     insertTransactionStmt.setTimestamp(3, new Timestamp(new Date().getTime()));
////                     insertTransactionStmt.setInt(4, transactionTypeId);
////                     insertTransactionStmt.setInt(5, destinationAccountId);
////                     insertTransactionStmt.setDouble(6,balance-amount);
////
////                     int transactionRecorded = insertTransactionStmt.executeUpdate();
////
////                     if (transactionRecorded > 0) {
////                         connection.commit();
////                         return true;
////                     }
//                	 
//                	 return addTransaction(connection, sourceAccountId, receiverAccountId, -amount) && addTransaction(connection,receiverAccountId , sourceAccountId, amount);
//                 }
//             } catch (SQLException e) {
//                 connection.rollback();
//                 throw e;
//             }
//         }
//
//         return false;
//
//    	
//    	
//    	
//    }
//    
//    private boolean addTransaction(Connection connection, Integer sourceAccountId, Integer receiverAccountId, Double amount) throws ClassNotFoundException, SQLException, AccountNumberNotExistException {
//    	
//
////    	Integer accountId = savingsAccountService.getAccountIdByAccountNo(sourceAccountNo);
//    	Integer transactionTypeId = 3;
////    	Integer destinationAccountId = savingsAccountService.getAccountIdByAccountNo(receiverAccountNo);
//    	
//    	String insertTransactionQuery = "INSERT INTO \"bankSchema\".transactions(\n"
//    			+ "	source_account_id, amount, transaction_date,transaction_type_id, receiver_account_id, description, available_balance)\n"
//    			+ "	VALUES (?, ?, ?, ?, ?, ?, ?)";
//    	
//    	
//    	 try {
//             connection.setAutoCommit(false);
//
//             try (PreparedStatement insertTransactionStmt = connection.prepareStatement(insertTransactionQuery)) {
//            	 
//            	 Double balance = savingsAccountService.getBalance(sourceAccountId);
//
//
//                 	
//                     // Record transaction
//                     insertTransactionStmt.setInt(1, sourceAccountId);
//                     insertTransactionStmt.setDouble(2, amount);
//                     insertTransactionStmt.setTimestamp(3, new Timestamp(new Date().getTime()));
//                     insertTransactionStmt.setInt(4, transactionTypeId);
//                     insertTransactionStmt.setInt(5, receiverAccountId);
//                     insertTransactionStmt.setString(6, "Transfer to Account " + receiverAccountId);
//                     insertTransactionStmt.setDouble(7,balance+amount);
//
//                     int transactionRecorded = insertTransactionStmt.executeUpdate();
//
//                     if (transactionRecorded > 0) {
//                         connection.commit();
//                         return true;
//                     }
//                 
//             } catch (SQLException e) {
//                 connection.rollback();
//                 throw e;
//             }
//         
//    	}catch(Exception e){
//    		e.printStackTrace();
//    	}
//         return false;
//
//
//    	
//    }

    private boolean updateBalanceAndRecordTransaction(Integer accountId, Double amount, Integer transactionTypeId, String description) throws SQLException, ClassNotFoundException {
        String updateBalanceQuery = "UPDATE \"bankSchema\".savings_account SET balance = balance + ? WHERE account_id = ?";
        String insertTransactionQuery = "INSERT INTO \"bankSchema\".transactions (source_account_id, amount, transaction_date, transaction_type_id,available_balance,description) VALUES (?, ?, ?, ?,?,?)";

        try (Connection connection = DB_MANAGER.createConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement updateBalanceStmt = connection.prepareStatement(updateBalanceQuery);
                 PreparedStatement insertTransactionStmt = connection.prepareStatement(insertTransactionQuery)) {
            	
            	Double balance = savingsAccountService.getBalance(accountId);
                // Update balance
                updateBalanceStmt.setDouble(1, amount);
                updateBalanceStmt.setInt(2, accountId);
                int balanceUpdated = updateBalanceStmt.executeUpdate();
                

                if (balanceUpdated > 0) {
                	
                	

                    // Record transaction
                    insertTransactionStmt.setInt(1, accountId);
                    insertTransactionStmt.setDouble(2, amount);
                    insertTransactionStmt.setTimestamp(3, new Timestamp(new Date().getTime()));
                    insertTransactionStmt.setInt(4, transactionTypeId);
                    insertTransactionStmt.setDouble(5, balance+amount);
                    insertTransactionStmt.setString(6, description);
                    

                    int transactionRecorded = insertTransactionStmt.executeUpdate();

                    if (transactionRecorded > 0) {
                        connection.commit();
                        return true;
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }

        return false;
    }
    
    private int generateUniqueTransactionGroupId() {
        return UUID.randomUUID().hashCode();
    }

}
