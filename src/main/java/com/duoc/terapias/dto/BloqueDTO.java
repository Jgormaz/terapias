package com.duoc.terapias.dto;

public class BloqueDTO {
    private String id;
    private Integer horaIni;
    private Integer horaFin;
    private Integer precio;
    private boolean disponible;
    private boolean enElPasado;

    public BloqueDTO() {}

    public BloqueDTO(String id, Integer horaIni, Integer horaFin, boolean disponible, boolean enElPasado, Integer precio) {
        this.id = id;
        this.horaIni = horaIni;
        this.horaFin = horaFin;
        this.precio = precio;
        this.disponible = disponible;
        this.enElPasado = enElPasado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getHoraIni() {
        return horaIni;
    }

    public void setHoraIni(Integer horaIni) {
        this.horaIni = horaIni;
    }

    public Integer getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Integer horaFin) {
        this.horaFin = horaFin;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public boolean isEnElPasado() {
        return enElPasado;
    }

    public void setEnElPasado(boolean enElPasado) {
        this.enElPasado = enElPasado;
    }

    /**
     * @return the precio
     */
    public Integer getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(Integer precio) {
        this.precio = precio;
    }
}

