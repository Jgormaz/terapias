
package com.duoc.terapias.model;

import jakarta.persistence.*;


@Entity
@Table(name = "direccionconsulta")
public class DireccionConsulta {
    @Id
    private String ID_direccionConsulta;   
    private String nombre;
    private String direccion;
    
    @ManyToOne
    @JoinColumn(name = "ID_comuna", nullable = false)
    private Comuna comuna;
    
    // Getters y Setters
    public String getID_direccionConsulta() { return ID_direccionConsulta; }
    public void setID_direccionConsulta(String ID_direccionConsulta) { this.ID_direccionConsulta = ID_direccionConsulta; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public Comuna getComuna() { return comuna; }
    public void setComuna(Comuna comuna) { this.comuna = comuna; }

    @Override
    public String toString() {
        return "DireccionConsulta{" + "ID_direccionConsulta='" + ID_direccionConsulta + '\'' + ", nombre='" + nombre + '\'' + '}';
    }
}

