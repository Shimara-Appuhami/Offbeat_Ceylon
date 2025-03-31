package lk.ijse.offbeatceylon.repo;

import lk.ijse.offbeatceylon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,String> {

    User findByEmail(String userName);

    boolean existsByEmail(String userName);


    void deleteByEmail(User user);
}
