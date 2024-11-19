package com.banking.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

import com.banking.entities.ATMCard;
import com.banking.requestEntities.AtmCardTransactionRequest;
import com.banking.service.ATMCardService;
import com.banking.service.CustomerService;
import com.banking.serviceImpl.ATMCardServiceImpl;
import com.banking.serviceImpl.CustomerServiceImpl;
import com.banking.serviceImpl.TransactionServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class AtmCardServlet
 */

@WebServlet("/AtmCardServlet")
public class AtmCardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final ATMCardService atmCardService = ATMCardServiceImpl.getInstance();
	private static final CustomerService customerService = CustomerServiceImpl.getInstance();
	private static final TransactionServiceImpl transactionService = TransactionServiceImpl.getInstance();
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Save ATM Card
		System.out.println("Inside Atm Deposit method.");

		HttpSession session = request.getSession(false);
		int customerId = Integer.parseInt(session.getAttribute("customer_id").toString());

		if (session != null) {
			String action = (String) request.getParameter("action");

			switch (action) {
			case "deposit":
				try {
					boolean isFreeze = atmCardService.getAtmCardFreezeStatus(customerId);
					boolean isSleep = atmCardService.getAtmCardSleepStatus(customerId);
					boolean isBlock = atmCardService.getAtmCardBlockStatus(customerId);
					
					int currentHour = LocalTime.now().getHour();
					
					if(isBlock == false) {
					
						//Check for is Card in freeze mode or not . if isFreeze == true card is in freeze mode
						if(isFreeze == false) {
							
							//Check for is Card in sleep mode or not . if isSleep == true card is in sleep mode
							if (isSleep == false || currentHour < 18 || currentHour >= 19) {
									AtmCardTransactionRequest atmCardTransactionRequest = objectMapper
											.readValue(request.getInputStream(), AtmCardTransactionRequest.class);
				
									String cardNo = atmCardTransactionRequest.getCardNo();
									String cvv = atmCardTransactionRequest.getCvv();
									String expiryDate = atmCardTransactionRequest.getExpiryDate();
									String cardHolderName = atmCardTransactionRequest.getCardHolderName();
									double amount = atmCardTransactionRequest.getAmount();
									String pin = atmCardTransactionRequest.getPin();
									
									
									
									try {
				
										boolean isSuccess = transactionService.depositByAtmCard(cardNo, cvv, expiryDate, cardHolderName,
												amount, pin);
										response.setContentType("application/json");
										response.setCharacterEncoding("UTF-8");
				
										if (isSuccess) {
											// If deposit is successful, send a success response
											System.out.println("Deposit SuccessFul");
				//	                             response.setStatus(HttpServletResponse.SC_OK);
											response.getWriter().write("{\"message\": \"Deposit successful.\",\"success\": \"true\"}");
										} else {
											// If deposit fails, send an error response
											System.out.println("Deposit Failed/ Invalid credentials.");
				
											response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
											response.getWriter().write("{\"message\": \"Deposit failed/Invalid credentials.\"}");
										}
				
									} catch (Exception e) {
										// Handle any exceptions that occur during the deposit process
										response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
										response.getWriter().write("{\"error\": \"An error occurred while processing the deposit.\"}");
										e.printStackTrace(); // Log the error for debugging purposes
									}
									
							}else {
								response.getWriter().write("{\"message\": \"Your Card is in sleep mode. You can not do transaction.\",\"success\": \"false\"}");

							}
						}else {
							response.getWriter().write("{\"message\": \"Your Card is freeze. You can not do transaction.\",\"success\": \"false\"}");
	
						}
					}else {
						response.getWriter().write("{\"message\": \"Your Card is blocked. You can not do transaction.\",\"success\": \"false\"}");

					}
					
				} catch (Exception e) {
					// Handle any exceptions that occur while parsing the request or other
					// operations
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("{\"error\": \"Invalid request data.\"}");
					e.printStackTrace(); // Log the error for debugging purposes
				}

				break;

			case "requestAtmCard": {
				String pin = request.getParameter("pin");
//				int customerId = Integer.parseInt(session.getAttribute("customerId").toString());
				ATMCard atmCard = null;
				try {
					atmCard = atmCardService.getAtmCardWithCustomerNameAndAccountIdByCustomerId(customerId);
					atmCard.setCardLimit(50000.00); // Default card limit
					atmCard.setCardTypeId(1); // Default card type ID
//						atmCard.setPinHash(bcryptUtil.hashPin(savingsAccountWithCustomer.getAtmPin()));
					atmCard.setPinHash(pin);
					ATMCard savedAtmCard = atmCardService.saveATMCard(atmCard);
					if (savedAtmCard != null) {
						boolean isStatusUpdate = customerService.updateAtmCardAddedStatus(customerId, true);
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");

						if (isStatusUpdate) {
							// If deposit is successful, send a success response
							System.out.println("AtmCard Created SuccessFully");
//	                             response.setStatus(HttpServletResponse.SC_OK);
							response.getWriter()
									.write("{\"message\": \"AtmCard Created SuccessFully.\",\"success\": \"true\"}");
						} else {
							// If deposit fails, send an error response
							System.out.println("AtmCard Created Failed");

							response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							response.getWriter().write("{\"error\": \"AtmCard failed.\"}");
						}

					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			case "resetPin": {

			}
			}
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"error\": \"Invalid request data.\"}");

		}

//	        try {
//	            ATMCard atmCard = objectMapper.readValue(request.getInputStream(), ATMCard.class);
//	            ATMCard savedCard = atmCardService.saveATMCard(atmCard);
//	            response.setContentType("application/json");
//	            response.setStatus(HttpServletResponse.SC_OK);
//	            objectMapper.writeValue(response.getOutputStream(), savedCard);
//	        } catch (Exception e) {
//	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//	            response.getWriter().write("{\"error\": \"An error occurred while saving the ATM card.\"}");
//	        }
//	        
	}

//	    @Override
//	    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
//	            throws ServletException, IOException {
//	        // Update ATM Card
//	        try {
//	            int cardId = Integer.parseInt(request.getParameter("cardId"));
//	            ATMCard atmCard = objectMapper.readValue(request.getInputStream(), ATMCard.class);
//	            ATMCard updatedCard = atmCardService.updateATMCard(cardId, atmCard);
//	            response.setContentType("application/json");
//	            response.setStatus(HttpServletResponse.SC_OK);
//	            objectMapper.writeValue(response.getOutputStream(), updatedCard);
//	        } catch (Exception e) {
//	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//	            response.getWriter().write("{\"error\": \"An error occurred while updating the ATM card.\"}");
//	        }
//	    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		String action = request.getParameter("action");
		Integer customerId = Integer.parseInt(session.getAttribute("customer_id").toString());
		// Get ATM Card by ID or Get All ATM Cards
//	        String cardIdParam = request.getParameter("cardId");
		if (customerId != null) {

			if ("getCard".equals(action)) {
				try {
					// int cardId = Integer.parseInt(cardIdParam);
					ATMCard atmCard = atmCardService.getATMCardById(customerId);
					if (atmCard != null) {
						response.setContentType("application/json");
						response.setStatus(HttpServletResponse.SC_OK);
						objectMapper.writeValue(response.getOutputStream(), atmCard);
					} else {
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);

						response.getWriter().write("{\"error\": \"ATM card not found.\"}");

					}
				} catch (Exception e) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().write("{\"error\": \"An error occurred while retrieving the ATM card.\"}");
				}
			} else if (action.equals("getCvv")) {
				try {
					// Call your service method to get the CVV for the customer
					String cvv = atmCardService.getCVV(customerId);

					if (cvv != null) {
						// Prepare JSON response
						ObjectMapper objectMapper = new ObjectMapper();
						String jsonResponse = objectMapper.writeValueAsString(cvv);

						// Write the JSON response
						response.getWriter().write("{\"cvv\": \"" + cvv + "\"}");
					} else {
						// No CVV found for the customer
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						response.getWriter().write("{\"error\": \"No CVV found for the given Customer ID\"}");
					}

				} catch (SQLException | ClassNotFoundException e) {
					e.printStackTrace();
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().write("{\"error\": \"Unable to fetch CVV. Please try again later.\"}");
				}
			}
			
			else if(action.equals("getFreezeStatus")) {
				
					try {
						boolean isFreeze = atmCardService.getAtmCardFreezeStatus(customerId);
						response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
						response.getWriter().write("{\"isFreeze\": " + isFreeze + "}");
						
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						response.setContentType("application/json");
		                response.setCharacterEncoding("UTF-8");
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.getWriter().write("{\"error\": \"Unable to get freeze status. Please try again later.\"}");
					}
				
			}
			
			else if(action.equals("getSleepStatus")) {
				
				try {
					boolean isSleep = atmCardService.getAtmCardSleepStatus(customerId);
					response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
					response.getWriter().write("{\"isSleep\": " + isSleep + "}");
					
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().write("{\"error\": \"Unable to get sleep status. Please try again later.\"}");
				}
			
		}

		}
