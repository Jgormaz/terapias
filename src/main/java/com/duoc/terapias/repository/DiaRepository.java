
package com.duoc.terapias.repository;

import com.duoc.terapias.model.Dia;
import com.duoc.terapias.model.Semana;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaRepository extends JpaRepository<Dia, String> {
    
    // Obtener los días de una semana
    List<Dia> findBySemana(Semana semana);
}



