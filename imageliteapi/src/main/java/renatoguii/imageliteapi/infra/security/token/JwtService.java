package renatoguii.imageliteapi.infra.security.token;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import renatoguii.imageliteapi.entities.user.UserEntity;
import renatoguii.imageliteapi.exceptions.InvalidTokenException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Autowired
    SecretKeyGenerator secretKeyGenerator;

    public AccessToken generateToken(UserEntity user) {

        var key = secretKeyGenerator.getKey();
        var expirationDate = generateExpirationDate();
        var claims = generateTokenClaims(user);

        String token = Jwts
                .builder()
                .signWith(key)
                .subject(user.getEmail()) // Usar um identificador único do usuário
                .expiration(expirationDate)
                .claims(claims)
                .compact();

        return new AccessToken(token);
    }

    private Date generateExpirationDate() {
        var expirationMinutes = 60;
        LocalDateTime now = LocalDateTime.now().plusMinutes(expirationMinutes);
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Map<String, Object> generateTokenClaims(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        return claims;
    }

    public String getEmailFromToken (String tokenJwt) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKeyGenerator.getKey())
                    .build()
                    .parseSignedClaims(tokenJwt) // Decodifica o Token JWT
                    .getPayload() // Resgata os valores do Token
                    .getSubject(); // Resgata o subject (Nesse caso o Email))
        } catch (JwtException e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }

}
