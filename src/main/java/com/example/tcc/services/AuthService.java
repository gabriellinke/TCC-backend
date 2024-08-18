package com.example.tcc.services;

import com.example.tcc.requests.AuthRequestDto;
import com.example.tcc.responses.AuthResponseDto;
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

    public AuthResponseDto login(String email, String password) {
        String url = this.baseURL+"/auth/login";

        AuthRequestDto authRequestDto = new AuthRequestDto(email, password);

        ResponseEntity<AuthResponseDto> response = restTemplate.postForEntity(url, authRequestDto, AuthResponseDto.class);
        return response.getBody();
    }
}
