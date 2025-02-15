package com.duoc.terapias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "paciente")
public class Paciente {
    @Id
    private String ID_paciente;
    
    private String nombre;
    private String ape_paterno;
    private String ape_materno;
    private String direccion;
    
    @ManyToOne
    @JoinColumn(name = "ID_comuna", nullable = false)
    private Comuna comuna;

    @ManyToOne
    @JoinColumn(name = "ID_region", nullable = false)
    private Region region;

    private String telefono;
    private String correo;
    private int atenciones;
    private int evaluacion;
    private String ID_categoria;

    public Paciente() {}

    public Paciente(String ID_paciente, String nombre, String ape_paterno, String ape_materno, String direccion, Comuna comuna, Region region, String telefono, String correo, int atenciones, int evaluacion, String ID_categoria) {
        this.ID_paciente = ID_paciente;
        this.nombre = nombre;
        this.ape_paterno = ape_paterno;
        this.ape_materno = ape_materno;
        this.direccion = direccion;
        this.comuna = comuna;
        this.region = region;
        this.telefono = telefono;
        this.correo = correo;
        this.atenciones = atenciones;
        this.evaluacion = evaluacion;
        this.ID_categoria = ID_categoria;
    }

    public String getID_paciente() { return ID_paciente; }
    public void setID_paciente(String ID_paciente) { this.ID_paciente = ID_paciente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApe_paterno() { return ape_paterno; }
    public void setApe_paterno(String ape_paterno) { this.ape_paterno = ape_paterno; }

    public String getApe_materno() { return ape_materno; }
    public void setApe_materno(String ape_materno) { this.ape_materno = ape_materno; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Comuna getComuna() { return comuna; }
    public void setComuna(Comuna comuna) { this.comuna = comuna; }

    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public int getAtenciones() { return atenciones; }
    public void setAtenciones(int atenciones) { this.atenciones = atenciones; }

    public int getEvaluacion() { return evaluacion; }
    public void setEvaluacion(int evaluacion) { this.evaluacion = evaluacion; }

    public String getID_categoria() { return ID_categoria; }
    public void setID_categoria(String ID_categoria) { this.ID_categoria = ID_categoria; }

    @Override
    public String toString() {
        return "Paciente{" +
                "ID_paciente='" + ID_paciente + '\'' +
                ", nombre='" + nombre + '\'' +
                ", ape_paterno='" + ape_paterno + '\'' +
                ", ape_materno='" + ape_materno + '\'' +
                ", direccion='" + direccion + '\'' +
                ", comuna=" + (comuna != null ? comuna.getNombre() : "null") +
                ", region=" + (region != null ? region.getNombre() : "null") +
                ", telefono='" + telefono + '\'' +
                ", correo='" + correo + '\'' +
                ", atenciones=" + atenciones +
                ", evaluacion=" + evaluacion +
                ", ID_categoria='" + ID_categoria + '\'' +
                '}';
    }
}

