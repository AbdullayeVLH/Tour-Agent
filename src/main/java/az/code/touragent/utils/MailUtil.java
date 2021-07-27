package az.code.touragent.utils;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {
    private final JavaMailSender sender;

    public MailUtil(JavaMailSender sender) {
        this.sender = sender;
    }

    public void sendNotificationEmail(String to, String subject, String text) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(text);
        sender.send(mail);
    }
}
