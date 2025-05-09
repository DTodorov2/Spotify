package bg.sofia.uni.fmi.mjt.spotify.server.security;

import bg.sofia.uni.fmi.mjt.spotify.exception.unchecked.UnsuccessfulHashingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class SHA256 {

    public static String hash(String strToBeHashed) {
        validateString(strToBeHashed, "string to be hashed");

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(strToBeHashed.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            String exceptionMessage = "The given string: " + strToBeHashed + " cannot be hashed with SHA-256 algorithm";
            throw new UnsuccessfulHashingException(exceptionMessage, e);
        }

    }

    public static boolean checkPassword(String input, String hashToCompareTo) {
        validateString(input, "input");
        validateString(hashToCompareTo, "hash to compare to");

        String hashInput = hash(input);
        return hashInput.equals(hashToCompareTo);
    }

}
