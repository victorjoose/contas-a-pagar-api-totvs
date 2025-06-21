package com.josefarias.contasapagar.service;

import com.josefarias.contasapagar.domain.model.Conta;
import com.josefarias.contasapagar.domain.model.Situacao;
import com.josefarias.contasapagar.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvImportService {

  private final ContaRepository contaRepository;

  public void importar(MultipartFile file) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
      String linha;
      boolean cabecalho = true;
      List<Conta> contas = new ArrayList<>();

      while ((linha = reader.readLine()) != null) {
        if (cabecalho) {
          cabecalho = false;
          continue;
        }

        String[] campos = linha.split(",");

        Conta conta = new Conta();
        conta.setDataVencimento(LocalDate.parse(campos[0]));
        conta.setDataPagamento(campos[1].isEmpty() ? null : LocalDate.parse(campos[1]));
        conta.setValor(new BigDecimal(campos[2]));
        conta.setDescricao(campos[3]);
        conta.setSituacao(Situacao.valueOf(campos[4].toUpperCase()));

        contas.add(conta);
      }

      contaRepository.saveAll(contas);
    } catch (Exception e) {
      throw new RuntimeException("Erro ao importar CSV: " + e.getMessage(), e);
    }
  }
}
