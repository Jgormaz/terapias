
package com.duoc.terapias.repository;

import com.duoc.terapias.model.Atencion;
import com.duoc.terapias.model.Calendario;
import com.duoc.terapias.model.Terapeuta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarioRepository extends JpaRepository<Calendario, String> {
    
    Optional<Calendario> findByTerapeuta(Terapeuta terapeuta);
    
    Optional<Calendario> findByTerapeutaAndAtencion(Terapeuta terapeuta, Atencion atencion);
    
    List<Calendario> findAllByTerapeuta(Terapeuta terapeuta);

}


