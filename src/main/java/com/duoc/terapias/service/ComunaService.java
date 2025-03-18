package com.duoc.terapias.service;

import com.duoc.terapias.model.Comuna;
import com.duoc.terapias.repository.ComunaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;

    /**
     * Retorna la lista de todas las comunas.
     */
    public List<Comuna> obtenerTodas() {
        return comunaRepository.findAll();
    }

    /**
     * Busca una comuna por su ID.
     *
     * @param id El identificador de la comuna.
     * @return La comuna encontrada o null si no existe.
     */
    public Comuna buscarPorId(String id) {
        return comunaRepository.findById(id).orElse(null);
    }
}

