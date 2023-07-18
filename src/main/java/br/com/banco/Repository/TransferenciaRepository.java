package br.com.banco.Repository;

import br.com.banco.Entity.TransferenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferenciaRepository extends JpaRepository<TransferenciaEntity, Long> {
    @Query("SELECT t FROM TransferenciaEntity t WHERE t.dataTransferencia BETWEEN :dataInicial AND :dataFinal AND t.nomeOperadorTransacao = :nomeOperador")
    List<TransferenciaEntity> transferenciasAllFiltros(LocalDateTime dataInicial, LocalDateTime dataFinal, String nomeOperador);

    @Query("SELECT t FROM TransferenciaEntity t WHERE t.dataTransferencia BETWEEN :dataInicial AND :dataFinal AND t.nomeOperadorTransacao = :nomeOperador AND t.conta.id  = :id_conta")
    List<TransferenciaEntity> transferenciasAllFiltrosConta(LocalDateTime dataInicial, LocalDateTime dataFinal, String nomeOperador, Long id_conta);

    @Query("SELECT t FROM TransferenciaEntity t WHERE t.dataTransferencia BETWEEN :dataInicial AND :dataFinal")
    List<TransferenciaEntity> transferenciasByData(LocalDateTime dataInicial, LocalDateTime dataFinal);

    @Query("SELECT t FROM TransferenciaEntity t WHERE t.nomeOperadorTransacao = :nomeOperador")
    List<TransferenciaEntity> transferenciasByOperador(String nomeOperador);

    @Query("SELECT t FROM TransferenciaEntity t WHERE t.conta.id = :id_conta")
    List<TransferenciaEntity> transferenciasByConta(@Param("id_conta") Long id_conta);
}
