package renatoguii.imageliteapi.dtos.user;

import jakarta.validation.constraints.NotBlank;

public record AutheticationDTO(@NotBlank String email,
                               @NotBlank String password) {
}
