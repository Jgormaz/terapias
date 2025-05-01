package com.duoc.terapias.controller;

import com.duoc.terapias.dto.PacienteDTO;
import com.duoc.terapias.dto.ReservaDTO;
import com.duoc.terapias.model.Atencion;
import com.duoc.terapias.model.Bloque;
import com.duoc.terapias.model.Calendario;
import com.duoc.terapias.model.Dia;
import com.duoc.terapias.model.EstadoReserva;
import com.duoc.terapias.model.Paciente;
import com.duoc.terapias.model.Reserva;
import com.duoc.terapias.model.Semana;
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
import com.duoc.terapias.service.ReservaService;
import com.duoc.terapias.service.TerapeutaService;
import com.duoc.terapias.service.CalendarioService;
import com.duoc.terapias.service.UserService;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class ReservaControllerTest {

    @InjectMocks
    private ReservaController reservaController;

    @Mock private CalendarioService calendarioService;
    @Mock private TerapeutaService terapeutaService;
    @Mock private TerapeutaRepository terapeutaRepository;
    @Mock private BloqueRepository bloqueRepository;
    @Mock private PacienteRepository pacienteRepository;
    @Mock private ComunaRepository comunaRepository;
    @Mock private RegionRepository regionRepository;
    @Mock private CategoriaRepository categoriaRepository;
    @Mock private AtencionRepository atencionRepository;
    @Mock private ReservaRepository reservaRepository;
    @Mock private JavaMailSender mailSender;
    @Mock private UserService userService;
    @Mock private ReservaService reservaService;

    @Mock
    private Model model;

    @Test
    void testMostrarFormularioReserva_BloqueExistente() {
        String bloqueId = "B001";
        String terapeutaId = "T001";
        String servicioId = "S001";

        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setNombre("Ana");
        terapeuta.setApe_paterno("Pérez");

        Servicio servicio = new Servicio();
        servicio.setNombre("Reiki");

        Calendario calendario = new Calendario();
        Atencion atencion = new Atencion();
        atencion.setServicio(servicio);
        calendario.setAtencion(atencion);
        calendario.setTerapeuta(terapeuta);

        Semana semana = new Semana();
        semana.setCalendario(calendario);
        Dia dia = new Dia();
        dia.setFecha(Date.from(LocalDate.of(2025, 5, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dia.setSemana(semana);

        Bloque bloque = new Bloque();
        bloque.setDia(dia);
        bloque.setHoraIni(1000);
        bloque.setHoraFin(1100);
        bloque.setPrecio(15000);

        when(bloqueRepository.findById(bloqueId)).thenReturn(Optional.of(bloque));

        String view = reservaController.mostrarFormularioReserva(bloqueId, terapeutaId, servicioId, model);

        assertEquals("formulario-reserva", view);
        verify(model).addAttribute(eq("reservaDTO"), any(ReservaDTO.class));
    }

    @Test
    void testMostrarFormularioReserva_BloqueNoExiste() {
        when(bloqueRepository.findById("B001")).thenReturn(Optional.empty());
        String view = reservaController.mostrarFormularioReserva("B001", "T001", "S001", model);
        assertEquals("redirect:/error", view);
    }
    
    @Test
    void testVerificarPaciente_PacienteExistente() {
        ReservaDTO dto = new ReservaDTO();
        PacienteDTO pacienteDTO = new PacienteDTO();
        pacienteDTO.setCorreo("paciente@example.com");
        dto.setPaciente(pacienteDTO);
        dto.setIdBloque("B001");

        Paciente paciente = new Paciente();
        paciente.setNombre("Carlos");
        paciente.setApe_paterno("Ramírez");
        paciente.setCorreo("paciente@example.com");

        Bloque bloque = new Bloque();
        Dia dia = new Dia();
        dia.setFecha(Date.from(LocalDate.of(2025, 5, 2).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Semana semana = new Semana();
        Calendario calendario = new Calendario();
        Servicio servicio = new Servicio();
        servicio.setNombre("Terapia");
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setNombre("Luis");
        terapeuta.setApe_paterno("González");
        calendario.setAtencion(new Atencion());
        calendario.getAtencion().setServicio(servicio);
        calendario.setTerapeuta(terapeuta);
        semana.setCalendario(calendario);
        dia.setSemana(semana);
        bloque.setDia(dia);
        bloque.setHoraIni(900);
        bloque.setHoraFin(1000);
        bloque.setPrecio(12000);

        when(pacienteRepository.findByCorreo("paciente@example.com")).thenReturn(Optional.of(paciente));
        when(bloqueRepository.findById("B001")).thenReturn(Optional.of(bloque));

        String view = reservaController.verificarPaciente(dto, model);
        assertEquals("formulario-reserva", view);
        verify(model).addAttribute(eq("reservaDTO"), any(ReservaDTO.class));
    }
    
 @Test
    void testEnviarConfirmacionReserva() throws Exception {
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setIdReserva("123");
        reservaDTO.setNombreTerapeuta("Ana");
        reservaDTO.setApellidoPaternoTerapeuta("Gómez");
        reservaDTO.setNombreServicio("Masaje");
        reservaDTO.setFecha(LocalDate.now());
        reservaDTO.setHoraIni(LocalDateTime.of(2025, 4, 29, 10, 0));
        reservaDTO.setPrecio(25000);
        reservaDTO.setCorreoTerapeuta("ana@correo.cl");

        PacienteDTO paciente = new PacienteDTO();
        paciente.setNombre("Luis");
        paciente.setApellidoPaterno("Pérez");
        paciente.setCorreo("luis@correo.cl");
        reservaDTO.setPaciente(paciente);

        Method method = ReservaController.class.getDeclaredMethod("enviarCofirmacionReserva", ReservaDTO.class);
        method.setAccessible(true);
        method.invoke(reservaController, reservaDTO);

        verify(mailSender, times(2)).send(any(SimpleMailMessage.class));
    }
    
    @Test
    void testVerReservasTerapeuta_Admin() {
        String idTerapeuta = "T001";
        when(userService.getRolUsuarioActual()).thenReturn("ROLE_ADMIN");

        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setNombre("Ana");
        terapeuta.setApe_paterno("Gómez");
        when(terapeutaService.obtenerPorId(idTerapeuta)).thenReturn(terapeuta);

        List<ReservaDTO> reservas = new ArrayList<>();
        when(reservaService.listarReservasPorTerapeuta(idTerapeuta)).thenReturn(reservas);

        String viewName = reservaController.verReservasTerapeuta(model, idTerapeuta, null, null, null, null);
        assertEquals("reservas-terapeuta", viewName);
    }

    @Test
    void testCancelarReserva() {
        String idReserva = "R001";

        Reserva reserva = mock(Reserva.class);
        Atencion atencion = mock(Atencion.class);
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setIdTerapeuta("T001");
        when(atencion.getTerapeuta()).thenReturn(terapeuta);
        when(reserva.getAtencion()).thenReturn(atencion);
        when(reserva.getEstado()).thenReturn(EstadoReserva.AGENDADA);
        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));

        Bloque bloque = new Bloque();
        when(bloqueRepository.findByReserva_IdReserva(idReserva)).thenReturn(Optional.of(bloque));

        String result = reservaController.cancelarReserva(idReserva, model);
        assertTrue(!result.contains("redirect:/reservas/verporterapeuta"));
        verify(reservaRepository).save(reserva);
        verify(bloqueRepository).save(bloque);
    }

    @Test
    void testCancelarReservaCliente() {
        String idReserva = "R002";
        Reserva reserva = mock(Reserva.class);
        Atencion atencion = mock(Atencion.class);
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setIdTerapeuta("T002");
        when(atencion.getTerapeuta()).thenReturn(terapeuta);
        when(reserva.getAtencion()).thenReturn(atencion);
        when(reserva.getEstado()).thenReturn(EstadoReserva.AGENDADA);

        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));
        Bloque bloque = new Bloque();
        when(bloqueRepository.findByReserva_IdReserva(idReserva)).thenReturn(Optional.of(bloque));

        String result = reservaController.cancelarReservaCliente(idReserva);
        assertEquals("Error al cancelar la reserva.", result);
        verify(reservaRepository).save(reserva);
        verify(bloqueRepository).save(bloque);
    }

    @Test
    void testCambiarEstadoReserva_Completada() {
        String idReserva = "R003";
        String nuevoEstado = "COMPLETADA";

        Reserva reserva = mock(Reserva.class);
        Paciente paciente = new Paciente();
        paciente.setCorreo("paciente@correo.com");
        Atencion atencion = mock(Atencion.class);
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setIdTerapeuta("T003");
        when(atencion.getTerapeuta()).thenReturn(terapeuta);
        when(reserva.getAtencion()).thenReturn(atencion);
        when(reserva.getPaciente()).thenReturn(paciente);

        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));
        when(reserva.getEstado()).thenReturn(EstadoReserva.COMPLETADA);

        String result = reservaController.cambiarEstadoReserva(idReserva, nuevoEstado);
        assertTrue(result.contains("redirect:/reservas/verporterapeuta"));
        verify(reservaRepository).save(reserva);
    }

}



