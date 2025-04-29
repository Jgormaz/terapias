package com.duoc.terapias.service;

import com.duoc.terapias.model.Atencion;
import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.model.ServicioTerapeuta;
import com.duoc.terapias.model.Terapeuta;
import com.duoc.terapias.repository.ServicioRepository;
import com.duoc.terapias.repository.ServicioTerapeutaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioTerapeutaService {

    @Autowired
    private ServicioTerapeutaRepository servicioTerapeutaRepository;

    @Autowired
    private ServicioRepository servicioRepository;
    
    @Autowired
    private CalendarioService calendarioService;
    
    @Autowired
    private AtencionService atencionService;

    @Transactional
    public void eliminarPorIdTerapeuta(String idTerapeuta) {
        servicioTerapeutaRepository.deleteByTerapeuta_IdTerapeuta(idTerapeuta);
    }

    public void asociarServiciosATerapeuta(Terapeuta terapeuta, List<String> serviciosIds) {
        List<Servicio> servicios = servicioRepository.findAllById(serviciosIds);

        for (Servicio servicio : servicios) {
            ServicioTerapeuta servicioTerapeuta = new ServicioTerapeuta();
            servicioTerapeuta.setTerapeuta(terapeuta);
            servicioTerapeuta.setServicio(servicio);
            servicioTerapeutaRepository.save(servicioTerapeuta);
            Atencion atencion = atencionService.crearAtencionPorDefecto(terapeuta, servicio);
            calendarioService.crearCalendarioParaTerapeuta(terapeuta, atencion);
        }
    }
    
    // Obtener los servicios asociados a un terapeuta por su userName
    public List<Servicio> findServiciosByUserName(String userName) {
        return servicioTerapeutaRepository.findServiciosByUserName(userName);
    }
    
    @Transactional
    public void desasociarServiciosDeTerapeuta(String idTerapeuta, List<String> idsServicios) {
        for (String idServicio : idsServicios) {
            servicioTerapeutaRepository.deleteByTerapeuta_IdTerapeutaAndServicio_IdServicio(idTerapeuta, idServicio);
        }
    }

}

