package com.josefarias.contasapagar.controller;

import com.josefarias.contasapagar.application.dto.AuthRequestDTO;
import com.josefarias.contasapagar.application.dto.RegisterRequestDTO;
import com.josefarias.contasapagar.application.dto.TokenResponseDTO;
import com.josefarias.contasapagar.application.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

  @Mock
  private AuthService authService;

  @InjectMocks
  private AuthController authController;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testLogin() {
    // Arrange
    AuthRequestDTO dto = new AuthRequestDTO("usuario@email.com", "senha123");
    TokenResponseDTO expectedToken = new TokenResponseDTO("fake-jwt-token");

    when(authService.login(dto)).thenReturn(expectedToken);

    // Act
    ResponseEntity<TokenResponseDTO> response = authController.login(dto);

    // Assert
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(expectedToken, response.getBody());
    verify(authService, times(1)).login(dto);

    System.out.println("✅ testLogin passou");
  }

  @Test
  public void testRegister() {
    // Arrange
    RegisterRequestDTO dto = new RegisterRequestDTO("Nome Teste", "novo@email.com", "senha123");
    TokenResponseDTO expectedToken = new TokenResponseDTO("new-jwt-token");

    when(authService.register(dto)).thenReturn(expectedToken);

    // Act
    ResponseEntity<TokenResponseDTO> response = authController.register(dto);

    // Assert
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(expectedToken, response.getBody());
    verify(authService, times(1)).register(dto);

    System.out.println("✅ testRegister passou");
  }
}
