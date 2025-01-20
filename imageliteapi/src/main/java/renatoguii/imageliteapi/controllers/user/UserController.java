package renatoguii.imageliteapi.controllers.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import renatoguii.imageliteapi.dtos.user.AutheticationDTO;
import renatoguii.imageliteapi.dtos.user.UserDTO;
import renatoguii.imageliteapi.entities.user.UserEntity;
import renatoguii.imageliteapi.services.UserService;

@Tag(name = "Users", description = "APIs relacionadas ao gerenciamento de usuários e autenticação")
@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(summary = "Registrar um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity save(@RequestBody @Valid UserDTO data) {
        userService.save(data);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    @Operation(summary = "Autenticar um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    @PostMapping("/auth")
    public ResponseEntity authenticate(@RequestBody @Valid AutheticationDTO data) throws AuthenticationException {
        var token = userService.authenticate(data);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
