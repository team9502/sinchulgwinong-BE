package team9502.sinchulgwinong.domain.email.service;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

    void sendHtmlMessage(String to, String subject, String htmlContent);
}
