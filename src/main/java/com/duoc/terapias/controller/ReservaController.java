
package com.duoc.terapias.controller;

import com.duoc.terapias.dto.ReservaDTO;
import com.duoc.terapias.model.Bloque;
import com.duoc.terapias.model.Paciente;
import com.duoc.terapias.repository.BloqueRepository;
import com.duoc.terapias.repository.PacienteRepository;
import com.duoc.terapias.service.CalendarioService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private CalendarioService calendarioService;   // Para contexto de calendario

    @Autowired
    private BloqueRepository bloqueRepository;      // Para cargar el bloque en sí

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping("/nueva")
    public String mostrarFormularioReserva(
            @RequestParam("bloqueId") String bloqueId,
            @RequestParam("terapeutaId") String idTerapeuta,
            @RequestParam("servicioId") String idServicio,
            Model model) {

        // Cargar bloque
        Optional<Bloque> bloqueOpt = bloqueRepository.findById(bloqueId);
        if (!bloqueOpt.isPresent()) {
            return "redirect:/error";
        }
        Bloque bloque = bloqueOpt.get();

        // Llenar DTO de reserva
        ReservaDTO dto = new ReservaDTO();
        dto.setIdReserva(UUID.randomUUID().toString());
        dto.setNombreTerapeuta(bloque.getDia().getSemana()
                                 .getCalendario()
                                 .getTerapeuta().getNombre());
        dto.setNombreServicio(bloque.getDia().getSemana()
                                .getCalendario()
                                .getAtencion()
                                .getServicio().getNombre());
        
        LocalDate fecha = bloque.getDia().getFecha().toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

        dto.setHoraIni(LocalDateTime.of(fecha, bloque.getHoraInicioLocalTime()));
        dto.setHoraFin(LocalDateTime.of(fecha, bloque.getHoraFinLocalTime()));
        
        Date fechaDia = bloque.getDia().getFecha(); // Tipo java.util.Date
        fecha = fechaDia.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        dto.setFecha(fecha);
        dto.setIdBloque(bloqueId);
        dto.setIdServicio(idServicio);
        dto.setIdTerapeuta(idTerapeuta);
        dto.setPrecio(bloque.getPrecio());
        dto.setCorreoPaciente("");  // quedará vacío para llenar en el formulario

        model.addAttribute("reservaDTO", dto);
        model.addAttribute("pacienteExistente", false);
        return "formulario-reserva";
    }

    @PostMapping("/verificar-paciente")
    public String verificarPaciente(
            @ModelAttribute("reserva") ReservaDTO dto,
            @RequestParam("correoPaciente") String correo,
            Model model) {

        Optional<Paciente> pacienteOpt = pacienteRepository.findByCorreo(correo);
        if (pacienteOpt.isPresent()) {
            model.addAttribute("paciente", pacienteOpt.get());
        } else {
            model.addAttribute("paciente", new Paciente());
        }
        model.addAttribute("reserva", dto);
        return "formulario-reserva";
    }
}
