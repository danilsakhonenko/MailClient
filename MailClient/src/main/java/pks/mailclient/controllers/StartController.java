package pks.mailclient.controllers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.mail.AuthenticationFailedException;
import pks.mailclient.DialogMessage;
import pks.mailclient.sessions.ImapSession;
import pks.mailclient.sessions.MailReadSession;
import pks.mailclient.sessions.Pop3Session;
import pks.mailclient.sessions.SmtpSession;

public class StartController {

    private static Stage _stage;
    private FXMLLoader _loader;
    private int _port;

    @FXML
    private TextField email_field;

    @FXML
    private PasswordField pass_field;

    @FXML
    private TextField port_field;

    @FXML
    private ChoiceBox<String> protocol_cb;

    @FXML
    private Button start_btn;

    @FXML
    void startClient(ActionEvent event) {
        _stage = (Stage) start_btn.getScene().getWindow();
        try {
            int protocol = protocol_cb.getSelectionModel().getSelectedIndex();
            _port = Integer.parseInt(port_field.getText());
            switch (protocol) {
                case 0:
                    runWriteMessage();
                    break;
                case 1:
                    runMailBox(true);
                    break;
                case 2:
                    runMailBox(false);
                    break;
                default:
                    return;
            }
            _stage.hide();
        } catch (AuthenticationFailedException ex) {
            new DialogMessage("Ошибка аутентификации. Проверьте правильность почты и пароля").ErrorMessage();
        } catch (Exception ex) {
            new DialogMessage("Ошибка при создании сессии").ErrorMessage();
        }

    }

    private void runWriteMessage() throws Exception {
        SmtpSession session = new SmtpSession(email_field.getText(), pass_field.getText(), _port);
        Stage newstage = new Stage();
        _loader = new FXMLLoader(getClass().getResource("WriteMessageForm.fxml"));
        newstage.setScene(new Scene(_loader.load()));
        WriteMessageController ctrl = _loader.getController();
        newstage.setResizable(false);
        newstage.show();
        ctrl.setSession(session);
    }

    private void runMailBox(boolean pop3) throws Exception {
        MailReadSession session;
        if (pop3) {
            session = new Pop3Session(email_field.getText(), pass_field.getText(), _port);
        } else {
            session = new ImapSession(email_field.getText(), pass_field.getText(), _port);
        }
        Stage newstage = new Stage();
        _loader = new FXMLLoader(getClass().getResource("MailBoxForm.fxml"));
        newstage.setScene(new Scene(_loader.load()));
        MailBoxController ctrl = _loader.getController();
        newstage.setResizable(false);
        newstage.show();
        ctrl.setSession(session);
    }

    public static void unHide() {
        _stage.show();
    }

    public void setPort(int index) {
        switch (index) {
            case 0:
                port_field.setText("587");
                break;
            case 1:
                port_field.setText("995");
                break;
            case 2:
                port_field.setText("993");
                break;
            default:
                return;
        }
    }

    @FXML
    public void initialize() {
        protocol_cb.getItems().add(0, "SMTP " + "(" + "Отправка почты" + ")");
        protocol_cb.getItems().add(1, "POP3 " + "(" + "Получение почты" + ")");
        protocol_cb.getItems().add(2, "IMAP " + "(" + "Получение почты" + ")");
        protocol_cb.setOnAction(event -> {
            int protocol = protocol_cb.getSelectionModel().getSelectedIndex();
            setPort(protocol);
        });
    }

}
