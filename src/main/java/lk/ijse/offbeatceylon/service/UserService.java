package lk.ijse.offbeatceylon.service;


import lk.ijse.offbeatceylon.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    int saveUser(UserDTO userDTO);
    UserDTO searchUser(String username);
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
    public UserDTO loadUserDetailsByUsername(String email);
}
