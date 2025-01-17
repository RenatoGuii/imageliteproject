package renatoguii.imageliteapi.infra.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessToken {
    private String accessToken;
}
