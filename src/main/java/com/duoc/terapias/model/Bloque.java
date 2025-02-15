
package com.duoc.terapias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bloque")
public class Bloque {
    @Id
    private String ID_bloque;

    @ManyToOne
    @JoinColumn(name = "ID_dia", nullable = false)
    private Dia dia;

    @ManyToOne
    @JoinColumn(name = "ID_reserva")
    private Reserva reserva;

    private Integer horaIni;
    private Integer horaFin;
    private Boolean disponible;
    private Integer precio;

    public String getID_bloque() { return ID_bloque; }
    public void setID_bloque(String ID_bloque) { this.ID_bloque = ID_bloque; }
    public Dia getDia() { return dia; }
    public void setDia(Dia dia) { this.dia = dia; }
    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }
    public Integer getHoraIni() { return horaIni; }
    public void setHoraIni(Integer horaIni) { this.horaIni = horaIni; }
    public Integer getHoraFin() { return horaFin; }
    public void setHoraFin(Integer horaFin) { this.horaFin = horaFin; }
    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    public Integer getPrecio() { return precio; }
    public void setPrecio(Integer precio) { this.precio = precio; }
    
    @Override
    public String toString() {
        return "Bloque{" + "ID_bloque=" + ID_bloque + ", dia=" + dia + "}";
    }
}


