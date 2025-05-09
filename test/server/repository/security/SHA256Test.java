package bg.sofia.uni.fmi.mjt.server.repository.security;

import bg.sofia.uni.fmi.mjt.spotify.server.security.SHA256;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SHA256Test {

    private static final String INPUT = "pass";
    private static final String DIFFERENT_PASS = "differentPassword";

    @Test
    void testHashEqualForEqualInputs() {
        String hash1 = SHA256.hash(INPUT);
        String hash2 = SHA256.hash(INPUT);

        assertEquals(hash1, hash2, "The hash must be equal for every hashing!");
    }

    @Test
    void testHashIsDifferentForDifferentInputs() {
        String hash1 = SHA256.hash(INPUT);
        String hash2 = SHA256.hash(DIFFERENT_PASS);

        assertNotEquals(hash1, hash2, "The hash must be different for different inputs!");
    }

    @Test
    void testHashLengthIsCorrect() {
        String hash = SHA256.hash(INPUT);

        assertEquals(64, hash.length(), "The hash must be 64 symbols long!");
    }

    @Test
    void testHashWithNullInput() {
        assertThrows(IllegalArgumentException.class, () -> SHA256.hash(null),
                "IllegalArgumentException is expected when the input is null!");
    }

    @Test
    void testHashWithBlankInput() {
        assertThrows(IllegalArgumentException.class, () -> SHA256.hash("    "),
                "IllegalArgumentException is expected when the input is blank!");
    }

    @Test
    void testHashWithEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> SHA256.hash(""),
                "IllegalArgumentException is expected when the input is empty!");
    }

    @Test
    void testCheckPasswordWithNullInput() {
        assertThrows(IllegalArgumentException.class, () -> SHA256.checkPassword(null, INPUT),
                "IllegalArgumentException is expected when the input is null!");
    }

    @Test
    void testCheckPasswordWithBlankInput() {
        assertThrows(IllegalArgumentException.class, () -> SHA256.checkPassword("    ", INPUT),
                "IllegalArgumentException is expected when the input is blank!");
    }

    @Test
    void testCheckPasswordWithEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> SHA256.checkPassword("", INPUT),
                "IllegalArgumentException is expected when the input is empty!");
    }

    @Test
    void testCheckPasswordWithNullHashToCompareTo() {
        assertThrows(IllegalArgumentException.class, () -> SHA256.checkPassword(INPUT, null),
                "IllegalArgumentException is expected when the hash for comparison is null!");
    }

    @Test
    void testCheckPasswordWithBlankHashToCompareTo() {
        assertThrows(IllegalArgumentException.class, () -> SHA256.checkPassword(INPUT, "    "),
                "IllegalArgumentException is expected when the hash for comparison is blank!");
    }

    @Test
    void testCheckPasswordWithEmptyHashToCompareTo() {
        assertThrows(IllegalArgumentException.class, () -> SHA256.checkPassword(INPUT, ""),
                "IllegalArgumentException is expected when the hash for comparison is empty!");
    }

    @Test
    void testCheckPasswordWithNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> SHA256.checkPassword(null, null),
                "IllegalArgumentException is expected when the arguments are null!");
    }

    @Test
    void testCheckPasswordWithBlankArguments() {
        assertThrows(IllegalArgumentException.class, () -> SHA256.checkPassword("   ", "    "),
                "IllegalArgumentException is expected when the arguments are blank!");
    }

    @Test
    void testCheckPasswordWithEmptyArguments() {
        assertThrows(IllegalArgumentException.class, () -> SHA256.checkPassword("", ""),
                "IllegalArgumentException is expected when the arguments are empty!");
    }

    @Test
    void testCheckPasswordForEqualPasswords() {
        String hash = SHA256.hash(INPUT);

        assertTrue(SHA256.checkPassword(INPUT, hash), "The password must be equal to the hash!");
    }

    @Test
    void testCheckPasswordForDifferentPasswords() {
        String hash = SHA256.hash(INPUT);

        assertFalse(SHA256.checkPassword(DIFFERENT_PASS, hash), "Different passwords must not be equal!");
    }

}
