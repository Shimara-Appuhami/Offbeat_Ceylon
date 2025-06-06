package lk.ijse.offbeatceylon.service.impl;

import lk.ijse.offbeatceylon.dto.UserDTO;
import lk.ijse.offbeatceylon.entity.User;
import lk.ijse.offbeatceylon.repo.UserRepository;
import lk.ijse.offbeatceylon.service.UserService;
import lk.ijse.offbeatceylon.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    public UserDTO loadUserDetailsByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        return modelMapper.map(user,UserDTO.class);
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return authorities;
    }

    @Override
    public UserDTO searchUser(String username) {
        if (userRepository.existsByEmail(username)) {
            User user=userRepository.findByEmail(username);
            return modelMapper.map(user,UserDTO.class);
        } else {
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean deleteUser(String uid) {
        User user = userRepository.findByEmail(String.valueOf(uid));
        if (user!= null) {
            userRepository.deleteByUid(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return modelMapper.map(userRepository.findAll(), List.class);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());
        if (user!= null) {
            user.setName(userDTO.getName());
            user.setPassword(userDTO.getPassword());
            userRepository.save(user);
            return modelMapper.map(user, UserDTO.class);
        } else {
            return null;
        }
    }

    @Override
    public boolean resetPassword(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return false;
        }
        if (password.length() < 8) {
            return false;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String hashedPassword = encoder.encode(password);
        user.setPassword(hashedPassword);
        userRepository.save(user);

        return true;
    }

    @Override
    public int saveUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return VarList.Not_Acceptable;
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userDTO.setRole("USER");
            userDTO.setRole("ADMIN");
            userRepository.save(modelMapper.map(userDTO, User.class));
            return VarList.Created;
        }
    }
}