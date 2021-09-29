package services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import core.PasswordManager;
import database.UserDAO;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import model.User;
import model.UserCredentials;
import model.UserJwtModel;

import javax.ws.rs.WebApplicationException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class UserService implements Authenticator<UserJwtModel, User> {
    final private UserDAO userDAO;

    final Algorithm jwtAlgorithm = Algorithm.HMAC256("secret");
    final JWTVerifier jwtVerifier = JWT.require(jwtAlgorithm)
            .withIssuer("auth0")
            .build();

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Optional<User> authenticate(UserJwtModel credentials) throws AuthenticationException {
        String username;
        try {
            DecodedJWT jwt = jwtVerifier.verify(credentials.getToken());
            username = jwt.getSubject();
        } catch (JWTVerificationException exception){
            throw new AuthenticationException("Invalid JWT signature/claims.");
        }
        if (username == null) {
            throw new AuthenticationException("Invalid credentials.");
        }
        User userFromDb = userDAO.findByUsername(username).get(0);
        userFromDb.setRoles(Arrays.asList("ADMIN"));
        return Optional.of(userFromDb);
    }

    public String registerUser(User user) {
        if(userDAO.checkUserCredentials(user)){
            throw new WebApplicationException("User already exists", 400);
        }
        userDAO.addUser(user);

        return JWT.create()
                .withSubject(user.getName())
                .withExpiresAt(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .withIssuer("auth0")
                .sign(jwtAlgorithm);
    }

    public String loginUser(UserCredentials userCredentials) throws AuthenticationException {
        User userFromDb = userDAO.findByUsername(userCredentials.getUsername()).get(0);
        if (userFromDb == null) {
            throw new AuthenticationException("Invalid credentials.");
        }

        if (PasswordManager.checkPassword(userCredentials.getPassword(), userFromDb.getPassword())) {
            return JWT.create()
                    .withSubject(userCredentials.getUsername())
                    .withExpiresAt(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                    .withIssuer("auth0")
                    .sign(jwtAlgorithm);
        } else {
            throw new AuthenticationException("Invalid credentials.");
        }
    }

}
