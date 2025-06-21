package com.josefarias.contasapagar.dto;

import com.josefarias.contasapagar.domain.model.Situacao;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContaRequestDTO {
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private BigDecimal valor;
    private String descricao;
    private Situacao situacao;
}
