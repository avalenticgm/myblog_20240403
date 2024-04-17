package it.cgmconsulting.myblog.mail;

import it.cgmconsulting.myblog.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;

    @Value("${application.mail.sender}")
    private String from;

    @Async
    public void sendMail(Mail mail) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setText(mail.getMailContent());

            javaMailSender.send(mimeMessage);
            log.info(mail.toString());
        } catch(MessagingException e){
            log.error("An error occurred sending the email: "+e.getMessage());
        }
    }

    public Mail createMail(User user, String subject, String content){
        Mail mail = new Mail();
        mail.setMailFrom(from);
        mail.setMailTo(user.getEmail());
        mail.setMailSubject(subject);
        mail.setMailContent(content);
        return mail;
    }
}


