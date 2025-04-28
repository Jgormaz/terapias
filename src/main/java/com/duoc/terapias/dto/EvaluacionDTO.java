
package com.duoc.terapias.dto;

public class EvaluacionDTO {
    private String idReserva;
    private Integer nota; // de 1 a 5

    // getters y setters
    public String getIdReserva() {
        return idReserva;
    }
    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }
    public Integer getNota() {
        return nota;
    }
    public void setNota(Integer nota) {
        this.nota = nota;
    }
}

