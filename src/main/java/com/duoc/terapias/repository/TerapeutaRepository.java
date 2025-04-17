
package com.duoc.terapias.repository;

import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.model.Terapeuta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TerapeutaRepository extends JpaRepository<Terapeuta, String> {
    Optional<Terapeuta> findByUserName(String userName);
    
    @Query("SELECT DISTINCT t FROM Terapeuta t " +
       "JOIN t.servicios st " +
       "JOIN st.servicio s " +
       "JOIN s.especialidad e " +
       "WHERE e.idEspecialidad = :especialidadId")
    List<Terapeuta> findByEspecialidadId(@Param("especialidadId") String especialidadId);
    
    @Modifying
    @Query("DELETE FROM ServicioTerapeuta st WHERE st.servicio = :servicio")
    void deleteByServicio(@Param("servicio") Servicio servicio);
    
    @Modifying
    @Transactional
    @Query("UPDATE Terapeuta t SET t.enabled = :enabled WHERE t.idTerapeuta = :id")
    void updateEnabledStatus(@Param("id") String id, @Param("enabled") boolean enabled);
    
    /*@Modifying
    
    @Query("UPDATE Terapeuta t SET t.enabled = :enabled WHERE t.idTerapeuta = :id")
    void updateEnabledStatus(String id, boolean enabled);*/




}


