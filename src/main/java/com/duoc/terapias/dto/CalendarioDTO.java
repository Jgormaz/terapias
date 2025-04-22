package com.duoc.terapias.dto;

import java.util.List;

public class CalendarioDTO {
    private String id; // ID_calendario
    private String idTerapeuta;
    private String idServicio;
    private List<SemanaDTO> semanas;

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdTerapeuta() {
        return idTerapeuta;
    }

    public void setIdTerapeuta(String idTerapeuta) {
        this.idTerapeuta = idTerapeuta;
    }

    public String getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }

    public List<SemanaDTO> getSemanas() {
        return semanas;
    }

    public void setSemanas(List<SemanaDTO> semanas) {
        this.semanas = semanas;
    }
}


