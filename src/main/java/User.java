import javax.security.auth.Subject;
import java.security.Principal;
import java.util.List;

public class User implements Principal {

    private String name;
    private List<String> roles;

    public User(String name, List<String> roles) {
        this.name = name;
        this.roles = roles;
    }

    @Override
    public String getName() {
        return this.name;
    }
    public List<String> getRoles() {
        return this.roles;
    }


    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
