
package com.duoc.terapias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "atencion")
public class Atencion {
    @Id
    private String ID_atencion;

    @ManyToOne
    @JoinColumn(name = "ID_terapeuta", nullable = false)
    private Terapeuta terapeuta;

    @ManyToOne
    @JoinColumn(name = "ID_servicio", nullable = false)
    private Servicio servicio;
    @Column(name = "tamaño_bloque")
    private Integer tamanoBloque;
    private Integer duracionMin;
    private Integer duracionMax;
    private Integer precioBloque;
    private Integer descuentoBloqueConsecutivo;
    private Integer bloquesEntreReservas;
    private Boolean bloqueoPorDistancia;
    private Integer distanciaMaxTransporte;
    private Integer distanciaMaxDirPrincip;

    public String getID_atencion() { return ID_atencion; }
    public void setID_atencion(String ID_atencion) { this.ID_atencion = ID_atencion; }
    public Terapeuta getTerapeuta() { return terapeuta; }
    public void setTerapeuta(Terapeuta terapeuta) { this.terapeuta = terapeuta; }
    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
    public Integer getTamanoBloque() { return tamanoBloque; }
    public void setTamanoBloque(Integer tamanoBloque) { this.tamanoBloque = tamanoBloque; }
    public Integer getDuracionMin() { return duracionMin; }
    public void setDuracionMin(Integer duracionMin) { this.duracionMin = duracionMin; }
    public Integer getDuracionMax() { return duracionMax; }
    public void setDuracionMax(Integer duracionMax) { this.duracionMax = duracionMax; }
    public Integer getPrecioBloque() { return precioBloque; }
    public void setPrecioBloque(Integer precioBloque) { this.precioBloque = precioBloque; }
    public Integer getDescuentoBloqueConsecutivo() { return descuentoBloqueConsecutivo; }
    public void setDescuentoBloqueConsecutivo(Integer descuentoBloqueConsecutivo) { this.descuentoBloqueConsecutivo = descuentoBloqueConsecutivo; }
    public Integer getBloquesEntreReservas() { return bloquesEntreReservas; }
    public void setBloquesEntreReservas(Integer bloquesEntreReservas) { this.bloquesEntreReservas = bloquesEntreReservas; }
    public Boolean getBloqueoPorDistancia() { return bloqueoPorDistancia; }
    public void setBloqueoPorDistancia(Boolean bloqueoPorDistancia) { this.bloqueoPorDistancia = bloqueoPorDistancia; }
    public Integer getDistanciaMaxTransporte() { return distanciaMaxTransporte; }
    public void setDistanciaMaxTransporte(Integer distanciaMaxTransporte) { this.distanciaMaxTransporte = distanciaMaxTransporte; }
    public Integer getDistanciaMaxDirPrincip() { return distanciaMaxDirPrincip; }
    public void setDistanciaMaxDirPrincip(Integer distanciaMaxDirPrincip) { this.distanciaMaxDirPrincip = distanciaMaxDirPrincip; }
    
    @Override
    public String toString() {
        return "Atencion{" + "ID_atencion=" + ID_atencion + ", terapeuta=" + terapeuta + ", servicio=" + servicio + "}";
    }
}

