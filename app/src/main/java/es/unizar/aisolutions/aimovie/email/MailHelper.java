package es.unizar.aisolutions.aimovie.email;

import java.util.Calendar;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * Created by diego on 16/05/15.
 */
public class MailHelper {

    private final String USERNAME = "aimovie.zar@gmail.com ";
    private final String PASSWORD = "culopedo";
    private final Properties props = new Properties() {{
        put("mail.smtp.auth", "true");
        put("mail.smtp.starttls.enable", "true");
        put("mail.smtp.host", "smtp.gmail.com");
        put("mail.smtp.port", "587");
    }};
    private String mail = "";
    private String destEmail;

    public MailHelper(String destEmail) {
        this.destEmail = destEmail;
    }

    public boolean sendMail(String title) {
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        Message m = new MimeMessage(session);
        try {
            m.setFrom(new InternetAddress(USERNAME));
            m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destEmail));
            m.setSubject(title);
            m.setText(mail);
            Transport.send(m);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void fillEmail(String name, String sur, String movieName) {
        mail = "Hello " + name + " " + sur +
                ",\nYou have rented '" + movieName + "' on " + Calendar.getInstance().getTime().toString()
                + ".\nThanks.\n";
    }
}
