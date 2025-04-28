
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


}
