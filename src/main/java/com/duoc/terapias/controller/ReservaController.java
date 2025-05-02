
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
import com.duoc.terapias.repository.TerapeutaRepository;
import com.duoc.terapias.service.CalendarioService;
import com.duoc.terapias.service.ReservaService;
import com.duoc.terapias.service.TerapeutaService;
import com.duoc.terapias.service.UserService;
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
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private CalendarioService calendarioService;
    
    @Autowired
    private TerapeutaService terapeutaService;
    
    @Autowired
    private TerapeutaRepository terapeutaRepository;

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
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ReservaService reservaService;

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
            dto.setCorreoTerapeuta(bloque.getDia().getSemana()
                    .getCalendario().getTerapeuta().getCorreo());

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
        String fechaDDMMAAAA = dto.getFecha().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        if (!PasswordGenerator.validarCodigo(fechaDDMMAAAA, codigo)) {
            model.addAttribute("mensaje", "Código incorrecto. Intente nuevamente.");
            model.addAttribute("reservaDTO", dto);
            return "verificar-codigo";
        }
        
        Paciente paciente = pacienteRepository.findById(dto.getPaciente().getCorreo()).orElse(null);
        Comuna comuna = comunaRepository.findById("COM001").orElse(null);
        Region region = regionRepository.findById("REG001").orElse(null);
        Bloque bloque = bloqueRepository.findById(dto.getIdBloque()).orElse(null);

        try {
            Optional<Atencion> atencionOpt = atencionRepository.findByTerapeuta_IdTerapeutaAndServicio_IdServicio(dto.getIdTerapeuta(), dto.getIdServicio());
            if (!atencionOpt.isPresent()) {
                model.addAttribute("mensaje", "No se encontró la atención correspondiente.");
                model.addAttribute("reservaDTO", dto);
                return "formulario-reserva";
            }
            Atencion atencion = atencionOpt.get();

            Reserva reserva = new Reserva();
            String idReserva = "RE" + LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));

            reserva.setIdReserva(idReserva);
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
            bloque.setReserva(reserva);
            bloqueRepository.save(bloque);
            dto.setIdReserva(reserva.getIdReserva());
            this.enviarCofirmacionReserva(dto);
            
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
    
    private void enviarCancelacionReserva(Reserva reserva, Bloque bloque) {

        String asunto = "Reserva cancelada";
        String mensaje = "Su reserva ha sido cancelada!\n";
        mensaje += "Terapeuta: " + reserva.getAtencion().getTerapeuta().getNombre() + " " + reserva.getAtencion().getTerapeuta().getApe_paterno() + "\n";
        mensaje += "Servicio: " + reserva.getAtencion().getServicio().getNombre() + "\n";
        mensaje += "Paciente: " + reserva.getPaciente().getNombre() + " " + reserva.getPaciente().getApe_materno() + "\n";
        mensaje += "Fecha: " + bloque.getDia().getFecha() + "\n";
        mensaje += "Hora: " + reserva.getHoraIni() + "\n";
        mensaje += "Precio: " + reserva.getPrecio() + "\n";
        mensaje += "ID Reserva: " + reserva.getIdReserva() + "\n";
        
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setSubject(asunto);
            mail.setText(mensaje);
            mail.setTo(reserva.getPaciente().getCorreo());
            mailSender.send(mail);
            mail.setTo(reserva.getAtencion().getTerapeuta().getCorreo());
            mailSender.send(mail);
        } catch (Exception e) {
            System.out.println("Error al enviar correo: " + e.getMessage());
        }
    }
    
    private void enviarCofirmacionReserva(ReservaDTO reserva) {

        String asunto = "Reserva confirmada";
        String mensaje = "Su reserva ha sido confirmada!\n";
        mensaje += "Terapeuta: " + reserva.getNombreTerapeuta() + " " + reserva.getApellidoPaternoTerapeuta() + "\n";
        mensaje += "Servicio: " + reserva.getNombreServicio() + "\n";
        mensaje += "Paciente: " + reserva.getPaciente().getNombre() + " " + reserva.getPaciente().getApellidoPaterno() + "\n";
        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");
        mensaje += "Fecha: " + reserva.getFecha().format(fechaFormatter) + "\n";
        mensaje += "Hora: " + reserva.getHoraIni().format(horaFormatter) + "\n";
        mensaje += "Precio: " + reserva.getPrecio() + "\n";
        mensaje += "ID Reserva: " + reserva.getIdReserva() + "\n";
        
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setSubject(asunto);
            mail.setText(mensaje);
            mail.setTo(reserva.getPaciente().getCorreo());
            mailSender.send(mail);
            mail.setTo(reserva.getCorreoTerapeuta());
            mailSender.send(mail);
        } catch (Exception e) {
            System.out.println("Error al enviar correo: " + e.getMessage());
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
    
    private void enviarCorreoEvaluación(String correo, String idReserva) {
        
        String asunto = "Evalúe a su terapeuta";
        String mensaje = "Ingrese a nuestro sitio y denos su feedback en la opción 'Evaluar' \n";
        mensaje += "Su ID de Reserva es: " + idReserva + "\n";

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
    
   @GetMapping("/verporterapeuta")
   public String verReservasTerapeuta(Model model,
                                      @RequestParam(value = "idTerapeuta", required = false) String idTerapeuta,
                                      @RequestParam(value = "filtroPaciente", required = false) String filtroPaciente,
                                      @RequestParam(value = "filtroFecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate filtroFecha,
                                      @RequestParam(value = "filtroServicio", required = false) String filtroServicio,
                                      @RequestParam(value = "filtroEstado", required = false) String filtroEstado) {

       String rol = userService.getRolUsuarioActual();

       List<ReservaDTO> reservas = List.of();
       String nombreCompletoTerapeuta = "";


       // Buscar reservas según el rol
       if ("ROLE_ADMIN".equals(rol) && idTerapeuta != null && !idTerapeuta.isEmpty()) {
           reservas = reservaService.listarReservasPorTerapeuta(idTerapeuta);

           Terapeuta terapeutaSeleccionado = terapeutaService.obtenerPorId(idTerapeuta);
           if (terapeutaSeleccionado != null) {
               nombreCompletoTerapeuta = terapeutaSeleccionado.getNombre() + " " + terapeutaSeleccionado.getApe_paterno();
           }
       } else if ("ROLE_TERAPEUTA".equals(rol)) {
           String userName = userService.getUsernameUsuarioActual();
           Terapeuta terapeutaActual = terapeutaService.obtenerPorUsername(userName);
           if (terapeutaActual != null) {
               String idTerapeutaActual = terapeutaActual.getIdTerapeuta();
               reservas = reservaService.listarReservasPorTerapeuta(idTerapeutaActual);
               nombreCompletoTerapeuta = terapeutaActual.getNombre() + " " + terapeutaActual.getApe_paterno();
           }
       }

       // 🔵 Aplicar filtros sobre la lista de reservas si corresponde
       if (filtroPaciente != null && !filtroPaciente.isEmpty()) {
           reservas = reservas.stream()
                   .filter(r -> (r.getPaciente().getNombre() + " " + r.getPaciente().getApellidoPaterno())
                           .toLowerCase()
                           .contains(filtroPaciente.toLowerCase()))
                   .collect(Collectors.toList());
       }

        if (filtroFecha != null) {
            reservas = reservas.stream()
                    .filter(r -> {
                        if (r.getFecha() == null) return false;
                        LocalDate fechaReserva = r.getFecha();
                        return fechaReserva.equals(filtroFecha);
                    })
                    .collect(Collectors.toList());
        }


       if (filtroServicio != null && !filtroServicio.isEmpty()) {
           reservas = reservas.stream()
                   .filter(r -> r.getNombreServicio()
                           .toLowerCase()
                           .contains(filtroServicio.toLowerCase()))
                   .collect(Collectors.toList());
       }

        if (filtroEstado != null && !filtroEstado.isEmpty()) {
            reservas = reservas.stream()
                    .filter(r -> r.getEstado() != null && r.getEstado().equalsIgnoreCase(filtroEstado))
                    .collect(Collectors.toList());
        }

        model.addAttribute("reservas", reservas);
        model.addAttribute("terapeutaNombreCompleto", nombreCompletoTerapeuta);
        model.addAttribute("rol", rol);
        model.addAttribute("idTerapeuta", idTerapeuta); // 


       return "reservas-terapeuta";  // Página que muestra las reservas
   }

   @GetMapping("/cancelar/{idReserva}")
    public String cancelarReserva(@PathVariable("idReserva") String idReserva, Model model) {
        try {
            // Buscar la reserva
            Optional<Reserva> reservaOpt = reservaRepository.findById(idReserva);

            if (!reservaOpt.isPresent()) {
                model.addAttribute("mensaje", "No se encontró la reserva a cancelar.");
                return "error";
            }

            Reserva reserva = reservaOpt.get();
            EstadoReserva estado = reserva.getEstado();
                if (estado != EstadoReserva.AGENDADA && estado != EstadoReserva.REAGENDADA) {
                    model.addAttribute("error", "Solo se pueden cancelar reservas en estado AGENDADA o REAGENDADA.");

                return "redirect:/reservas/verporterapeuta";
            }

            // Cambiar estado de la reserva
            reserva.setEstado(EstadoReserva.CANCELADA);

            // Si quieres además liberar el bloque asociado:
            Optional<Bloque> bloqueOpc = bloqueRepository.findByReserva_IdReserva(idReserva);
            
            if(!bloqueOpc.isPresent()){
                return "";
            }
            
            Bloque bloque = bloqueOpc.get();
            
            if (bloque != null) {
                bloque.setReserva(null); // Quitamos la referencia a la reserva
                calendarioService.marcarBloquesDesocupados(bloque, reserva.getAtencion().getTerapeuta().getIdTerapeuta());
                bloqueRepository.save(bloque);
            }

            // Guardar los cambios
            reservaRepository.save(reserva);
            this.enviarCancelacionReserva(reserva, bloque);
            model.addAttribute("mensaje", "Reserva cancelada exitosamente.");
            return "redirect:/reservas/verporterapeuta?idTerapeuta=" + reserva.getAtencion().getTerapeuta().getIdTerapeuta();

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("mensaje", "Error al cancelar la reserva.");
            return "error";
        }
    }
    
    @GetMapping("/cancelarPorCliente/{idReserva}")
    @ResponseBody
    public String cancelarReservaCliente(@PathVariable("idReserva") String idReserva) {
        try {
            Optional<Reserva> reservaOpt = reservaRepository.findById(idReserva);

            if (!reservaOpt.isPresent()) {
                return "Error: No se encontró la reserva.";
            }

            Reserva reserva = reservaOpt.get();
            EstadoReserva estado = reserva.getEstado();
            if (estado != EstadoReserva.AGENDADA && estado != EstadoReserva.REAGENDADA) {
                return "Error: Solo se pueden cancelar reservas en estado AGENDADA o REAGENDADA.";
            }

            reserva.setEstado(EstadoReserva.CANCELADA);

            Optional<Bloque> bloqueOpc = bloqueRepository.findByReserva_IdReserva(idReserva);
            Bloque bloque = null;
            if (bloqueOpc.isPresent()) {
                bloque = bloqueOpc.get();
                bloque.setReserva(null);
                calendarioService.marcarBloquesDesocupados(bloque, reserva.getAtencion().getTerapeuta().getIdTerapeuta());
                bloqueRepository.save(bloque);
            }
            
            reservaRepository.save(reserva);
            this.enviarCancelacionReserva(reserva, bloque);
            return "Reserva cancelada exitosamente.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al cancelar la reserva.";
        }
    }
    

    
    @GetMapping("/cambiar-estado/{idReserva}/{nuevoEstado}")
    public String cambiarEstadoReserva(@PathVariable String idReserva, 
                                       @PathVariable String nuevoEstado) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(idReserva);

        if(!reservaOpt.isPresent()){
            return "/";
        }

        Reserva reserva = reservaOpt.get();

        if (reserva != null) {
            reserva.setEstado(EstadoReserva.valueOf(nuevoEstado));
            reservaRepository.save(reserva);
            if (reserva.getEstado().name().equals("COMPLETADA")){
                this.enviarCorreoEvaluación(reserva.getPaciente().getCorreo(), idReserva);
            }
        }
        return "redirect:/reservas/verporterapeuta?idTerapeuta=" + reserva.getAtencion().getTerapeuta().getIdTerapeuta();
    }
    
    
}




