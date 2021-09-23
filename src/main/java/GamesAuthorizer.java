import io.dropwizard.auth.Authorizer;

import javax.annotation.Nullable;
import javax.ws.rs.container.ContainerRequestContext;

public class GamesAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User principal, String role) {
        return principal.getRoles().contains(role);
    }
}