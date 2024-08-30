package com.example.tcc.services;

import com.example.tcc.configs.TokenStore;
import com.example.tcc.requests.AuthRequestDto;
import com.example.tcc.responses.AssetInfoResponseDto;
import com.example.tcc.responses.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class AssetInfoService {
    @Value("${tcc.patrimonio.base-url}")
    private String baseURL;
    @Value("${security.admin.user}")
    private String username;
    @Value("${security.admin.password}")
    private String password;
    private final RestTemplate restTemplate;
    private final TokenStore tokenStore;

    @Autowired
    public AssetInfoService(TokenStore tokenStore) {
        this.restTemplate = new RestTemplate();
        this.tokenStore = tokenStore;
    }

    private void refreshToken() {
        String url = this.baseURL+"/auth/login";

        HttpEntity<AuthRequestDto> entity = new HttpEntity<>(new AuthRequestDto(username, password));
        ResponseEntity<LoginResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, LoginResponse.class);

        if(response.getBody() != null) {
            tokenStore.setToken(response.getBody().getToken());
            tokenStore.setExpirationDate(new Date(System.currentTimeMillis() + response.getBody().getExpiresIn()).getTime());
        }
    }

    public AssetInfoResponseDto getAssetInfo(Long userId, String assetNumber) {
        String url = this.baseURL+"/espelho-patrimonio/" + assetNumber;

        if(tokenStore.getToken().isBlank() || tokenStore.isTokenExpired()) {
            refreshToken();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenStore.getToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<AssetInfoResponseDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, AssetInfoResponseDto.class);

        // TODO: verificar se o User pode ter acesso às informações desse bem

        return response.getBody();
    }
}
