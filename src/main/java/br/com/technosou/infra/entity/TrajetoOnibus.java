package br.com.technosou.infra.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "trajeto_onibus", indexes = {
        @Index(name = "idx_trajeto_onibus", columnList = "linha")
})
public class TrajetoOnibus extends PanacheEntity {
    public String linha;
    public double latitude;
    public double longitude;
    public int sequencia;
}
