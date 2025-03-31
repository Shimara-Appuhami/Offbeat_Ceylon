package lk.ijse.offbeatceylon.service;


import lk.ijse.offbeatceylon.dto.UserDTO;
import lk.ijse.offbeatceylon.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    int saveUser(UserDTO userDTO);
    UserDTO searchUser(String username);

    User getUserByEmail(String email);
}
