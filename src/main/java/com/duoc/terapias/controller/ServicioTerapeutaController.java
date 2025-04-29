
package com.duoc.terapias.controller;
import com.duoc.terapias.model.ServicioTerapeuta;
import com.duoc.terapias.service.ServicioTerapeutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/servicioterapeuta")
public class ServicioTerapeutaController {

    @Autowired
    private ServicioTerapeutaService servicioTerapeutaService;

    // ✅ Obtener todas las asociaciones (opcional para administración o debugging)
   /* @GetMapping
    @ResponseBody
    public List<ServicioTerapeuta> obtenerTodas() {
        return servicioTerapeutaService.obtenerTodas();
    }*/

    // ✅ Eliminar asociaciones por ID del terapeuta (para que luego puedas eliminar el terapeuta)
    @DeleteMapping("/eliminarPorTerapeuta/{idTerapeuta}")
    @ResponseBody
    public String eliminarPorTerapeuta(@PathVariable String idTerapeuta) {
        servicioTerapeutaService.eliminarPorIdTerapeuta(idTerapeuta);
        return "Asociaciones eliminadas para terapeuta ID: " + idTerapeuta;
    }
}
