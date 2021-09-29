package resources;

import io.dropwizard.auth.AuthenticationException;
import model.User;
import model.UserCredentials;
import services.UserService;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public String login(@Valid UserCredentials userCredentials) throws AuthenticationException {
        return userService.loginUser(userCredentials);
    }

}
