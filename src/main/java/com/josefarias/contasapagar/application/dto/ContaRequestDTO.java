package com.josefarias.contasapagar.application.dto;

import com.josefarias.contasapagar.domain.enums.Situacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaRequestDTO {
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private BigDecimal valor;
    private String descricao;
    private Situacao situacao;
}
