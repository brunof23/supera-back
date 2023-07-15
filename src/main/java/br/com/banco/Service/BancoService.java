package br.com.banco.Service;

import br.com.banco.Entity.TransferenciaEntity;
import br.com.banco.Repository.ContaRepository;
import br.com.banco.Repository.TransferenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BancoService {
    private final TransferenciaRepository transferenciaRepository;

    private final ContaRepository contaRepository;

    @Autowired
    public BancoService(TransferenciaRepository transferenciaRepository, ContaRepository contaRepository) {
        this.transferenciaRepository = transferenciaRepository;
        this.contaRepository = contaRepository;
    }

//    public List<TransferenciaEntity> obterTransferencias(Long numeroConta, LocalDateTime dataInicial, LocalDateTime dataFinal, String nomeOperador) {
//        if (numeroConta == null && dataInicial == null && dataFinal == null && nomeOperador == null) {
//            return transferenciaRepository.findAll();
//        } else if (dataInicial != null && dataFinal != null && nomeOperador != null) {
//            return transferenciaRepository.findByDataTransferenciaBetweenAndNomeOperadorTransacao(dataInicial, dataFinal, nomeOperador);
//        } else if (dataInicial != null && dataFinal != null) {
//            return transferenciaRepository.findByDataTransferenciaBetween(dataInicial, dataFinal);
//        } else if (nomeOperador != null) {
//            return transferenciaRepository.findByNomeOperadorTransacao(nomeOperador);
//        } else {
//            return contaRepository.findAllById(numeroConta);
//        }
//    }

    public List<TransferenciaEntity> allTransferencias() {
        return transferenciaRepository.findAll();
    }
    public List<TransferenciaEntity> transferenciasByNumConta(Long idConta) {
        return transferenciaRepository.findAllByContaId(idConta);
    }
    public List<TransferenciaEntity> transferenciasByData(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return transferenciaRepository.transferenciasByData(dataInicial, dataFinal);
    }
    public List<TransferenciaEntity> transferenciasByOperador(String nomeOperador) {
        return transferenciaRepository.transferenciasByOperador(nomeOperador);
    }
    public List<TransferenciaEntity> transferenciasAllFiltros(LocalDateTime dataInicial, LocalDateTime dataFinal, String nomeOperador) {
        return transferenciaRepository.transferenciasAllFiltros(dataInicial, dataFinal, nomeOperador);
    }




    public BigDecimal calcularSaldoTotal(List<TransferenciaEntity> transferencias) {
        BigDecimal saldoTotal = BigDecimal.ZERO;
        for (TransferenciaEntity transferencia : transferencias) {
            BigDecimal valor = transferencia.getValor();

            if (transferencia.getTipo().equals("SAQUE")) {
                BigDecimal valorPositivo = valor.abs();
                saldoTotal = saldoTotal.subtract(valorPositivo);
            } else if (transferencia.getTipo().equals("TRANSFERENCIA") || transferencia.getTipo().equals("DEPOSITO")) {
                saldoTotal = saldoTotal.add(valor);
            }
        }
        return saldoTotal.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularSaldoTotalPeriodo(List<TransferenciaEntity> transferencias, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        BigDecimal saldoTotalPeriodo = BigDecimal.ZERO;
        for (TransferenciaEntity transferencia : transferencias) {
            LocalDateTime dataTransferencia = transferencia.getDataTransferencia();
            if (dataTransferencia.isAfter(dataInicial) && dataTransferencia.isBefore(dataFinal)) {
                BigDecimal valor = transferencia.getValor();
                if ("SAIDA".equals(transferencia.getTipo())) {
                    saldoTotalPeriodo = saldoTotalPeriodo.subtract(valor);
                } else if ("ENTRADA".equals(transferencia.getTipo())) {
                    saldoTotalPeriodo = saldoTotalPeriodo.add(valor);
                }
            }
        }
        return saldoTotalPeriodo.setScale(2, RoundingMode.HALF_UP);
    }
}
