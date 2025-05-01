
package com.duoc.terapias.controller;

import com.duoc.terapias.model.Especialidad;
import com.duoc.terapias.service.EspecialidadService;
import com.duoc.terapias.service.ServicioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.Optional;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EspecialidadController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EspecialidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EspecialidadService especialidadService;

    @MockBean
    private ServicioService servicioService;

    private Especialidad especialidad;
    
    private final String especialidadId = EspecialidadController.generarIdEspecialidad();
    private final String servicioId = EspecialidadController.generarIdServicio();

    @BeforeEach
    public void setup() {
        especialidad = new Especialidad();
        especialidad.setIdEspecialidad(especialidadId);
        especialidad.setNombre("Psicología");
    }


    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void testGuardarEspecialidad() throws Exception {
        when(especialidadService.guardar(any(Especialidad.class))).thenReturn(especialidad);

        mockMvc.perform(post("/especialidades/guardar")
                        .param("nombre", "Psicología"))
                .andExpect(status().isOk()); // Espera un 200 OK, porque no se redirige

        verify(especialidadService, times(1)).guardar(any(Especialidad.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void testListarEspecialidades() throws Exception {
        when(especialidadService.obtenerTodas()).thenReturn(Collections.singletonList(especialidad));

        mockMvc.perform(get("/especialidades"))
                .andExpect(status().isOk())
                .andExpect(view().name("especialidades"))
                .andExpect(model().attributeExists("especialidades"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void testMostrarFormularioEditarEspecialidad() throws Exception {
        when(especialidadService.obtenerPorId(especialidadId)).thenReturn(Optional.of(especialidad));

        mockMvc.perform(get("/especialidades/editar/" + this.especialidadId))
                .andExpect(status().isOk())
                .andExpect(view().name("editar-especialidad"))
                .andExpect(model().attributeExists("especialidad"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void testEliminarEspecialidad() throws Exception {
        doNothing().when(especialidadService).eliminar(especialidadId);

        mockMvc.perform(get("/especialidades/eliminar/" + this.especialidadId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/especialidades"));

        verify(especialidadService, times(1)).eliminar(especialidadId);
    }
    
    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void testAgregarServicioTemporal() throws Exception {
        System.out.println("ROL actual: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        mockMvc.perform(post("/especialidades/agregarServicio")
                .param("idEspecialidad", especialidadId)
                .param("nombre", "Terapia Infantil")
                .param("descripcion", "Tratamiento para niños"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/especialidades"));
    }
}


