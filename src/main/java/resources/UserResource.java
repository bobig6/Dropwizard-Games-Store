package resources;

import io.dropwizard.auth.AuthenticationException;
import model.User;
import model.UserCredentials;
import services.UserService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/auth/")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class UserResource {
    UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("/register")
    public String register(@Valid User user) {
        return userService.registerUser(user);
    }

    @POST
    @Path("/login")
    public String login(@Valid UserCredentials userCredentials) {
        try {
            return userService.loginUser(userCredentials);
        } catch (AuthenticationException e) {
            throw new WebApplicationException("Invalid credentials", 401);
        }
    }

}
