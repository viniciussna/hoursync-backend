package com.acadflow.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarEmailAprovacao(String emailAluno, String nomeAluno, String tituloCertificado) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hoursync.sistema@gmail.com");
        message.setTo(emailAluno);
        message.setSubject("Certificado Aprovado - HourSync");
        message.setText(
                "Olá, " + nomeAluno + "!\n\n" +
                        "Seu certificado \"" + tituloCertificado + "\" foi APROVADO.\n\n" +
                        "Acesse a plataforma HourSync para mais detalhes.\n\n" +
                        "Atenciosamente,\nEquipe HourSync"
        );
        mailSender.send(message);
    }

    public void enviarEmailRejeicao(String emailAluno, String nomeAluno, String tituloCertificado, String justificativa) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hoursync.sistema@gmail.com");
        message.setTo(emailAluno);
        message.setSubject("Certificado Rejeitado - HourSync");
        message.setText(
                "Olá, " + nomeAluno + "!\n\n" +
                        "Seu certificado \"" + tituloCertificado + "\" foi REJEITADO.\n\n" +
                        "Motivo: " + justificativa + "\n\n" +
                        "Acesse a plataforma HourSync para mais detalhes.\n\n" +
                        "Atenciosamente,\nEquipe HourSync"
        );
        mailSender.send(message);
    }
}