package com.josefarias.contasapagar.domain.repository;

import com.josefarias.contasapagar.domain.entity.Conta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    // Filtro por descrição e data de vencimento
    Page<Conta> findByDescricaoContainingIgnoreCaseAndDataVencimentoBetween(
            String descricao,
            LocalDate dataInicio,
            LocalDate dataFim,
            Pageable pageable
    );

    // Valor total pago por período
    Page<Conta> findByDataPagamentoBetween(LocalDate dataInicio, LocalDate dataFim, Pageable pageable);
}
