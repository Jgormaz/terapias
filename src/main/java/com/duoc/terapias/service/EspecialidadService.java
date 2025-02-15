

package com.duoc.terapias.service;

import com.duoc.terapias.model.Especialidad;
import com.duoc.terapias.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    
    public Especialidad guardar(Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }
    
    public void eliminar(String id) {
        especialidadRepository.deleteById(id);
    }
}
