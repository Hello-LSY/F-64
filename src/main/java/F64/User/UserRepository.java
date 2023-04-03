package F64.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByusername(String username);

    Optional<Member> findByNicknameEquals(String nickname);
}
