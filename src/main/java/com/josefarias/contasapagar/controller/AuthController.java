package com.josefarias.contasapagar.controller;

import com.josefarias.contasapagar.dto.AuthRequestDTO;
import com.josefarias.contasapagar.dto.RegisterRequestDTO;
import com.josefarias.contasapagar.dto.TokenResponseDTO;
import com.josefarias.contasapagar.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<TokenResponseDTO> login(@RequestBody AuthRequestDTO dto) {
    return ResponseEntity.ok(authService.login(dto));
  }

  @PostMapping("/register")
  public ResponseEntity<TokenResponseDTO> register(@RequestBody RegisterRequestDTO dto) {
    return ResponseEntity.ok(authService.register(dto));
  }
}
