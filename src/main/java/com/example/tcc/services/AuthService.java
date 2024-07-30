package com.example.tcc.services;

import com.example.tcc.models.AuthRequest;
import com.example.tcc.models.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    @Value("${tcc.patrimonio.base-url}")
    private String baseURL;
    private final RestTemplate restTemplate;

    public AuthService() {
        this.restTemplate = new RestTemplate();
    }

    public String login(String email, String password) {
        String url = this.baseURL+"/auth/login";

        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail(email);
        authRequest.setPassword(password);

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(url, authRequest, AuthResponse.class);
        return response.getBody().getToken();
    }
}
