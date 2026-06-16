package com.uca.pokedexcapas012026.controller;

import com.uca.pokedexcapas012026.dto.request.LoginRequest;
import com.uca.pokedexcapas012026.dto.response.JwtAuthResponse;
import com.uca.pokedexcapas012026.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/pokedex/auth")
@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginRequest request){
        String token = authService.login(request);
        return ResponseEntity.ok(JwtAuthResponse.builder()
                .accessToken(token)
                .build());
    }
}