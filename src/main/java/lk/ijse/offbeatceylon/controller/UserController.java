package lk.ijse.offbeatceylon.controller;

import jakarta.validation.Valid;
import lk.ijse.offbeatceylon.dto.AuthDTO;
import lk.ijse.offbeatceylon.dto.ResponseDTO;
import lk.ijse.offbeatceylon.dto.UserDTO;
import lk.ijse.offbeatceylon.service.UserService;
import lk.ijse.offbeatceylon.util.JwtUtil;
import lk.ijse.offbeatceylon.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin(origins = "http://localhost:63342")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Register a new user and return JWT token if successful
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            int result = userService.saveUser(userDTO);

            if (result == VarList.Created) {
                String token = jwtUtil.generateToken(userDTO);

                AuthDTO authDTO = new AuthDTO();
                authDTO.setEmail(userDTO.getEmail());
                authDTO.setToken(token);

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "User Registered Successfully", authDTO));

            } else if (result == VarList.Not_Acceptable) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(new ResponseDTO(VarList.Not_Acceptable, "Email Already Used", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO(VarList.Bad_Gateway, "Failed to Register User", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "An Error Occurred: " + e.getMessage(), null));
        }
    }
}
