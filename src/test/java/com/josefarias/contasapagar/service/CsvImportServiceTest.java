package com.josefarias.contasapagar.service;

import com.josefarias.contasapagar.application.service.CsvImportService;
import com.josefarias.contasapagar.domain.entity.Conta;
import com.josefarias.contasapagar.domain.enums.Situacao;
import com.josefarias.contasapagar.domain.repository.ContaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CsvImportServiceTest {

  @Mock
  private ContaRepository contaRepository;

  @InjectMocks
  private CsvImportService csvImportService;

  private static final String CSV_VALIDO = """
    data_vencimento,data_pagamento,valor,descricao,situacao
    2024-06-01,2024-06-02,1500.00,Conta de Luz,PAGA
    2024-06-10,,100.00,Conta de Água,PENDENTE
    """;

  private static final String CSV_INVALIDO = """
    data_vencimento,data_pagamento,valor,descricao,situacao
    2024-06-01,INVALID_DATE,1500.00,Conta Errada,PAGA
    """;

  @Test
  void deveImportarContasDeCsvValido() {
    MockMultipartFile mockFile = new MockMultipartFile(
        "file", "contas.csv", "text/csv", CSV_VALIDO.getBytes(StandardCharsets.UTF_8)
    );

    csvImportService.importar(mockFile);

    verify(contaRepository, times(1)).saveAll(argThat(contas -> {
      if (contas instanceof List<Conta> lista) {
        return lista.size() == 2 &&
            lista.get(0).getDescricao().equals("Conta de Luz") &&
            lista.get(1).getSituacao().equals(Situacao.PENDENTE);
      }
      return false;
    }));

    System.out.println("✅ deveImportarContasDeCsvValido passou");
  }

  @Test
  void deveLancarExcecaoAoImportarCsvInvalido() {
    MockMultipartFile mockFile = new MockMultipartFile(
        "file", "invalido.csv", "text/csv", CSV_INVALIDO.getBytes(StandardCharsets.UTF_8)
    );

    RuntimeException ex = assertThrows(RuntimeException.class, () -> csvImportService.importar(mockFile));

    assertTrue(ex.getMessage().contains("Erro ao importar CSV"));

    System.out.println("✅ deveLancarExcecaoAoImportarCsvInvalido passou");
  }
}
