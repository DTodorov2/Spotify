package bg.sofia.uni.fmi.mjt.spotify.server.model;

import bg.sofia.uni.fmi.mjt.spotify.server.security.SHA256;
import java.util.Map;
import java.util.Objects;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class User extends IdentifiableModel {

    private final String email;
    private final String password;
    private boolean loggedIn;

    public User(String email, String password) {
        validateString(email, "email");
        validateString(password, "password");

        this.email = email;
        this.password = SHA256.hash(password);
        this.loggedIn = false;
    }

    @Override
    public String getId() {
        return email;
    }

    @Override
    public boolean isDuplicate(Map<String, ? extends IdentifiableModel> users) {
        return users.containsKey(email);
    }

    public String getPassword() {
        return password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

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

}
