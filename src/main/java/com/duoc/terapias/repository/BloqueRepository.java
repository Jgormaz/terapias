
package com.duoc.terapias.repository;

import com.duoc.terapias.model.Bloque;
import com.duoc.terapias.model.Dia;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BloqueRepository extends JpaRepository<Bloque, String> {

    @Query("SELECT b FROM Bloque b WHERE b.dia.ID_dia = :idDia")
    List<Bloque> findByDiaId(@Param("idDia") String idDia);
    
    // Obtener los bloques de un día específico
    List<Bloque> findByDia(Dia dia);
    
}



