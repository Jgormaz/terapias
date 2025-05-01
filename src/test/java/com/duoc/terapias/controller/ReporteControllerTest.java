package com.duoc.terapias.controller;

import com.duoc.terapias.dto.ResumenReservaDTO;
import com.duoc.terapias.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ReporteController.class)
public class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReporteService reporteService;

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void testObtenerReporteTerapeutas_devuelveVistaConReporte() throws Exception {
        // Arrange
        ResumenReservaDTO dto1 = new ResumenReservaDTO("Terapeuta 1", "", "", 5L, 2L, 1L, 0L, 4L, 3L);
        ResumenReservaDTO dto2 = new ResumenReservaDTO("Terapeuta 2", "", "", 8L, 1L, 0L, 2L, 6L, 5L);
        List<ResumenReservaDTO> mockReporte = Arrays.asList(dto1, dto2);

        Mockito.when(reporteService.obtenerReporteReservas()).thenReturn(mockReporte);

        // Act & Assert
        mockMvc.perform(get("/reportes/terapeutas"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("reporteTerapeutas"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("reporte"))
                .andExpect(MockMvcResultMatchers.model().attribute("reporte", mockReporte));
    }
}

