package filter;

import core.UserSecurityContext;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthenticationException;
import model.User;
import model.UserJwtModel;
import services.UserService;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Optional;

public class AuthenticationFilter extends AuthFilter<UserJwtModel, User> {
    final UserService authenticationService;

    public AuthenticationFilter(UserService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Optional<User> authenticatedUser;
        try {
            String jwt = requestContext
                    .getHeaders()
                    .getFirst("Authorization")
                    .replaceAll("Bearer ", "");
            UserJwtModel credentials = new UserJwtModel(jwt);
            authenticatedUser = authenticationService.authenticate(credentials);
        } catch (AuthenticationException e) {
            throw new WebApplicationException("Unable to validate credentials.", Response.Status.UNAUTHORIZED);
        }

        if (authenticatedUser.isPresent()) {
            SecurityContext securityContext = new UserSecurityContext(
                    authenticatedUser.get(),
                    requestContext.getSecurityContext()
            );
            requestContext.setSecurityContext(securityContext);
            System.out.println(requestContext.getSecurityContext());
        } else {
            throw new WebApplicationException("Credentials are not valid.", Response.Status.UNAUTHORIZED);
        }
    }
}
