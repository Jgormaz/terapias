
package com.duoc.terapias.controller;

import com.duoc.terapias.dto.PacienteDTO;
import com.duoc.terapias.dto.ReservaDTO;
import com.duoc.terapias.model.Atencion;
import com.duoc.terapias.model.Bloque;
import com.duoc.terapias.model.Categoria;
import com.duoc.terapias.model.Comuna;
import com.duoc.terapias.model.EstadoReserva;
import com.duoc.terapias.model.Paciente;
import com.duoc.terapias.model.Region;
import com.duoc.terapias.model.Reserva;
import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.model.Terapeuta;
import com.duoc.terapias.repository.AtencionRepository;
import com.duoc.terapias.repository.BloqueRepository;
import com.duoc.terapias.repository.CategoriaRepository;
import com.duoc.terapias.repository.ComunaRepository;
import com.duoc.terapias.repository.PacienteRepository;
import com.duoc.terapias.repository.RegionRepository;
import com.duoc.terapias.repository.ReservaRepository;
import com.duoc.terapias.service.CalendarioService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.duoc.terapias.util.PasswordGenerator;

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
    
    @Autowired
    private AtencionRepository atencionRepository;
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private JavaMailSender mailSender;

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
        
        System.out.println(">>> VERIFICAR PACIENTE");
        System.out.println("Terapeuta ID antes: " + dto.getIdTerapeuta());
        System.out.println("Servicio ID antes: " + dto.getIdServicio());


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

        // Recuperar el bloque para volver a cargar los datos del terapeuta, fecha, hora, precio, etc.
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
            dto.setIdTerapeuta(bloque.getDia().getSemana().getCalendario().getTerapeuta().getIdTerapeuta());
            dto.setIdServicio(bloque.getDia().getSemana().getCalendario().getAtencion().getServicio().getIdServicio());
        }

        model.addAttribute("reservaDTO", dto);
        return "formulario-reserva";
    }


    @PostMapping("/enviar-codigo")
    public String guardarPacienteYCrearReserva(@ModelAttribute("reservaDTO") ReservaDTO dto, Model model) {
        PacienteDTO pacienteDTO = dto.getPaciente();

        System.out.println(">>> GUARDAR RESERVA");
        System.out.println("Terapeuta ID: " + dto.getIdTerapeuta());
        System.out.println("Servicio ID: " + dto.getIdServicio());

        Paciente paciente = pacienteRepository.findById(pacienteDTO.getCorreo()).orElse(null);
        Comuna comuna = comunaRepository.findById("COM001").orElse(null);
        Region region = regionRepository.findById("REG001").orElse(null);
            
        if (paciente == null) {
            paciente = new Paciente();
            paciente.setIdPaciente(pacienteDTO.getCorreo());
            paciente.setNombre(pacienteDTO.getNombre());
            paciente.setApe_paterno(pacienteDTO.getApellidoPaterno());
            paciente.setApe_materno(pacienteDTO.getApellidoMaterno());
            paciente.setTelefono(pacienteDTO.getTelefono());
            paciente.setDireccion(pacienteDTO.getDireccion());
            paciente.setCorreo(pacienteDTO.getCorreo());


            Categoria categoria = categoriaRepository.findById("CAT0001").orElse(null);

            if (comuna == null || region == null) {
                model.addAttribute("mensaje", "Error: comuna o región no encontrada.");
                model.addAttribute("reservaDTO", dto);
                return "formulario-reserva";
            }

            paciente.setComuna(comuna);
            paciente.setRegion(region);
            paciente.setCategoria(categoria);
            paciente.setAtenciones(0);
            paciente.setEvaluacion(0);

            pacienteRepository.save(paciente);
        }
        
            // Generar fecha para código
        String fechaDDMMAAAA = dto.getFecha().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        enviarCodigoValidacion(dto.getPaciente().getCorreo(), fechaDDMMAAAA);

        model.addAttribute("reservaDTO", dto);

        return "verificar-codigo";
    }
    
    @PostMapping("/validar-codigo")
    public String validarCodigoYCrearReserva(@ModelAttribute("reservaDTO") ReservaDTO dto,
                                             @RequestParam("codigoIngresado") String codigo,
                                             Model model) {
        System.out.println("validar codigo");
        String fechaDDMMAAAA = dto.getFecha().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        System.out.println("Fecha: " + fechaDDMMAAAA);
        if (!PasswordGenerator.validarCodigo(fechaDDMMAAAA, codigo)) {
            model.addAttribute("mensaje", "Código incorrecto. Intente nuevamente.");
            model.addAttribute("reservaDTO", dto);
            System.out.println("Código incorrecto. Intente nuevamente.");
            return "verificar-codigo";
        }
        
        Paciente paciente = pacienteRepository.findById(dto.getPaciente().getCorreo()).orElse(null);
        Comuna comuna = comunaRepository.findById("COM001").orElse(null);
        Region region = regionRepository.findById("REG001").orElse(null);
        Bloque bloque = bloqueRepository.findById(dto.getIdBloque()).orElse(null);

        try {
            System.out.println("Buscar atención correspondiente " + dto.getIdTerapeuta() + " " + dto.getIdServicio());
            Optional<Atencion> atencionOpt = atencionRepository.findByTerapeuta_IdTerapeutaAndServicio_IdServicio(dto.getIdTerapeuta(), dto.getIdServicio());
            if (!atencionOpt.isPresent()) {
                System.out.println("No se encontró la atención correspondiente.");
                model.addAttribute("mensaje", "No se encontró la atención correspondiente.");
                model.addAttribute("reservaDTO", dto);
                return "formulario-reserva";
            }
            Atencion atencion = atencionOpt.get();

            System.out.println("Crear nueva reserva");
            Reserva reserva = new Reserva();
            String idReserva = "RE" + LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));

            reserva.setID_reserva(idReserva);
            reserva.setPaciente(paciente);
            reserva.setAtencion(atencion);
            reserva.setNombre(paciente.getNombre() + " " + paciente.getApe_paterno());
            reserva.setDireccionAtencion("");

            reserva.setComuna(comuna);
            reserva.setRegion(region);

            Date dateIni = Date.from(dto.getHoraIni().atZone(ZoneId.systemDefault()).toInstant());
            reserva.setHoraIni(dateIni);
            Date dateFin = Date.from(dto.getHoraFin().atZone(ZoneId.systemDefault()).toInstant());
            reserva.setHoraFin(dateFin);
            reserva.setPrecio(dto.getPrecio());
            reserva.setAbono(0);
            reserva.setEstado(EstadoReserva.AGENDADA); // Enum de estado
            //bloque.setDisponible(false);
            calendarioService.marcarBloquesOcupados(bloque, dto.getIdTerapeuta());
            reservaRepository.save(reserva);

            model.addAttribute("mensaje", "Reserva realizada con éxito.");
            System.out.println("Reserva realizada con éxito.");
            return "reserva-exitosa"; 

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR:::" + e);
            model.addAttribute("mensaje", "Error al crear la reserva.");
            model.addAttribute("reservaDTO", dto);
            return "formulario-reserva";
        }
    }
    
    private void enviarCodigoValidacion(String correo, String fechaDDMMAAAA) {
        String codigo = PasswordGenerator.generarCodigo(fechaDDMMAAAA);
        String asunto = "Código de validación para su reserva";
        String mensaje = "Su código de validación es: " + codigo + "\nVálido solo por hoy.";

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(correo);
            mail.setSubject(asunto);
            mail.setText(mensaje);
            mailSender.send(mail);
        } catch (Exception e) {
            System.out.println("Error al enviar correo: " + e.getMessage());
        }
    }


}




