
package com.duoc.terapias.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @Column(name = "ID_reserva")
    private String idReserva;

    @ManyToOne
    @JoinColumn(name = "ID_paciente", nullable = false)
    private Paciente paciente;
    
    @ManyToOne
    @JoinColumn(name = "ID_atencion", nullable = false)
    private Atencion atencion; // Nueva relación con la especialidad

    private String nombre;

    private String direccionAtencion;

    @ManyToOne
    @JoinColumn(name = "ID_comuna", nullable = false)
    private Comuna comuna;

    @ManyToOne
    @JoinColumn(name = "ID_region", nullable = false)
    private Region region;

    private Date horaIni;

    private Date horaFin;

    private Integer precio;
    private Integer abono;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_reserva", nullable = false)
    private EstadoReserva estado = EstadoReserva.AGENDADA; // Valor por defecto

    // Getters y Setters

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccionAtencion() { return direccionAtencion; }
    public void setDireccionAtencion(String direccionAtencion) { this.direccionAtencion = direccionAtencion; }
    public Comuna getComuna() { return comuna; }
    public void setComuna(Comuna comuna) { this.comuna = comuna; }
    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }
    public Date getHoraIni() { return horaIni; }
    public void setHoraIni(Date horaIni) { this.horaIni = horaIni; }
    public Date getHoraFin() { return horaFin; }
    public void setHoraFin(Date horaFin) { this.horaFin = horaFin; }
    public Integer getPrecio() { return precio; }
    public void setPrecio(Integer precio) { this.precio = precio; }
    public Integer getAbono() { return abono; }
    public void setAbono(Integer abono) { this.abono = abono; }
    
    @Override
    public String toString() {
        return "Reserva{" + "ID_reserva=" + idReserva + ", paciente=" + paciente + "}";
    }

    /**
     * @return the atencion
     */
    public Atencion getAtencion() {
        return atencion;
    }

    /**
     * @param atencion the atencion to set
     */
    public void setAtencion(Atencion atencion) {
        this.atencion = atencion;
    }

    /**
     * @return the idReserva
     */
    public String getIdReserva() {
        return idReserva;
    }

    /**
     * @param idReserva the idReserva to set
     */
    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }


}

