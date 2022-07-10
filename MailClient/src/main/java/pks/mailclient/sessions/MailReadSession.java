package pks.mailclient.sessions;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ListView;
import javafx.util.Duration;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;

public abstract class MailReadSession {

    protected Properties _properties = new Properties();
    protected Session _session;
    protected String _userEmail;
    protected String _password;
    protected int _port;
    protected String _host;
    protected Store _store;
    protected boolean _transfer = false;
    protected Folder _emailFolder;
    protected ArrayList<MimeBodyPart> _fileparts;

    MailReadSession(String email, String pass, int port) {
        _userEmail = email;
        _password = pass;
        _port = port;

    }

    protected Session getSession() {
        return Session.getInstance(_properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(_userEmail, _password);
            }
        });
    }

    protected abstract void checkConnection() throws Exception;

    protected String getHostPart() {
        String host = _userEmail.substring(_userEmail.indexOf("@") + 1);
        return host;
    }

    public void getAllMessages(ListView<String> list) throws Exception {
        ArrayList<String> titles = new ArrayList<String>();
        list.getItems().clear();
        int count = _emailFolder.getMessageCount();
        Runnable task = () -> {
            for (int i = count; i > 0; i--) {
                try {

                    if (!_emailFolder.isOpen() || !_transfer) {
                        return;
                    }
                    Message msg = _emailFolder.getMessage(i);
                    titles.add(msg.getMessageNumber() + " " + MimeUtility.decodeText(msg.getFrom()[0].toString()) + " - " + MimeUtility.decodeText(msg.getSubject()));
                } catch (MessagingException ex) {
                    continue;
                } catch (UnsupportedEncodingException ex) {
                    continue;
                }catch (NullPointerException ex) {
                    continue;
                }
            }
        };
        KeyFrame set = new KeyFrame(
                Duration.seconds(1),
                event -> {
                    list.getItems().addAll(titles);
                    titles.clear();
                }
        );

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        Timeline timeline = new Timeline(set);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public String[] getMessage(int index) throws Exception {
        _fileparts = new ArrayList<MimeBodyPart>();
        Message msg = _emailFolder.getMessage(index);
        String[] items = new String[3];
        items[0] = msg.getFrom()[0].toString();
        items[1] = msg.getSubject();
        if (msg.getContentType().contains("multipart")) {
            Multipart multiPart = (Multipart) msg.getContent();
            int numberOfParts = multiPart.getCount();
            for (int i = 0; i < numberOfParts; i++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    _fileparts.add(part);
                } else {
                    items[2] = part.getContent().toString();
                }
            }
        } else if (msg.getContentType().contains("text/plain") || msg.getContentType().contains("text/html")) {
            Object content = msg.getContent();
            if (content != null) {
                items[2] = content.toString();
            }
        }
        return items;
    }

    public void loadFiles(File dir) throws Exception, IOException {
        if (_fileparts.size() == 0) {
            throw new Exception();
        }
        for (MimeBodyPart p : _fileparts) {
            String filename = MimeUtility.decodeText(p.getFileName());
            p.saveFile(dir + File.separator + filename);
        }
    }

    public void openConnection() throws MessagingException {
        _store.connect();
        _emailFolder = _store.getFolder("INBOX");
        _emailFolder.open(Folder.READ_ONLY);
    }

    public void closeConnection() throws MessagingException {
        _emailFolder.close(false);
        _store.close();
    }

    public void setTransfer(boolean b) {
        _transfer = b;
    }

    public boolean getTransfer() {
        return _transfer;
    }
}
