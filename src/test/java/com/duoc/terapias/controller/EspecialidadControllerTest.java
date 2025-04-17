
package com.duoc.terapias.controller;

import com.duoc.terapias.model.Especialidad;
import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.service.EspecialidadService;
import com.duoc.terapias.service.ServicioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EspecialidadController.class)
public class EspecialidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EspecialidadService especialidadService;

    @MockBean
    private ServicioService servicioService;

    private Especialidad especialidad;
    
    private final String especialidadId = EspecialidadController.generarIdEspecialidad();

    @BeforeEach
    public void setup() {
        especialidad = new Especialidad();
        especialidad.setIdEspecialidad(especialidadId);
        especialidad.setNombre("Psicología");
    }

    @Test
    public void testMostrarFormularioEspecialidad() throws Exception {
        mockMvc.perform(get("/especialidad/nueva"))
                .andExpect(status().isOk())
                .andExpect(view().name("formulario-especialidad"))
                .andExpect(model().attributeExists("especialidad"))
                .andExpect(model().attributeExists("servicioTemporal"));
    }

    @Test
    public void testGuardarEspecialidad() throws Exception {
        when(especialidadService.guardar(any(Especialidad.class))).thenReturn(especialidad);

        mockMvc.perform(post("/especialidad/guardar")
                        .param("nombre", "Psicología"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/especialidad/listar"));

        verify(especialidadService, times(1)).guardar(any(Especialidad.class));
    }

    @Test
    public void testListarEspecialidades() throws Exception {
        when(especialidadService.obtenerTodas()).thenReturn(Collections.singletonList(especialidad));

        mockMvc.perform(get("/especialidad/listar"))
                .andExpect(status().isOk())
                .andExpect(view().name("lista-especialidad"))
                .andExpect(model().attributeExists("especialidades"));
    }

    @Test
    public void testMostrarFormularioEditarEspecialidad() throws Exception {
        when(especialidadService.obtenerPorId(especialidadId)).thenReturn(Optional.of(especialidad));

        mockMvc.perform(get("/especialidad/editar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("formulario-editar-especialidad"))
                .andExpect(model().attributeExists("especialidad"));
    }

    @Test
    public void testEliminarEspecialidad() throws Exception {
        doNothing().when(especialidadService).eliminar(especialidadId);

        mockMvc.perform(get("/especialidad/eliminar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/especialidad/listar"));

        verify(especialidadService, times(1)).eliminar(especialidadId);
    }

    @Test
    public void testAgregarServicioTemporal() throws Exception {
        mockMvc.perform(post("/especialidad/agregarServicioTemporal")
                        .param("nombre", "Terapia Infantil")
                        .param("descripcion", "Tratamiento para niños"))
                .andExpect(status().isOk());
    }
}

