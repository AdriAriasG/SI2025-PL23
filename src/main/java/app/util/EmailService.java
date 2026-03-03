package app.util;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.Address;

public class EmailService {

    private final String smtpUser;      // Email de la agencia (desde BD)
    private final String smtpPassword;  // App Password de esa cuenta Gmail

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    public EmailService(String smtpUser, String smtpPassword) {
        this.smtpUser = smtpUser;
        this.smtpPassword = "ehpx hkty jwyf kjmm";
    }

    public void enviarEmail(String destino,
                            String asunto,
                            String cuerpo,
                            String replyToEmail) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props,
            new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUser, smtpPassword);
                }
            });

        try {
            Message message = new MimeMessage(session);

            // Remitente (la agencia)
            message.setFrom(new InternetAddress(smtpUser));

            // Destinatario (empresa de comunicación)
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(destino)
            );

            // Reply-To (opcional, pero profesional)
            if (replyToEmail != null && !replyToEmail.isBlank()) {
                message.setReplyTo(new Address[]{
                    new InternetAddress(replyToEmail)
                });
            }

            message.setSubject(asunto);
            message.setText(cuerpo);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(
                "Error enviando el email: " + e.getMessage(), e);
        }
    }
}
