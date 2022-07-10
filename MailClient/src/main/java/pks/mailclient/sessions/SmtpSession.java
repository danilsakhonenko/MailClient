package pks.mailclient.sessions;

import java.io.File;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SmtpSession {
    protected Properties _properties = new Properties();
    protected Session _session;
    protected String _email;
    protected String _password;
    protected int _port;
    
    public SmtpSession(String email, String pass, int port) throws Exception {
        _email = email;
        _password = pass;
        _port = port;
        String host = "smtp." + _email.substring(_email.indexOf("@") + 1);
        _properties.put("mail.smtp.host", host);
        _properties.put("mail.smtp.port", port);
        _properties.put("mail.smtp.auth", "true");
        _properties.put("mail.smtp.starttls.enable", "true");
        _properties.put("mail.smtp.connectiontimeout", "10000");
        _properties.put("mail.smtp.timeout", "10000");
        _session = Session.getInstance(_properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(_email, _password);
            }
        });
        _session.setDebug(true);
        checkConnection();
    }
    
    protected void checkConnection() throws Exception {
        Transport transport = null;
        try {
            transport = _session.getTransport("smtp");
            transport.connect();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (transport != null) {
                transport.close();
            }
        }
    }

    public void sendMail(String recepient, String subject, String text, List<File> files) throws Exception {
        Message msg = new MimeMessage(_session);
        msg.setFrom(new InternetAddress(_email));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
        msg.setSubject(subject);
        if (files == null) {
            msg.setText(text);
        } else {
            BodyPart msgBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            msgBodyPart.setText(text);
            multipart.addBodyPart(msgBodyPart);
            for (File f : files) {
                msgBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(f);
                msgBodyPart.setDataHandler(new DataHandler(source));
                msgBodyPart.setFileName(f.getName());
                multipart.addBodyPart(msgBodyPart);
            }
            msg.setContent(multipart);
        }
        Transport.send(msg);
    }

}
