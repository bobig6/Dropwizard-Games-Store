package services;

import io.dropwizard.auth.Authorizer;
import model.User;

public class GamesAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User principal, String role) {
        return principal.getRoles().contains(role);
    }
}