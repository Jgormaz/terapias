
package com.duoc.terapias.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "semana")
public class Semana {
    @Id
    private String ID_semana;

    @ManyToOne
    @JoinColumn(name = "ID_calendario", nullable = false)
    private Calendario calendario;

    private Date fecha;

    public String getID_semana() { return ID_semana; }
    public void setID_semana(String ID_semana) { this.ID_semana = ID_semana; }
    public Calendario getCalendario() { return calendario; }
    public void setCalendario(Calendario calendario) { this.calendario = calendario; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    
    @Override
    public String toString() {
        return "Semana{" + "ID_semana=" + ID_semana + ", calendario=" + calendario + "}";
    }
}
