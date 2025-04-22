
package com.duoc.terapias.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "dia")
public class Dia {
    @Id
    private String ID_dia;

    @ManyToOne
    @JoinColumn(name = "ID_semana", nullable = false)
    private Semana semana;

    private Date fecha;
    private Integer horaIni;
    private Integer horaFin;
    @Column(name = "tamaño_bloque")
    private Integer tamanoBloque;
    private Integer descuentoEspecial;

    public String getID_dia() { return ID_dia; }
    public void setID_dia(String ID_dia) { this.ID_dia = ID_dia; }
    public Semana getSemana() { return semana; }
    public void setSemana(Semana semana) { this.semana = semana; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public Integer getHoraIni() { return horaIni; }
    public void setHoraIni(Integer horaIni) { this.horaIni = horaIni; }
    public Integer getHoraFin() { return horaFin; }
    public void setHoraFin(Integer horaFin) { this.horaFin = horaFin; }
    public Integer getTamanoBloque() { return tamanoBloque; }
    public void setTamanoBloque(Integer tamanoBloque) { this.tamanoBloque = tamanoBloque; }
    public Integer getDescuentoEspecial() { return descuentoEspecial; }
    public void setDescuentoEspecial(Integer descuentoEspecial) { this.descuentoEspecial = descuentoEspecial; }
    
    @Override
    public String toString() {
        return "Dia{" + "ID_dia=" + ID_dia + ", semana=" + semana + "}";
    }
}

