
package com.duoc.terapias.service;

import com.duoc.terapias.dto.BloqueDTO;
import com.duoc.terapias.dto.CalendarioDTO;
import com.duoc.terapias.dto.DiaDTO;
import com.duoc.terapias.dto.SemanaDTO;
import com.duoc.terapias.model.Atencion;
import com.duoc.terapias.model.Bloque;
import com.duoc.terapias.model.Calendario;
import com.duoc.terapias.model.Dia;
import com.duoc.terapias.model.Semana;
import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.model.Terapeuta;
import com.duoc.terapias.repository.AtencionRepository;
import com.duoc.terapias.repository.BloqueRepository;
import com.duoc.terapias.repository.CalendarioRepository;
import com.duoc.terapias.repository.DiaRepository;
import com.duoc.terapias.repository.SemanaRepository;
import com.duoc.terapias.repository.TerapeutaRepository;
import com.duoc.terapias.util.FechaSemanaUtils;
import java.util.UUID;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarioService {

    @Autowired
    private CalendarioRepository calendarioRepository;
    
    @Autowired
    private AtencionRepository atencionRepository;

    @Autowired
    private SemanaRepository semanaRepository;

    @Autowired
    private DiaRepository diaRepository;

    @Autowired
    private BloqueRepository bloqueRepository;
    
    @Autowired
    private TerapeutaRepository terapeutaRepository;

public CalendarioDTO obtenerCalendarioParaTerapeutaYServicio(Terapeuta terapeuta, Servicio servicio) {
    // Buscar todas las atenciones del terapeuta que coincidan con ese servicio
    Optional<Atencion> atencionesOptional = atencionRepository.findByTerapeutaAndServicio(terapeuta, servicio);
    if (!atencionesOptional.isPresent()) {
        return null;
    }

    // Tomamos la primera atención (asumiendo que hay una por combinación terapeuta/servicio)
    Atencion atencion = atencionesOptional.get();

    Optional<Calendario> calendarioOptional = calendarioRepository.findByTerapeutaAndAtencion(terapeuta, atencion);
    if (!calendarioOptional.isPresent()) {
        return null;
    }

    Calendario calendario = calendarioOptional.get();

    // Armamos el CalendarioDTO
    CalendarioDTO calendarioDTO = new CalendarioDTO();
    calendarioDTO.setId(calendario.getID_calendario());
    calendarioDTO.setIdTerapeuta(terapeuta.getIdTerapeuta());
    calendarioDTO.setIdServicio(servicio.getIdServicio());

    List<Semana> semanas = semanaRepository.findByCalendario(calendario);
    List<SemanaDTO> semanasDTO = new ArrayList<>();

    int i = 1;
    for (Semana semana : semanas) {
        SemanaDTO semanaDTO = new SemanaDTO();
        semanaDTO.setNumeroSemana(i++);

        List<Dia> dias = diaRepository.findBySemana(semana);
        List<DiaDTO> diasDTO = new ArrayList<>();

        for (Dia dia : dias) {
            DiaDTO diaDTO = new DiaDTO();
            diaDTO.setFecha(dia.getFecha());

            List<Bloque> bloques = bloqueRepository.findByDia(dia);
            List<BloqueDTO> bloquesDTO = new ArrayList<>();

            for (Bloque bloque : bloques) {
                BloqueDTO bloqueDTO = new BloqueDTO();
                bloqueDTO.setId(bloque.getID_bloque());
                bloqueDTO.setHoraIni(bloque.getHoraIni());
                bloqueDTO.setHoraFin(bloque.getHoraFin());
                bloqueDTO.setDisponible(!bloque.isEnElPasado());
                bloquesDTO.add(bloqueDTO);
            }

            diaDTO.setBloques(bloquesDTO);
            diasDTO.add(diaDTO);
        }

        semanaDTO.setDias(diasDTO);
        semanasDTO.add(semanaDTO);
    }

    calendarioDTO.setSemanas(semanasDTO);
    return calendarioDTO;
}

    
    public void crearCalendarioParaTerapeuta(String terapeutaId, Atencion atencion) {
        Terapeuta terapeuta = terapeutaRepository.findById(terapeutaId).orElseThrow();
        Calendario calendario = new Calendario();
        calendario.setAtencion(atencion);
        calendario.setID_calendario(UUID.randomUUID().toString().substring(0, 29)); 
        calendario.setTerapeuta(terapeuta);
        // Fecha de inicio (hoy como Date)
        Date fechaHoy = new Date();
        calendario.setFechaIni(fechaHoy);

        // Número de la semana ISO actual
        LocalDate hoyLocal = fechaHoy.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
        int semanaActual = hoyLocal.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

        // Configurar semanas visibles y rango de semanas
        calendario.setSemanasVisibles(4);
        calendario.setSemanaIni("");
        calendario.setSemanaFin("");
        calendarioRepository.save(calendario);

        for (int i = 0; i < 4; i++) {
            Semana semana = new Semana();
            semana.setFecha(FechaSemanaUtils.getPrimerDiaDeSemana(i));
            semana.setID_semana(FechaSemanaUtils.getPrimerDiaDeSemana(i).toString().substring(0,10));
            semana.setCalendario(calendario);
            semanaRepository.save(semana);

            for (int j = 0; j < 7; j++) {
                Dia dia = new Dia();
                
                dia.setSemana(semana);
                Date fechaDia = Date.from(LocalDate.now().plusWeeks(i).with(DayOfWeek.MONDAY).plusDays(j)
                    .atStartOfDay(ZoneId.systemDefault()).toInstant());
                dia.setFecha(fechaDia);
                String fechaDiaStr = fechaDia.toString().substring(0,20);
                dia.setID_dia(fechaDiaStr);
                diaRepository.save(dia);

                for (int k = 9; k < 17; k++) {
                    Bloque bloque = new Bloque();

                    bloque.setID_bloque(fechaDiaStr + "_" + k);
                    bloque.setDia(dia);
                    bloque.setHoraIni(k * 100);
                    bloque.setHoraFin((k + 1) * 100);
                    bloque.setDisponible(!bloque.isEnElPasado());
                    bloque.setPrecio(20000); // por ejemplo
                    bloqueRepository.save(bloque);
                }
            }
        }
    }
}




