package huan.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import huan.backend.dto.response.MailResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class EmailService {


    private final JavaMailSender javaMailSender;


    @Value("$(spring.mail.username)")
    private String fromMail;
    public  void sendEmail(MailResponse mailResponse){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setTo(mailResponse.getTo());
        simpleMailMessage.setSubject(mailResponse.getSubject());
        simpleMailMessage.setText(mailResponse.getText());
        javaMailSender.send(simpleMailMessage);
    }
    
}
