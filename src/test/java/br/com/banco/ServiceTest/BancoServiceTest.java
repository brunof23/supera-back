package br.com.banco.ServiceTest;

import br.com.banco.Constants.Constants;
import br.com.banco.Entity.TransferenciaEntity;
import br.com.banco.Repository.TransferenciaRepository;
import br.com.banco.Service.BancoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class BancoServiceTest {

    @Mock
    private TransferenciaRepository transferenciaRepository;

    private BancoService bancoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bancoService = new BancoService(transferenciaRepository);
    }

    @Test
    void testAllTransferencias() {
        List<TransferenciaEntity> transferencias = new ArrayList<>();
        transferencias.add(new TransferenciaEntity());
        transferencias.add(new TransferenciaEntity());
        when(transferenciaRepository.findAll()).thenReturn(transferencias);

        List<TransferenciaEntity> result = bancoService.allTransferencias();

        Assertions.assertEquals(2, result.size());
        verify(transferenciaRepository, times(1)).findAll();
    }

    @Test
    void testTransferenciasByNumConta() {
        Long idConta = 1L;
        List<TransferenciaEntity> transferencias = new ArrayList<>();
        transferencias.add(new TransferenciaEntity());
        when(transferenciaRepository.transferenciasByConta(idConta)).thenReturn(transferencias);

        List<TransferenciaEntity> result = bancoService.transferenciasByConta(idConta);

        Assertions.assertEquals(1, result.size());
        verify(transferenciaRepository, times(1)).transferenciasByConta(idConta);
    }

    @Test
    void testTransferenciasByData() {
        LocalDateTime dataInicial = LocalDateTime.now();
        LocalDateTime dataFinal = LocalDateTime.now().plusDays(1);
        List<TransferenciaEntity> transferencias = new ArrayList<>();
        transferencias.add(new TransferenciaEntity());
        when(transferenciaRepository.transferenciasByData(dataInicial, dataFinal)).thenReturn(transferencias);

        List<TransferenciaEntity> result = bancoService.transferenciasByData(dataInicial, dataFinal);

        Assertions.assertEquals(1, result.size());
        verify(transferenciaRepository, times(1)).transferenciasByData(dataInicial, dataFinal);
    }

    @Test
    void testTransferenciasByOperador() {
        String nomeOperador = "João";
        List<TransferenciaEntity> transferencias = new ArrayList<>();
        transferencias.add(new TransferenciaEntity());
        when(transferenciaRepository.transferenciasByOperador(nomeOperador)).thenReturn(transferencias);

        List<TransferenciaEntity> result = bancoService.transferenciasByOperador(nomeOperador);

        Assertions.assertEquals(1, result.size());
        verify(transferenciaRepository, times(1)).transferenciasByOperador(nomeOperador);
    }

    @Test
    void testTransferenciasAllFiltros() {
        LocalDateTime dataInicial = LocalDateTime.now();
        LocalDateTime dataFinal = LocalDateTime.now().plusDays(1);
        String nomeOperador = "João";
        List<TransferenciaEntity> transferencias = new ArrayList<>();
        transferencias.add(new TransferenciaEntity());
        when(transferenciaRepository.transferenciasAllFiltros(dataInicial, dataFinal, nomeOperador)).thenReturn(transferencias);

        List<TransferenciaEntity> result = bancoService.transferenciasAllFiltros(dataInicial, dataFinal, nomeOperador);

        Assertions.assertEquals(1, result.size());
        verify(transferenciaRepository, times(1)).transferenciasAllFiltros(dataInicial, dataFinal, nomeOperador);
    }

    @Test
    void testTransferenciasAllFiltrosConta() {
        LocalDateTime dataInicial = LocalDateTime.now();
        LocalDateTime dataFinal = LocalDateTime.now().plusDays(1);
        String nomeOperador = "João";
        Long numeroConta = 1L;
        List<TransferenciaEntity> transferencias = new ArrayList<>();
        transferencias.add(new TransferenciaEntity());
        when(transferenciaRepository.transferenciasAllFiltrosConta(dataInicial, dataFinal, nomeOperador, numeroConta)).thenReturn(transferencias);

        List<TransferenciaEntity> result = bancoService.transferenciasAllFiltrosConta(dataInicial, dataFinal, nomeOperador, numeroConta);

        Assertions.assertEquals(1, result.size());
        verify(transferenciaRepository, times(1)).transferenciasAllFiltrosConta(dataInicial, dataFinal, nomeOperador, numeroConta);
    }

    @Test
    void testCalcularSaldoTotal() {
        List<TransferenciaEntity> transferencias = new ArrayList<>();
        transferencias.add(createTransferencia(Constants.TIPO_OPERACAO_SAQUE, BigDecimal.valueOf(-100.00)));
        transferencias.add(createTransferencia(Constants.TIPO_OPERACAO_TRANSFERENCIA, BigDecimal.valueOf(50.00)));
        transferencias.add(createTransferencia(Constants.TIPO_OPERACAO_DEPOSITO, BigDecimal.valueOf(75.00)));

        BigDecimal saldoTotal = bancoService.calcularSaldoTotal(transferencias);

        Assertions.assertEquals(BigDecimal.valueOf(25.00).setScale(2, RoundingMode.HALF_UP), saldoTotal);
    }

    private TransferenciaEntity createTransferencia(String tipoOperacao, BigDecimal valor) {
        TransferenciaEntity transferencia = new TransferenciaEntity();
        transferencia.setTipo(tipoOperacao);
        transferencia.setValor(valor);
        return transferencia;
    }

    @Test
    void testCalcularSaldoTotalPeriodo() {
        LocalDateTime dataInicial = LocalDateTime.of(2023, 7, 1, 0, 0);
        LocalDateTime dataFinal = LocalDateTime.of(2023, 7, 31, 23, 59);
        List<TransferenciaEntity> transferencias = new ArrayList<>();
        transferencias.add(createTransferencia(Constants.TIPO_OPERACAO_SAQUE, BigDecimal.valueOf(-100.00), LocalDateTime.of(2023, 7, 5, 10, 0)));
        transferencias.add(createTransferencia(Constants.TIPO_OPERACAO_TRANSFERENCIA, BigDecimal.valueOf(50.00), LocalDateTime.of(2023, 7, 10, 14, 30)));
        transferencias.add(createTransferencia(Constants.TIPO_OPERACAO_DEPOSITO, BigDecimal.valueOf(75.00), LocalDateTime.of(2023, 7, 15, 8, 15)));
        transferencias.add(createTransferencia(Constants.TIPO_OPERACAO_SAQUE, BigDecimal.valueOf(-50.00), LocalDateTime.of(2023, 8, 1, 9, 0)));

        BigDecimal saldoTotalPeriodo = bancoService.calcularSaldoTotalPeriodo(transferencias, dataInicial, dataFinal);

        Assertions.assertEquals(BigDecimal.valueOf(25.00).setScale(2, RoundingMode.HALF_UP), saldoTotalPeriodo);
    }

    private TransferenciaEntity createTransferencia(String tipoOperacao, BigDecimal valor, LocalDateTime dataTransferencia) {
        TransferenciaEntity transferencia = new TransferenciaEntity();
        transferencia.setTipo(tipoOperacao);
        transferencia.setValor(valor);
        transferencia.setDataTransferencia(dataTransferencia);
        return transferencia;
    }
}