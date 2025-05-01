package com.duoc.terapias.controller;

import com.duoc.terapias.dto.EvaluacionDTO;
import com.duoc.terapias.service.ReservaService;
import com.duoc.terapias.service.TerapeutaService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class EvaluacionControllerTest {

    @InjectMocks
    private EvaluacionController evaluacionController;

    @Mock
    private ReservaService reservaService;

    @Mock
    private TerapeutaService terapeutaService;

    @Mock
    private Model model;

    @Test
    void testMostrarFormularioEvaluacion() {
        // Act
        String viewName = evaluacionController.mostrarFormularioEvaluacion(model);

        // Assert
        assertEquals("evaluacion-formulario", viewName);
        verify(model).addAttribute(eq("evaluacionDTO"), any(EvaluacionDTO.class));
    }

    @Test
    void testGuardarEvaluacion_Success() {
        // Arrange
        EvaluacionDTO evaluacionDTO = new EvaluacionDTO();
        evaluacionDTO.setIdReserva("R123");
        evaluacionDTO.setNota(5);

        // No exception thrown
        doNothing().when(reservaService).evaluarReserva("R123", 5);

        // Act
        String viewName = evaluacionController.guardarEvaluacion(evaluacionDTO, model);

        // Assert
        assertEquals("evaluacion-formulario", viewName);
        verify(reservaService).evaluarReserva("R123", 5);
        verify(model).addAttribute("mensaje", "¡Gracias por tu evaluación!");
    }

    @Test
    void testGuardarEvaluacion_Failure() {
        // Arrange
        EvaluacionDTO evaluacionDTO = new EvaluacionDTO();
        evaluacionDTO.setIdReserva("R123");
        evaluacionDTO.setNota(5);

        doThrow(new IllegalStateException("Reserva no válida")).when(reservaService).evaluarReserva("R123", 5);

        // Act
        String viewName = evaluacionController.guardarEvaluacion(evaluacionDTO, model);

        // Assert
        assertEquals("evaluacion-formulario", viewName);
        verify(model).addAttribute("error", "Reserva no válida");
    }
}

