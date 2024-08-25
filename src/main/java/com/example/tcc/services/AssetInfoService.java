package com.example.tcc.services;

import com.example.tcc.responses.AssetInfoResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AssetInfoService {
    @Value("${tcc.patrimonio.base-url}")
    private String baseURL;
    private final RestTemplate restTemplate;

    public AssetInfoService() {
        this.restTemplate = new RestTemplate();
    }

    public AssetInfoResponseDto getAssetInfo(String token, String assetNumber) {
        String url = this.baseURL+"/espelho-patrimonio/" + assetNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<AssetInfoResponseDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, AssetInfoResponseDto.class);

        return response.getBody();
    }
}
