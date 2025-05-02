
package com.duoc.terapias.controller;

import com.duoc.terapias.service.ComunaService;
import com.duoc.terapias.service.ServicioTerapeutaService;
import com.duoc.terapias.service.TerapeutaService;
import static org.mockito.Mockito.when;
import com.duoc.terapias.dto.TerapeutaInfoDTO;
import com.duoc.terapias.model.Comuna;
import com.duoc.terapias.model.Region;
import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.model.Terapeuta;
import com.duoc.terapias.service.CalendarioService;
import com.duoc.terapias.service.RegionService;
import com.duoc.terapias.service.ServicioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TerapeutaControllerTest {

    @InjectMocks
    private TerapeutaController controller;

    @Mock
    private TerapeutaService terapeutaService;
    @Mock
    private ComunaService comunaService;
    @Mock
    private RegionService regionService;
    @Mock
    private ServicioService servicioService;
    @Mock
    private ServicioTerapeutaService servicioTerapeutaService;
    @Mock
    private CalendarioService calendarioService;
    @Mock
    private Model model;
    @Mock
    private Principal principal;
    @Mock
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testActualizarEstado() {
        String id = "1";
        boolean enabled = true;

        var response = controller.actualizarEstado(id, enabled);
        verify(terapeutaService).actualizarEstado(id, enabled);
        assertEquals("Estado actualizado", response.getBody());
    }

    @Test
    void testListarTerapeutasBasico() {
        List<Terapeuta> terapeutas = List.of(new Terapeuta());
        when(terapeutaService.obtenerTodos()).thenReturn(terapeutas);

        String view = controller.listarTerapeutasBasico(model);

        verify(model).addAttribute("terapeutas", terapeutas);
        assertEquals("lista-terapeutas", view);
    }

    @Test
    void testActualizarServicios_AsociarYDesasociar() {
        String username = "usuario";
        List<String> seleccionados = List.of("s1", "s2");
        Terapeuta t = new Terapeuta();
        Servicio servicio = new Servicio();
        servicio.setIdServicio("s1");
        servicio.setNombre("nombre");
        servicio.setDescripcion("desc");
        List<Servicio> actuales = List.of(servicio);

        when(principal.getName()).thenReturn(username);
        when(terapeutaService.obtenerPorUsername(username)).thenReturn(t);
        when(servicioTerapeutaService.findServiciosByUserName(username)).thenReturn(actuales);

        String view = controller.actualizarServicios(seleccionados, principal, redirectAttributes);

        verify(servicioTerapeutaService).asociarServiciosATerapeuta(eq(t), eq(List.of("s2")));
        verify(servicioTerapeutaService, never()).desasociarServiciosDeTerapeuta(anyString(), eq(List.of("s2")));
        assertEquals("redirect:/", view);
    }

    @Test
    void testMostrarFormularioAsociacion() {
        String username = "user";
        Terapeuta terapeuta = new Terapeuta();
        List<Servicio> servicios = List.of(new Servicio());
        List<Servicio> asociados = List.of(new Servicio());

        when(principal.getName()).thenReturn(username);
        when(terapeutaService.obtenerPorUsername(username)).thenReturn(terapeuta);
        when(servicioService.obtenerTodosLosServicios()).thenReturn(servicios);
        when(servicioTerapeutaService.findServiciosByUserName(username)).thenReturn(asociados);

        String view = controller.mostrarFormularioAsociacion(model, principal);

        verify(model).addAttribute("terapeuta", terapeuta);
        verify(model).addAttribute("servicios", servicios);
        verify(model).addAttribute("serviciosAsociados", asociados);
        assertEquals("asociar-servicios", view);
    }

    @Test
    void testObtenerTerapeutasConServicios() {
        List<TerapeutaInfoDTO> terapeutas = List.of(new TerapeutaInfoDTO());
        when(terapeutaService.obtenerTerapeutasConServicios()).thenReturn(terapeutas);

        String view = controller.obtenerTerapeutasConServicios(model);
        verify(model).addAttribute("terapeutas", terapeutas);
        assertEquals("terapeutas", view);
    }

    @Test
    void testObtenerTerapeutasPorEspecialidad() {
        String id = "e1";
        List<Terapeuta> terapeutas = List.of(new Terapeuta());
        when(terapeutaService.obtenerTerapeutasPorEspecialidad(id)).thenReturn(terapeutas);

        String view = controller.obtenerTerapeutasPorEspecialidad(id, model);
        verify(model).addAttribute("terapeutas", terapeutas);
        assertEquals("terapeutas", view);
    }

    @Test
    void testMostrarFormularioNuevo() {
        List<Comuna> comunas = List.of(new Comuna());
        List<Region> regiones = List.of(new Region());

        when(comunaService.obtenerTodas()).thenReturn(comunas);
        when(regionService.obtenerTodas()).thenReturn(regiones);

        String view = controller.mostrarFormulario(model);

        verify(model).addAttribute(eq("terapeuta"), any(Terapeuta.class));
        verify(model).addAttribute("comunas", comunas);
        verify(model).addAttribute("regiones", regiones);
        assertEquals("nuevo-terapeuta", view);
    }


    @Test
    void testMostrarFormularioEdicion_Existe() {
        Terapeuta terapeuta = new Terapeuta();
        when(terapeutaService.obtenerPorId("1")).thenReturn(terapeuta);
        when(comunaService.obtenerTodas()).thenReturn(List.of());
        when(regionService.obtenerTodas()).thenReturn(List.of());

        String view = controller.mostrarFormularioEdicion("1", model);
        verify(model).addAttribute("terapeuta", terapeuta);
        assertEquals("editar-terapeuta", view);
    }

    @Test
    void testMostrarFormularioEdicion_NoExiste() {
        when(terapeutaService.obtenerPorId("1")).thenReturn(null);

        String view = controller.mostrarFormularioEdicion("1", model);
        assertEquals("redirect:/terapeuta/?error", view);
    }

    @Test
    void testActualizarTerapeuta() {
        Terapeuta terapeuta = new Terapeuta();
        Comuna comuna = new Comuna();
        Region region = new Region();

        when(comunaService.buscarPorId("c1")).thenReturn(comuna);
        when(regionService.buscarPorId("r1")).thenReturn(region);

        String view = controller.actualizarTerapeuta("id123", terapeuta, "c1", "r1");

        verify(terapeutaService).guardar(terapeuta);
        assertEquals("redirect:/terapeuta/lista-terapeutas", view);
    }

    @Test
    void testEliminarTerapeuta() {
        String id = "t1";

        String view = controller.eliminarTerapeuta(id);

        verify(servicioTerapeutaService).eliminarPorIdTerapeuta(id);
        verify(terapeutaService).eliminarPorId(id);
        assertEquals("redirect:/terapeuta/lista-terapeutas", view);
    }
}


