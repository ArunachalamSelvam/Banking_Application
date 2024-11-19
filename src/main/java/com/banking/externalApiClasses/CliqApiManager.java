package com.banking.externalApiClasses;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import com.banking.exception.InvalidEmailException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CliqApiManager {
	
	private static final String OAUTH_URL = "https://accounts.zoho.com/oauth/v2/token?grant_type=refresh_token"
            + "&scope=ZohoCliq.Webhooks.CREATE"
            + "&client_id=1000.S94KKZEDIZ3WKGIBSX3SS5YJPU3MEM"
            + "&client_secret=1bb61c8a937970c23598ab4d504bd8ce741ffa309d"
            + "&redirect_uri=http://localhost:8080"
            + "&refresh_token=1000.a8a9591f151efb11d35f1434a3e4eb6f.3a9ce2843cb6c98aa77f2ed64836aee1";
	
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    private static CliqApiManager instance = null;
    
    private CliqApiManager() {
		// TODO Auto-generated constructor stub
	}
    
    public static CliqApiManager getInstance() {
    	if(instance == null) {
    		instance = new CliqApiManager();
  
    	}
    	return instance;
    }
	
	public String getAccessToken() throws org.apache.hc.core5.http.ParseException {
		String accessToken = null;
		
		 CloseableHttpClient httpClient = null;
	        ClassicHttpResponse httpResponse = null;

	        try {
	            httpClient = HttpClients.createDefault();
	            HttpPost postRequest = new HttpPost(OAUTH_URL);

	            httpResponse = (ClassicHttpResponse) httpClient.execute(postRequest);

	            int responseCode = httpResponse.getCode();
	            if (responseCode == 200) {
	                String responseBody = EntityUtils.toString(httpResponse.getEntity());

	                // Extract the access token from the response (assuming it’s in JSON)
	                try {
	                    JsonNode accessTokenNode = OBJECT_MAPPER.readTree(responseBody);
	                    if (accessTokenNode != null) {
	                        System.out.println("access token full : " + accessTokenNode.toString());
	                        
	                        // Extract the access_token field
	                        JsonNode tokenNode = accessTokenNode.get("access_token");
	                        if (tokenNode != null) {
	                            accessToken = tokenNode.asText(); // Get the token as text
	                            System.out.println("access token : " + accessToken);
	                            return accessToken; // Return the access token
	                        } else {
	                            System.out.println("Access token not found in the response");
	                            throw new RuntimeException("Access token not found in the response");
	                        }
	                    } else {
	                        System.out.println("Invalid JSON response.");
	                        throw new RuntimeException("Invalid JSON response.");
	                    }
//	                    accessToken = jsonNode.get("access_token").asText();  // Extract the access token
	                } catch (Exception e) {
	                    System.out.println("Error parsing JSON response: " + e.getMessage());
	                    e.printStackTrace();
	                }
	            } else {
	                System.out.println("Failed to get access token. Response Code: " + responseCode);
	            }
	        } catch (IOException e) {
	            System.out.println("Error executing HTTP request: " + e.getMessage());
	            e.printStackTrace();
	        } finally {
	            try {
	                if (httpResponse != null) {
	                    httpResponse.close();
	                }
	                if (httpClient != null) {
	                    httpClient.close();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

        return accessToken;

	}
	
	
	public boolean sendMessageToZohoCliq(String userMail, String message) throws IOException, org.apache.hc.core5.http.ParseException, InvalidEmailException {
	    // Encode the message to handle special characters
	    String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
		System.out.println("Inside message sending.. " +userMail);
		
		String accessToken = getAccessToken();
		
	    // Construct the URL with the encoded message
	    String url = "https://cliq.zoho.com/api/v2/buddies/" + userMail + "/message";

	    CloseableHttpClient httpClient = HttpClients.createDefault();
	    HttpPost post = new HttpPost(url);
	    
	    // Set the Authorization header
	    post.setHeader("Authorization", "Zoho-oauthtoken " + accessToken);
	    post.setHeader("Content-Type", "application/json"); // Set the content type to JSON

	    // Create the JSON body
	    String jsonBody = "{\"text\":\"" + message + "\"}";
	    post.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));


	    // Execute the request
	    try (CloseableHttpResponse httpResponse = httpClient.execute(post)) {
	        int responseCode = httpResponse.getCode();
	        String responseBody = null;

	        // Handle different response codes
	        if (responseCode == 200 || responseCode == 204) {
	            if (httpResponse.getEntity() != null) {
	                responseBody = EntityUtils.toString(httpResponse.getEntity());
	            }
	            System.out.println("Message sent successfully.");
//	            return "{\"response\":\"Message sent successfully.\"}"; // Return the response body if successful
	            return true;
	        }
	        else if(responseCode ==400) {
	        	throw new InvalidEmailException("Invalid Email.");
	        }
	        else {
	        	
	            System.out.println("Failed to send message. Response Code: " + responseCode);
	            if (httpResponse.getEntity() != null) {
	                responseBody = EntityUtils.toString(httpResponse.getEntity());
	                System.out.println("Response Body: " + responseBody);
	            }
//	            return responseBody;
	            return false;
	        }
	    } catch (IOException e) {
	        System.out.println("Error executing HTTP request: " + e.getMessage());
	        e.printStackTrace();
	        
	        throw e; // Rethrow if you need to handle it higher up
	    }
	}

	
	
	public String sendMessageToZohoCliq(String accessToken, String userMail, String message) throws IOException, org.apache.hc.core5.http.ParseException {
	    // Encode the message to handle special characters
//	    String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());

	    // Construct the URL with the encoded message
	    String url = "https://cliq.zoho.com/api/v2/buddies/" + userMail + "/message";

	    CloseableHttpClient httpClient = HttpClients.createDefault();
	    HttpPost post = new HttpPost(url);
	    
	    // Set the Authorization header
	    post.setHeader("Authorization", "Zoho-oauthtoken " + accessToken);
	    post.setHeader("Content-Type", "application/json"); // Set the content type to JSON

	    // Create the JSON body
	    String jsonBody = "{\"text\":\"" + message + "\"}";
	    post.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));


	    // Execute the request
	    try (CloseableHttpResponse httpResponse = httpClient.execute(post)) {
	        int responseCode = httpResponse.getCode();
	        String responseBody = null;

	        // Handle different response codes
	        if (responseCode == 200 || responseCode == 204) {
	            if (httpResponse.getEntity() != null) {
	                responseBody = EntityUtils.toString(httpResponse.getEntity());
	            }
	            System.out.println("Message sent successfully.");
	            return "{\"response\":\"Message sent successfully.\"}"; // Return the response body if successful
	        } else {
	            System.out.println("Failed to send message. Response Code: " + responseCode);
	            if (httpResponse.getEntity() != null) {
	                responseBody = EntityUtils.toString(httpResponse.getEntity());
	                System.out.println("Response Body: " + responseBody);
	            }
	            return responseBody;
	        }
	    } catch (IOException e) {
	        System.out.println("Error executing HTTP request: " + e.getMessage());
	        e.printStackTrace();
	        throw e; // Rethrow if you need to handle it higher up
	    }
	}

}
