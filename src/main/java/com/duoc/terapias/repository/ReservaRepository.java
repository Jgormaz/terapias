
package com.duoc.terapias.repository;


import com.duoc.terapias.model.EstadoReserva;
import com.duoc.terapias.model.Paciente;
import com.duoc.terapias.model.Reserva;
import com.duoc.terapias.model.Terapeuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.repository.query.Param;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, String> {

    /*@Query("SELECT new com.duoc.terapias.dto.ReservaDTO(" +
           "r.ID_reserva, " +
           "r.atencion.terapeuta.nombre, " +
           "r.atencion.terapeuta.ape_paterno, " +
           "r.atencion.servicio.nombre, " +
           "r.precio, " +
           "r.horaIni, " +
           "r.paciente.nombre, " +
           "r.paciente.ape_paterno, " +
           "r.paciente.correo, " +
           "r.estado) " +
           "FROM Reserva r " +
           "WHERE r.atencion.terapeuta.idTerapeuta = :idTerapeuta")
    List<Reserva> findReservasByTerapeuta(@Param("idTerapeuta") String idTerapeuta);*/
    
    @Query("SELECT r FROM Reserva r WHERE r.atencion.terapeuta.idTerapeuta = :idTerapeuta")
    List<Reserva> findReservasByTerapeuta(String idTerapeuta);
    
    int countByAtencion_TerapeutaAndEstado(Terapeuta terapeuta, EstadoReserva estado);
    
    @Query("SELECT DISTINCT r.paciente FROM Reserva r WHERE r.atencion.terapeuta.idTerapeuta = :idTerapeuta")
    List<Paciente> findPacientesByTerapeutaId(@Param("idTerapeuta") String idTerapeuta);
    

    /*    // Obtener todas las reservas de un terapeuta con sus fechas
    @Query("SELECT r FROM Reserva r " +
           "JOIN FETCH r.bloque b " +
           "JOIN FETCH b.dia d " +
           "WHERE r.atencion.terapeuta.idTerapeuta = :idTerapeuta")
    List<Reserva> findReservasByTerapeutaId(@Param("idTerapeuta") String idTerapeuta);

    // Filtrar las reservas por paciente, fecha y estado
    @Query("SELECT r FROM Reserva r " +
           "JOIN FETCH r.bloque b " +
           "JOIN FETCH b.dia d " +
           "WHERE r.atencion.terapeuta.idTerapeuta = :idTerapeuta " +
           "AND (r.paciente.nombre LIKE %:filtroPaciente% " +
           "OR r.paciente.apePaterno LIKE %:filtroPaciente%) " +
           "AND (d.fecha BETWEEN :fechaInicio AND :fechaFin) " +
           "AND r.estado = :filtroEstado")
    List<Reserva> findReservasByFilters(@Param("idTerapeuta") String idTerapeuta,
                                        @Param("filtroPaciente") String filtroPaciente,
                                        @Param("fechaInicio") Date fechaInicio,
                                        @Param("fechaFin") Date fechaFin,
                                        @Param("filtroEstado") EstadoReserva filtroEstado); */
    
    // Consulta personalizada para obtener el número de reservas por estado para cada terapeuta
    @Query("SELECT r.atencion.terapeuta.idTerapeuta AS terapeutaId, " +
           "   r.atencion.terapeuta.nombre AS terapeutaNombre, r.atencion.terapeuta.ape_paterno AS terapeutaApePaterno," +
           "   COUNT(CASE WHEN r.estado = 'AGENDADA' THEN 1 END) AS agendadas, " +
           "   COUNT(CASE WHEN r.estado = 'CANCELADA' THEN 1 END) AS canceladas, " +
           "   COUNT(CASE WHEN r.estado = 'REAGENDADA' THEN 1 END) AS reagendadas, " +
           "   COUNT(CASE WHEN r.estado = 'NOSHOW' THEN 1 END) AS noshow, " +
           "   COUNT(CASE WHEN r.estado = 'COMPLETADA' THEN 1 END) AS completadas, " +
           "   COUNT(CASE WHEN r.estado = 'EVALUADA' THEN 1 END) AS evaluadas " +
           "FROM Reserva r " +
           "GROUP BY r.atencion.terapeuta.idTerapeuta, r.atencion.terapeuta.nombre")
    List<Object[]> findResumenReservasPorTerapeuta();
    
 


}
