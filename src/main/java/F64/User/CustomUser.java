package F64.User;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUser extends User {

    private final String nickname;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String nickname) {
        super(username, password, authorities);
        this.nickname = nickname;
    }
}
