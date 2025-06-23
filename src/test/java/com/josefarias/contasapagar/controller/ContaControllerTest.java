package com.josefarias.contasapagar.controller;

import com.josefarias.contasapagar.domain.enums.Situacao;
import com.josefarias.contasapagar.application.dto.AlterarSituacaoDTO;
import com.josefarias.contasapagar.application.dto.ContaRequestDTO;
import com.josefarias.contasapagar.application.dto.ContaResponseDTO;
import com.josefarias.contasapagar.application.service.ContaService;
import com.josefarias.contasapagar.application.service.CsvImportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.*;

import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContaControllerTest {

  @Mock
  private ContaService contaService;

  @Mock
  private CsvImportService csvImportService;

  @InjectMocks
  private ContaController contaController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private ContaResponseDTO mockResponse() {
    return new ContaResponseDTO(1L, LocalDate.now(), LocalDate.now(), new BigDecimal("200.00"), "Luz", Situacao.PENDENTE);
  }

  @Test
  void testCriarConta() {
    ContaRequestDTO request = new ContaRequestDTO(LocalDate.now(), LocalDate.now(), new BigDecimal("200.00"), "Luz", Situacao.PENDENTE);
    ContaResponseDTO response = mockResponse();

    when(contaService.salvar(request)).thenReturn(response);

    ResponseEntity<ContaResponseDTO> result = contaController.criar(request);

    assertEquals(200, result.getStatusCodeValue());
    assertEquals("Luz", result.getBody().getDescricao());
    verify(contaService).salvar(request);
  }

  @Test
  void testAtualizarConta() {
    ContaRequestDTO request = new ContaRequestDTO(LocalDate.now(), LocalDate.now(), new BigDecimal("100.00"), "Água", Situacao.PAGA);
    ContaResponseDTO response = new ContaResponseDTO(1L, request.getDataVencimento(), request.getDataPagamento(), request.getValor(), request.getDescricao(), request.getSituacao());

    when(contaService.atualizar(1L, request)).thenReturn(response);

    ResponseEntity<ContaResponseDTO> result = contaController.atualizar(1L, request);

    assertEquals(200, result.getStatusCodeValue());
    assertEquals("Água", result.getBody().getDescricao());
    verify(contaService).atualizar(1L, request);

    System.out.println("✅ testAtualizarConta passou");
  }

  @Test
  void testAlterarSituacao() {
    AlterarSituacaoDTO dto = new AlterarSituacaoDTO();
    dto.setSituacao(Situacao.PAGA);

    ContaResponseDTO response = mockResponse();
    response.setSituacao(Situacao.PAGA);

    when(contaService.alterarSituacao(1L, Situacao.PAGA)).thenReturn(response);

    ResponseEntity<ContaResponseDTO> result = contaController.alterarSituacao(1L, dto);

    assertEquals(200, result.getStatusCodeValue());
    assertEquals(Situacao.PAGA, result.getBody().getSituacao());
    verify(contaService).alterarSituacao(1L, Situacao.PAGA);

    System.out.println("✅ testAlterarSituacao passou");
  }

  @Test
  void testBuscarPorId() {
    ContaResponseDTO response = mockResponse();

    when(contaService.buscarPorId(1L)).thenReturn(response);

    ResponseEntity<ContaResponseDTO> result = contaController.buscarPorId(1L);

    assertEquals(200, result.getStatusCodeValue());
    assertEquals(1L, result.getBody().getId());
    verify(contaService).buscarPorId(1L);

    System.out.println("✅ testBuscarPorId passou");
  }

  @Test
  void testListar() {
    LocalDate inicio = LocalDate.of(2024, 1, 1);
    LocalDate fim = LocalDate.of(2024, 12, 31);
    Pageable pageable = PageRequest.of(0, 10);

    Page<ContaResponseDTO> page = new PageImpl<>(List.of(mockResponse()), pageable, 1);

    when(contaService.listar("", inicio, fim, pageable)).thenReturn(page);

    ResponseEntity<Page<ContaResponseDTO>> result = contaController.listar("", inicio, fim, pageable);

    assertEquals(200, result.getStatusCodeValue());
    assertEquals(1, result.getBody().getTotalElements());
    verify(contaService).listar("", inicio, fim, pageable);

    System.out.println("✅ testListar passou");
  }

  @Test
  void testValorTotalPagoPorPeriodo() {
    LocalDate inicio = LocalDate.of(2024, 1, 1);
    LocalDate fim = LocalDate.of(2024, 12, 31);

    when(contaService.valorTotalPagoPorPeriodo(inicio, fim)).thenReturn(new BigDecimal("350.00"));

    ResponseEntity<BigDecimal> result = contaController.valorTotalPagoPorPeriodo(inicio, fim);

    assertEquals(200, result.getStatusCodeValue());
    assertEquals(new BigDecimal("350.00"), result.getBody());
    verify(contaService).valorTotalPagoPorPeriodo(inicio, fim);

    System.out.println("✅ testValorTotalPagoPorPeriodo passou");
  }

  @Test
  void testImportarCSV() {
    MockMultipartFile file = new MockMultipartFile("file", "dados.csv", "text/csv", "teste".getBytes());

    ResponseEntity<Void> result = contaController.importarCSV(file);

    assertEquals(200, result.getStatusCodeValue());
    verify(csvImportService).importar(file);

    System.out.println("✅ testImportarCSV passou");
  }
}
