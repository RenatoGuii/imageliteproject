package renatoguii.imageliteapi.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import renatoguii.imageliteapi.entities.user.UserEntity;
import renatoguii.imageliteapi.exceptions.CustomAuthenticationException;
import renatoguii.imageliteapi.services.UserService;

import java.io.IOException;

// Classe que serve como filtro para poder autenticar usuários no back-end
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;

    // Ele é chamado para cada requisição e resposta
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Recupera o token de requisição HTTP
        var token = this.recoverToken(request);

        // Valida o Token e guarda as credenciais do usuário autenticado de maneira global
        if (token != null) {
            try {
                // A validação do token retorna o subject (nesse caso o email)
                var login = jwtService.getEmailFromToken(token);
                UserEntity user = userService.getByEmail(login);

                // Configura usuário como autenticado
                this.setUserAsAuthenticated(user);
            } catch (Exception e) {
                throw new CustomAuthenticationException("There was an error in token validation");
            }
        }

        // Chamar/Ir para o próximo filtro
        filterChain.doFilter(request, response);
    }

    // Método que faz a autenticação do usuário
    private void setUserAsAuthenticated(UserEntity user) {
        UserDetails userDetails = User // User do import UserDetails
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // Extrair o token JWT do cabeçalho "Authorization" da requisição
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        //Retirando a palavra "Bearer" e deixando só o valor do token (Bearer xxxxxxxxxxxxxxxxx)
        return authHeader.replace("Bearer ", "");
    }

    // Método que indica quais urls não passaram pelo filtro (doFilterInternal)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().contains("/v1/users");
    }
}
