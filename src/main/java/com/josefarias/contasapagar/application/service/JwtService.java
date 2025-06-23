package com.josefarias.contasapagar.application.service;

import com.josefarias.contasapagar.domain.entity.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

  private Key key;

  @PostConstruct
  public void init() {
    this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  }

  public String generateToken(Usuario usuario) {
    return Jwts.builder()
        .setSubject(usuario.getUsername())
        .claim("role", usuario.getRole().name())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
        .signWith(key)
        .compact();
  }

  public String extractUsername(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public boolean isTokenValid(String token, Usuario usuario) {
    final String username = extractUsername(token);
    return username.equals(usuario.getUsername());
  }
}
