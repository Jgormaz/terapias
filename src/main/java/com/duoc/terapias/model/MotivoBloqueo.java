
package com.duoc.terapias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "motivobloqueo")
public class MotivoBloqueo {
    @Id
    private String ID_motivoBloqueo;
    private String nombre;
    
    // Getters y Setters
    public String getID_motivoBloqueo() { return ID_motivoBloqueo; }
    public void setID_motivoBloqueo(String ID_motivoBloqueo) { this.ID_motivoBloqueo = ID_motivoBloqueo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return "MotivoBloqueo{" + "ID_motivoBloqueo='" + ID_motivoBloqueo + '\'' + ", nombre='" + nombre + '\'' + '}';
    }
}

