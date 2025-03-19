

package com.duoc.terapias.service;

import com.duoc.terapias.model.Especialidad;
import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EspecialidadService {
    
    @Autowired
    private EspecialidadRepository especialidadRepository;
    
    public List<Especialidad> obtenerTodas() {
        return especialidadRepository.findAll();
    }
    
    public Optional<Especialidad> obtenerPorId(String id) {
        return especialidadRepository.findById(id);
    }
    
    /*public Especialidad guardar(Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }*/
    
    @Transactional
    public Especialidad guardar(Especialidad especialidad) {
        Especialidad especialidadExistente = especialidadRepository.findById(especialidad.getIdEspecialidad())
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));

        // Eliminar servicios que ya no están
        especialidadExistente.getServicios()
                .removeIf(servicio -> !especialidad.getServicios().contains(servicio));

        // Agregar o actualizar los nuevos servicios
        for (Servicio servicio : especialidad.getServicios()) {
            if (!especialidadExistente.getServicios().contains(servicio)) {
                especialidadExistente.addServicio(servicio);
            }
        }

        // Actualizar los campos básicos
        especialidadExistente.setNombre(especialidad.getNombre());
        especialidadExistente.setDescripcion(especialidad.getDescripcion());

        return especialidadRepository.save(especialidadExistente);
    }

    
    public void eliminar(String id) {
        especialidadRepository.deleteById(id);
    }
}
