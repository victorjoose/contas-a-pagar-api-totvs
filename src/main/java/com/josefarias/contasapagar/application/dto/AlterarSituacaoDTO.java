package com.josefarias.contasapagar.application.dto;

import com.josefarias.contasapagar.domain.enums.Situacao;
import lombok.Data;

@Data
public class AlterarSituacaoDTO {
    private Situacao situacao;
}
