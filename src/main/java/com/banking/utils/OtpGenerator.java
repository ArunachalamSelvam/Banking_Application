package com.banking.utils;

import java.security.SecureRandom;
import java.util.Random;

public class OtpGenerator {
	
	private final SecureRandom RANDOM = new SecureRandom();
	
	private final String OTP_CHARS = "0123456789";
	
	private static OtpGenerator instance = null;
	
	private OtpGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	public static OtpGenerator getInstance() {
		if(instance == null) {
			instance = new OtpGenerator();
		}
		
		return instance;
	}
	
	public String getOtp(int count) {
		StringBuilder otp = new StringBuilder(count);
		
		for(int i=0;i<count;i++) {
			int randomIndex = RANDOM.nextInt(OTP_CHARS.length());
			otp.append(OTP_CHARS.charAt(randomIndex));
		}
		return otp.toString();
	}

}
