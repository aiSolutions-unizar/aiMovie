package es.unizar.aisolutions.aimovie.email;

import android.util.Log;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * Created by diego on 16/05/15.
 */
public class MailHelper {
    // TODO: not expose credentials
    private static final String USERNAME = "aimovie.zar@gmail.com ";
    private static final String PASSWORD = "*******";
    private static final Properties PROPERTIES = new Properties() {{
        put("mail.smtp.auth", "true");
        put("mail.smtp.starttls.enable", "true");
        put("mail.smtp.host", "smtp.gmail.com");
        put("mail.smtp.port", "587");
    }};
    private static final String MAIL_TEMPLATE = "Hello Mr. %s %s\nYou have rented %s on %s\nThanks";

    public static boolean sendMail(String subject, String dest, String name, String surname, String movie) {
        Session session = Session.getInstance(PROPERTIES, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        Message m = new MimeMessage(session);
        try {
            String body = String.format(MAIL_TEMPLATE, name, surname, movie, new Date(System.currentTimeMillis()));
            m.setFrom(new InternetAddress(USERNAME));
            m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(dest));
            m.setSubject(subject);
            m.setText(body);
            Transport.send(m);
            return true;
        } catch (MessagingException e) {
            Log.e(e.getMessage(), e.toString(), e);
            return false;
        }
    }
}
