package com.josefarias.contasapagar.service;

import com.josefarias.contasapagar.application.service.ContaService;
import com.josefarias.contasapagar.domain.entity.Conta;
import com.josefarias.contasapagar.domain.enums.Situacao;
import com.josefarias.contasapagar.application.dto.ContaRequestDTO;
import com.josefarias.contasapagar.application.dto.ContaResponseDTO;
import com.josefarias.contasapagar.domain.repository.ContaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

  @Mock
  private ContaRepository contaRepository;

  @InjectMocks
  private ContaService contaService;

  private Conta conta;
  private ContaRequestDTO requestDTO;

  @BeforeEach
  void setUp() {
    conta = new Conta(1L, LocalDate.now(), LocalDate.now(), BigDecimal.valueOf(100), "Conta de teste", Situacao.PENDENTE);
    requestDTO = new ContaRequestDTO(LocalDate.now(), LocalDate.now(), BigDecimal.valueOf(100), "Conta de teste", Situacao.PENDENTE);
  }

  @Test
  void deveSalvarConta() {
    when(contaRepository.save(any(Conta.class))).thenReturn(conta);

    ContaResponseDTO response = contaService.salvar(requestDTO);

    assertNotNull(response);
    assertEquals("Conta de teste", response.getDescricao());
    verify(contaRepository, times(1)).save(any(Conta.class));

    System.out.println("✅ deveSalvarConta passou");
  }

  @Test
  void deveAtualizarContaExistente() {
    when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
    when(contaRepository.save(any(Conta.class))).thenReturn(conta);

    ContaResponseDTO response = contaService.atualizar(1L, requestDTO);

    assertNotNull(response);
    assertEquals("Conta de teste", response.getDescricao());
    verify(contaRepository).findById(1L);
    verify(contaRepository).save(any(Conta.class));

    System.out.println("✅ deveAtualizarContaExistente passou");
  }

  @Test
  void deveLancarExcecao_AtualizarContaInexistente() {
    when(contaRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> contaService.atualizar(1L, requestDTO));

    System.out.println("✅ deveLancarExcecao_AtualizarContaInexistente passou");
  }

  @Test
  void deveAlterarSituacao() {
    when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
    conta.setSituacao(Situacao.PENDENTE);
    when(contaRepository.save(any(Conta.class))).thenReturn(conta);

    ContaResponseDTO response = contaService.alterarSituacao(1L, Situacao.PAGA);

    assertEquals(Situacao.PAGA, response.getSituacao());
    verify(contaRepository).save(any(Conta.class));

    System.out.println("✅ deveAlterarSituacao passou");
  }

  @Test
  void deveListarContasComFiltro() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<Conta> page = new PageImpl<>(List.of(conta));

    when(contaRepository.findByDescricaoContainingIgnoreCaseAndDataVencimentoBetween(anyString(), any(), any(), eq(pageable)))
        .thenReturn(page);

    Page<ContaResponseDTO> resultado = contaService.listar("teste", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), pageable);

    assertEquals(1, resultado.getTotalElements());

    System.out.println("✅ deveListarContasComFiltro passou");
  }

  @Test
  void deveBuscarPorId() {
    when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

    ContaResponseDTO response = contaService.buscarPorId(1L);

    assertNotNull(response);
    assertEquals("Conta de teste", response.getDescricao());

    System.out.println("✅ deveBuscarPorId passou");
  }

  @Test
  void deveCalcularValorTotalPagoPorPeriodo() {
    Conta contaPaga = new Conta(2L, LocalDate.now(), LocalDate.now(), BigDecimal.valueOf(200), "Paga", Situacao.PAGA);
    Page<Conta> page = new PageImpl<>(List.of(contaPaga, conta)); // Apenas uma paga

    when(contaRepository.findByDataPagamentoBetween(any(), any(), eq(Pageable.unpaged())))
        .thenReturn(page);

    BigDecimal total = contaService.valorTotalPagoPorPeriodo(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2));

    assertEquals(BigDecimal.valueOf(200), total);

    System.out.println("✅ deveCalcularValorTotalPagoPorPeriodo passou");
  }
}