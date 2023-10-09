package Handlers;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoderDecoder {
    public static String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Function to verify a password against its hash
    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

}
