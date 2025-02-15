
package com.duoc.terapias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "consultaterapeuta")
public class ConsultaTerapeuta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID_consultaTerapeuta;
    
    @ManyToOne
    @JoinColumn(name = "ID_direccionConsulta", nullable = false)
    private DireccionConsulta direccionConsulta;
    
    @ManyToOne
    @JoinColumn(name = "ID_terapeuta", nullable = false)
    private Terapeuta terapeuta;
    
    private Integer codigoDiasDisponible;

    // Getters y Setters
    public Long getID_consultaTerapeuta() { return ID_consultaTerapeuta; }
    public void setID_consultaTerapeuta(Long ID_consultaTerapeuta) { this.ID_consultaTerapeuta = ID_consultaTerapeuta; }
    public DireccionConsulta getDireccionConsulta() { return direccionConsulta; }
    public void setDireccionConsulta(DireccionConsulta direccionConsulta) { this.direccionConsulta = direccionConsulta; }
    public Terapeuta getTerapeuta() { return terapeuta; }
    public void setTerapeuta(Terapeuta terapeuta) { this.terapeuta = terapeuta; }
    public Integer getCodigoDiasDisponible() { return codigoDiasDisponible; }
    public void setCodigoDiasDisponible(Integer codigoDiasDisponible) { this.codigoDiasDisponible = codigoDiasDisponible; }

    @Override
    public String toString() {
        return "ConsultaTerapeuta{" + "ID_consultaTerapeuta=" + ID_consultaTerapeuta + '}';
    }
}

