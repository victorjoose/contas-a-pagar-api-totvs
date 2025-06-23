package com.josefarias.contasapagar.application.service;

import com.josefarias.contasapagar.domain.entity.Conta;
import com.josefarias.contasapagar.domain.enums.Situacao;
import com.josefarias.contasapagar.application.dto.ContaRequestDTO;
import com.josefarias.contasapagar.application.dto.ContaResponseDTO;
import com.josefarias.contasapagar.domain.repository.ContaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;

    public ContaResponseDTO salvar(ContaRequestDTO dto) {
        Conta conta = mapToEntity(dto);
        conta = contaRepository.save(conta);
        return mapToResponse(conta);
    }

    public ContaResponseDTO atualizar(Long id, ContaRequestDTO dto) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        conta.setDataVencimento(dto.getDataVencimento());
        conta.setDataPagamento(dto.getDataPagamento());
        conta.setDescricao(dto.getDescricao());
        conta.setValor(dto.getValor());
        conta.setSituacao(dto.getSituacao());
        return mapToResponse(contaRepository.save(conta));
    }

    public ContaResponseDTO alterarSituacao(Long id, Situacao situacao) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
        conta.setSituacao(situacao);
        return mapToResponse(contaRepository.save(conta));
    }

    public Page<ContaResponseDTO> listar(String descricao, LocalDate inicio, LocalDate fim, Pageable pageable) {
        Page<Conta> contas = contaRepository.findByDescricaoContainingIgnoreCaseAndDataVencimentoBetween(
                descricao, inicio, fim, pageable
        );
        return contas.map(this::mapToResponse);
    }

    public ContaResponseDTO buscarPorId(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
        return mapToResponse(conta);
    }

    public BigDecimal valorTotalPagoPorPeriodo(LocalDate inicio, LocalDate fim) {
        List<Conta> contasPagas = contaRepository.findByDataPagamentoBetween(inicio, fim, Pageable.unpaged()).getContent();
        return contasPagas.stream()
                .filter(c -> c.getSituacao() == Situacao.PAGA)
                .map(Conta::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Métodos auxiliares
    private ContaResponseDTO mapToResponse(Conta conta) {
        return new ContaResponseDTO(
                conta.getId(),
                conta.getDataVencimento(),
                conta.getDataPagamento(),
                conta.getValor(),
                conta.getDescricao(),
                conta.getSituacao()
        );
    }

    private Conta mapToEntity(ContaRequestDTO dto) {
        Conta conta = new Conta();
        conta.setDataVencimento(dto.getDataVencimento());
        conta.setDataPagamento(dto.getDataPagamento());
        conta.setDescricao(dto.getDescricao());
        conta.setValor(dto.getValor());
        conta.setSituacao(dto.getSituacao());
        return conta;
    }
}
