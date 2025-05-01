
package com.duoc.terapias.controller;

import com.duoc.terapias.dto.CalendarioDTO;
import com.duoc.terapias.dto.SemanaDTO;
import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.model.Terapeuta;
import com.duoc.terapias.service.CalendarioService;
import com.duoc.terapias.service.ServicioService;
import com.duoc.terapias.service.TerapeutaService;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class CalendarioControllerTest {

    @InjectMocks
    private CalendarioController calendarioController;

    @Mock
    private TerapeutaService terapeutaService;

    @Mock
    private ServicioService servicioService;

    @Mock
    private CalendarioService calendarioService;

    @Mock
    private Model model;

    @Test
    //@WithMockUser(username = "jazmingormaz@gmail.com", roles = {"TERAPEUTA"})
    void testVerCalendarioTerapeutaServicio_OK() {
        // Arrange
        String idTerapeuta = "T1";
        String idServicio = "S1";

        Terapeuta terapeuta = new Terapeuta();
        Servicio servicio = new Servicio();
        CalendarioDTO calendarioDTO = new CalendarioDTO();
        calendarioDTO.setSemanas(List.of(new SemanaDTO())); // al menos una semana

        when(terapeutaService.obtenerPorId(idTerapeuta)).thenReturn(terapeuta);
        when(servicioService.obtenerServicioPorId(idServicio)).thenReturn(Optional.of(servicio));
        when(calendarioService.obtenerCalendarioParaTerapeutaYServicio(terapeuta, servicio)).thenReturn(calendarioDTO);

        // Act
        String view = calendarioController.verCalendarioTerapeutaServicio(idTerapeuta, idServicio, model);

        // Assert
        assertEquals("calendario-terapeuta", view);
        verify(model).addAttribute("terapeuta", terapeuta);
        verify(model).addAttribute("servicio", servicio);
        verify(model).addAttribute(eq("semanas"), any());
    }

    @Test
    //@WithMockUser(username = "jazmingormaz@gmail.com", roles = {"TERAPEUTA"})
    void testVerCalendarioTerapeutaServicio_TerapeutaNoEncontrado() {
        // Arrange
        when(terapeutaService.obtenerPorId("invalid")).thenReturn(null);

        // Act
        String view = calendarioController.verCalendarioTerapeutaServicio("invalid", "S1", model);

        // Assert
        assertEquals("redirect:/terapeutas", view);
        verifyNoInteractions(servicioService, calendarioService);
    }

    @Test
    //@WithMockUser(username = "jazmingormaz@gmail.com", roles = {"TERAPEUTA"})
    void testVerCalendarioTerapeutaServicio_ServicioNoEncontrado() {
        // Arrange
        Terapeuta terapeuta = new Terapeuta();
        when(terapeutaService.obtenerPorId("T1")).thenReturn(terapeuta);
        when(servicioService.obtenerServicioPorId("invalid")).thenReturn(Optional.empty());

        // Act
        String view = calendarioController.verCalendarioTerapeutaServicio("T1", "invalid", model);

        // Assert
        assertEquals("redirect:/terapeutas", view);
        verifyNoInteractions(calendarioService);
    }
}

