package com.banking.utils;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptUtil {
	
	
	private static BcryptUtil instance = null;
	
	private BcryptUtil() {
		
	}
	public static BcryptUtil getInstance() {
		if(instance == null) {
			instance = new BcryptUtil();
		}
		
		return instance;
	}
	// Generates a hashed PIN using bcrypt
    public String hashPin(String pin) {
        // Generate a salt and hash the PIN
        return BCrypt.hashpw(pin, BCrypt.gensalt());
    }

    // Verifies a plain text PIN against a hashed PIN
    public boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

}
