
package com.duoc.terapias.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.repository.ServicioRepository;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    public List<Servicio> obtenerServiciosPorEspecialidad(String especialidadId) {
        return servicioRepository.findByEspecialidad_IdEspecialidad(especialidadId);
    }

    public void guardarServicio(Servicio servicio) {
        servicioRepository.save(servicio);
    }

    public Optional<Servicio> obtenerServicioPorId(String id) {
        return servicioRepository.findById(id);
    }

    public void eliminarServicio(String id) {
        servicioRepository.deleteById(id);
    }
}

