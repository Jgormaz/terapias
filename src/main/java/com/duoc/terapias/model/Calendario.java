
package com.duoc.terapias.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "calendario")
public class Calendario {
    @Id
    private String ID_calendario;

    @ManyToOne
    @JoinColumn(name = "ID_atencion", nullable = false)
    private Atencion atencion;
    
    @ManyToOne
    @JoinColumn(name = "ID_terapeuta", nullable = false)
    private Terapeuta terapeuta; // Nueva relación con Terapeuta

    private Date fechaIni;

    private String semanaIni;
    private String semanaFin;
    private Integer semanasVisibles;

    public String getID_calendario() { return ID_calendario; }
    public void setID_calendario(String ID_calendario) { this.ID_calendario = ID_calendario; }
    public Atencion getAtencion() { return atencion; }
    public void setAtencion(Atencion atencion) { this.atencion = atencion; }
    public Terapeuta getTerapeuta() { return terapeuta; }
    public void setTerapeuta(Terapeuta terapeuta) { this.terapeuta = terapeuta; }
    public Date getFechaIni() { return fechaIni; }
    public void setFechaIni(Date fechaIni) { this.fechaIni = fechaIni; }
    public String getSemanaIni() { return semanaIni; }
    public void setSemanaIni(String semanaIni) { this.semanaIni = semanaIni; }
    public String getSemanaFin() { return semanaFin; }
    public void setSemanaFin(String semanaFin) { this.semanaFin = semanaFin; }
    public Integer getSemanasVisibles() { return semanasVisibles; }
    public void setSemanasVisibles(Integer semanasVisibles) { this.semanasVisibles = semanasVisibles; }
    
    @Override
    public String toString() {
        return "Calendario{" + "ID_calendario=" + ID_calendario + ", atencion=" + atencion + "}";
    }

}

