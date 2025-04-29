package com.duoc.terapias.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class FechaSemanaUtils {

    public static Date getPrimerDiaDeSemana(int numeroSemana) {
        LocalDate hoy = LocalDate.now();

        // Obtiene el lunes de esta semana
        LocalDate lunesActual = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // Sumamos las semanas correspondientes
        LocalDate resultado = lunesActual.plusWeeks(numeroSemana - 1);

        // Convertimos a java.util.Date
        return Date.from(resultado.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
