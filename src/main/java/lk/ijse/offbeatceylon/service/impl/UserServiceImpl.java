package lk.ijse.offbeatceylon.service.impl;

import lk.ijse.offbeatceylon.dto.UserDTO;
import lk.ijse.offbeatceylon.entity.User;
import lk.ijse.offbeatceylon.repo.UserRepository;
import lk.ijse.offbeatceylon.service.UserService;
import lk.ijse.offbeatceylon.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Load user details for Spring Security authentication
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );
    }


    /**
     * Load user details as DTO (custom use, not for Spring Security)
     */
    @Override
    public UserDTO loadUserDetailsByUsername(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return modelMapper.map(user, UserDTO.class);
    }

    /**
     * Search for a user by email and return UserDTO if exists
     */
    @Override
    public UserDTO searchUser(String email) {
        return userRepository.findByEmail(email) != null
                ? modelMapper.map(userRepository.findByEmail(email), UserDTO.class)
                : null;
    }

    /**
     * Register a new user with encrypted password
     */
    @Override
    public int saveUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return VarList.Not_Acceptable;
        } else {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            User user = modelMapper.map(userDTO, User.class);
            userRepository.save(user);
            return VarList.Created;
        }
    }
}
