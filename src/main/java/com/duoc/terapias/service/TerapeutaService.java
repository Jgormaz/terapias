
package com.duoc.terapias.service;

import com.duoc.terapias.dto.TerapeutaInfoDTO;
import com.duoc.terapias.model.ServicioTerapeuta;
import com.duoc.terapias.model.Terapeuta;
import com.duoc.terapias.repository.TerapeutaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TerapeutaService {

    private final TerapeutaRepository terapeutaRepository;

    public TerapeutaService(TerapeutaRepository terapeutaRepository) {
        this.terapeutaRepository = terapeutaRepository;
    }
    /**
     * Obtiene la lista de terapeutas con nombre, apellidos, evaluación y lista de servicios que prestan.
     * @return Lista de terapeutas con los datos requeridos.
     */
    @Transactional(readOnly = true)
    public List<TerapeutaInfoDTO> obtenerTerapeutasConServicios() {
        List<Terapeuta> terapeutas = terapeutaRepository.findAll();
        List<TerapeutaInfoDTO> terapeutasInfo = new ArrayList<>();

        for (Terapeuta terapeuta : terapeutas) {
            TerapeutaInfoDTO infoDTO = new TerapeutaInfoDTO();
            infoDTO.setNombre(terapeuta.getNombre());
            infoDTO.setApePaterno(terapeuta.getApe_paterno());
            infoDTO.setApeMaterno(terapeuta.getApe_materno());
            infoDTO.setEvaluacion(terapeuta.getEvaluacion());

            // Obtener servicios que presta el terapeuta
            List<String> servicios = new ArrayList<>();
            for (ServicioTerapeuta servicioTerapeuta : terapeuta.getServicios()) {
                String nombreEspecialidad = servicioTerapeuta.getServicio().getEspecialidad().getNombre();
                String nombreServicio = servicioTerapeuta.getServicio().getNombre();
                servicios.add(nombreEspecialidad + ":" + nombreServicio);
            }

            infoDTO.setServicios(servicios);
            terapeutasInfo.add(infoDTO);
        }

        return terapeutasInfo;
    }
    
    @Transactional
    public void guardar(Terapeuta terapeuta) {
        terapeutaRepository.save(terapeuta);
    }   

}

