package com.example.tcc.configs;

import com.example.tcc.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        // Ignorar a rota /login e /signup
        if (request.getServletPath().equals("/auth/login") || request.getServletPath().equals("/auth/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Ignorar as rotas para os recursos
        if (request.getServletPath().matches(".*\\.(jpg|pdf)$")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            // Se o token não estiver presente ou não começar com "Bearer ", retorna 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Forbidden: Token is missing or invalid.");
            return;
        }

        try {
            String token = authorizationHeader.substring(7);
            final String userEmail = jwtService.extractUsername(token);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(token, userDetails)) {
                    Long userId = extractUserIdFromToken(token);
                    JwtAuthenticationToken jwtAuthentication = new JwtAuthenticationToken(userId, token);
                    SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
                }
            }
        } catch (Exception e) {
            // Se ocorrer qualquer erro ao processar o token, retorna 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Token is invalid or cannot be processed.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private Long extractUserIdFromToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Token JWT inválido");
        }

        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(payloadJson);
        return jsonObject.getLong("id"); // Supondo que o campo "id" seja uma string no token
    }
}
