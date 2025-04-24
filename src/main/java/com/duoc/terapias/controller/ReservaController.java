
package com.duoc.terapias.controller;

import com.duoc.terapias.dto.PacienteDTO;
import com.duoc.terapias.dto.ReservaDTO;
import com.duoc.terapias.model.Bloque;
import com.duoc.terapias.model.Categoria;
import com.duoc.terapias.model.Comuna;
import com.duoc.terapias.model.Paciente;
import com.duoc.terapias.model.Region;
import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.model.Terapeuta;
import com.duoc.terapias.repository.BloqueRepository;
import com.duoc.terapias.repository.CategoriaRepository;
import com.duoc.terapias.repository.ComunaRepository;
import com.duoc.terapias.repository.PacienteRepository;
import com.duoc.terapias.repository.RegionRepository;
import com.duoc.terapias.service.CalendarioService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private CalendarioService calendarioService;

    @Autowired
    private BloqueRepository bloqueRepository;

    @Autowired
    private PacienteRepository pacienteRepository; 
    
    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private RegionRepository regionRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/nueva")
    public String mostrarFormularioReserva(
            @RequestParam("bloqueId") String bloqueId,
            @RequestParam("terapeutaId") String idTerapeuta,
            @RequestParam("servicioId") String idServicio,
            Model model) {

        Optional<Bloque> bloqueOpt = bloqueRepository.findById(bloqueId);
        if (!bloqueOpt.isPresent()) {
            return "redirect:/error";
        }

        Bloque bloque = bloqueOpt.get();

        ReservaDTO dto = new ReservaDTO();
        dto.setIdReserva(UUID.randomUUID().toString());

        // Info del terapeuta y servicio
        Terapeuta terapeuta = bloque.getDia().getSemana().getCalendario().getTerapeuta();
        Servicio servicio = bloque.getDia().getSemana().getCalendario().getAtencion().getServicio();

        dto.setNombreTerapeuta(terapeuta.getNombre());
        dto.setApellidoPaternoTerapeuta(terapeuta.getApe_paterno());
        dto.setNombreServicio(servicio.getNombre());
        dto.setPrecio(bloque.getPrecio());

        // Fecha y hora
        LocalDate fecha = bloque.getDia().getFecha().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        dto.setFecha(fecha);
        dto.setHoraIni(LocalDateTime.of(fecha, bloque.getHoraInicioLocalTime()));
        dto.setHoraFin(LocalDateTime.of(fecha, bloque.getHoraFinLocalTime()));

        // IDs para mantener contexto
        dto.setIdBloque(bloqueId);
        dto.setIdServicio(idServicio);
        dto.setIdTerapeuta(idTerapeuta);

        // Paciente vacío
        PacienteDTO pacienteDTO = new PacienteDTO();
        pacienteDTO.setPacienteExistente(false);
        dto.setPaciente(pacienteDTO);

        model.addAttribute("reservaDTO", dto);
        return "formulario-reserva";
    }

    @PostMapping("/verificar-paciente")
    public String verificarPaciente(
            @ModelAttribute("reservaDTO") ReservaDTO dto,
            Model model) {

        // Buscar paciente por correo
        Optional<Paciente> pacienteOpt = pacienteRepository.findByCorreo(dto.getPaciente().getCorreo());

        PacienteDTO pacienteDTO = new PacienteDTO();
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            pacienteDTO.setNombre(paciente.getNombre());
            pacienteDTO.setApellidoPaterno(paciente.getApe_paterno());
            pacienteDTO.setApellidoMaterno(paciente.getApe_materno());
            pacienteDTO.setTelefono(paciente.getTelefono());
            pacienteDTO.setDireccion(paciente.getDireccion());
            pacienteDTO.setCorreo(paciente.getCorreo());
            pacienteDTO.setPacienteExistente(true);
        } else {
            pacienteDTO.setCorreo(dto.getPaciente().getCorreo());
            pacienteDTO.setPacienteExistente(false);
        }
        dto.setPaciente(pacienteDTO);

        // 🔁 Recuperar el bloque para volver a cargar los datos del terapeuta, fecha, hora, precio, etc.
        Optional<Bloque> bloqueOpt = bloqueRepository.findById(dto.getIdBloque());
        if (bloqueOpt.isPresent()) {
            Bloque bloque = bloqueOpt.get();
            dto.setNombreTerapeuta(bloque.getDia().getSemana()
                    .getCalendario().getTerapeuta().getNombre());
            dto.setApellidoPaternoTerapeuta(bloque.getDia().getSemana()
                    .getCalendario().getTerapeuta().getApe_paterno());
            dto.setNombreServicio(bloque.getDia().getSemana()
                    .getCalendario().getAtencion().getServicio().getNombre());

            LocalDate fecha = bloque.getDia().getFecha().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
            dto.setFecha(fecha);
            dto.setHoraIni(LocalDateTime.of(fecha, bloque.getHoraInicioLocalTime()));
            dto.setHoraFin(LocalDateTime.of(fecha, bloque.getHoraFinLocalTime()));
            dto.setPrecio(bloque.getPrecio());
        }

        model.addAttribute("reservaDTO", dto);
        return "formulario-reserva";
    }


    @PostMapping("/guardar-paciente")
    public String guardarPaciente(@ModelAttribute("reservaDTO") ReservaDTO dto, Model model) {
        PacienteDTO pacienteDTO = dto.getPaciente();

        if (pacienteDTO != null && !pacienteDTO.isPacienteExistente()) {
            Paciente paciente = new Paciente();
            paciente.setID_paciente(pacienteDTO.getCorreo());
            paciente.setNombre(pacienteDTO.getNombre());
            paciente.setApe_paterno(pacienteDTO.getApellidoPaterno());
            paciente.setApe_materno(pacienteDTO.getApellidoMaterno());
            paciente.setTelefono(pacienteDTO.getTelefono());
            paciente.setDireccion(pacienteDTO.getDireccion());
            paciente.setCorreo(pacienteDTO.getCorreo());

            // 💡 Recuperar comuna y región desde la base de datos
            Comuna comuna = comunaRepository.findById("COM001").orElse(null);
            Region region = regionRepository.findById("REG001").orElse(null);

            if (comuna == null || region == null) {
                model.addAttribute("mensaje", "Error: comuna o región no encontrada.");
                model.addAttribute("reservaDTO", dto);
                return "formulario-reserva";
            }

            paciente.setComuna(comuna);
            paciente.setRegion(region);

            paciente.setAtenciones(0);
            paciente.setEvaluacion(0);
            Categoria categoria = categoriaRepository.findById("CAT0001").orElse(null);
            paciente.setCategoria(categoria);

            pacienteRepository.save(paciente);
        }

        model.addAttribute("mensaje", "Paciente registrado correctamente.");
        model.addAttribute("reservaDTO", dto);
        return "formulario-reserva";
    }




    // Puedes agregar este método en el futuro cuando quieras confirmar la reserva
    // @PostMapping("/confirmar")
    // public String confirmarReserva(@ModelAttribute("reservaDTO") ReservaDTO dto, Model model) {
    //     // Guardar reserva en base de datos...
    //     return "redirect:/reservas/confirmacion";
    // }
}




