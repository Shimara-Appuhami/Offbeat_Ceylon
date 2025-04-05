package lk.ijse.offbeatceylon.controller;

import jakarta.validation.Valid;
import lk.ijse.offbeatceylon.dto.AuthDTO;
import lk.ijse.offbeatceylon.dto.ResponseDTO;
import lk.ijse.offbeatceylon.dto.UserDTO;
import lk.ijse.offbeatceylon.entity.AddPlaces;
import lk.ijse.offbeatceylon.entity.User;
import lk.ijse.offbeatceylon.service.UserService;
import lk.ijse.offbeatceylon.util.JwtUtil;
import lk.ijse.offbeatceylon.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin("http://localhost:63342")

public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping(value = "/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            int res = userService.saveUser(userDTO);
            switch (res) {
                case VarList.Created -> {
                    String token = jwtUtil.generateToken(userDTO);
                    AuthDTO authDTO = new AuthDTO();
                    authDTO.setEmail(userDTO.getEmail());
                    authDTO.setToken(token);
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Success", authDTO));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Email Already Used", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    //update
    @PutMapping(value = "/update")
    public ResponseEntity<ResponseDTO> updateUser(@RequestBody @Valid UserDTO userDTO) {
        //update user
        try {
            UserDTO updatedUser = userService.updateUser(userDTO);
            if (updatedUser!= null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO(VarList.OK, "Success", updatedUser));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "User Not Found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    //delete
    @DeleteMapping(value = "/delete/{email}")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable String email) {
        //delete by uuid
        try {
            boolean deleted = userService.deleteUser(email);
            if (deleted) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO(VarList.OK, "User Deleted Successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "User Not Found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    //getAll
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllPlaces() {
        List<User> places = userService.getAllUsers();
        if (!places.isEmpty()) {
            return ResponseEntity.ok(places);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

    }

}