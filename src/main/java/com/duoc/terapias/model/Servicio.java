
package com.duoc.terapias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "servicio")
public class Servicio {
    @Id
    @Column(name = "ID_servicio")
    private String idServicio;
    
    @ManyToOne
    @JoinColumn(name = "ID_especialidad", nullable = false)
    private Especialidad especialidad;

    private String nombre;
    private String descripcion;

    // Getters y Setters
    public String getIdServicio() { return idServicio; }
    public void setIdServicio(String idServicio) { this.idServicio = idServicio; }
    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return "Servicio{" + "ID_servicio='" + idServicio + '\'' + ", nombre='" + nombre + '\'' + '}';
    }
}


