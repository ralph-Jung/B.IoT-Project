package ac.gachon.iot.service;

import ac.gachon.iot.domain.enums.AlertType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertEmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${alert.email.recipient}")
    private String recipient;

    @Async
    public void sendAlert(String sensorIdentifier, AlertType alertType, String alertMessage) {
        if (recipient == null || recipient.isBlank()) {
            log.warn("Alert email skipped — ALERT_EMAIL_RECIPIENT is not configured.");
            return;
        }
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(from);
            mail.setTo(recipient);
            mail.setSubject("[IoT Alert] " + alertType.name());
            mail.setText(
                    "센서: " + sensorIdentifier + "\n" +
                    "이상 유형: " + alertType.name() + "\n" +
                    "내용: " + alertMessage
            );
            mailSender.send(mail);
            log.info("Alert email sent. alertType={}, recipient={}", alertType, recipient);
        } catch (MailException e) {
            log.error("Failed to send alert email. alertType={}", alertType, e);
        }
    }
}
