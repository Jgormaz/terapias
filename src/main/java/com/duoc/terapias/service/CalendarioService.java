
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            System.out.println("no existe la atención");
            return null;
        }

        Atencion atencion = atencionesOptional.get();

        Optional<Calendario> calendarioOptional = calendarioRepository.findByTerapeutaAndAtencion(terapeuta, atencion);
        if (!calendarioOptional.isPresent()) {
            System.out.println("no existe el calendario");
            return null;
        }

        Calendario calendario = calendarioOptional.get();
        this.ajustarCalendario(calendario); //elimina semanas antes de la fecha actual y crea un número igual de nuevas semanas al final

        CalendarioDTO calendarioDTO = new CalendarioDTO();
        calendarioDTO.setId(calendario.getID_calendario());
        calendarioDTO.setIdTerapeuta(terapeuta.getIdTerapeuta());
        calendarioDTO.setIdServicio(servicio.getIdServicio());

        List<Semana> semanas = semanaRepository.findByCalendario(calendario);
        semanas.sort((a, b) -> a.getFecha().compareTo(b.getFecha())); // ✅ ordenar semanas por fecha

        List<SemanaDTO> semanasDTO = new ArrayList<>();
        int i = 1;

        for (Semana semana : semanas) {
            SemanaDTO semanaDTO = new SemanaDTO();
            semanaDTO.setNumeroSemana(i++);

            List<Dia> dias = diaRepository.findBySemana(semana);
            dias.sort((a, b) -> a.getFecha().compareTo(b.getFecha())); // ✅ ordenar días por fecha

            List<DiaDTO> diasDTO = new ArrayList<>();
            for (Dia dia : dias) {
                DiaDTO diaDTO = new DiaDTO();
                diaDTO.setFecha(dia.getFecha());

                List<Bloque> bloques = bloqueRepository.findByDia(dia);
                bloques.sort((a, b) -> Integer.compare(a.getHoraIni(), b.getHoraIni())); // ✅ ordenar bloques por hora

                List<BloqueDTO> bloquesDTO = new ArrayList<>();
                for (Bloque bloque : bloques) {
                    BloqueDTO bloqueDTO = new BloqueDTO();
                    bloqueDTO.setId(bloque.getID_bloque());
                    bloqueDTO.setHoraIni(bloque.getHoraIni());
                    bloqueDTO.setHoraFin(bloque.getHoraFin());
                    bloqueDTO.setDisponible(!bloque.isEnElPasado() && bloque.getDisponible());
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
        //System.out.println("calendarioDTO " + calendarioDTO);
        return calendarioDTO;
    }

    public void crearCalendarioParaTerapeuta(Terapeuta terapeuta, Atencion atencion) {
        String idAtencion = atencion.getID_atencion();
        Optional<Calendario> existente = calendarioRepository.findById(idAtencion);
        if (existente.isPresent()) {
            return;
        }
        
        List<BloqueDTO> bloquesBloqueados = this.obtenerBloquesNoDisponiblesPorTerapeuta(terapeuta.getIdTerapeuta());

        Calendario calendario = new Calendario();
        calendario.setAtencion(atencion);
        calendario.setID_calendario(atencion.getID_atencion());
        calendario.setTerapeuta(terapeuta);

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
            semana.setID_semana(UUID.randomUUID().toString().substring(0,10));
            semana.setCalendario(calendario);
            semanaRepository.save(semana);

            for (int j = 0; j < 7; j++) {
                LocalDate fechaDiaLD = fechaSemana.plusDays(j);
                Date fechaDia = Date.from(fechaDiaLD.atStartOfDay(ZoneId.systemDefault()).toInstant());

                Dia dia = new Dia();
                dia.setSemana(semana);
                dia.setFecha(fechaDia);
                dia.setID_dia(UUID.randomUUID().toString().substring(0,20));
                diaRepository.save(dia);

                for (int k = 9; k < 17; k++) {
                    Bloque bloque = new Bloque();
                    bloque.setID_bloque(UUID.randomUUID().toString().substring(0,30));
                    bloque.setDia(dia);
                    bloque.setHoraIni(k * 100);
                    bloque.setHoraFin((k + 1) * 100);
                    boolean esBloqueado = this.existeBloqueEnLista(bloquesBloqueados, dia.getFecha(), bloque.getHoraIni(), bloque.getHoraFin());
                    System.out.println("El bloque " + dia.getFecha() + " " + bloque.getHoraIni() + bloque.getHoraFin() + "es bloquedo:" + esBloqueado);
                    bloque.setDisponible(!bloque.isEnElPasado() && !esBloqueado);
                    //bloque.setDisponible(!bloque.isEnElPasado());
                    bloque.setPrecio(20000);
                    bloqueRepository.save(bloque);
                }
            }
        }
    }
    
    @Transactional
    public void marcarBloquesOcupados(Bloque bloqueReservado, String idTerapeuta) {
        
        Optional<Terapeuta> terapeutaOpc = terapeutaRepository.findById(idTerapeuta);
        
        if(!terapeutaOpc.isPresent()){
            System.out.println("No se encontró el terapeuta con ID: " + idTerapeuta);
            return;
        }
        
        Terapeuta terapeuta = terapeutaOpc.get();
        
        // Buscar todos los calendarios del terapeuta
        List<Calendario> calendarios = calendarioRepository.findAllByTerapeuta(terapeuta);

        if (calendarios.isEmpty()) {
            return;
        }

        for (Calendario calendario : calendarios) {
            // Buscar todas las semanas de ese calendario
            List<Semana> semanas = semanaRepository.findByCalendario(calendario);
            
            bloqueReservado.setDisponible(false);
            bloqueRepository.save(bloqueReservado);

           /* for (Semana semana : semanas) {
                // Buscar todos los días de esa semana
                List<Dia> dias = diaRepository.findBySemana(semana);

                for (Dia dia : dias) {
                    // Buscar todos los bloques de ese día que coincidan en horaIni y horaFin
                    
                    if(dia.getFecha().equals(bloqueReservado.getDia().getFecha())){*/
                        Date fechaTruncada = truncateToDate(bloqueReservado.getDia().getFecha());

                        System.out.println("idTerapeuta:" + idTerapeuta + " fecha:" + fechaTruncada + " horaIni:" + bloqueReservado.getHoraIni() + " horaFin:" + bloqueReservado.getHoraFin());
                        List<Bloque> bloques = bloqueRepository.findByTerapeutaAndFechaAndHoraIniAndHoraFin(idTerapeuta, fechaTruncada, bloqueReservado.getHoraIni(), bloqueReservado.getHoraFin());
                        System.out.println(bloques);
                        for (Bloque bloque : bloques) {
                            if (bloque.getDisponible()) {
                                bloque.setDisponible(false);
                                bloqueRepository.save(bloque);
                            }
                        }
                    //}
                //}
            //}
        }
    }
    
    @Transactional(readOnly = true)
    public List<BloqueDTO> obtenerBloquesNoDisponiblesPorTerapeuta(String idTerapeuta) {
        Optional<Terapeuta> terapeutaOptional = terapeutaRepository.findById(idTerapeuta);

        if (!terapeutaOptional.isPresent()) {
            System.out.println("No se encontró el terapeuta con ID: " + idTerapeuta);
            return Collections.emptyList();
        }

        Terapeuta terapeuta = terapeutaOptional.get();
        List<BloqueDTO> bloquesNoDisponibles = new ArrayList<>();

        List<Calendario> calendarios = calendarioRepository.findAllByTerapeuta(terapeuta);

        for (Calendario calendario : calendarios) {
            List<Semana> semanas = semanaRepository.findByCalendario(calendario);

            for (Semana semana : semanas) {
                List<Dia> dias = diaRepository.findBySemana(semana);

                for (Dia dia : dias) {
                    List<Bloque> bloques = bloqueRepository.findByDia(dia);

                    for (Bloque bloque : bloques) {
                        if (Boolean.FALSE.equals(bloque.getDisponible())) {
                            BloqueDTO dto = new BloqueDTO();
                            dto.setId(bloque.getID_bloque());
                            dto.setHoraIni(bloque.getHoraIni());
                            dto.setHoraFin(bloque.getHoraFin());
                            dto.setPrecio(bloque.getPrecio());
                            dto.setDisponible(false);
                            dto.setFecha(dia.getFecha());
                            dto.setEnElPasado(bloque.isEnElPasado());
                            bloquesNoDisponibles.add(dto);
                        }
                    }
                }
            }
        }

        return bloquesNoDisponibles;
    }
    
    @Transactional(readOnly = true)
    public boolean existeBloqueNoDisponible(Terapeuta terapeuta, Bloque bloqueReferencia) {
        List<Calendario> calendarios = calendarioRepository.findAllByTerapeuta(terapeuta);

        for (Calendario calendario : calendarios) {
            List<Semana> semanas = semanaRepository.findByCalendario(calendario);

            for (Semana semana : semanas) {
                List<Dia> dias = diaRepository.findBySemana(semana);

                for (Dia dia : dias) {
                    List<Bloque> bloques = bloqueRepository.findByDia(dia);

                    for (Bloque bloque : bloques) {
                        // Comparar fecha, horaIni y horaFin
                        boolean mismaFecha = dia.getFecha().equals(bloqueReferencia.getDia().getFecha());
                        boolean mismaHoraInicio = bloque.getHoraIni().equals(bloqueReferencia.getHoraIni());
                        boolean mismaHoraFin = bloque.getHoraFin().equals(bloqueReferencia.getHoraFin());
                        boolean noDisponible = Boolean.FALSE.equals(bloque.getDisponible());

                        if (mismaFecha && mismaHoraInicio && mismaHoraFin && noDisponible) {
                            return true; // Encontrado
                        }
                    }
                }
            }
        }

        return false; // No encontrado
    }
    
    @Transactional
    public void ajustarCalendario(Calendario calendario) {
        List<Semana> semanas = semanaRepository.findByCalendario(calendario);

        if (semanas.isEmpty()) {
            // No hay semanas, crear 4 nuevas semanas como en crearCalendarioParaTerapeuta
            crearSemanasConDiasYBloques(calendario, LocalDate.now().with(DayOfWeek.MONDAY), 4);
            return;
        }

        // Ordenar semanas por fecha
        semanas.sort(Comparator.comparing(Semana::getFecha));

        LocalDate hoy = LocalDate.now();
        LocalDate lunesActual = hoy.with(DayOfWeek.MONDAY);

        // Identificar semanas anteriores a esta semana
        List<Semana> semanasAnteriores = semanas.stream()
                .filter(semana -> semana.getFecha().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .isBefore(lunesActual))
                .collect(Collectors.toList());

        int semanasAReemplazar = semanasAnteriores.size();

        // Borrar las semanas anteriores (y sus hijos: días y bloques)
        for (Semana semana : semanasAnteriores) {
            List<Dia> dias = diaRepository.findBySemana(semana);
            for (Dia dia : dias) {
                List<Bloque> bloques = bloqueRepository.findByDia(dia);
                bloqueRepository.deleteAll(bloques);
            }
            diaRepository.deleteAll(dias);
        }
        semanaRepository.deleteAll(semanasAnteriores);

        // Recuperar las semanas restantes (después del borrado)
        List<Semana> semanasRestantes = semanaRepository.findByCalendario(calendario);
        semanasRestantes.sort(Comparator.comparing(Semana::getFecha));

        LocalDate fechaInicioNuevasSemanas;
        if (semanasRestantes.isEmpty()) {
            // Si no queda ninguna semana, iniciar desde el lunes actual
            fechaInicioNuevasSemanas = lunesActual;
        } else {
            // Si quedan semanas, iniciar desde la semana siguiente a la última semana existente
            Semana ultimaSemana = semanasRestantes.get(semanasRestantes.size() - 1);
            fechaInicioNuevasSemanas = ultimaSemana.getFecha().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .plusWeeks(1);
        }

        // Crear las nuevas semanas necesarias
        crearSemanasConDiasYBloques(calendario, fechaInicioNuevasSemanas, semanasAReemplazar);
    }

    /**
     * Método auxiliar para crear semanas consecutivas completas con días y bloques.
     */
    private void crearSemanasConDiasYBloques(Calendario calendario, LocalDate fechaInicio, int cantidadSemanas) {
        List<BloqueDTO> bloquesBloqueados = this.obtenerBloquesNoDisponiblesPorTerapeuta(calendario.getTerapeuta().getIdTerapeuta());

        for (int i = 0; i < cantidadSemanas; i++) {
            LocalDate fechaSemana = fechaInicio.plusWeeks(i);
            Semana semana = new Semana();
            semana.setID_semana(UUID.randomUUID().toString().substring(0,10));
            semana.setFecha(Date.from(fechaSemana.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            semana.setCalendario(calendario);
            semanaRepository.save(semana);

            for (int j = 0; j < 7; j++) {
                LocalDate fechaDiaLD = fechaSemana.plusDays(j);
                Date fechaDia = Date.from(fechaDiaLD.atStartOfDay(ZoneId.systemDefault()).toInstant());

                Dia dia = new Dia();
                dia.setSemana(semana);
                dia.setFecha(fechaDia);
                dia.setID_dia(UUID.randomUUID().toString().substring(0,20));
                diaRepository.save(dia);

                for (int k = 9; k < 17; k++) {
                    Bloque bloque = new Bloque();
                    bloque.setID_bloque(UUID.randomUUID().toString().substring(0,30));
                    bloque.setDia(dia);
                    bloque.setHoraIni(k * 100);
                    bloque.setHoraFin((k + 1) * 100);
                    boolean esBloqueado = this.existeBloqueEnLista(bloquesBloqueados, dia.getFecha(), bloque.getHoraIni(), bloque.getHoraFin());
                    bloque.setDisponible(!bloque.isEnElPasado() && !esBloqueado);
                    bloque.setPrecio(20000);
                    bloqueRepository.save(bloque);
                }
            }
        }
    }


    public boolean existeBloqueEnLista(List<BloqueDTO> bloques, Date fecha, Integer horaIni, Integer horaFin) {
        
        System.out.println( fecha + " " + horaIni + " " + horaFin + " " + bloques);
        if (bloques == null || bloques.isEmpty()) {
            return false;
        }
        
        LocalDate fechaParam = fecha.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

        for (BloqueDTO bloque : bloques) {
            //System.out.println("Bloque:" + bloque);
            
            LocalDate fechaBloque = bloque.getFecha().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
            
            boolean mismaFecha = fechaBloque.equals(fechaParam);
            boolean mismaHoraInicio = bloque.getHoraIni().equals(horaIni);
            boolean mismaHoraFin = bloque.getHoraFin().equals(horaFin);

            if (mismaFecha && mismaHoraInicio && mismaHoraFin) {
                return true;
            }
        }

        return false;
    }

    public static Date truncateToDate(Date fecha) {
        LocalDate localDate = fecha.toInstant()
                                   .atZone(ZoneId.systemDefault())
                                   .toLocalDate();
        return java.sql.Date.valueOf(localDate);
    }
}




