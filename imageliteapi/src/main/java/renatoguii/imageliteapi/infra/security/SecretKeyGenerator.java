package renatoguii.imageliteapi.infra.security;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class SecretKeyGenerator {

    private SecretKey key;

    // Gera uma chave secreta para ser usada em operações criptográficas
    public SecretKey getKey() {
        if (this.key == null){
            this.key = Jwts.SIG.HS256.key().build();
        }
        return this.key;
    }
}
