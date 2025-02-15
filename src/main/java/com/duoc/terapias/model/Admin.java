
package com.duoc.terapias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Admin") // Asegura que coincida con el nombre exacto de la tabla en la BD
public class Admin {

    @Id
    @Column(length = 10, nullable = false) // PRIMARY KEY con longitud 10
    private String userName = "Admin"; // Valor por defecto 'Admin'

    @Column(nullable = false, length = 255)
    private String password;

    // Constructor vacío (necesario para JPA)
    public Admin() {}

    // Constructor con parámetros
    public Admin(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // Getters y Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

