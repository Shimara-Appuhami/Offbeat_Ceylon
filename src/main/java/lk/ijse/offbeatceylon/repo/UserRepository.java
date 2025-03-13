package lk.ijse.offbeatceylon.repo;

import lk.ijse.offbeatceylon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    int deleteByEmail(String email);
}
