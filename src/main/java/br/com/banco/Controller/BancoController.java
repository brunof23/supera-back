package br.com.banco.Controller;

import br.com.banco.Entity.TransferenciaEntity;
import br.com.banco.Service.BancoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/banco")
public class BancoController {
    private final BancoService bancoService;

    @Autowired
    public BancoController(BancoService bancoService) {
        this.bancoService = bancoService;
    }
    @GetMapping("/transferencias")
    public ResponseEntity<List<TransferenciaEntity>> allTransferencias() {

        List<TransferenciaEntity> transferencias = bancoService.allTransferencias();
        return ResponseEntity.ok(transferencias);
    }

    @GetMapping("/transferencias-por-conta/{numeroConta}")
    public ResponseEntity<List<TransferenciaEntity>> transferenciasByConta(
            @PathVariable(value = "numeroConta", required = true) Long numeroConta) {

        List<TransferenciaEntity> transferencias = bancoService.transferenciasByNumConta(numeroConta);
        return ResponseEntity.ok(transferencias);
    }

    @GetMapping("/transferencias-por-data")
    public ResponseEntity<List<TransferenciaEntity>> transferenciasByData(
            @RequestParam(value = "dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam(value = "dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal) {

        List<TransferenciaEntity> transferencias = bancoService.transferenciasByData(dataInicial, dataFinal);
        return ResponseEntity.ok(transferencias);
    }

    @GetMapping("/transferencias-por-operador/{nomeOperador}")
    public ResponseEntity<List<TransferenciaEntity>> transferenciasByOperador(
            @PathVariable(value = "nomeOperador") String nomeOperador) {

        List<TransferenciaEntity> transferencias = bancoService.transferenciasByOperador(nomeOperador);
        return ResponseEntity.ok(transferencias);
    }

    @GetMapping("/transferencias-todos-filtros")
    public ResponseEntity<List<TransferenciaEntity>> transferenciasAllFiltros(
            @RequestParam(value = "dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam(value = "dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal,
            @RequestParam(value = "nomeOperador") String nomeOperador) {

        List<TransferenciaEntity> transferencias = bancoService.transferenciasAllFiltros(dataInicial, dataFinal, nomeOperador);
        return ResponseEntity.ok(transferencias);
    }
    @GetMapping("/saldo-total/{numeroConta}")
    public ResponseEntity<BigDecimal> calcularSaldoTotal(
            @PathVariable(value = "numeroConta") Long numeroConta) {

        List<TransferenciaEntity> transferencias = bancoService.transferenciasByNumConta(numeroConta);
        BigDecimal saldoTotal = bancoService.calcularSaldoTotal(transferencias);
        return ResponseEntity.ok(saldoTotal);
    }

    @GetMapping("/saldo-total-periodo")
    public ResponseEntity<BigDecimal> calcularSaldoTotalPeriodo(
            @PathVariable(value = "numeroConta") Long numeroConta,
            @PathVariable(value = "dataInicial") LocalDateTime dataInicial,
            @PathVariable(value = "dataFinal") LocalDateTime dataFinal) {

        List<TransferenciaEntity> transferencias = bancoService.transferenciasByNumConta(numeroConta);
        BigDecimal saldoTotalPeriodo = bancoService.calcularSaldoTotalPeriodo(transferencias, dataInicial, dataFinal);
        return ResponseEntity.ok(saldoTotalPeriodo);
    }
}
