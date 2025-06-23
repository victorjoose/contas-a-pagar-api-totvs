package com.josefarias.contasapagar.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
  private String nome;
  private String email;
  private String senha;
}
