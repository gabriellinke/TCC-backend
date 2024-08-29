package com.example.tcc.configs;

import org.json.JSONObject;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        // Ignorar a rota /login
        if (request.getServletPath().equals("/auth/login") || request.getServletPath().equals("/auth/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            // Se o token não estiver presente ou não começar com "Bearer ", retorna 403 Forbidden
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Forbidden: Token is missing or invalid.");
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            Long userId = extractUserIdFromToken(token);
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(userId, token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            // Se ocorrer qualquer erro ao processar o token, retorna 403 Forbidden
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Forbidden: Token is invalid or cannot be processed.");
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
