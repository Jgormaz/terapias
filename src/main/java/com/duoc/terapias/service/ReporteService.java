package com.duoc.terapias.service;

import com.duoc.terapias.dto.ResumenReservaDTO;
import com.duoc.terapias.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ReporteService {

    @Autowired
    private ReservaRepository reservaRepository;

    // Método para obtener el reporte de reservas por terapeuta
    public List<ResumenReservaDTO> obtenerReporteReservas() {
        List<Object[]> resultados = reservaRepository.findResumenReservasPorTerapeuta();
        
        // Convertir los resultados en un objeto DTO para mejor manejo en la vista
        List<ResumenReservaDTO> reporte = new ArrayList<>();
        
        for (Object[] resultado : resultados) {
            String terapeutaId = (String) resultado[0];
            String terapeutaNombre = (String) resultado[1];
            String terapeutaApePaterno = (String) resultado[2];
            Long agendadas = (Long) resultado[3];
            Long canceladas = (Long) resultado[4];
            Long reagendadas = (Long) resultado[5];
            Long noshow = (Long) resultado[6];
            Long completadas = (Long) resultado[7];
            Long evaluadas = (Long) resultado[8];

            reporte.add(new ResumenReservaDTO(terapeutaId, terapeutaNombre, terapeutaApePaterno,
                    agendadas, canceladas, reagendadas, noshow, completadas, evaluadas));
        }
        return reporte;
    }
}


