
package com.duoc.terapias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    private String ID_categoria;

    private String nombre;

    // Getters y Setters
    public String getID_categoria() { return ID_categoria; }
    public void setID_categoria(String ID_categoria) { this.ID_categoria = ID_categoria; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return "Categoria{" + "ID_categoria='" + ID_categoria + '\'' + ", nombre='" + nombre + '\'' + '}';
    }
}

