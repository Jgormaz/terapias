
package com.duoc.terapias.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    private String ID_reserva;

    @ManyToOne
    @JoinColumn(name = "ID_paciente", nullable = false)
    private Paciente paciente;
    
    @ManyToOne
    @JoinColumn(name = "ID_terapeuta", nullable = false)
    private Terapeuta terapeuta; // Nueva relación con Terapeuta
    
    @ManyToOne
    @JoinColumn(name = "ID_especialidad", nullable = false)
    private Especialidad especialidad; // Nueva relación con la especialidad

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

    public String getID_reserva() { return ID_reserva; }
    public void setID_reserva(String ID_reserva) { this.ID_reserva = ID_reserva; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Terapeuta getTerapeuta() { return terapeuta; }
    public void setTerapeuta(Terapeuta terapeuta) { this.terapeuta = terapeuta; }
    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }
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
        return "Reserva{" + "ID_reserva=" + ID_reserva + ", paciente=" + paciente + "}";
    }


}

