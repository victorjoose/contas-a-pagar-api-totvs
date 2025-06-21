package com.josefarias.contasapagar.controller;

import com.josefarias.contasapagar.dto.AlterarSituacaoDTO;
import com.josefarias.contasapagar.dto.ContaRequestDTO;
import com.josefarias.contasapagar.dto.ContaResponseDTO;
import com.josefarias.contasapagar.service.ContaService;
import com.josefarias.contasapagar.service.CsvImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
public class ContaController {

  private final ContaService contaService;
  private final CsvImportService csvImportService;

  @PostMapping
  public ResponseEntity<ContaResponseDTO> criar(@RequestBody ContaRequestDTO dto) {
    return ResponseEntity.ok(contaService.salvar(dto));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ContaResponseDTO> atualizar(@PathVariable Long id, @RequestBody ContaRequestDTO dto) {
    return ResponseEntity.ok(contaService.atualizar(id, dto));
  }

  @PatchMapping("/{id}/situacao")
  public ResponseEntity<ContaResponseDTO> alterarSituacao(@PathVariable Long id, @RequestBody AlterarSituacaoDTO dto) {
    return ResponseEntity.ok(contaService.alterarSituacao(id, dto.getSituacao()));
  }

  @GetMapping
  public ResponseEntity<Page<ContaResponseDTO>> listar(
      @RequestParam(defaultValue = "") String descricao,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
      Pageable pageable
  ) {
    return ResponseEntity.ok(contaService.listar(descricao, inicio, fim, pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ContaResponseDTO> buscarPorId(@PathVariable Long id) {
    return ResponseEntity.ok(contaService.buscarPorId(id));
  }

  @GetMapping("/valor-total")
  public ResponseEntity<BigDecimal> valorTotalPagoPorPeriodo(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
  ) {
    return ResponseEntity.ok(contaService.valorTotalPagoPorPeriodo(inicio, fim));
  }

  @PostMapping("/importar")
  public ResponseEntity<Void> importarCSV(@RequestParam("file") MultipartFile file) {
    csvImportService.importar(file);
    return ResponseEntity.ok().build();
  }

}
