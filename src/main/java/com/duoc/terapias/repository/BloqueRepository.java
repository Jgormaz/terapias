
package com.duoc.terapias.repository;

import com.duoc.terapias.model.Bloque;
import com.duoc.terapias.model.Dia;
import java.util.Date;
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
    
    List<Bloque> findByDiaAndHoraIniAndHoraFin(Dia dia, Integer horaIni, Integer horaFin);
    
    @Query(value = "SELECT b.* " +
                   "FROM bloque b " +
                   "INNER JOIN dia d ON b.ID_dia = d.ID_dia " +
                   "INNER JOIN semana s ON d.ID_semana = s.ID_semana " +
                   "INNER JOIN calendario c ON s.ID_calendario = c.ID_calendario " +
                   "WHERE c.ID_terapeuta = :idTerapeuta " +
                   "AND d.fecha = :fecha " +
                   "AND b.hora_ini = :horaIni " +
                   "AND b.hora_fin = :horaFin",
           nativeQuery = true)
    List<Bloque> findByTerapeutaAndFechaAndHoraIniAndHoraFin(
        @Param("idTerapeuta") String idTerapeuta,
        @Param("fecha") Date fecha,
        @Param("horaIni") Integer horaIni,
        @Param("horaFin") Integer horaFin
    );

}



