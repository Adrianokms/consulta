package com.adrianokms.consulta.service;

import com.adrianokms.consulta.domain.Consulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarNotificacaoAgendamento(Consulta consulta) {
        String assunto = "Consulta Agendada";
        String texto = String.format(
                "Olá %s, sua consulta com Dr(a). %s foi agendada para %s.",
                consulta.getPaciente().getNome(),
                consulta.getMedico().getNome(),
                consulta.getDataHora().toString()
        );
        enviarEmail(consulta.getPaciente().getEmail(), assunto, texto);
    }

    public void enviarNotificacaoCancelamento(Consulta consulta) {
        String assunto = "Consulta Cancelada";
        String texto = String.format(
                "Olá %s, sua consulta com Dr(a). %s marcada para %s foi cancelada.",
                consulta.getPaciente().getNome(),
                consulta.getMedico().getNome(),
                consulta.getDataHora().toString()
        );
        enviarEmail(consulta.getPaciente().getEmail(), assunto, texto);
    }

    private void enviarEmail(String para, String assunto, String texto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(para);
        message.setSubject(assunto);
        message.setText(texto);
        mailSender.send(message);
    }
}