package com.example.tcc.controllers;

import com.example.tcc.requests.AuthRequestDto;
import com.example.tcc.responses.AuthResponseDto;
import com.example.tcc.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: Remove
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<AuthResponseDto> create(@RequestBody AuthRequestDto requestDto) {
        try {
            AuthResponseDto response = authService.login(requestDto.getEmail(), requestDto.getPassword());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}