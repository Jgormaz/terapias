package com.duoc.terapias.repository;

import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.model.ServicioTerapeuta;
import com.duoc.terapias.model.Terapeuta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioTerapeutaRepository extends JpaRepository<ServicioTerapeuta, Long> {
    
    // Verifica si ya existe una relación entre un terapeuta y un servicio
    boolean existsByTerapeutaAndServicio(Terapeuta terapeuta, Servicio servicio);
    
    // Método para obtener las asociaciones de un servicioS
    List<ServicioTerapeuta> findByServicio_IdServicio(String idServicio);
    
    // Encuentra todos los servicios asociados a un terapeuta por su userName
    @Query("SELECT st.servicio FROM ServicioTerapeuta st JOIN st.terapeuta t WHERE t.userName = :userName")
    List<Servicio> findServiciosByUserName(@Param("userName") String userName);
    
    void deleteByTerapeuta_IdTerapeuta(String idTerapeuta);

}



