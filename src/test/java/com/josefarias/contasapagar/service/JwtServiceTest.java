package com.josefarias.contasapagar.service;

import com.josefarias.contasapagar.application.service.JwtService;
import com.josefarias.contasapagar.domain.enums.Role;
import com.josefarias.contasapagar.domain.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

  private JwtService jwtService;
  private Usuario usuario;

  @BeforeEach
  void setUp() {
    jwtService = new JwtService();

    jwtService.init();

    usuario = new Usuario();
    usuario.setId(1L);
    usuario.setNome("Usuário Teste");
    usuario.setEmail("teste@email.com");
    usuario.setSenha("123456");
    usuario.setRole(Role.USER);
  }

  @Test
  void deveGerarTokenValido() {
    String token = jwtService.generateToken(usuario);

    assertNotNull(token);
    assertFalse(token.isEmpty());

    System.out.println("✅ deveGerarTokenValido passou");
  }

  @Test
  void deveExtrairUsernameDoToken() {
    String token = jwtService.generateToken(usuario);

    String usernameExtraido = jwtService.extractUsername(token);

    assertEquals(usuario.getUsername(), usernameExtraido);

    System.out.println("✅ deveExtrairUsernameDoToken passou");
  }

  @Test
  void deveValidarTokenCorretamente() {
    String token = jwtService.generateToken(usuario);

    boolean valido = jwtService.isTokenValid(token, usuario);

    assertTrue(valido);

    System.out.println("✅ deveValidarTokenCorretamente passou");
  }
}
