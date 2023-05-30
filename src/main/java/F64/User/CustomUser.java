package F64.User;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

@Getter
public class CustomUser extends User {

    private final String nickname;
    private final Long id;
    private final String session;
    public CustomUser(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities, String nickname, String sessionId) {
        super(username, password, authorities);
        this.id = id;
        this.nickname = nickname;
        this.session = sessionId;
    }
}
