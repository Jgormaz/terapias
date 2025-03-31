
package com.duoc.terapias.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.duoc.terapias.model.Servicio;
import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, String> {
    List<Servicio> findByEspecialidad_IdEspecialidad(String idEspecialidad);
    //List<Servicio> findAllById(List<String> serviciosIds);
    //List<Servicio> findAll();
}

