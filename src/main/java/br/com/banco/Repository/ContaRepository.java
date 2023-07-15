package br.com.banco.Repository;

import br.com.banco.Entity.ContaEntity;
import br.com.banco.Entity.TransferenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContaRepository extends JpaRepository<ContaEntity, Long> {

    List<TransferenciaEntity> findAllById(Long numeroConta);
}
