package renatoguii.imageliteapi.controllers;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import renatoguii.imageliteapi.dtos.AutheticationDTO;
import renatoguii.imageliteapi.dtos.UserDTO;
import renatoguii.imageliteapi.entities.user.UserEntity;
import renatoguii.imageliteapi.services.UserService;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity save(@RequestBody UserDTO data) {
        UserEntity newUser = userService.save(data);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    @PostMapping("/auth")
    public ResponseEntity authenticate(@RequestBody AutheticationDTO data) throws AuthenticationException {
        var token = userService.authenticate(data);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

}
