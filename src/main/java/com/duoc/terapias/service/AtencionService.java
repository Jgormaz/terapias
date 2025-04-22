package com.duoc.terapias.service;

import com.duoc.terapias.model.Atencion;
import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.model.Terapeuta;
import com.duoc.terapias.repository.AtencionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AtencionService {

    @Autowired
    private AtencionRepository atencionRepository;

    public Atencion crearAtencionPorDefecto(Terapeuta terapeuta, Servicio servicio) {
        Atencion atencion = new Atencion();

        String idAtencion = terapeuta.getIdTerapeuta() + "_" + servicio.getIdServicio();
        atencion.setID_atencion(idAtencion);

        atencion.setTerapeuta(terapeuta);
        atencion.setServicio(servicio);
        atencion.setTamanoBloque(60);
        atencion.setDuracionMin(1);
        atencion.setDuracionMax(1);
        atencion.setPrecioBloque(30000);
        atencion.setDescuentoBloqueConsecutivo(0);
        atencion.setBloquesEntreReservas(0);
        atencion.setBloqueoPorDistancia(false);
        atencion.setDistanciaMaxTransporte(-1);
        atencion.setDistanciaMaxDirPrincip(-1);

        return atencionRepository.save(atencion);
    }
} 
