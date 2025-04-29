// DTO ResumenReservaDTO.java
package com.duoc.terapias.dto;

public class ResumenReservaDTO {
    private String terapeutaId;
    private String terapeutaNombre;
    private String terapeutaApePaterno;
    private long agendadas;
    private long canceladas;
    private long reagendadas;
    private long noShow;
    private long completadas;
    private long evaluadas;
    private long noshow;

    public ResumenReservaDTO(String terapeutaId, String terapeutaNombre, String terapeutaApePaterno,
                                      Long agendadas, Long canceladas, Long reagendadas, 
                                      Long noshow, Long completadas, Long evaluadas) {
        this.terapeutaId = terapeutaId;
        this.terapeutaNombre = terapeutaNombre;
        this.terapeutaApePaterno = terapeutaApePaterno;
        this.agendadas = agendadas;
        this.canceladas = canceladas;
        this.reagendadas = reagendadas;
        this.noshow = noshow;
        this.completadas = completadas;
        this.evaluadas = evaluadas;
    }

    // Getters y setters

    public String getTerapeutaNombre() { return terapeutaNombre; }
    public void setTerapeutaNombre(String nombreTerapeuta) { this.terapeutaNombre = nombreTerapeuta; }

    public long getAgendadas() { return agendadas; }
    public void setAgendadas(long agendadas) { this.agendadas = agendadas; }

    public long getCanceladas() { return canceladas; }
    public void setCanceladas(long canceladas) { this.canceladas = canceladas; }

    public long getReagendadas() { return reagendadas; }
    public void setReagendadas(long reagendadas) { this.reagendadas = reagendadas; }

    public long getNoShow() { return noShow; }
    public void setNoShow(long noShow) { this.noShow = noShow; }

    public long getCompletadas() { return completadas; }
    public void setCompletadas(long completadas) { this.completadas = completadas; }

    public long getEvaluadas() { return evaluadas; }
    public void setEvaluadas(long evaluadas) { this.evaluadas = evaluadas; }

    /**
     * @return the terapeutaId
     */
    public String getTerapeutaId() {
        return terapeutaId;
    }

    /**
     * @param terapeutaId the terapeutaId to set
     */
    public void setTerapeutaId(String terapeutaId) {
        this.terapeutaId = terapeutaId;
    }
    /**
     * @return the noshow
     */
    public long getNoshow() {
        return noshow;
    }

    /**
     * @param noshow the noshow to set
     */
    public void setNoshow(long noshow) {
        this.noshow = noshow;
    }

    /**
     * @return the terapeutaApePaterno
     */
    public String getTerapeutaApePaterno() {
        return terapeutaApePaterno;
    }

    /**
     * @param terapeutaApePaterno the terapeutaApePaterno to set
     */
    public void setTerapeutaApePaterno(String terapeutaApePaterno) {
        this.terapeutaApePaterno = terapeutaApePaterno;
    }
}

