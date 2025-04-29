
package com.duoc.terapias.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.model.ServicioTerapeuta;
import com.duoc.terapias.repository.ServicioRepository;
import com.duoc.terapias.repository.ServicioTerapeutaRepository;
import java.util.UUID;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;
    
    @Autowired
    private ServicioTerapeutaRepository servicioTerapeutaRepository;

    public List<Servicio> obtenerServiciosPorEspecialidad(String especialidadId) {
        return servicioRepository.findByEspecialidad_IdEspecialidad(especialidadId);
    }

    public void guardarServicio(Servicio servicio) {
        if (servicio.getIdServicio() == null) {
            servicio.setIdServicio(UUID.randomUUID().toString());  // Asignar UUID manualmente
        }
        servicioRepository.save(servicio);
    }
    
    public Optional<Servicio> obtenerServicioPorId(String id) {
        return servicioRepository.findById(id);
    }
    
    public List<Servicio> obtenerTodosLosServicios() {
        return servicioRepository.findAll();
    }

    /*public void eliminarServicio(String id) {
        servicioRepository.deleteById(id);
    }*/
    
    public void eliminarPorId(String id) {
        // Eliminar las asociaciones antes de eliminar el servicio
        List<ServicioTerapeuta> asociaciones = servicioTerapeutaRepository.findByServicio_IdServicio(id);  // Método que obtiene las asociaciones por el ID del servicio
        for (ServicioTerapeuta asociacion : asociaciones) {
            servicioTerapeutaRepository.delete(asociacion);  // Eliminar cada asociación
        }

        // Ahora se puede eliminar el servicio
        servicioRepository.deleteById(id);
    }
    
 
}

