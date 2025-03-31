
package com.duoc.terapias.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "especialidad")
public class Especialidad {
    @Id
    @Column(name = "ID_especialidad")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String idEspecialidad;
    
    private String nombre;
    private String descripcion;

    @OneToMany(mappedBy = "especialidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Servicio> servicios;

    // Getters y Setters
    public String getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(String idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public List<Servicio> getServicios() { return servicios; }
    public void setServicios(List<Servicio> servicios) { this.servicios = servicios; }
    
    @Override
    public String toString() {
        return "Especialidad{" + "ID_especialidad='" + idEspecialidad + '\'' + ", nombre='" + nombre + '\'' + '}';
    }
    
    public void addServicio(Servicio servicio) {
        servicios.add(servicio);
        servicio.setEspecialidad(this);
    }

    public void removeServicio(Servicio servicio) {
        servicios.remove(servicio);
        servicio.setEspecialidad(null);
    }

}



