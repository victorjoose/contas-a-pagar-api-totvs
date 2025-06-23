package com.josefarias.contasapagar.service;

import com.josefarias.contasapagar.application.service.AuthService;
import com.josefarias.contasapagar.application.service.JwtService;
import com.josefarias.contasapagar.domain.enums.Role;
import com.josefarias.contasapagar.domain.entity.Usuario;
import com.josefarias.contasapagar.application.dto.AuthRequestDTO;
import com.josefarias.contasapagar.application.dto.RegisterRequestDTO;
import com.josefarias.contasapagar.application.dto.TokenResponseDTO;
import com.josefarias.contasapagar.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private UsuarioRepository usuarioRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtService jwtService;

  @Mock
  private AuthenticationManager authManager;

  @InjectMocks
  private AuthService authService;

  private AuthRequestDTO authRequest;
  private RegisterRequestDTO registerRequest;
  private Usuario usuario;

  @BeforeEach
  void setUp() {
    authRequest = new AuthRequestDTO("teste@email.com", "123456");
    registerRequest = new RegisterRequestDTO("Usuário Teste", "teste@email.com", "123456");

    usuario = new Usuario();
    usuario.setId(1L);
    usuario.setNome("Usuário Teste");
    usuario.setEmail("teste@email.com");
    usuario.setSenha("senha-criptografada");
    usuario.setRole(Role.USER);
  }

  @Test
  void deveFazerLoginERetornarToken() {
    when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
    when(jwtService.generateToken(usuario)).thenReturn("fake-jwt-token");

    TokenResponseDTO response = authService.login(authRequest);

    assertNotNull(response);
    assertEquals("fake-jwt-token", response.getToken());
    verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(jwtService).generateToken(usuario);

    System.out.println("✅ deveFazerLoginERetornarToken passou");
  }

  @Test
  void deveRegistrarUsuarioENRetornarToken() {
    when(passwordEncoder.encode("123456")).thenReturn("senha-criptografada");
    when(jwtService.generateToken(any(Usuario.class))).thenReturn("token-gerado");
    when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

    TokenResponseDTO response = authService.register(registerRequest);

    assertNotNull(response);
    assertEquals("token-gerado", response.getToken());
    verify(passwordEncoder).encode("123456");
    verify(usuarioRepository).save(any(Usuario.class));
    verify(jwtService).generateToken(any(Usuario.class));

    System.out.println("✅ deveRegistrarUsuarioENRetornarToken passou");
  }
}
