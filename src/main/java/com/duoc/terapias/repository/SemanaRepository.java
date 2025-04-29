
package com.duoc.terapias.repository;

import com.duoc.terapias.model.Calendario;
import com.duoc.terapias.model.Semana;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SemanaRepository extends JpaRepository<Semana, String> {
        
     // Obtener todas las semanas que pertenecen a un calendario
    List<Semana> findByCalendario(Calendario calendario);
}

