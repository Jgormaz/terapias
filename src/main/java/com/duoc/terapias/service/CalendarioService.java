
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
import java.util.UUID;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
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
        Optional<Atencion> atencionesOptional = atencionRepository.findByTerapeutaAndServicio(terapeuta, servicio);
        if (!atencionesOptional.isPresent()) {
            return null;
        }

        Atencion atencion = atencionesOptional.get();

        Optional<Calendario> calendarioOptional = calendarioRepository.findByTerapeutaAndAtencion(terapeuta, atencion);
        if (!calendarioOptional.isPresent()) {
            return null;
        }

        Calendario calendario = calendarioOptional.get();

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
                    bloqueDTO.setPrecio(bloque.getPrecio());
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
    

    public void crearCalendarioParaTerapeuta(Terapeuta terapeuta, Atencion atencion) {
        //Terapeuta terapeuta = terapeutaRepository.findById(terapeutaId).orElseThrow();
        String idAtencion =atencion.getID_atencion();
        // Verificar si ya existe un calendario con ese ID
        Optional<Calendario> existente = calendarioRepository.findById(idAtencion);
        if (existente.isPresent()) {
            return;  // Retornar la atención existente sin crear una nueva
        }
        Calendario calendario = new Calendario();
        calendario.setAtencion(atencion);
        calendario.setID_calendario(atencion.getID_atencion());
        calendario.setTerapeuta(terapeuta);

        // Establecer fecha base como el lunes de la semana actual
        LocalDate primerLunes = LocalDate.now().with(DayOfWeek.MONDAY);
        Date fechaHoy = Date.from(primerLunes.atStartOfDay(ZoneId.systemDefault()).toInstant());
        calendario.setFechaIni(fechaHoy);

        calendario.setSemanasVisibles(4);
        calendario.setSemanaIni("");
        calendario.setSemanaFin("");
        calendarioRepository.save(calendario);

        for (int i = 0; i < 4; i++) {
            LocalDate fechaSemana = primerLunes.plusWeeks(i);
            Semana semana = new Semana();
            semana.setFecha(Date.from(fechaSemana.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            semana.setID_semana(fechaSemana.toString());
            semana.setCalendario(calendario);
            semanaRepository.save(semana);

            for (int j = 0; j < 7; j++) {
                LocalDate fechaDiaLD = fechaSemana.plusDays(j);
                Date fechaDia = Date.from(fechaDiaLD.atStartOfDay(ZoneId.systemDefault()).toInstant());

                Dia dia = new Dia();
                dia.setSemana(semana);
                dia.setFecha(fechaDia);
                String fechaDiaStr = fechaDiaLD.toString();
                dia.setID_dia(fechaDiaStr);
                diaRepository.save(dia);

                for (int k = 9; k < 17; k++) {
                    Bloque bloque = new Bloque();
                    bloque.setID_bloque(fechaDiaStr + "_" + k);
                    bloque.setDia(dia);
                    bloque.setHoraIni(k * 100);
                    bloque.setHoraFin((k + 1) * 100);
                    bloque.setDisponible(!bloque.isEnElPasado());
                    bloque.setPrecio(20000);
                    bloqueRepository.save(bloque);
                }
            }
        }
    }
}




