package br.com.banco.Entity;

import javax.persistence.*;

@Entity
@Table(name = "conta")
public class ContaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conta")
    private Long id;

    @Column(name = "nome_responsavel", nullable = false, length = 50)
    private String nomeResponsavel;

    public ContaEntity() {
    }

    public ContaEntity(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }
}
