
package com.duoc.terapias.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "terapeuta")
public class Terapeuta {
    @Id
    private String ID_terapeuta;

    private String nombre;

    private String ape_paterno;

    private String ape_materno;

    private String direccion_principal;
    
    @Column(name = "user_name", unique = true, nullable = false)
    private String userName; // El correo será el username

    @Column(name = "password", nullable = false)
    private String password;
    
    private boolean enabled; // Este campo indica si el usuario está activo o no


    @ManyToOne
    @JoinColumn(name = "ID_comuna", nullable = false)
    private Comuna comuna;

    @ManyToOne
    @JoinColumn(name = "ID_region", nullable = false)
    private Region region;

    private String telefono;

    private String correo;

    private Integer evaluacion;

    private String url_foto;  // Campo para la URL de la foto del terapeuta

    @OneToMany(mappedBy = "terapeuta")
    private List<ServicioTerapeuta> servicios;

    // Getters y Setters
    public String getID_terapeuta() { return ID_terapeuta; }
    public void setID_terapeuta(String ID_terapeuta) { this.ID_terapeuta = ID_terapeuta; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApe_paterno() { return ape_paterno; }
    public void setApe_paterno(String ape_paterno) { this.ape_paterno = ape_paterno; }
    public String getApe_materno() { return ape_materno; }
    public void setApe_materno(String ape_materno) { this.ape_materno = ape_materno; }
    public String getDireccion_principal() { return direccion_principal; }
    public void setDireccion_principal(String direccion_principal) { this.direccion_principal = direccion_principal; }
    public Comuna getComuna() { return comuna; }
    public void setComuna(Comuna comuna) { this.comuna = comuna; }
    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public Integer getEvaluacion() { return evaluacion; }
    public void setEvaluacion(Integer evaluacion) { this.evaluacion = evaluacion; }
    public String getUrl_foto() { return url_foto; }
    public void setUrl_foto(String url_foto) { this.url_foto = url_foto; }
    public List<ServicioTerapeuta> getServicios() { return servicios; }
    public void setServicios(List<ServicioTerapeuta> servicios) { this.servicios = servicios; }

    @Override
    public String toString() {
        return "Terapeuta{" +
                "ID_terapeuta='" + ID_terapeuta + '\'' +
                ", nombre='" + nombre + '\'' +
                ", ape_paterno='" + ape_paterno + '\'' +
                ", ape_materno='" + ape_materno + '\'' +
                ", direccion_principal='" + direccion_principal + '\'' +
                ", comuna=" + comuna +
                ", region=" + region +
                ", telefono='" + telefono + '\'' +
                ", correo='" + correo + '\'' +
                ", evaluacion=" + evaluacion +
                ", url_foto='" + url_foto + '\'' +
                '}';
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isEnabled() { 
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
