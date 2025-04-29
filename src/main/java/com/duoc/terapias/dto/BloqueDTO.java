package com.duoc.terapias.dto;

import java.util.Date;

public class BloqueDTO {
    private String id;
    private Integer horaIni;
    private Integer horaFin;
    private Integer precio;
    private boolean disponible;
    private boolean enElPasado;
    private Date fecha;

    public BloqueDTO() {}

    public BloqueDTO(String id, Integer horaIni, Integer horaFin, boolean disponible, boolean enElPasado, Integer precio, Date fecha) {
        this.id = id;
        this.horaIni = horaIni;
        this.horaFin = horaFin;
        this.precio = precio;
        this.disponible = disponible;
        this.enElPasado = enElPasado;
        this.fecha = fecha;
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

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public String toString(){
        return   "horaIni:" + horaIni + " horaFin:" + horaFin + " fecha:" + fecha;
    }
}