//	        else {
//	            try {
//	                List<ATMCard> atmCards = atmCardService.getAllATMCard();
//	                response.setContentType("application/json");
//	                response.setStatus(HttpServletResponse.SC_OK);
//	                objectMapper.writeValue(response.getOutputStream(), atmCards);
//	            } catch (Exception e) {
//	                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//	                response.getWriter().write("{\"error\": \"An error occurred while retrieving the ATM cards.\"}");
//	            }
//	        }

	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Delete ATM Card
		try {
			int cardId = Integer.parseInt(request.getParameter("cardId"));
			int result = atmCardService.deleteATMCard(cardId);
			if (result > 0) {
				response.setContentType("application/json");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"message\": \"ATM card deleted successfully.\"}");
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("{\"error\": \"ATM card not found.\"}");
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"error\": \"An error occurred while deleting the ATM card.\"}");
		}
	}

	protected void doPatch(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Block ATM Card
		try {
			int cardId = Integer.parseInt(request.getParameter("cardId"));
			boolean status = Boolean.parseBoolean(request.getParameter("status"));
			int result = atmCardService.changeBlockStatusOfTheAtmCard(cardId, status);
			if (result > 0) {
				response.setContentType("application/json");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"message\": \"ATM card blocked successfully.\"}");
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("{\"error\": \"ATM card not found.\"}");
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"error\": \"An error occurred while blocking the ATM card.\"}");
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		
		JsonNode jsonNode = objectMapper.readTree(request.getInputStream());

		if ("resetPin".equals(action)) {
			System.out.println("inside pin update servlet");
			String oldPin = jsonNode.get("oldPin").asText();
			String newPin = jsonNode.get("newPin").asText();
			// Reset PIN
			try {
				HttpSession session = request.getSession(false);
				Integer customerId = Integer.parseInt(session.getAttribute("customer_id").toString());
//	                String pin = request.getParameter("pin");
//	                String oldPin = request.getParameter("oldPin");
//	                boolean isOldPinvalid = atmCardService.verifyPin(customerId, newPin);

				boolean result = atmCardService.resetPin(customerId, oldPin, newPin);
				if (result) {
					response.setContentType("application/json");
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().write("{\"success\": \"true\",\"message\": \"PIN reset successfully.\"}");
				} else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().write("{\"error\": \"Failed to reset PIN.\"}");
				}
			} catch (SQLException | ClassNotFoundException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("{\"error\": \"An error occurred while resetting the PIN.\"}");
			}
		} 
		
		else if ("blockStatus".equals(action)) {

			boolean status = true;
			HttpSession session = request.getSession(false);
			Integer customerId = Integer.parseInt(session.getAttribute("customer_id").toString());

			try {
				boolean isFreeze = atmCardService.getAtmCardBlockStatus(customerId);
				System.out.println("Freeze status : " + isFreeze);
				
				if(isFreeze) {
					status = false;
					int updatedRows = atmCardService.changeBlockStatusOfTheAtmCard(customerId, status);

					if (updatedRows > 0) {
						response.setContentType("application/json");
//						response.setStatus(HttpServletResponse.SC_OK);
						response.getWriter()
								.write("{\"success\": \"false\",\"message\": \"Resumed all transactions.\"}");
					} else {
//						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.getWriter().write("{\"error\": \"Failed to change freeze status.\"}");
					}
				}else {

					int updatedRows = atmCardService.changeBlockStatusOfTheAtmCard(customerId, status);

					if (updatedRows > 0) {
						response.setContentType("application/json");
//						response.setStatus(HttpServletResponse.SC_OK);
						response.getWriter()
								.write("{\"success\": \"true\",\"message\": \"Card is Blocked. Paused all the transactions.\"}");
					} else {
//						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.getWriter().write("{\"error\": \"Failed to change freeze status.\"}");
					}
				}
				
			} catch (SQLException | ClassNotFoundException e) {
//				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("{\"error\": \"An error occurred while changing the freeze status.\"}");
			}
		}
		
		else if ("freezeStatus".equals(action)) {

			boolean status = true;
			HttpSession session = request.getSession(false);
			Integer customerId = Integer.parseInt(session.getAttribute("customer_id").toString());

			try {
				boolean isFreeze = atmCardService.getAtmCardFreezeStatus(customerId);
				System.out.println("Freeze status : " + isFreeze);
				
				if(isFreeze) {
					status = false;
					int updatedRows = atmCardService.changeFreezeStatusOfAtmCard(customerId, status);

					if (updatedRows > 0) {
						response.setContentType("application/json");
//						response.setStatus(HttpServletResponse.SC_OK);
						response.getWriter()
								.write("{\"success\": \"false\",\"message\": \"Resumed all transactions.\"}");
					} else {
//						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.getWriter().write("{\"error\": \"Failed to change freeze status.\"}");
					}
				}else {

					int updatedRows = atmCardService.changeFreezeStatusOfAtmCard(customerId, status);

					if (updatedRows > 0) {
						response.setContentType("application/json");
//						response.setStatus(HttpServletResponse.SC_OK);
						response.getWriter()
								.write("{\"success\": \"true\",\"message\": \"Paused all the transactions.\"}");
					} else {
//						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.getWriter().write("{\"error\": \"Failed to change freeze status.\"}");
					}
				}
				
			} catch (SQLException | ClassNotFoundException e) {
//				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("{\"error\": \"An error occurred while changing the freeze status.\"}");
			}
		} else if(action.equals("sleepStatus")) {
			boolean status = true;
			HttpSession session = request.getSession(false);
			Integer customerId = Integer.parseInt(session.getAttribute("customer_id").toString());

			
			try {
				boolean isSleep = atmCardService.getAtmCardSleepStatus(customerId);
				
				if(isSleep) {
					status = false;
					int updatedRows = atmCardService.changeSleepStatusOfAtmCard(customerId, status);
					
					if(updatedRows>0) {
						response.setContentType("application/json");
						response.getWriter().write("{\"success\": \"false\", \"message\":\"Resume all transaction from sleep mode.\"}");
					}else {
						response.getWriter().write("{\"error\": \"Failed to change sleep status.\"}");

					}
				} else {

					int updatedRows = atmCardService.changeSleepStatusOfAtmCard(customerId, status);

					if (updatedRows > 0) {
						response.setContentType("application/json");
//						response.setStatus(HttpServletResponse.SC_OK);
						response.getWriter()
								.write("{\"success\": \"true\",\"message\": \"Paused all the transactions between 12AM to 6AM\"}");
					} else {
//						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.getWriter().write("{\"error\": \"Failed to change sleep status.\"}");
					}
				}
			} catch (SQLException | ClassNotFoundException e) {
//				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("{\"error\": \"An error occurred while changing the sleep status.\"}");
			}
		}

		else {

			super.doPut(request, response);
		}
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AtmCardServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

}
