package com.example.tcc.controllers;

import com.example.tcc.dto.AuthRequestDto;
import com.example.tcc.dto.AuthResponseDto;
import com.example.tcc.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<AuthResponseDto> create(@RequestBody AuthRequestDto requestDto) {
        AuthResponseDto response = authService.login(requestDto.getEmail(), requestDto.getPassword());
        return ResponseEntity.ok(response);
    }
}