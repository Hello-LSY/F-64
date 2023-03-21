package F64.Service;

import F64.Model.Member;
import F64.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Member create(String username, String password, String nickname){
        Member member = new Member();
        member.setUsername(username);
        member.setNickname(nickname);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member.setPassword(passwordEncoder.encode(password));
        return member;
    }
}
