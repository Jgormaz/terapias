package com.duoc.terapias.dto;

import java.util.List;

public class SemanaDTO {
    private int numeroSemana;
    private List<DiaDTO> dias;

    public SemanaDTO() {}

    public SemanaDTO(int numeroSemana, List<DiaDTO> dias) {
        this.numeroSemana = numeroSemana;
        this.dias = dias;
    }

    public int getNumeroSemana() {
        return numeroSemana;
    }

    public void setNumeroSemana(int numeroSemana) {
        this.numeroSemana = numeroSemana;
    }

    public List<DiaDTO> getDias() {
        return dias;
    }

    public void setDias(List<DiaDTO> dias) {
        this.dias = dias;
    }
}
