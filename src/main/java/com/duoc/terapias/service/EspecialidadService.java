

package com.duoc.terapias.service;

import com.duoc.terapias.model.Especialidad;
import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.repository.EspecialidadRepository;
import com.duoc.terapias.repository.ServicioRepository;
import com.duoc.terapias.repository.TerapeutaRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EspecialidadService {
    
    @Autowired
    private EspecialidadRepository especialidadRepository;
    
    @Autowired
    private TerapeutaRepository terapeutaRepository;
    
    @Autowired
    private ServicioRepository servicioRepository;
    
    public List<Especialidad> obtenerTodas() {
        return especialidadRepository.findAll();
    }
    
    public Optional<Especialidad> obtenerPorId(String id) {
        return especialidadRepository.findById(id);
    }
    
    public Especialidad guardar(Especialidad especialidad) {
        if (especialidad.getServicios() != null) {
            for (Servicio servicio : especialidad.getServicios()) {
                servicio.setEspecialidad(especialidad);
            }
        }
        System.out.println("Guardando especialidad con ID: " + especialidad.getIdEspecialidad());
        return especialidadRepository.save(especialidad);
    }
    
    public void agregarServicioAEspecialidad(String idEspecialidad, String id, String nombre, String descripcion) {
        Optional<Especialidad> opt = especialidadRepository.findById(idEspecialidad);
        if (opt.isPresent()) {
            Especialidad esp = opt.get();
            Servicio nuevo = new Servicio();
            nuevo.setIdServicio(id);
            nuevo.setNombre(nombre);
            nuevo.setDescripcion(descripcion);
            nuevo.setEspecialidad(esp); 
          

            esp.getServicios().add(nuevo);
            especialidadRepository.save(esp);
        }
    }
    
    /*@Transactional
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
    }*/

    
    @Transactional
    public void eliminar(String idEspecialidad) {
        Optional<Especialidad> optional = especialidadRepository.findById(idEspecialidad);
        if (optional.isPresent()) {
            Especialidad especialidad = optional.get();

            // Elimina primero las relaciones en servicioterapeuta
             for (Servicio servicio : new ArrayList<>(especialidad.getServicios())) {
                terapeutaRepository.deleteByServicio(servicio); // 
                // Quitar servicio de la especialidad y eliminarlo
                especialidad.removeServicio(servicio);
                servicioRepository.delete(servicio);
            }

            // Luego elimina la especialidad (con sus servicios en cascada)
            especialidadRepository.delete(especialidad);
        }
    }
    

}
