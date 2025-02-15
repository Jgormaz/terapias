
package com.duoc.terapias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "servicioterapeuta")
public class ServicioTerapeuta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio_terapeuta")
    private Long ID_servicioTerapeuta;
    
    @ManyToOne
    @JoinColumn(name = "ID_servicio", nullable = false)
    private Servicio servicio;
    
    @ManyToOne
    @JoinColumn(name = "ID_terapeuta", nullable = false)
    private Terapeuta terapeuta;

    // Getters y Setters
    public Long getID_servicioTerapeuta() { return ID_servicioTerapeuta; }
    public void setID_servicioTerapeuta(Long ID_servicioTerapeuta) { this.ID_servicioTerapeuta = ID_servicioTerapeuta; }
    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
    public Terapeuta getTerapeuta() { return terapeuta; }
    public void setTerapeuta(Terapeuta terapeuta) { this.terapeuta = terapeuta; }

    @Override
    public String toString() {
        return "ServicioTerapeuta{" + "ID_servicioTerapeuta=" + ID_servicioTerapeuta + '}';
    }
}

