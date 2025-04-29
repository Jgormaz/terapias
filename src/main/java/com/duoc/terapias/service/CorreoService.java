package com.duoc.terapias.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class CorreoService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void enviarCodigoVerificacion(String destinatario, String codigo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject("Código de verificación de reserva");
        mensaje.setText("Tu código de verificación es: " + codigo + "\nEste código es válido solo por hoy.");
        javaMailSender.send(mensaje);
    }
}
