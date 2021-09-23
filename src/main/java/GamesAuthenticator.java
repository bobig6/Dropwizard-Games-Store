import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;


public class GamesAuthenticator implements Authenticator<BasicCredentials, User> {

    private String login;

    private String password;

    public GamesAuthenticator(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials)
            throws AuthenticationException {
        if (password.equals(credentials.getPassword())
                && login.equals(credentials.getUsername())) {
            return Optional.of(new User());
        } else {
            return Optional.empty();
        }
    }
}
