package renatoguii.imageliteapi.dtos.user;

import jakarta.validation.constraints.NotBlank;

public record UserDTO(@NotBlank String name,
                      @NotBlank String email,
                      @NotBlank String password) {
}
