package com.duoc.terapias.service;

import com.duoc.terapias.model.Region;
import com.duoc.terapias.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    /**
     * Retorna la lista de todas las regiones.
     */
    public List<Region> obtenerTodas() {
        return regionRepository.findAll();
    }

    /**
     * Busca una región por su ID.
     *
     * @param id El identificador de la región.
     * @return La región encontrada o null si no existe.
     */
    public Region buscarPorId(String id) {
        return regionRepository.findById(id).orElse(null);
    }
}
