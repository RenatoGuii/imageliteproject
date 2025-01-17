package renatoguii.imageliteapi.services;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renatoguii.imageliteapi.dtos.AutheticationDTO;
import renatoguii.imageliteapi.dtos.UserDTO;
import renatoguii.imageliteapi.entities.user.UserEntity;
import renatoguii.imageliteapi.exceptions.CustomAuthenticationException;
import renatoguii.imageliteapi.exceptions.RegisterException;
import renatoguii.imageliteapi.infra.security.AccessToken;
import renatoguii.imageliteapi.infra.security.JwtService;
import renatoguii.imageliteapi.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    @Transactional
    public UserEntity save(UserDTO data) {
        Optional<UserEntity> possibleUser = userRepository.findByEmail(data.email());
        if (possibleUser.isPresent()) {
            throw new RegisterException("This email is already registered");
        }

        UserEntity user = new UserEntity(data.name(), data.email(), data.password());

        encodePassword(user);
        return userRepository.save(user);
    }

    public AccessToken authenticate(AutheticationDTO data) throws AuthenticationException {
        Optional<UserEntity> possibleUser = userRepository.findByEmail(data.email());
        if (possibleUser.isEmpty()) {
            throw new CustomAuthenticationException("There is no user with these credentials");
        }

        // Verifica se a senha digitada bate com a senha do usuário
        boolean matches = passwordEncoder.matches(data.password(), possibleUser.get().getPassword());

        if (matches) {
            return jwtService.generateToken(possibleUser.get());
        }

        return null;
    }

    private void encodePassword (UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

}
