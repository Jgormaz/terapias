
package com.duoc.terapias.service;

import com.duoc.terapias.dto.PacienteDTO;
import com.duoc.terapias.dto.ReservaDTO;
import com.duoc.terapias.model.EstadoReserva;
import com.duoc.terapias.model.Paciente;
import com.duoc.terapias.model.Reserva;
import com.duoc.terapias.model.Terapeuta;
import com.duoc.terapias.repository.BloqueRepository;
import com.duoc.terapias.repository.ReservaRepository;
import com.duoc.terapias.repository.TerapeutaRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;


    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }
 
    @Autowired
    private BloqueRepository bloqueRepository;
    
    @Autowired
    private TerapeutaRepository terapeutaRepository;

   /* @Transactional(readOnly = true)
    public List<Reserva> listarReservasPorTerapeuta(String idTerapeuta) {
        return reservaRepository.findReservasByTerapeuta(idTerapeuta);
    }*/
    
    @Transactional(readOnly = true)
    public List<ReservaDTO> listarReservasPorTerapeuta(String idTerapeuta) {
        List<Reserva> reservas = reservaRepository.findReservasByTerapeuta(idTerapeuta);

        return reservas.stream().map(reserva -> {
            ReservaDTO dto = new ReservaDTO();

            // Setear paciente
            PacienteDTO pacienteDTO = new PacienteDTO();
            pacienteDTO.setNombre(reserva.getPaciente().getNombre());
            pacienteDTO.setApellidoPaterno(reserva.getPaciente().getApe_paterno());
            dto.setPaciente(pacienteDTO);

            // Setear otros campos
            dto.setIdReserva(reserva.getIdReserva());
            dto.setNombreServicio(reserva.getAtencion().getServicio().getNombre());
            dto.setHoraIni(convertirDateALocalDateTime(reserva.getHoraIni()));
            dto.setHoraFin(convertirDateALocalDateTime(reserva.getHoraFin()));
            dto.setPrecio(reserva.getPrecio());
            dto.setEstado(reserva.getEstado().name()); // Asumo que estado es un Enum
            LocalDate fecha = obtenerFechaPorReserva(reserva.getIdReserva());
            System.out.println("FECHA " + fecha);
            dto.setFecha(fecha);

            return dto;
        }).collect(Collectors.toList());
    }
    
    public LocalDate obtenerFechaPorReserva(String idReserva) {
        return bloqueRepository.findByReserva_IdReserva(idReserva)
                .map(bloque -> convertirDateALocalDate(bloque.getDia().getFecha()))
                .orElse(null);
    }

    private LocalDate convertirDateALocalDate(Date fecha) {
        if (fecha == null) {
            return null;
        }
        return fecha.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    
    private LocalDateTime convertirDateALocalDateTime(Date fecha) {
        if (fecha == null) {
            return null;
        }
        return fecha.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    
    public void cambiarEstado(String idReserva, EstadoReserva nuevoEstado) {
        Reserva reserva = reservaRepository.findById(idReserva)
                            .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        reserva.setEstado(nuevoEstado);
        reservaRepository.save(reserva);
    }
    
    @Transactional
    public void evaluarReserva(String idReserva, int nuevaNota) {
        Reserva reserva = reservaRepository.findById(idReserva)
            .orElseThrow(() -> new IllegalStateException("Reserva no encontrada"));

        if (reserva.getEstado() == EstadoReserva.EVALUADA) {
            throw new IllegalStateException("Esta reserva ya fue evaluada, no se puede volver a evaluar");
        }
        else if (reserva.getEstado() != EstadoReserva.COMPLETADA) {
            throw new IllegalStateException("Solo puedes evaluar reservas completadas");
        }

        Terapeuta terapeuta = reserva.getAtencion().getTerapeuta();
        if (terapeuta == null) {
            throw new IllegalStateException("La reserva no tiene terapeuta asociado");
        }

        // Obtener cuántas reservas COMPLETADAS tiene el terapeuta (antes de esta evaluación)
        long cantidadCompletadas = reservaRepository.countByAtencion_TerapeutaAndEstado(terapeuta, EstadoReserva.COMPLETADA);

        if (cantidadCompletadas <= 0) {
            throw new IllegalStateException("Error en la cantidad de reservas completadas");
        }

        // Calcular nueva evaluación
        Double evaluacionActual = terapeuta.getEvaluacion();
        if (evaluacionActual == null) {
            evaluacionActual = 0.0;
        }

        double nuevaEvaluacion = ((evaluacionActual * (cantidadCompletadas - 1)) + nuevaNota) / cantidadCompletadas;
        terapeuta.setEvaluacion(nuevaEvaluacion);

        // Actualizar terapeuta
        terapeutaRepository.save(terapeuta);

        // Cambiar estado de la reserva
        reserva.setEstado(EstadoReserva.EVALUADA);
        reservaRepository.save(reserva);
    }
    
    public List<Paciente> obtenerPacientesPorTerapeuta(String idTerapeuta) {
        return reservaRepository.findPacientesByTerapeutaId(idTerapeuta);
    }
    


}
