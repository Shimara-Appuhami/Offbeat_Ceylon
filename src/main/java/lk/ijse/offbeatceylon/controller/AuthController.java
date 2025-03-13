package lk.ijse.offbeatceylon.controller;

import lk.ijse.offbeatceylon.dto.AuthDTO;
import lk.ijse.offbeatceylon.dto.ResponseDTO;
import lk.ijse.offbeatceylon.dto.UserDTO;
import lk.ijse.offbeatceylon.service.impl.UserServiceImpl;
import lk.ijse.offbeatceylon.util.JwtUtil;
import lk.ijse.offbeatceylon.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;

    @Autowired
    public AuthController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserServiceImpl userService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    /**
     * Authenticate user and return JWT token if successful
     */
    @PostMapping("/authenticate")
    public ResponseEntity<ResponseDTO> authenticate(@RequestBody UserDTO userDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword())
            );

            UserDetails userDetails = userService.loadUserByUsername(userDTO.getEmail());

            String token = jwtUtil.generateToken(userDetails);

            if (token == null || token.trim().isEmpty()) {
                return new ResponseEntity<>(new ResponseDTO(
                        VarList.Internal_Server_Error, "Failed to generate token. Please try again.", null), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            AuthDTO authDTO = new AuthDTO(userDTO.getEmail(), token);
            return new ResponseEntity<>(new ResponseDTO(
                    VarList.OK, "Authentication successful", authDTO), HttpStatus.OK);

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ResponseDTO(
                    VarList.Unauthorized, "Invalid email or password", null), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseDTO(
                    VarList.Internal_Server_Error, "An error occurred: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
