
package com.duoc.terapias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bloqueo")
public class Bloqueo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID_bloqueo;
    
    @ManyToOne
    @JoinColumn(name = "ID_paciente", nullable = false)
    private Paciente paciente;
    
    @ManyToOne
    @JoinColumn(name = "ID_terapeuta", nullable = false)
    private Terapeuta terapeuta;
    
    @ManyToOne
    @JoinColumn(name = "ID_motivoBloqueo", nullable = false)
    private MotivoBloqueo motivoBloqueo;

    // Getters y Setters
    public Long getID_bloqueo() { return ID_bloqueo; }
    public void setID_bloqueo(Long ID_bloqueo) { this.ID_bloqueo = ID_bloqueo; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Terapeuta getTerapeuta() { return terapeuta; }
    public void setTerapeuta(Terapeuta terapeuta) { this.terapeuta = terapeuta; }
    public MotivoBloqueo getMotivoBloqueo() { return motivoBloqueo; }
    public void setMotivoBloqueo(MotivoBloqueo motivoBloqueo) { this.motivoBloqueo = motivoBloqueo; }

    @Override
    public String toString() {
        return "Bloqueo{" + "ID_bloqueo=" + ID_bloqueo + '}';
    }
}

