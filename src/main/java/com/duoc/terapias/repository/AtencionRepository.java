package com.duoc.terapias.repository;

import com.duoc.terapias.model.Atencion;
import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.model.Terapeuta;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtencionRepository extends JpaRepository<Atencion, String> {
    Optional<Atencion> findByTerapeutaAndServicio(Terapeuta terapeuta, Servicio servicio);
    Optional<Atencion> findByTerapeuta_IdTerapeutaAndServicio_IdServicio(String idTerapeuta, String idServicio);

}

