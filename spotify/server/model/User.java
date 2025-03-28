package bg.sofia.uni.fmi.mjt.spotify.server.model;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.AddOperationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SuchUserAlreadyExistsException;

import java.util.Map;
import java.util.Objects;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.EMAIL_REGEX;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.MIN_LEN_PASS;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class User extends BaseModel<String> implements Identifiable {

    private final String email;
    private final String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    public User(String email, String password) {
        validateString(email, "email");
        validateString(password, "password");

        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("The email is invalid!");
        }

        if (password.length() < MIN_LEN_PASS) {
            throw new IllegalArgumentException("The password must be at least " + MIN_LEN_PASS + " characters long!");
        }

        this.email = email;
        this.password = password;
    }

    @Override
    public String getId() {
        return email;
    }

    @Override
    public void checkForDuplicate(Map<String, ? extends Identifiable> users) throws AddOperationException {
        if (users.containsKey(email)) {
            throw new SuchUserAlreadyExistsException("This email is already in use: " + email);
        }
    }
}
