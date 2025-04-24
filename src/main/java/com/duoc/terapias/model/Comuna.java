
package com.duoc.terapias.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "comuna")
public class Comuna {
    @Id
    private String ID_comuna;
    
    @ManyToOne
    @JoinColumn(name = "ID_region", nullable = false)
    private Region region;
    private String nombre;

   @OneToMany(mappedBy = "comuna")
    private List<Paciente> pacientes;

    @OneToMany(mappedBy = "comuna")
    private List<Terapeuta> terapeutas;

    // Getters y Setters
    public String getID_comuna() { return ID_comuna; }
    public void setID_comuna(String ID_comuna) { this.ID_comuna = ID_comuna; }
    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<Paciente> getPacientes() { return pacientes; }
    public void setPacientes(List<Paciente> pacientes) { this.pacientes = pacientes; }
    public List<Terapeuta> getTerapeutas() { return terapeutas; }
    public void setTerapeutas(List<Terapeuta> terapeutas) { this.terapeutas = terapeutas; }

    @Override
    public String toString() {
        return "Comuna{" + "ID_comuna='" + ID_comuna + '\'' + ", nombre='" + nombre + '\'' + '}';
    }
}

