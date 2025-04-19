package lk.ijse.offbeatceylon.repo;

import lk.ijse.offbeatceylon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,String> {

    User findByEmail(String userName);

    boolean existsByEmail(String userName);

    void deleteByUid(User user);


}
