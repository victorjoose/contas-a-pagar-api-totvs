package com.josefarias.contasapagar.application.service;

import com.josefarias.contasapagar.domain.enums.Role;
import com.josefarias.contasapagar.domain.entity.Usuario;
import com.josefarias.contasapagar.application.dto.AuthRequestDTO;
import com.josefarias.contasapagar.application.dto.RegisterRequestDTO;
import com.josefarias.contasapagar.application.dto.TokenResponseDTO;
import com.josefarias.contasapagar.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;

  public TokenResponseDTO login(AuthRequestDTO dto) {
    authManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));
    Usuario usuario = usuarioRepository.findByEmail(dto.getEmail()).orElseThrow();
    String token = jwtService.generateToken(usuario);
    return new TokenResponseDTO(token);
  }

  public TokenResponseDTO register(RegisterRequestDTO dto) {
    Usuario novoUsuario = new Usuario();
    novoUsuario.setNome(dto.getNome());
    novoUsuario.setEmail(dto.getEmail());
    novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));
    novoUsuario.setRole(Role.USER);

    usuarioRepository.save(novoUsuario);
    String token = jwtService.generateToken(novoUsuario);
    return new TokenResponseDTO(token);
  }
}
