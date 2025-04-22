package com.duoc.terapias.dto;

import java.util.Date;
import java.util.List;

public class DiaDTO {
    private Date fecha;
    private List<BloqueDTO> bloques;

    public DiaDTO() {}

    public DiaDTO(Date fecha, List<BloqueDTO> bloques) {
        this.fecha = fecha;
        this.bloques = bloques;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<BloqueDTO> getBloques() {
        return bloques;
    }

    public void setBloques(List<BloqueDTO> bloques) {
        this.bloques = bloques;
    }
}

