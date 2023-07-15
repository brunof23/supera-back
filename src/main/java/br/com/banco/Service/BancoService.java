package br.com.banco.Service;

import br.com.banco.Constants.Constants;
import br.com.banco.Entity.TransferenciaEntity;
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

    @Autowired
    public BancoService(TransferenciaRepository transferenciaRepository) {
        this.transferenciaRepository = transferenciaRepository;
    }

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

            if (transferencia.getTipo().equals(Constants.TIPO_OPERACAO_SAQUE)) {
                BigDecimal valorPositivo = valor.abs();
                saldoTotal = saldoTotal.subtract(valorPositivo);
            } else if (transferencia.getTipo().equals(Constants.TIPO_OPERACAO_TRANSFERENCIA) || transferencia.getTipo().equals(Constants.TIPO_OPERACAO_DEPOSITO)) {
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
                if (transferencia.getTipo().equals(Constants.TIPO_OPERACAO_SAQUE)) {
                    BigDecimal valorPositivo = valor.abs();
                    saldoTotalPeriodo = saldoTotalPeriodo.subtract(valorPositivo);
                } else if (transferencia.getTipo().equals(Constants.TIPO_OPERACAO_TRANSFERENCIA) || transferencia.getTipo().equals(Constants.TIPO_OPERACAO_DEPOSITO)) {
                    saldoTotalPeriodo = saldoTotalPeriodo.add(valor);
                }
            }
        }
        return saldoTotalPeriodo.setScale(2, RoundingMode.HALF_UP);
    }
}
