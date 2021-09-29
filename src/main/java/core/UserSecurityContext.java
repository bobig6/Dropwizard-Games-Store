package core;

import model.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class UserSecurityContext implements SecurityContext{
    private final User principal;
    private final SecurityContext securityContext;

    public UserSecurityContext(User principal, SecurityContext securityContext) {
        this.principal = principal;
        this.securityContext = securityContext;
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(String s) {
        if(principal.getRoles().contains(s)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean isSecure() {
        return securityContext.isSecure();
    }

    @Override
    public String getAuthenticationScheme() {
        return "JWT";
    }

    @Override
    public String toString() {
        return "AuthenticationSecurityContext{" +
                "principal=" + principal +
                ", securityContext=" + securityContext +
                '}';
    }
}
