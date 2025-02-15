
package com.duoc.terapias.model;

import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "region")
public class Region {
    @Id
    private String ID_region;
    private String nombre;

    @OneToMany(mappedBy = "region")
    private List<Comuna> comunas;

    // Getters y Setters
    public String getID_region() { return ID_region; }
    public void setID_region(String ID_region) { this.ID_region = ID_region; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<Comuna> getComunas() { return comunas; }
    public void setComunas(List<Comuna> comunas) { this.comunas = comunas; }

    @Override
    public String toString() {
        return "Region{" + "ID_region='" + ID_region + '\'' + ", nombre='" + nombre + '\'' + '}';
    }
}

