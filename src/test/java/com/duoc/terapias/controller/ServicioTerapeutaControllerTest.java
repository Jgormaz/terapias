package com.duoc.terapias.controller;

import com.duoc.terapias.service.ServicioTerapeutaService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServicioTerapeutaControllerTest {

    @InjectMocks
    private ServicioTerapeutaController servicioTerapeutaController;

    @Mock
    private ServicioTerapeutaService servicioTerapeutaService;

    @Test
    void testEliminarPorTerapeuta() {
        // Arrange
        String idTerapeuta = "T123";

        // Act
        String response = servicioTerapeutaController.eliminarPorTerapeuta(idTerapeuta);

        // Assert
        verify(servicioTerapeutaService, times(1)).eliminarPorIdTerapeuta(idTerapeuta);
        assertEquals("Asociaciones eliminadas para terapeuta ID: T123", response);
    }
}
