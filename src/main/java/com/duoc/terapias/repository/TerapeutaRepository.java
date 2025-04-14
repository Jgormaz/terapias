
package com.duoc.terapias.repository;

import com.duoc.terapias.model.Terapeuta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface TerapeutaRepository extends JpaRepository<Terapeuta, String> {
    Optional<Terapeuta> findByUserName(String userName);
    
    @Query("SELECT DISTINCT t FROM Terapeuta t " +
       "JOIN t.servicios st " +
       "JOIN st.servicio s " +
       "JOIN s.especialidad e " +
       "WHERE e.idEspecialidad = :especialidadId")
    List<Terapeuta> findByEspecialidadId(@Param("especialidadId") String especialidadId);


}


