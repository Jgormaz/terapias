
package com.duoc.terapias.dto;

import java.util.List;

public class TerapeutaInfoDTO {

    private String nombre;
    private String apePaterno;
    private String apeMaterno;
    private Double evaluacion;
    private List<String> servicios;  // Lista de pares nombre_especialidad:nombre_servicio
    private boolean enabled;
    // Getters y Setters

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApePaterno() { return apePaterno; }
    public void setApePaterno(String apePaterno) { this.apePaterno = apePaterno; }

    public String getApeMaterno() { return apeMaterno; }
    public void setApeMaterno(String apeMaterno) { this.apeMaterno = apeMaterno; }

    public Double getEvaluacion() { return evaluacion; }
    public void setEvaluacion(Double evaluacion) { this.evaluacion = evaluacion; }

    public List<String> getServicios() { return servicios; }
    public void setServicios(List<String> servicios) { this.servicios = servicios; }

    @Override
    public String toString() {
        return "TerapeutaInfoDTO{" +
                "nombre='" + nombre + '\'' +
                ", apePaterno='" + apePaterno + '\'' +
                ", apeMaterno='" + apeMaterno + '\'' +
                ", evaluacion=" + evaluacion +
                ", servicios=" + servicios +
                '}';
    }
}

