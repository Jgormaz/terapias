
package com.duoc.terapias.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservaDTO {
    
    private PacienteDTO paciente;
    private String idReserva;
    private String nombreTerapeuta;
    private String apellidoPaternoTerapeuta;
    private String nombreServicio;
    private String nombreEspecialidad;
    private String direccionAtencion;
    private String comuna;
    private String region;
    private LocalDateTime  horaIni;
    private LocalDateTime  horaFin;
    private Integer precio;
    private Integer abono;
    private String estado;
    private LocalDate fecha;
    private String idBloque;
    private String idTerapeuta;
    private String idServicio;
    

    /**
     * @return the idReserva
     */
    public String getIdReserva() {
        return idReserva;
    }

    /**
     * @param idReserva the idReserva to set
     */
    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    /**
     * @return the nombreTerapeuta
     */
    public String getNombreTerapeuta() {
        return nombreTerapeuta;
    }

    /**
     * @param nombreTerapeuta the nombreTerapeuta to set
     */
    public void setNombreTerapeuta(String nombreTerapeuta) {
        this.nombreTerapeuta = nombreTerapeuta;
    }

    /**
     * @return the nombreServicio
     */
    public String getNombreServicio() {
        return nombreServicio;
    }

    /**
     * @param nombreServicio the nombreServicio to set
     */
    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    /**
     * @return the nombreEspecialidad
     */
    public String getNombreEspecialidad() {
        return nombreEspecialidad;
    }

    /**
     * @param nombreEspecialidad the nombreEspecialidad to set
     */
    public void setNombreEspecialidad(String nombreEspecialidad) {
        this.nombreEspecialidad = nombreEspecialidad;
    }

    /**
     * @return the direccionAtencion
     */
    public String getDireccionAtencion() {
        return direccionAtencion;
    }

    /**
     * @param direccionAtencion the direccionAtencion to set
     */
    public void setDireccionAtencion(String direccionAtencion) {
        this.direccionAtencion = direccionAtencion;
    }

    /**
     * @return the comuna
     */
    public String getComuna() {
        return comuna;
    }

    /**
     * @param comuna the comuna to set
     */
    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return the horaIni
     */
    public LocalDateTime  getHoraIni() {
        return horaIni;
    }

    /**
     * @param horaIni the horaIni to set
     */
    public void setHoraIni(LocalDateTime  horaIni) {
        this.horaIni = horaIni;
    }

    /**
     * @return the horaFin
     */
    public LocalDateTime  getHoraFin() {
        return horaFin;
    }

    /**
     * @param horaFin the horaFin to set
     */
    public void setHoraFin(LocalDateTime  horaFin) {
        this.horaFin = horaFin;
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
     * @return the abono
     */
    public Integer getAbono() {
        return abono;
    }

    /**
     * @param abono the abono to set
     */
    public void setAbono(Integer abono) {
        this.abono = abono;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }


    // Getters y setters

    /**
     * @return the fecha
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the IdBloque
     */
    public String getIdBloque() {
        return idBloque;
    }

    /**
     * @param IdBloque the IdBloque to set
     */
    public void setIdBloque(String idBloque) {
        this.idBloque = idBloque;
    }

    /**
     * @return the idTerapeuta
     */
    public String getIdTerapeuta() {
        return idTerapeuta;
    }

    /**
     * @param idTerapeuta the idTerapeuta to set
     */
    public void setIdTerapeuta(String idTerapeuta) {
        this.idTerapeuta = idTerapeuta;
    }

    /**
     * @return the idServicio
     */
    public String getIdServicio() {
        return idServicio;
    }

    /**
     * @param idServicio the idServicio to set
     */
    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }

    /**
     * @return the paciente
     */
    public PacienteDTO getPaciente() {
        return paciente;
    }

    /**
     * @param paciente the paciente to set
     */
    public void setPaciente(PacienteDTO paciente) {
        this.paciente = paciente;
    }

    /**
     * @return the apellidoPaternoTerapeuta
     */
    public String getApellidoPaternoTerapeuta() {
        return apellidoPaternoTerapeuta;
    }

    /**
     * @param apellidoPaternoTerapeuta the apellidoPaternoTerapeuta to set
     */
    public void setApellidoPaternoTerapeuta(String apellidoPaternoTerapeuta) {
        this.apellidoPaternoTerapeuta = apellidoPaternoTerapeuta;
    }

}

