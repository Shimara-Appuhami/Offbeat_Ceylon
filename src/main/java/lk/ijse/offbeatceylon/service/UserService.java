package lk.ijse.offbeatceylon.service;


import lk.ijse.offbeatceylon.dto.UserDTO;
import lk.ijse.offbeatceylon.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.UUID;

public interface UserService {
    int saveUser(UserDTO userDTO);
    UserDTO searchUser(String username);

    User getUserByEmail(String email);

    boolean deleteUser(String email);

    List<User> getAllUsers();

    UserDTO updateUser(UserDTO userDTO);

    boolean resetPassword(String email, String password);
}
