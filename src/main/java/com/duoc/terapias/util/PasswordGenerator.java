
package com.duoc.terapias.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class PasswordGenerator {
    public static String generarCodigo(String fechaDDMMAAAA) {
        try {
            // Hash MD5 de la fecha
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(fechaDDMMAAAA.getBytes(StandardCharsets.UTF_8));

            // Codificar en Base64
            String base64 = Base64.getEncoder().encodeToString(hash);

            // Eliminar caracteres no alfanuméricos si quieres (opcional)
            base64 = base64.replaceAll("[^A-Za-z0-9]", "");

            // Tomar solo los primeros 8 caracteres
            return base64.substring(0, 8).toUpperCase();
        } catch (Exception e) {
            return "ERROR123"; // fallback
        }
    }
    
    public static boolean validarCodigo(String fechaDDMMAAAA, String codigoIngresado) {
        
        String codigoEsperado = generarCodigo(fechaDDMMAAAA);
        System.out.println("Ingresado " + codigoIngresado + " esperado " + codigoEsperado);
        return codigoEsperado.equalsIgnoreCase(codigoIngresado);
    }
}

